package ServidorAplicacao.Servicos.sop;

import java.util.List;

import framework.factory.ServiceManagerServiceFactory;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import DataBeans.InfoExecutionYear;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Tools.dbaccess;

/**
 * @author Tânia Pousão Created on 9/Out/2003
 */
public class ReadExecutionDegreesByExecutionYearTest extends TestCase {

    protected dbaccess dbAcessPoint = null;

    public ReadExecutionDegreesByExecutionYearTest(String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ReadExecutionDegreesByExecutionYearTest.class);
        return suite;
    }

    protected String getApplication() {
        return Autenticacao.EXTRANET;
    }

    protected String getDataSetFilePath() {
        return "etc/testDataSetShowDegrees.xml";
    }

    protected void setUp() {

        try {
            dbAcessPoint = new dbaccess();
            dbAcessPoint.setDbName("fenixTeste");
            dbAcessPoint.openConnection();
            //dbAcessPoint.backUpDataBaseContents("etc/testBackup.xml");
            dbAcessPoint.loadDataBase(getDataSetFilePath());
            dbAcessPoint.closeConnection();
        } catch (Exception ex) {
            System.out.println(ex.toString());
            fail("Loading Database With Test Data Set!");
        }

        SuportePersistenteOJB.resetInstance();
    }

    protected void tearDown() {
        //		try {
        //			dbAcessPoint.openConnection();
        //			dbAcessPoint.loadDataBase(this.getDBBackupDataSetFilePath());
        //			dbAcessPoint.closeConnection();
        //		} catch (Exception ex) {
        //			System.out.println(ex.toString());
        //			fail("Loading Database With DB Backup Data Set!");
        //		}
    }

    public void testReadAllDegreesTest() {
        List degreesList = null;

        System.out.println("test Case 1-ReadAllDegreesTest- with valid data");

        try {

            InfoExecutionYear executionYear = new InfoExecutionYear("2003/2004");
            Object[] args = { executionYear };

            degreesList = (List) ServiceManagerServiceFactory.executeService(null,
                    "ReadExecutionDegreesByExecutionYear", args);
        } catch (FenixServiceException ex) {
            ex.printStackTrace();
            System.out.println(ex.toString());
            fail("Reading all degrees a FenixServiceException");
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.toString());
            fail("Reading all degrees a Exception");
        }

        assertNotNull("Find all degrees", degreesList);
        assertEquals("Find all degrees", degreesList.size(), 1);

        System.out.println("test case 1 ok!");

        System.out.println("test Case 2-ReadAllDegreesTest- with no execution year");

        try {
            Object[] args = { null };

            degreesList = (List) ServiceManagerServiceFactory.executeService(null,
                    "ReadExecutionDegreesByExecutionYear", args);
        } catch (FenixServiceException ex) {
            System.out.println(ex.toString());
            fail("Reading all degrees a FenixServiceException");
        } catch (Exception ex) {
            System.out.println(ex.toString());
            fail("Reading all degrees a Exception");
        }

        assertNotNull("Find all degrees", degreesList);
        assertEquals("Find all degrees", degreesList.size(), 2);

        System.out.println("test case 2 ok!");
    }
}