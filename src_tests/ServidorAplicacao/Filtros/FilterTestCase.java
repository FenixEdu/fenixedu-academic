/*
 * Created on 06/Out/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtros;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

import junit.framework.TestCase;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;

import pt.utl.ist.berserk.ServiceRequest;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

/**
 * @author <a href="mailto:cgmp@mega.ist.utl.pt">Carlos Pereira </a> & <a href="mailto:fmmp@mega.ist.utl.pt">Francisco Passos </a>
 */
public abstract class FilterTestCase extends TestCase {

    public FilterTestCase(String name) {
        super(name);
    }

    protected IDatabaseConnection getConnection() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        Connection jdbcConnection = DriverManager.getConnection("jdbc:mysql://localhost/ciapl", "root",
                "");
        return new DatabaseConnection(jdbcConnection);
    }

    protected IDataSet getDataSet() throws Exception {
        return new FlatXmlDataSet(new File(getDataSetFilePath()));
    }

    protected void setUp() {

        try {
            super.setUp();
            IDatabaseConnection connection = getConnection();
            IDataSet dataSet = getDataSet();

            IDataSet fullDataSet = connection.createDataSet();
            DatabaseOperation.DELETE_ALL.execute(connection, fullDataSet);

            DatabaseOperation.INSERT.execute(connection, dataSet);
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();
            sp.clearCache();
            sp.confirmarTransaccao();

            connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Setup failed loading database with test data set: " + ex);
        }
    }

    protected void tearDown() throws Exception {
    }

    protected abstract String getDataSetFilePath();

    protected abstract String getNameOfFilterToBeTested();
    
    protected abstract ServiceRequest createWorkingRequest();
    
    protected abstract ServiceRequest createNonWorkingRequest();

}