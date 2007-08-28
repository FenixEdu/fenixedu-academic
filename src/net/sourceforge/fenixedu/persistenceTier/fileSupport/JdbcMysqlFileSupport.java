package net.sourceforge.fenixedu.persistenceTier.fileSupport;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import net.sourceforge.fenixedu._development.LogLevel;
import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.fileSuport.FileSuportObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import pt.utl.ist.fenix.tools.util.StringAppender;

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

    private static final String REVISION_CONTENT_INSERT_STATEMENT = "insert into revisioncontent values(?, \"1.0\", ?)";
    private static final String PROPERTY_INSERT_STATEMENT = "insert into property values(?, \"1.0\", ?, ?, \"DAV:\", null, ?)";

    private interface Closure {
        public ResultSet execute(final Connection connection, final String query) throws SQLException;
    }

    private static final Closure queryExecuter = new Closure() {
        public ResultSet execute(final Connection connection, final String query) throws SQLException {
            final PreparedStatement preparedStatement = connection.prepareStatement(query);
            final ResultSet resultSet = preparedStatement.executeQuery();
            //preparedStatement.close();
            return resultSet;
        }
    };

    private static final Closure updateExecuter = new Closure() {
        public ResultSet execute(final Connection connection, final String query) throws SQLException {
            final PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.executeUpdate();
            //preparedStatement.close();
            return null;
        }
    };

    private static ResultSet execute(final Closure closure, final String... queries) {
        Connection connection = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection(driver, alias, user, password);
            for (int i = 0; i < queries.length; i++) {
                resultSet = closure.execute(connection, queries[i]);
            }
            connection.commit();
        } catch (ClassNotFoundException e) {
            if (LogLevel.FATAL) {
                logger.fatal("Unable to get JdbcMysqlFileSupport - ClassNotFoundException", e);
            }
            try {
                if (connection != null) connection.rollback();
            } catch (SQLException e1) {
                if (LogLevel.FATAL) {
                    logger.fatal("Unable to roleback connection", e1);
                }
            }
            throw new Error(e);
        } catch (SQLException e) {
            if (LogLevel.FATAL) {
                logger.fatal("Unable to get JdbcMysqlFileSupport - SQLException", e);
            }
            try {
                if (connection != null) connection.rollback();
            } catch (SQLException e1) {
                if (LogLevel.FATAL) {
                    logger.fatal("Unable to roleback connection", e1);
                }
            }
            throw new Error(e);
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                if (LogLevel.FATAL) {
                    logger.fatal("Unable to close connection", e);
                }
            }
        }
        return resultSet;
    }

    private static void executeInsert(FileSuportObject fileSuportObject) {
        Connection connection = null;
        try {
            connection = getConnection(driver, alias, user, password);

            final PreparedStatement revisionContentPS = connection.prepareStatement(REVISION_CONTENT_INSERT_STATEMENT);
            revisionContentPS.setString(1, fileSuportObject.getUri());
            revisionContentPS.setBytes(2, fileSuportObject.getContent());
            revisionContentPS.executeUpdate();
            //revisionContentPS.close();

            final PreparedStatement propertyPSresourcetype = connection.prepareStatement(PROPERTY_INSERT_STATEMENT);
            propertyPSresourcetype.setString(1, fileSuportObject.getUri());
            propertyPSresourcetype.setString(2, "resourcetype");
            propertyPSresourcetype.setString(3, "<collection/>");
            propertyPSresourcetype.setInt(4, 1);
            propertyPSresourcetype.executeUpdate();
            //propertyPSresourcetype.close();

            final PreparedStatement propertyPSsource = connection.prepareStatement(PROPERTY_INSERT_STATEMENT);
            propertyPSsource.setString(1, fileSuportObject.getUri());
            propertyPSsource.setString(2, "source");
            propertyPSsource.setString(3, "");
            propertyPSsource.setInt(4, 1);
            propertyPSsource.executeUpdate();
            //propertyPSsource.close();

            final PreparedStatement propertyPSdisplayname = connection.prepareStatement(PROPERTY_INSERT_STATEMENT);
            propertyPSdisplayname.setString(1, fileSuportObject.getUri());
            propertyPSdisplayname.setString(2, "displayname");
            propertyPSdisplayname.setString(3, "<![CDATA[" + fileSuportObject.getFileName() + "]]>");
            propertyPSdisplayname.setInt(4, 1);
            propertyPSdisplayname.executeUpdate();
            //propertyPSdisplayname.close();

            final PreparedStatement propertyPSlinkName = connection.prepareStatement(PROPERTY_INSERT_STATEMENT);
            propertyPSlinkName.setString(1, fileSuportObject.getUri());
            propertyPSlinkName.setString(2, "linkName");
            propertyPSlinkName.setString(3, fileSuportObject.getLinkName());
            propertyPSlinkName.setInt(4, 0);
            propertyPSlinkName.executeUpdate();
            //propertyPSlinkName.close();

            final PreparedStatement propertyPSgetlastmodified = connection.prepareStatement(PROPERTY_INSERT_STATEMENT);
            propertyPSgetlastmodified.setString(1, fileSuportObject.getUri());
            propertyPSgetlastmodified.setString(2, "getlastmodified");
            propertyPSgetlastmodified.setString(3, Calendar.getInstance().getTime().toString());
            propertyPSgetlastmodified.setInt(4, 1);
            propertyPSgetlastmodified.executeUpdate();
            //propertyPSgetlastmodified.close();

            final PreparedStatement propertyPSgetcontenttype = connection.prepareStatement(PROPERTY_INSERT_STATEMENT);
            propertyPSgetcontenttype.setString(1, fileSuportObject.getUri());
            propertyPSgetcontenttype.setString(2, "getcontenttype");
            propertyPSgetcontenttype.setString(3, fileSuportObject.getContentType());
            propertyPSgetcontenttype.setInt(4, 1);
            propertyPSgetcontenttype.executeUpdate();
            //propertyPSgetcontenttype.close();

            final PreparedStatement propertyPSgetcontentlength = connection.prepareStatement(PROPERTY_INSERT_STATEMENT);
            propertyPSgetcontentlength.setString(1, fileSuportObject.getUri());
            propertyPSgetcontentlength.setString(2, "getcontentlength");
            propertyPSgetcontentlength.setString(3, String.valueOf(fileSuportObject.getContent().length));
            propertyPSgetcontentlength.setInt(4, 1);
            propertyPSgetcontentlength.executeUpdate();
            //propertyPSgetcontentlength.close();

            final PreparedStatement propertyPScreationdate = connection.prepareStatement(PROPERTY_INSERT_STATEMENT);
            propertyPScreationdate.setString(1, fileSuportObject.getUri());
            propertyPScreationdate.setString(2, "creationdate");
            propertyPScreationdate.setString(3, Calendar.getInstance().getTime().toString());
            propertyPScreationdate.setInt(4, 1);
            propertyPScreationdate.executeUpdate();
            //propertyPScreationdate.close();

            connection.commit();
        } catch (ClassNotFoundException e) {
            if (LogLevel.FATAL) {
                logger.fatal("Unable to get JdbcMysqlFileSupport - ClassNotFoundException", e);
            }
            try {
                if (connection != null) connection.rollback();
            } catch (SQLException e1) {
                if (LogLevel.FATAL) {
                    logger.fatal("Unable to roleback connection", e1);
                }
            }
            throw new Error(e);
        } catch (SQLException e) {
            if (LogLevel.FATAL) {
                logger.fatal("Unable to get JdbcMysqlFileSupport - SQLException", e);
            }
            try {
                if (connection != null) connection.rollback();
            } catch (SQLException e1) {
                if (LogLevel.FATAL) {
                    logger.fatal("Unable to roleback connection", e1);
                }
            }
            throw new Error(e);
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                if (LogLevel.FATAL) {
                    logger.fatal("Unable to close connection", e);
                }
            }
        }
    }

    public static ResultSet executeQuery(final String query) {
        return execute(queryExecuter, query);
    }

    public static void executeUpdates(final String... queries) {
        execute(updateExecuter, queries);
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
            if (LogLevel.INFO) {
                logger.info(errorString, ex);
            }
            throw new Error(errorString, ex);
        } finally {
            try {
                resultSet.close();
            } catch (final SQLException ex) {
                final String errorString = "Uable to close result set." + directory;
                if (LogLevel.INFO) {
                    logger.info(errorString, ex);
                }
                throw new Error(errorString, ex);
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
            if (LogLevel.INFO) {
                logger.info(errorString, ex);
            }
            throw new Error(errorString, ex);
        } finally {
            try {
                propertyResultSet.close();
            } catch (final SQLException ex) {
                final String errorString = "Uable to close result set." + directory;
                if (LogLevel.INFO) {
                    logger.info(errorString, ex);
                }
                throw new Error(errorString, ex);
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
            if (LogLevel.INFO) {
                logger.info(errorString, ex);
            }
            throw new Error(errorString, ex);
        } finally {
            try {
                resultSet.close();
            } catch (final SQLException ex) {
                final String errorString = "Uable to close result set." + directory;
                if (LogLevel.INFO) {
                    logger.info(errorString, ex);
                }
                throw new Error(errorString, ex);
            }
        }

        return fileSuportObject;
    }

    public static void createFile(final FileSuportObject fileSuportObject) {
        executeInsert(fileSuportObject);
    }

    public static void deleteFile(final String directory, final String filename) {
        //TODO: temporarily disabled to allow data migration to dspace
        //final String propertyTableQuery = StringAppender.append("delete from property where uri = \"/files", directory, "/", filename, "\"");
        //final String revisionContentTableQuery = StringAppender.append("delete from revisioncontent where uri = \"/files", directory, "/", filename, "\"");
        //executeUpdates(propertyTableQuery, revisionContentTableQuery);
        
        throw new RuntimeException();
    }

    private static Connection getConnection(final String driver, final String alias, final String user, final String pass)
            throws ClassNotFoundException, SQLException {
        Class.forName(driver);
        final Connection connection = DriverManager.getConnection(alias, user, pass);
        connection.setAutoCommit(false);
        return connection;
    }

}
