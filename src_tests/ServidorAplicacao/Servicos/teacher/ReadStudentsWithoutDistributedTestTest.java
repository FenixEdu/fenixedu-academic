/*
 * Created on 19/Set/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servicos.teacher;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.applicationTier.Servicos.TestCaseReadServices;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

/**
 * @author Susana Fernandes
 */
public class ReadStudentsWithoutDistributedTestTest extends TestCaseReadServices {

    /**
     * @param testName
     */
    public ReadStudentsWithoutDistributedTestTest(String testName) {
        super(testName);

    }

    protected String getNameOfServiceToBeTested() {
        return "ReadStudentsWithoutDistributedTest";
    }

    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
        return null;
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
        Object[] args = { new Integer(26), new Integer(25) };
        return args;
    }

    protected int getNumberOfItemsToRetrieve() {
        return 2;
    }

    protected Object getObjectToCompare() {
        List infoStudentList = new ArrayList();
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            IStudent student10 = (IStudent) sp.getIPersistentStudent().readByOID(Student.class,
                    new Integer(10));

            IStudent student11 = (IStudent) sp.getIPersistentStudent().readByOID(Student.class,
                    new Integer(11));

            assertNotNull("student10 null", student10);
            assertNotNull("student11 null", student11);

            sp.confirmarTransaccao();

            InfoStudent infoStudent10 = Cloner.copyIStudent2InfoStudent(student10);
            InfoStudent infoStudent11 = Cloner.copyIStudent2InfoStudent(student11);

            infoStudentList.add(infoStudent10);
            infoStudentList.add(infoStudent11);
        } catch (ExcepcaoPersistencia e) {
            fail("exception: ExcepcaoPersistencia ");
        }
        return infoStudentList;
    }

    protected boolean needsAuthorization() {
        return true;
    }
}