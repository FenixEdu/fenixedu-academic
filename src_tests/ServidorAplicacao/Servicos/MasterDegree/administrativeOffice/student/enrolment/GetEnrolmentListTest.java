package net.sourceforge.fenixedu.applicationTier.Servicos.MasterDegree.administrativeOffice.student.enrolment;

/**
 * 
 * @author Nuno Nunes & Joana Mota
 */

import junit.framework.Test;
import junit.framework.TestSuite;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.applicationTier.Servicos.TestCaseReadServicesIntranet;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.util.EnrollmentState;
import net.sourceforge.fenixedu.util.Specialization;
import net.sourceforge.fenixedu.util.TipoCurso;

public class GetEnrolmentListTest extends TestCaseReadServicesIntranet {

    public GetEnrolmentListTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(GetEnrolmentListTest.class);

        return suite;
    }

    protected void setUp() {
        super.setUp();
    }

    protected void tearDown() {
        super.tearDown();
    }

    protected String getNameOfServiceToBeTested() {
        return "GetEnrolmentList";
    }

    protected int getNumberOfItemsToRetrieve() {
        return 2;
    }

    protected Object getObjectToCompare() {

        return null;
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
        ISuportePersistente sp = null;
        InfoStudentCurricularPlan infoStudentCurricularPlan = null;

        try {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();
            IStudentCurricularPlan iStudentCurricularPlan = sp.getIStudentCurricularPlanPersistente()
                    .readActiveStudentAndSpecializationCurricularPlan(new Integer(46865),
                            new TipoCurso(TipoCurso.MESTRADO_STRING),
                            new Specialization(Specialization.ESPECIALIZACAO_STRING));
            assertNotNull(iStudentCurricularPlan);

            infoStudentCurricularPlan = Cloner
                    .copyIStudentCurricularPlan2InfoStudentCurricularPlan(iStudentCurricularPlan);
            sp.confirmarTransaccao();

        } catch (Exception e) {
            fail("Error !");
        }

        Object[] args = { infoStudentCurricularPlan, EnrollmentState.ENROLLED };

        return args;

    }

    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
        return null;
    }
}