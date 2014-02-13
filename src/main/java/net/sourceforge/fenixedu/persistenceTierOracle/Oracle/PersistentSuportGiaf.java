package net.sourceforge.fenixedu.persistenceTierOracle.Oracle;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.fenixedu.FenixIstConfiguration;

public class PersistentSuportGiaf {
    private static PersistentSuportGiaf instance = null;

    private static String databaseUrl = null;

    private static Map<Thread, Connection> connectionsMap = new HashMap<>();

    public static synchronized PersistentSuportGiaf getInstance() {
        if (instance == null) {
            instance = new PersistentSuportGiaf();
        }
        return instance;
    }

    private Connection openConnection() throws SQLException {
        if (databaseUrl == null) {
            String DBUserName = FenixIstConfiguration.getConfiguration().dbGiafUser();
            String DBUserPass = FenixIstConfiguration.getConfiguration().dbGiafPass();
            String DBUrl = FenixIstConfiguration.getConfiguration().dbGiafAlias();
            if (DBUserName == null || DBUserPass == null || DBUrl == null) {
                throw new Error("Please configure GIAF database connection");
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
        DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        Connection connection = DriverManager.getConnection(databaseUrl);
        connectionsMap.put(Thread.currentThread(), connection);
        return connection;
    }

    public void closeConnection() throws SQLException {
        Connection thisconnection = connectionsMap.get(Thread.currentThread());
        if (thisconnection != null) {
            thisconnection.close();
            connectionsMap.remove(Thread.currentThread());
        }
    }

    public synchronized void startTransaction() throws SQLException {
        Connection connection = openConnection();
        connection.setAutoCommit(false);
    }

    public synchronized void commitTransaction() throws SQLException {
        Connection thisConnection = connectionsMap.get(Thread.currentThread());
        thisConnection.commit();
        closeConnection();
    }

    public synchronized void cancelTransaction() throws SQLException {
        Connection thisConnection = connectionsMap.get(Thread.currentThread());
        thisConnection.rollback();
        closeConnection();
    }

    public synchronized PreparedStatement prepareStatement(String statement) throws SQLException {
        Connection thisConnection = connectionsMap.get(Thread.currentThread());
        if (thisConnection == null) {
            thisConnection = openConnection();
        }
        return thisConnection.prepareStatement(statement);
    }

    public synchronized CallableStatement prepareCall(String statement) throws SQLException {
        Connection thisConnection = connectionsMap.get(Thread.currentThread());
        if (thisConnection == null) {
            thisConnection = openConnection();
        }
        return thisConnection.prepareCall(statement);
    }

}
