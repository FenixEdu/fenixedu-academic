/*
 * Created on Jan 10, 2005
 *
 */
package ServidorPersistenteOracle.Oracle;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistenteOracle.IPersistentExpensesReport;
import ServidorPersistenteOracle.IPersistentProject;
import ServidorPersistenteOracle.IPersistentProjectUser;
import ServidorPersistenteOracle.IPersistentReport;
import ServidorPersistenteOracle.IPersistentRubric;
import ServidorPersistenteOracle.IPersistentSuportOracle;

/**
 * @author Susana Fernandes
 * 
 */
public class PersistentSuportOracle implements IPersistentSuportOracle {
    private static PersistentSuportOracle instance = null;

    private static String databaseUrl = null;

    private static Connection connection = null;

    private static Map connectionsMap = new HashMap();

    public static synchronized PersistentSuportOracle getInstance() throws ExcepcaoPersistencia {
        if (instance == null) {
            instance = new PersistentSuportOracle();
        }
        return instance;
    }

    private void openConnection() throws ExcepcaoPersistencia {
        if (databaseUrl == null) {
            Properties properties = new Properties();
            InputStream inputStream = instance.getClass().getResourceAsStream("/projectsManagement.properties");
            try {
                properties.load(inputStream);
            } catch (IOException e) {
                throw new ExcepcaoPersistencia(e.getMessage());
            }
            String DBUserName = properties.getProperty("db.projectManagement.user");
            String DBUserPass = properties.getProperty("db.projectManagement.pass");
            String DBUrl = properties.getProperty("db.projectManagement.alias");
            if (DBUserName == null || DBUserPass == null || DBUrl == null) {
                System.out.println("ServidorPersistenteOracle: Erro ao ler propriedades.");
                throw new ExcepcaoPersistencia();
            }
            databaseUrl = new String("jdbc:oracle:thin:" + DBUserName + "/" + DBUserPass + "@" + DBUrl);
            System.out.println("databaseUrl: " + databaseUrl);
        }
        try {
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        } catch (SQLException e) {
            System.out.println("ServidorPersistenteOracle: Erro openConnection." + e);
            throw new ExcepcaoPersistencia();
        }
    }

    private void closeConnection() throws ExcepcaoPersistencia {
        Connection thisconnection = (Connection) connectionsMap.get(Thread.currentThread());
        try {
            thisconnection.close();
            connectionsMap.remove(Thread.currentThread());
        } catch (Exception e) {
            System.out.println("ServidorPersistenteOracle: Erro closeConnection." + e);
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
            System.out.println("ServidorPersistenteOracle: Erro startTransaction." + e);
            throw new ExcepcaoPersistencia();
        }
    }

    public synchronized void commitTransaction() throws ExcepcaoPersistencia {
        Connection thisConnection = (Connection) connectionsMap.get(Thread.currentThread());
        try {
            thisConnection.commit();
            closeConnection();
        } catch (SQLException e) {
            System.out.println("ServidorPersistenteOracle: Erro commitTransaction." + e);
            throw new ExcepcaoPersistencia();
        }
    }

    public synchronized void cancelTransaction() throws ExcepcaoPersistencia {
        Connection thisConnection = (Connection) connectionsMap.get(Thread.currentThread());
        try {
            thisConnection.rollback();
            closeConnection();
        } catch (SQLException e) {
            System.out.println("ServidorPersistenteOracle: Erro cancelTransaction." + e);
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
                    System.out.println("ServidorPersistenteOracle: Erro prepareStatement1." + e);
                }
            thisConnection = connection;
        }
        PreparedStatement sql = null;
        try {
            sql = thisConnection.prepareStatement(statement);
        } catch (java.sql.SQLException e) {
            System.out.println("ServidorPersistenteOracle: Erro prepareStatement2." + e);
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

    public IPersistentReport getIPersistentSummaryReport() {
        return new PersistentSummaryReport();
    }

    public IPersistentReport getIPersistentAdiantamentosReport() {
        return new PersistentAdiantamentosReport();
    }

    public IPersistentReport getIPersistentCabimentosReport() {
        return new PersistentCabimentosReport();
    }

    public IPersistentReport getIPersistentSummaryPTEReport() {
        return new PersistentSummaryPTEReport();
    }

    public IPersistentReport getIPersistentSummaryEURReport() {
        return new PersistentSummaryEURReport();
    }

}
