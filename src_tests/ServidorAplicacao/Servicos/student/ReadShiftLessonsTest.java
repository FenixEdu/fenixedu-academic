/*
 * ReadShiftLessonsTest.java JUnit based test
 * 
 * Created on January 4th, 2003, 23:35
 */

package net.sourceforge.fenixedu.applicationTier.Servicos.student;

/**
 * @author tfc130
 */
import junit.framework.Test;
import junit.framework.TestSuite;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.applicationTier.Servicos.TestCaseReadServices;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

public class ReadShiftLessonsTest extends TestCaseReadServices {

    public ReadShiftLessonsTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ReadShiftLessonsTest.class);

        return suite;
    }

    protected void setUp() {
        super.setUp();
    }

    protected void tearDown() {
        super.tearDown();
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadShiftLessons";
    }

    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {

        IExecutionCourse executionCourse = null;
        try {
            PersistenceSupportFactory.getDefaultPersistenceSupport().iniciarTransaccao();
            executionCourse = PersistenceSupportFactory.getDefaultPersistenceSupport().getIPersistentExecutionCourse()
                    .readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
            assertNotNull(executionCourse);
            PersistenceSupportFactory.getDefaultPersistenceSupport().confirmarTransaccao();
            Object[] result = { new InfoShift("turnoINEX", null, null, (InfoExecutionCourse) Cloner
                    .get(executionCourse)) };
            return result;
        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
        }
        return null;
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
        IExecutionCourse executionCourse = null;
        try {
            PersistenceSupportFactory.getDefaultPersistenceSupport().iniciarTransaccao();
            executionCourse = PersistenceSupportFactory.getDefaultPersistenceSupport().getIPersistentExecutionCourse()
                    .readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
            assertNotNull(executionCourse);
            PersistenceSupportFactory.getDefaultPersistenceSupport().confirmarTransaccao();
            Object[] result = { new InfoShift("turno4", null, null, (InfoExecutionCourse) Cloner
                    .get(executionCourse)) };
            return result;
        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
        }
        return null;
    }

    protected int getNumberOfItemsToRetrieve() {
        return 1;
    }

    protected Object getObjectToCompare() {
        return null;
    }

}