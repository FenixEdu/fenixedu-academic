package net.sourceforge.fenixedu.persistenceTier.fileSupport;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.fileSuport.FileSuportObject;
import net.sourceforge.fenixedu.util.StringAppender;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class JdbcMysqlFileSupport {

    private static final Logger logger = Logger.getLogger(JdbcMysqlFileSupport.class);

    private static final String driver;
    private static final String alias;
    private static final String user;
    private static final String password;

    static {
        final Properties properties = new Properties();
        try {
            PropertiesManager.loadProperties(properties, "/slide.properties");
        } catch (IOException e) {
            throw new Error(e);
        }
        driver = properties.getProperty("db.slide.driver").trim();
        alias = properties.getProperty("db.slide.alias").trim();
        user = properties.getProperty("db.slide.user").trim();
        password = properties.getProperty("db.slide.pass").trim();
    }

    private interface Closure {
        public ResultSet execute(final Connection connection, final String query) throws SQLException;
    }

    private static final Closure queryExecuter = new Closure() {
        public ResultSet execute(final Connection connection, final String query) throws SQLException {
            final PreparedStatement preparedStatement = connection.prepareStatement(query);
            final ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet;
        }
    };

    private static ResultSet execute(final Closure closure, final String query) {
        Connection connection = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection(driver, alias, user, password);
            resultSet = closure.execute(connection, query);
        } catch (ClassNotFoundException e) {
            logger.fatal("Unable to get JdbcMysqlFileSupport - ClassNotFoundException", e);
            try {
                connection.rollback();
            } catch (SQLException e1) {
                logger.fatal("Unable to roleback connection", e1);
            }
            throw new Error(e);
        } catch (SQLException e) {
            logger.fatal("Unable to get JdbcMysqlFileSupport - SQLException", e);
            try {
                connection.rollback();
            } catch (SQLException e1) {
                logger.fatal("Unable to roleback connection", e1);
            }
            throw new Error(e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.fatal("Unable to close connection", e);
            }
        }
        return resultSet;
    }

    private static ResultSet executeQuery(final String query) {
        return execute(queryExecuter, query);
    }

    public static Collection<FileSuportObject> listFiles(final String directory) {
        final String query = StringAppender.append("select * from property where uri like \"/files", directory, "/%\"");

        final Map<String, FileSuportObject> fileSupportObjectsMap = new HashMap<String, FileSuportObject>();
        final ResultSet resultSet = executeQuery(query);
        try {
            while (resultSet.next()) {
                final String uri = resultSet.getString("uri");
                final FileSuportObject fileSuportObject;
                if (fileSupportObjectsMap.containsKey(uri)) {
                    fileSuportObject = fileSupportObjectsMap.get(uri);
                } else {
                    fileSuportObject = new FileSuportObject();
                    fileSuportObject.setUri(uri);
                    fileSuportObject.setRootUri(uri);
                    fileSuportObject.setFileName(StringUtils.substringAfterLast(uri, "/"));
                    fileSuportObject.setLinkName(fileSuportObject.getFileName());
                    fileSupportObjectsMap.put(uri, fileSuportObject);
                }

                final String name = resultSet.getString("name");
                if (name.equals("linkName")) {
                    final String value = resultSet.getString("value");
                    if (value != null && value.length() > 0) {
                        fileSuportObject.setLinkName(value);
                    }
                }
            }
        } catch (final SQLException ex) {
            final String errorString = "Uable to perform query for: " + directory;
            logger.info(errorString, ex);
            throw new Error(errorString);
        } finally {
            try {
                resultSet.close();
            } catch (final SQLException ex) {
                final String errorString = "Uable to close result set." + directory;
                logger.info(errorString, ex);
                throw new Error(errorString);
            }
        }
        return fileSupportObjectsMap.values();
    }

    public static FileSuportObject retrieveFile(final String directory, final String filename) {
        final FileSuportObject fileSuportObject = new FileSuportObject();

        final String propertyQuery = StringAppender.append("select * from property where uri = \"/files", directory, "/", filename, "\"");
        final ResultSet propertyResultSet = executeQuery(propertyQuery);
        try {
            while (propertyResultSet.next()) {
                final String uri = propertyResultSet.getString("uri");
                fileSuportObject.setUri(uri);
                fileSuportObject.setRootUri(uri);
                fileSuportObject.setFileName(filename);

                final String name = propertyResultSet.getString("name");
                if (name.equals("linkName")) {
                    final String value = propertyResultSet.getString("value");
                    fileSuportObject.setLinkName(value);
                } else if (name.equals("getcontenttype")) {
                    final String value = propertyResultSet.getString("value");
                    fileSuportObject.setContentType(value);
                }
            }
        } catch (final SQLException ex) {
            final String errorString = "Uable to perform query for: " + directory;
            logger.info(errorString, ex);
            throw new Error(errorString);
        } finally {
            try {
                propertyResultSet.close();
            } catch (final SQLException ex) {
                final String errorString = "Uable to close result set." + directory;
                logger.info(errorString, ex);
                throw new Error(errorString);
            }
        }

        final String query = StringAppender.append("select * from revisioncontent where uri = \"/files", directory, "/", filename, "\"");
        final ResultSet resultSet = executeQuery(query);
        try {
            if (resultSet.next()) {
                final byte[] content = resultSet.getBytes("content");
                fileSuportObject.setContent(content);
            }
        } catch (final SQLException ex) {
            final String errorString = "Uable to perform query for: " + directory;
            logger.info(errorString, ex);
            throw new Error(errorString);
        } finally {
            try {
                resultSet.close();
            } catch (final SQLException ex) {
                final String errorString = "Uable to close result set." + directory;
                logger.info(errorString, ex);
                throw new Error(errorString);
            }
        }

        return fileSuportObject;
    }

    private static Connection getConnection(final String driver, final String alias, final String user, final String pass)
            throws ClassNotFoundException, SQLException {
        Class.forName(driver);
        final Connection connection = DriverManager.getConnection(alias, user, pass);
        connection.setAutoCommit(false);
        return connection;
    }

}
