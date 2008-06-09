/*
 * Created on Jan 10, 2005
 *
 */
package net.sourceforge.fenixedu.persistenceTierOracle.Oracle;

import java.io.IOException;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.IPersistentExpensesReport;
import net.sourceforge.fenixedu.persistenceTierOracle.IPersistentExpensesResume;
import net.sourceforge.fenixedu.persistenceTierOracle.IPersistentOpeningProjectFileReport;
import net.sourceforge.fenixedu.persistenceTierOracle.IPersistentProject;
import net.sourceforge.fenixedu.persistenceTierOracle.IPersistentProjectUser;
import net.sourceforge.fenixedu.persistenceTierOracle.IPersistentReport;
import net.sourceforge.fenixedu.persistenceTierOracle.IPersistentRubric;
import net.sourceforge.fenixedu.persistenceTierOracle.IPersistentSummaryReport;
import net.sourceforge.fenixedu.persistenceTierOracle.IPersistentSuportOracle;

/**
 * @author Susana Fernandes
 * 
 */
public class PersistentSuportOracle implements IPersistentSuportOracle {
    private static PersistentSuportOracle instance = null;

    private static String databaseUrl = null;

    private static Connection connection = null;

    private static Map connectionsMap = new HashMap();

    public class ConnectionProperties {
	private String resourceFileName;

	private String userNamePropertyName;

	private String userPassPropertyName;

	private String urlPropertyName;

	public ConnectionProperties(String resourceFileName, String userNamePropertyName,
		String userPassPropertyName, String urlPropertyName) {
	    super();
	    this.resourceFileName = resourceFileName;
	    this.userNamePropertyName = userNamePropertyName;
	    this.userPassPropertyName = userPassPropertyName;
	    this.urlPropertyName = urlPropertyName;
	}
    }

    private static ConnectionProperties connectionProperties;

    public PersistentSuportOracle(String resourceFileName, String userNamePropertyName,
	    String userPassPropertyName, String urlPropertyName) {
	super();
	connectionProperties = new ConnectionProperties(resourceFileName, userNamePropertyName,
		userPassPropertyName, urlPropertyName);
    }

    public static synchronized PersistentSuportOracle getProjectDBInstance() {
	if (instance == null) {
	    instance = new PersistentSuportOracle("/projectsManagement.properties",
		    "db.projectManagement.user", "db.projectManagement.pass",
		    "db.projectManagement.alias");
	}
	return instance;
    }

    public static synchronized PersistentSuportOracle getGiafDBInstance() {
	if (instance == null) {
	    instance = new PersistentSuportOracle("/giaf.properties", "db.giaf.user", "db.giaf.pass",
		    "db.giaf.alias");
	}
	return instance;
    }

    private void openConnection() throws ExcepcaoPersistencia {
	if (databaseUrl == null) {
	    Properties properties = new Properties();
	    InputStream inputStream = instance.getClass().getResourceAsStream(
		    connectionProperties.resourceFileName);
	    try {
		properties.load(inputStream);
	    } catch (IOException e) {
		throw new ExcepcaoPersistencia(e.getMessage());
	    }
	    String DBUserName = properties.getProperty(connectionProperties.userNamePropertyName);
	    String DBUserPass = properties.getProperty(connectionProperties.userPassPropertyName);
	    String DBUrl = properties.getProperty(connectionProperties.urlPropertyName);
	    if (DBUserName == null || DBUserPass == null || DBUrl == null) {
		throw new ExcepcaoPersistencia();
	    }
	    StringBuilder stringBuffer = new StringBuilder();
	    stringBuffer.append("jdbc:oracle:thin:");
	    stringBuffer.append(DBUserName);
	    stringBuffer.append("/");
	    stringBuffer.append(DBUserPass);
	    stringBuffer.append("@");
	    stringBuffer.append(DBUrl);
	    databaseUrl = stringBuffer.toString();
	}
	try {
	    DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
	} catch (SQLException e) {
	    throw new ExcepcaoPersistencia();
	}
    }

    private void closeConnection() throws ExcepcaoPersistencia {
	Connection thisconnection = (Connection) connectionsMap.get(Thread.currentThread());
	try {
	    thisconnection.close();
	    connectionsMap.remove(Thread.currentThread());
	} catch (Exception e) {
	    throw new ExcepcaoPersistencia();
	}
    }

