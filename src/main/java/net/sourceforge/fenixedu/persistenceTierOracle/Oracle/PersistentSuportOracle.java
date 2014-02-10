package net.sourceforge.fenixedu.persistenceTierOracle.Oracle;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class PersistentSuportOracle {

    private Map<Thread, Connection> connectionsMap = new HashMap<Thread, Connection>();

    public class ConnectionProperties {
        protected String userNamePropertyName;

        protected String userPassPropertyName;

        protected String urlPropertyName;

        public ConnectionProperties(String userNamePropertyName, String userPassPropertyName, String urlPropertyName) {
            super();
            this.userNamePropertyName = userNamePropertyName;
            this.userPassPropertyName = userPassPropertyName;
            this.urlPropertyName = urlPropertyName;
        }
    }

    protected ConnectionProperties connectionProperties;

    public PersistentSuportOracle(String userNamePropertyName, String userPassPropertyName, String urlPropertyName) {
        connectionProperties = new ConnectionProperties(userNamePropertyName, userPassPropertyName, urlPropertyName);
    }

    private void openConnection() throws SQLException {
        setDatabaseUrl();
        DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
    }

    private void closeConnection() throws SQLException {
        Connection thisconnection = connectionsMap.get(Thread.currentThread());
        thisconnection.close();
        connectionsMap.remove(Thread.currentThread());
    }

    public synchronized void startTransaction() throws SQLException {
        openConnection();
        Connection connection = DriverManager.getConnection(getDatabaseUrl());
        connection.setAutoCommit(false);
        connectionsMap.put(Thread.currentThread(), connection);
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
            openConnection();
            thisConnection = DriverManager.getConnection(getDatabaseUrl());
        }
        PreparedStatement sql = null;
        try {
            sql = thisConnection.prepareStatement(statement);
        } catch (java.sql.SQLException e) {
        }
        return sql;
    }

    public synchronized CallableStatement prepareCall(String statement) throws SQLException {
        Connection thisConnection = connectionsMap.get(Thread.currentThread());
        if (thisConnection == null) {
            openConnection();
            thisConnection = DriverManager.getConnection(getDatabaseUrl());
        }
        return thisConnection.prepareCall(statement);
    }

    public String getDatabaseUrl() {
        return null;
    }

    public void setDatabaseUrl() {
    }
}
