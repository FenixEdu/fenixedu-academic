package net.sourceforge.fenixedu.persistenceTierOracle.Oracle;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.IPersistentSuportOracle;

public class PersistentSuportOracle implements IPersistentSuportOracle {

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

    private void openConnection() throws ExcepcaoPersistencia {
        setDatabaseUrl();
        try {
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        } catch (SQLException e) {
            throw new ExcepcaoPersistencia();
        }
    }

    private void closeConnection() throws ExcepcaoPersistencia {
        Connection thisconnection = connectionsMap.get(Thread.currentThread());
        try {
            thisconnection.close();
            connectionsMap.remove(Thread.currentThread());
        } catch (Exception e) {
            throw new ExcepcaoPersistencia();
        }
    }

    @Override
    public synchronized void startTransaction() throws ExcepcaoPersistencia {
        try {
            openConnection();
            Connection connection = DriverManager.getConnection(getDatabaseUrl());
            connection.setAutoCommit(false);
            connectionsMap.put(Thread.currentThread(), connection);
        } catch (SQLException e) {
            throw new ExcepcaoPersistencia(e);
        }
    }

    @Override
    public synchronized void commitTransaction() throws ExcepcaoPersistencia {
        Connection thisConnection = connectionsMap.get(Thread.currentThread());
        try {
            thisConnection.commit();
            closeConnection();
        } catch (SQLException e) {
            throw new ExcepcaoPersistencia(e);
        }
    }

    @Override
    public synchronized void cancelTransaction() throws ExcepcaoPersistencia {
        Connection thisConnection = connectionsMap.get(Thread.currentThread());
        try {
            thisConnection.rollback();
            closeConnection();
        } catch (SQLException e) {
            throw new ExcepcaoPersistencia(e);
        }
    }

    @Override
    public synchronized PreparedStatement prepareStatement(String statement) throws ExcepcaoPersistencia {
        Connection thisConnection = connectionsMap.get(Thread.currentThread());
        if (thisConnection == null) {
            try {
                openConnection();
                thisConnection = DriverManager.getConnection(getDatabaseUrl());
            } catch (java.sql.SQLException e) {
                throw new ExcepcaoPersistencia(e);
            }
        }
        PreparedStatement sql = null;
        try {
            sql = thisConnection.prepareStatement(statement);
        } catch (java.sql.SQLException e) {
        }
        return sql;
    }

    public synchronized CallableStatement prepareCall(String statement) throws ExcepcaoPersistencia {
        Connection thisConnection = connectionsMap.get(Thread.currentThread());
        if (thisConnection == null) {

            try {
                openConnection();
                thisConnection = DriverManager.getConnection(getDatabaseUrl());
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

    public String getDatabaseUrl() throws ExcepcaoPersistencia {
        return null;
    }

    public void setDatabaseUrl() throws ExcepcaoPersistencia {
    }
}