    public synchronized void startTransaction() throws ExcepcaoPersistencia {
	try {
	    openConnection();
	    connection = DriverManager.getConnection(databaseUrl);
	    connection.setAutoCommit(false);
	    connectionsMap.put(Thread.currentThread(), connection);
	} catch (SQLException e) {
	    throw new ExcepcaoPersistencia("", e);
	}
    }

    public synchronized void commitTransaction() throws ExcepcaoPersistencia {
	Connection thisConnection = (Connection) connectionsMap.get(Thread.currentThread());
	try {
	    thisConnection.commit();
	    closeConnection();
	} catch (SQLException e) {
	    throw new ExcepcaoPersistencia();
	}
    }

    public synchronized void cancelTransaction() throws ExcepcaoPersistencia {
	Connection thisConnection = (Connection) connectionsMap.get(Thread.currentThread());
	try {
	    thisConnection.rollback();
	    closeConnection();
	} catch (SQLException e) {
	    throw new ExcepcaoPersistencia();
	}
    }

    public synchronized PreparedStatement prepareStatement(String statement) throws ExcepcaoPersistencia {
	Connection thisConnection = (Connection) connectionsMap.get(Thread.currentThread());
	if (thisConnection == null) {
	    if (connection == null)
		try {
		    openConnection();
		    connection = DriverManager.getConnection(databaseUrl);
		} catch (java.sql.SQLException e) {
		}
	    thisConnection = connection;
	}
	PreparedStatement sql = null;
	try {
	    sql = thisConnection.prepareStatement(statement);
	} catch (java.sql.SQLException e) {
	}
	return sql;
    }

    public synchronized CallableStatement prepareCall(String statement) throws ExcepcaoPersistencia {
	Connection thisConnection = (Connection) connectionsMap.get(Thread.currentThread());
	if (thisConnection == null) {
	    if (connection == null)
		try {
		    openConnection();
		    connection = DriverManager.getConnection(databaseUrl);
		} catch (java.sql.SQLException e) {
		}
	    thisConnection = connection;
	}
	CallableStatement sql = null;
	try {
	    sql = thisConnection.prepareCall(statement);
	} catch (java.sql.SQLException e) {
	    throw new ExcepcaoPersistencia(e);
	} catch (java.lang.NullPointerException e) {
	    throw new ExcepcaoPersistencia(e);
	}
	return sql;
    }

    public IPersistentProject getIPersistentProject() {
	return new PersistentProject();
    }

    public IPersistentProjectUser getIPersistentProjectUser() {
	return new PersistentProjectUser();
    }

    public IPersistentRubric getIPersistentRubric() {
	return new PersistentRubric();
    }

    public IPersistentExpensesReport getIPersistentExpensesReport() {
	return new PersistentExpensesReport();
    }

    public IPersistentReport getIPersistentRevenueReport() {
	return new PersistentRevenueReport();
    }

    public IPersistentSummaryReport getIPersistentSummaryReport() {
	return new PersistentSummaryReport();
    }

    public IPersistentExpensesResume getIPersistentExpensesResume() {
	return new PersistentExpensesResume();
    }

    public IPersistentReport getIPersistentMovementReport() {
	return new PersistentMovementReport();
    }

    public IPersistentExpensesReport getIPersistentCompleteExpensesReport() {
	return new PersistentCompleteExpensesReport();
    }

    public IPersistentOpeningProjectFileReport getIPersistentOpeningProjectFileReport() {
	return new PersistentOpeningProjectFileReport();
    }

    public IPersistentReport getIPersistentProjectMemberBudget() {
	return new PersistentProjectMemberBudget();
    }

    public IPersistentReport getIPersistentProjectBudgetaryBalanceReport() {
	return new PersistentProjectBudgetaryBalanceReport();
    }

    public IPersistentReport getIPersistentGeneratedOverheadsReport() {
	return new PersistentGeneratedOverheadsReport();
    }

    public IPersistentReport getIPersistentTransferedOverheadsReport() {
	return new PersistentTransferedOverheadsReport();
    }

    public IPersistentReport getIPersistentOverheadsSummaryReport() {
	return new PersistentOverheadsSummaryReport();
    }

}
