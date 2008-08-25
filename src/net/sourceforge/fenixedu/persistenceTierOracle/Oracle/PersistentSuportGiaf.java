package net.sourceforge.fenixedu.persistenceTierOracle.Oracle;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class PersistentSuportGiaf {
    private static PersistentSuportGiaf instance = null;

    private static String databaseUrl = null;

    private static Map<Thread, Connection> connectionsMap = new HashMap<Thread, Connection>();

    public class ConnectionProperties {
	private String userNamePropertyName;

	private String userPassPropertyName;

	private String urlPropertyName;

	public ConnectionProperties(String userNamePropertyName, String userPassPropertyName, String urlPropertyName) {
	    super();
	    this.userNamePropertyName = userNamePropertyName;
	    this.userPassPropertyName = userPassPropertyName;
	    this.urlPropertyName = urlPropertyName;
	}
    }

    private static ConnectionProperties connectionProperties;

    public PersistentSuportGiaf(String userNamePropertyName, String userPassPropertyName, String urlPropertyName) {
	super();
	connectionProperties = new ConnectionProperties(userNamePropertyName, userPassPropertyName, urlPropertyName);
    }

    public static synchronized PersistentSuportGiaf getInstance() {
	if (instance == null) {
	    instance = new PersistentSuportGiaf("db.giaf.user", "db.giaf.pass", "db.giaf.alias");
	}
	return instance;
    }

    private void openConnection() throws ExcepcaoPersistencia {
	if (databaseUrl == null) {
	    String DBUserName = PropertiesManager.getProperty(connectionProperties.userNamePropertyName);
	    String DBUserPass = PropertiesManager.getProperty(connectionProperties.userPassPropertyName);
	    String DBUrl = PropertiesManager.getProperty(connectionProperties.urlPropertyName);
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
	    Connection connection = DriverManager.getConnection(databaseUrl);
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
	    throw new ExcepcaoPersistencia(e);
	}
    }

    public synchronized void cancelTransaction() throws ExcepcaoPersistencia {
	Connection thisConnection = (Connection) connectionsMap.get(Thread.currentThread());
	try {
	    thisConnection.rollback();
	    closeConnection();
	} catch (SQLException e) {
	    throw new ExcepcaoPersistencia(e);
	}
    }

    public synchronized PreparedStatement prepareStatement(String statement) throws ExcepcaoPersistencia {
	Connection thisConnection = (Connection) connectionsMap.get(Thread.currentThread());
	if (thisConnection == null) {
	    try {
		openConnection();
		thisConnection = DriverManager.getConnection(databaseUrl);
	    } catch (java.sql.SQLException e) {
		throw new ExcepcaoPersistencia(e);
	    }
	}
	PreparedStatement sql = null;
	try {
	    sql = thisConnection.prepareStatement(statement);
	} catch (java.sql.SQLException e) {
	    throw new ExcepcaoPersistencia(e);
	}
	return sql;
    }

    public synchronized CallableStatement prepareCall(String statement) throws ExcepcaoPersistencia {
	Connection thisConnection = (Connection) connectionsMap.get(Thread.currentThread());
	if (thisConnection == null) {
	    try {
		openConnection();
		thisConnection = DriverManager.getConnection(databaseUrl);
	    } catch (java.sql.SQLException e) {
		throw new ExcepcaoPersistencia(e);
	    }
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

}
