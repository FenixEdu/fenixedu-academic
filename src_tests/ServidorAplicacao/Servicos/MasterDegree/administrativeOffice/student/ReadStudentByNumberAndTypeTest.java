package net.sourceforge.fenixedu.applicationTier.Servicos.MasterDegree.administrativeOffice.student;

import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.Autenticacao;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.tools.dbaccess;
import net.sourceforge.fenixedu.util.TipoCurso;

/**
 * @author David Santos 2/Out/2003
 */

public class ReadStudentByNumberAndTypeTest extends TestCase {

    protected dbaccess dbAcessPoint = null;

    protected IUserView userView = null;

    public ReadStudentByNumberAndTypeTest(String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ReadStudentByNumberAndTypeTest.class);
        return suite;
    }

    protected String getApplication() {
        return Autenticacao.INTRANET;
    }

    protected String getDataSetFilePath() {
        return "etc/testDataSetForStudent.xml";
    }

    protected void setUp() {

        try {
            dbAcessPoint = new dbaccess();
            dbAcessPoint.openConnection();
            dbAcessPoint.backUpDataBaseContents("etc/testBackup.xml");
            dbAcessPoint.loadDataBase(getDataSetFilePath());
            dbAcessPoint.closeConnection();
        } catch (Exception ex) {
            System.out.println(ex.toString());
            fail("Loading Database With Test Data Set!");
        }

        SuportePersistenteOJB.resetInstance();

        String args[] = { "user", "pass", this.getApplication() };

        try {
            this.userView = (IUserView) ServiceManagerServiceFactory.executeService(null,
                    "Autenticacao", args);
        } catch (Exception ex) {
            System.out.println(ex.toString());
            fail("Authenticating User!");
        }
    }

    protected void tearDown() {
    }

    public void testReadStudentByNumberAndTypeTest() {
        Integer studentNumber = new Integer(5212);
        TipoCurso tipoCurso = new TipoCurso(TipoCurso.MESTRADO);
        Object args1[] = { studentNumber, tipoCurso };
        IStudent student = new Student();
        try {
            student = (IStudent) ServiceManagerServiceFactory.executeService(userView,
                    "ReadStudentByNumberAndType", args1);
        } catch (FenixServiceException ex) {
            System.out.println(ex.toString());
            fail("Reading One Student");
        } catch (Exception ex) {
            System.out.println(ex.toString());
            fail("Reading One Student");
        }

        assertNotNull(student);
        System.out.println("\nOK - One Student Read!");

        /*
         * Object args2[] = {null, null , null, null};
         * 
         * try { result = (List) this.serviceManager.executar(userView,
         * "ReadStudentsByNameIDnumberIDtypeAndStudentNumber", args2); } catch
         * (FenixServiceException ex) { System.out.println(ex.toString());
         * fail("Reading All Students"); } catch (Exception ex) {
         * System.out.println(ex.toString()); fail("Reading One Student"); }
         * 
         * assertEquals(result.size(), 528); System.out.println("OK - All
         * Students Read!");
         */
    }
}