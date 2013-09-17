package net.sourceforge.fenixedu._development;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;

import pt.ist.fenixframework.DomainModelParser;
import pt.ist.fenixframework.backend.jvstmojb.ojb.OJBMetadataGenerator;
import pt.ist.fenixframework.backend.jvstmojb.repository.SQLUpdateGenerator;
import pt.ist.fenixframework.core.DmlFile;
import pt.ist.fenixframework.core.Project;
import pt.ist.fenixframework.core.exception.ProjectException;
import pt.ist.fenixframework.dml.DomainModel;

public class SQLUpdateGeneratorMain {

    public static void main(String[] args) throws Exception {

        System.setProperty("OJB.properties", "pt/ist/fenixframework/OJB.properties");

        final List<URL> dmlFiles = new ArrayList<URL>();
        try {
            for (DmlFile dmlFile : Project.fromName("fenix").getFullDmlSortedList()) {
                dmlFiles.add(dmlFile.getUrl());
            }
            DomainModel model = DomainModelParser.getDomainModel(dmlFiles);

            OJBMetadataGenerator.updateOJBMappingFromDomainModel(model);

            String updates = SQLUpdateGenerator.generateSqlUpdates(model, getNewConnection(), "utf8", false);

            final File file = new File("etc/database_operations/updates.sql");
            if (file.exists()) {
                updates = FileUtils.readFileToString(file) + "\n\n\n" + "-- Inserted at " + new DateTime() + "\n\n" + updates;
            }
            FileUtils.writeStringToFile(file, updates);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Error(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new Error(e);
        } catch (ProjectException e) {
            e.printStackTrace();
            throw new Error(e);
        }
    }

    private static Connection getNewConnection() throws IOException {
        try {
            final InputStream configStream = SQLUpdateGeneratorMain.class.getResourceAsStream("/fenix-framework.properties");
            final Properties props = new Properties();
            props.load(configStream);

            final String url = "jdbc:mysql:" + props.getProperty("dbAlias");
            final Connection connection =
                    DriverManager.getConnection(url, props.getProperty("dbUsername"), props.getProperty("dbPassword"));
            connection.setAutoCommit(false);
            return connection;
        } catch (SQLException ex) {
            throw new Error(ex);
        }
    }

}
