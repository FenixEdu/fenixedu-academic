/*
 *
 * Created on 03/09/2003
 */

package ServidorAplicacao.Servicos.student;

/**
 * 
 * @author asnr and scpo
 */
import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoSiteStudentsWithoutGroup;
import DataBeans.util.Cloner;
import Dominio.IStudent;
import Dominio.Student;
import ServidorAplicacao.Servicos.TestCaseReadServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ReadStudentsWithoutGroupTest extends TestCaseReadServices {

    public ReadStudentsWithoutGroupTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ReadStudentsWithoutGroupTest.class);

        return suite;
    }

    protected void setUp() {
        super.setUp();
    }

    protected void tearDown() {
        super.tearDown();
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadStudentsWithoutGroup";
    }

    //WHEN ALL STUDENTS HAVE GROUPS
    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
        return null;
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
        Object[] result = { new Integer(3), "user" };
        return result;

    }

    protected int getNumberOfItemsToRetrieve() {
        return 0;
    }

    protected Object getObjectToCompare() {
        InfoSiteStudentsWithoutGroup infoSiteStudentsWithoutGroup = new InfoSiteStudentsWithoutGroup();

        try {
            ISuportePersistente ps = SuportePersistenteOJB.getInstance();
            ps.iniciarTransaccao();
            IStudent student = null;
            List infoStudentList = new ArrayList();

            student = (IStudent) ps.getIPersistentStudent().readByOID(Student.class, new Integer(6));
            infoStudentList.add(Cloner.copyIStudent2InfoStudent(student));

            student = (IStudent) ps.getIPersistentStudent().readByOID(Student.class, new Integer(7));
            infoStudentList.add(Cloner.copyIStudent2InfoStudent(student));

            student = (IStudent) ps.getIPersistentStudent().readByOID(Student.class, new Integer(10));
            infoStudentList.add(Cloner.copyIStudent2InfoStudent(student));

            student = (IStudent) ps.getIPersistentStudent().readByOID(Student.class, new Integer(11));
            infoStudentList.add(Cloner.copyIStudent2InfoStudent(student));

            infoSiteStudentsWithoutGroup.setGroupNumber(new Integer(5));
            infoSiteStudentsWithoutGroup.setInfoStudentList(infoStudentList);

        } catch (ExcepcaoPersistencia e) {
            System.out.println("failed setting up the test data");
        }
        return infoSiteStudentsWithoutGroup;
    }

}