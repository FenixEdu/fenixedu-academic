package net.sourceforge.fenixedu.applicationTier.Servicos.MasterDegree.administrativeOffice.student.studentCurricularPlan;

import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.Autenticacao;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.tools.dbaccess;
import net.sourceforge.fenixedu.util.TipoCurso;

/**
 * @author Tânia Pousão Created on 6/Out/2003
 */
public class ReadPosGradStudentCurricularPlanByIdTest extends TestCase {

    protected dbaccess dbAcessPoint = null;

    protected IUserView userView = null;

    public ReadPosGradStudentCurricularPlanByIdTest(String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ReadPosGradStudentCurricularPlanByIdTest.class);
        return suite;
    }

    protected String getApplication() {
        return Autenticacao.INTRANET;
    }

    protected String getDataSetFilePath() {
        return "etc/testDataSetForStudentCurricularPlanPosGrad.xml";
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

        //Test with user that isn´t master degree administrator
        String args1[] = { "alunoGrad", "pass", this.getApplication() };

        try {
            this.userView = (IUserView) ServiceManagerServiceFactory.executeService(null,
                    "Autenticacao", args1);
        } catch (Exception ex) {
            System.out.println(ex.toString());
            System.out
                    .println("Don´t Authenticating User, because user isn't masterDegree Administrated!");
        }

        //Test with user that is master degree administrator
        String args2[] = { "posGrad", "pass", this.getApplication() };

        try {
            this.userView = (IUserView) ServiceManagerServiceFactory.executeService(null,
                    "Autenticacao", args2);
        } catch (Exception ex) {
            System.out.println(ex.toString());
            fail("Authenticating User!");
        }
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

    public void testReadPosGradStudentCurricularPlanById() {
        InfoStudentCurricularPlan infoStudentCurricularPlan = null;

        //Test with master degree student, alunoPosGrad, with curricular 1
        System.out.println("test Case 1-testReadPosGradStudentCurricularPlanById- with valid data");
        Integer studentCurricularPlanId = new Integer(1);

        Object args3[] = { studentCurricularPlanId };

        try {
            infoStudentCurricularPlan = (InfoStudentCurricularPlan) ServiceManagerServiceFactory
                    .executeService(userView, "ReadPosGradStudentCurricularPlanById", args3);
        } catch (FenixServiceException ex) {
            System.out.println(ex.toString());
            fail("Reading the Student's Curricular Plans of a PosGrad");
        } catch (Exception ex) {
            System.out.println(ex.toString());
            fail("Reading the Student's Curricular Plans of a PosGrad");
        }

        assertNotNull("Find correct Student Curricular Plan", infoStudentCurricularPlan);
        assertEquals("Find correct Student of the Student curricular Plan", infoStudentCurricularPlan
                .getInfoStudent().getNumber(), new Integer(6000));
        assertEquals("Expect Student is master degree student", infoStudentCurricularPlan
                .getInfoStudent().getDegreeType(), new TipoCurso(2));
        assertEquals("Find enrolments of the Student curricular Plan", new Integer(
                infoStudentCurricularPlan.getInfoEnrolments().size()), new Integer(4));

        System.out.println("test case 1 ok!");

        //Test with degree student, alunoGrad, with curricular 2
        System.out.println("test Case 2-testReadPosGradStudentCurricularPlanById- with invalid data");
        studentCurricularPlanId = new Integer(2);

        Object args4[] = { studentCurricularPlanId };

        try {
            infoStudentCurricularPlan = (InfoStudentCurricularPlan) ServiceManagerServiceFactory
                    .executeService(userView, "ReadPosGradStudentCurricularPlanById", args4);
        } catch (FenixServiceException ex) {
            System.out.println(ex.toString());
            fail("Reading the Student's Curricular Plans");
        } catch (Exception ex) {
            System.out.println(ex.toString());
            fail("Reading the Student's Curricular Plans");
        }

        assertNotNull("Find correct Student Curricular Plan", infoStudentCurricularPlan);
        assertEquals("Find correct Student of the Student curricular Plan", infoStudentCurricularPlan
                .getInfoStudent().getNumber(), new Integer(7000));
        assertEquals("Expect Student is degree student", infoStudentCurricularPlan.getInfoStudent()
                .getDegreeType(), new TipoCurso(1));

        System.out.println("test case 2 ok!");
    }
}