package net.sourceforge.fenixedu.applicationTier.Servicos.student;

import junit.framework.Test;
import junit.framework.TestSuite;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.applicationTier.Servicos.TestCaseReadServices;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.util.TipoAula;

public class ReadShiftsByTypeFromExecutionCourseServicesTest extends TestCaseReadServices {
    public ReadShiftsByTypeFromExecutionCourseServicesTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ReadShiftsByTypeFromExecutionCourseServicesTest.class);

        return suite;
    }

    protected void setUp() {
        super.setUp();
    }

    protected void tearDown() {
        super.tearDown();
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadShiftsByTypeFromExecutionCourse";
    }

    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {

        IExecutionCourse executionCourse = null;
        try {
            PersistenceSupportFactory.getDefaultPersistenceSupport().iniciarTransaccao();
            executionCourse = PersistenceSupportFactory.getDefaultPersistenceSupport().getIPersistentExecutionCourse()
                    .readBySiglaAndAnoLectivoAndSiglaLicenciatura("APR", "2002/2003", "LEIC");
            assertNotNull(executionCourse);
            PersistenceSupportFactory.getDefaultPersistenceSupport().confirmarTransaccao();
            Object[] result = { Cloner.get(executionCourse), new TipoAula(TipoAula.RESERVA) };
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
            Object[] result = { Cloner.get(executionCourse), new TipoAula(TipoAula.TEORICA) };
            return result;
        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
        }

        return null;
    }

    protected int getNumberOfItemsToRetrieve() {
        return 5;
    }

    protected Object getObjectToCompare() {
        return null;
    }

}