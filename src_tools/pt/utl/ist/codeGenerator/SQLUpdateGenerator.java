package pt.utl.ist.codeGenerator;

import java.sql.Connection;
import java.sql.SQLException;

import net.sourceforge.fenixedu._development.PropertiesManager;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;

import pt.ist.fenixWebFramework.Config;
import pt.ist.fenixframework.FenixFramework;

public class SQLUpdateGenerator {

    public static void main(String[] args) {
	Connection connection = null;
	try {
	    Config config = PropertiesManager.getFenixFrameworkConfig("web/WEB-INF/classes/domain_model.dml");
	    FenixFramework.initialize(config);

	    final PersistenceBroker persistenceBroker = PersistenceBrokerFactory.defaultPersistenceBroker();
	    connection = persistenceBroker.serviceConnectionManager().getConnection();
	    generate(connection);
	} catch (Exception ex) {
	    ex.printStackTrace();
	} finally {
	    if (connection != null) {
		try {
		    connection.close();
		} catch (SQLException e) {
		    // nothing can be done.
		}
	    }
	}

	System.out.println("Generation Complete.");
	System.exit(0);
    }

    private static void generate(final Connection connection) throws Exception {
	final String destinationFilename = "etc/database_operations/updates.sql";
	final String result = pt.ist.fenixWebFramework.repository.SQLUpdateGenerator.generateInMem(connection);
	pt.ist.fenixWebFramework.repository.SQLUpdateGenerator.writeFile(destinationFilename, result);
    }

}
