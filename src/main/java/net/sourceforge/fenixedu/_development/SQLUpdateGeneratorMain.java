package net.sourceforge.fenixedu._development;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.accesslayer.LookupException;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Config;
import pt.ist.fenixframework.FenixFrameworkPlugin;
import pt.ist.fenixframework.artifact.FenixFrameworkArtifact;
import pt.ist.fenixframework.project.DmlFile;
import pt.ist.fenixframework.project.exception.FenixFrameworkProjectException;
import pt.ist.fenixframework.pstm.MetadataManager;
import pt.ist.fenixframework.pstm.repository.SQLUpdateGenerator;
import dml.DomainModel;

public class SQLUpdateGeneratorMain {

    public static void main(String[] args) {
        final List<URL> dmlFiles = new ArrayList<URL>();
        try {
            for (DmlFile dmlFile : FenixFrameworkArtifact.fromName("fenix").getFullDmlSortedList()) {
                dmlFiles.add(dmlFile.getUrl());
            }
            MetadataManager.init(getConfig(dmlFiles));
            final PersistenceBroker persistenceBroker = PersistenceBrokerFactory.defaultPersistenceBroker();
            Connection connection = persistenceBroker.serviceConnectionManager().getConnection();
            DomainModel model = MetadataManager.getDomainModel();
            String updates = SQLUpdateGenerator.generateSqlUpdates(model, connection, "utf8", false);

            final File file = new File("etc/database_operations/updates.sql");
            if (file.exists()) {
                updates = FileUtils.readFileToString(file) + "\n\n\n" + "-- Inserted at " + new DateTime() + "\n\n" + updates;
            }
            FileUtils.writeStringToFile(file, updates);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Error(e);
        } catch (LookupException e) {
            e.printStackTrace();
            throw new Error(e);
        } catch (FenixFrameworkProjectException e) {
            e.printStackTrace();
            throw new Error(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new Error(e);
        }
    }

    private static Config getConfig(final List<URL> domainModelUrls) throws IOException {
        final InputStream configStream = SQLUpdateGeneratorMain.class.getResourceAsStream("/configuration.properties");
        final Properties props = new Properties();
        props.load(configStream);

        return new Config() {
            {
                domainModelPaths = new String[0];
                dbAlias = props.getProperty("db.alias");
                dbUsername = props.getProperty("db.user");
                dbPassword = props.getProperty("db.pass");
                plugins = new FenixFrameworkPlugin[0];
            }

            @Override
            public java.util.List<URL> getDomainModelURLs() {
                return domainModelUrls;
            };
        };
    }
}
