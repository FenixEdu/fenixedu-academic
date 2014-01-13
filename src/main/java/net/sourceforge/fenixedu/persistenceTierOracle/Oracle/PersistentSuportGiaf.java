package net.sourceforge.fenixedu.persistenceTierOracle.Oracle;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.fenixedu.FenixIstConfiguration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

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

    private Connection openConnection() throws ExcepcaoPersistencia {
        if (databaseUrl == null) {
            String DBUserName = FenixIstConfiguration.getConfiguration().dbGiafUser();
            String DBUserPass = FenixIstConfiguration.getConfiguration().dbGiafPass();
            String DBUrl = FenixIstConfiguration.getConfiguration().dbGiafAlias();
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
            Connection connection = DriverManager.getConnection(databaseUrl);
            connectionsMap.put(Thread.currentThread(), connection);
            connectionsMap.put(Thread.currentThread(), connection);
            return connection;
        } catch (SQLException e) {
            throw new ExcepcaoPersistencia();
        }
    }

    public void closeConnection() throws ExcepcaoPersistencia {
        Connection thisconnection = connectionsMap.get(Thread.currentThread());
        if (thisconnection != null) {
            try {
                thisconnection.close();
                connectionsMap.remove(Thread.currentThread());
            } catch (Exception e) {
                throw new ExcepcaoPersistencia();
            }
        }
    }

    public synchronized void startTransaction() throws ExcepcaoPersistencia {
        try {
            Connection connection = openConnection();
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new ExcepcaoPersistencia("", e);
        }
    }

    public synchronized void commitTransaction() throws ExcepcaoPersistencia {
        Connection thisConnection = connectionsMap.get(Thread.currentThread());
        try {
            thisConnection.commit();
            closeConnection();
        } catch (SQLException e) {
            throw new ExcepcaoPersistencia(e);
        }
    }

    public synchronized void cancelTransaction() throws ExcepcaoPersistencia {
        Connection thisConnection = connectionsMap.get(Thread.currentThread());
        try {
            thisConnection.rollback();
            closeConnection();
        } catch (SQLException e) {
            throw new ExcepcaoPersistencia(e);
        }
    }

    public synchronized PreparedStatement prepareStatement(String statement) throws ExcepcaoPersistencia {
        Connection thisConnection = connectionsMap.get(Thread.currentThread());
        if (thisConnection == null) {
            thisConnection = openConnection();
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
        Connection thisConnection = connectionsMap.get(Thread.currentThread());
        if (thisConnection == null) {
            thisConnection = openConnection();
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
