package net.sourceforge.fenixedu.applicationTier.Servicos.publico;

import junit.framework.Test;
import junit.framework.TestSuite;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ICurso;
import net.sourceforge.fenixedu.domain.ICursoExecucao;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.applicationTier.Servicos.TestCaseServicos;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ICursoPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

/**
 * @author tfc130
 *  
 */
public class ReadExecutionDegreesByExecutionYearAndDegreeInitialsTest extends TestCaseServicos {

    private InfoExecutionDegree infoExecutionDegree = null;

    private String degreeInitials = null;

    private String nameDegreeCurricularPlan = null;

    private InfoExecutionYear infoExecutionYear = null;

    /**
     * Constructor for SelectClassesTest.
     */
    public ReadExecutionDegreesByExecutionYearAndDegreeInitialsTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ReadExecutionDegreesByExecutionYearAndDegreeInitialsTest.class);

        return suite;
    }

    protected void setUp() {
        super.setUp();
    }

    protected void tearDown() {
        super.tearDown();
    }

    public void testReadAll() {

        Object argsReadExecutionDegreesByExecutionYearAndDegreeInitials[] = new Object[3];

        Object result = null;

        // executionDegree exists in database
        this.prepareTestCase(true);

        argsReadExecutionDegreesByExecutionYearAndDegreeInitials[0] = this.infoExecutionYear;
        argsReadExecutionDegreesByExecutionYearAndDegreeInitials[1] = this.degreeInitials;
        argsReadExecutionDegreesByExecutionYearAndDegreeInitials[2] = this.nameDegreeCurricularPlan;

        try {
            result = ServiceManagerServiceFactory.executeService(_userView,
                    "ReadExecutionDegreesByExecutionYearAndDegreeInitials",
                    argsReadExecutionDegreesByExecutionYearAndDegreeInitials);
            assertTrue(((InfoExecutionDegree) result).equals(infoExecutionDegree));

        } catch (Exception ex) {
            fail("testReadAll: executionCourse with 1 curricularCourses: " + ex);
        }

        // Empty database - executionDegrees does not exist in db
        this.prepareTestCase(false);
        argsReadExecutionDegreesByExecutionYearAndDegreeInitials[0] = this.infoExecutionYear;
        argsReadExecutionDegreesByExecutionYearAndDegreeInitials[1] = this.degreeInitials;
        argsReadExecutionDegreesByExecutionYearAndDegreeInitials[2] = this.nameDegreeCurricularPlan;

        try {
            result = ServiceManagerServiceFactory.executeService(_userView,
                    "ReadExecutionDegreesByExecutionYearAndDegreeInitials",
                    argsReadExecutionDegreesByExecutionYearAndDegreeInitials);
            assertNull(result);
        } catch (Exception ex) {
            fail("testReadAll: no curricularCourses of executionCourse: " + ex);
        }

    }

    private void prepareTestCase(boolean exists) {

        ISuportePersistente sp = null;

        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            sp.iniciarTransaccao();

            ICursoPersistente cursoPersistente = sp.getICursoPersistente();
            ICurso degree = cursoPersistente.readBySigla("LEIC");
            assertNotNull(degree);

            IPersistentDegreeCurricularPlan planoCurricularCursoPersistente = sp
                    .getIPersistentDegreeCurricularPlan();
            IDegreeCurricularPlan degreeCurricularPlan = planoCurricularCursoPersistente
                    .readByNameAndDegree("plano1", degree);
            assertNotNull(degreeCurricularPlan);

            IPersistentExecutionYear persistenExecutionYear = sp.getIPersistentExecutionYear();
            IExecutionYear executionYear = persistenExecutionYear.readExecutionYearByName("2002/2003");
            assertNotNull(executionYear);

            IPersistentExecutionDegree cursoExecucaoPersistente = sp.getIPersistentExecutionDegree();
            ICursoExecucao executionDegree = cursoExecucaoPersistente
                    .readByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan, executionYear);
            assertNotNull(executionDegree);

            /*
             * IPersistentExecutionPeriod persistentExecutionPeriod =
             * sp.getIPersistentExecutionPeriod(); IExecutionPeriod
             * executionPeriod =
             * persistentExecutionPeriod.readByNameAndExecutionYear( "2º
             * Semestre", executionYear); assertNotNull(executionPeriod);
             * 
             * IDisciplinaExecucaoPersistente disciplinaExecucaoPersistente =
             * sp.getIDisciplinaExecucaoPersistente(); IDisciplinaExecucao
             * executionCourse = disciplinaExecucaoPersistente
             * .readByExecutionCourseInitialsAndExecutionPeriod( "TFCI",
             * executionPeriod); assertNotNull(executionCourse);
             */
            this.degreeInitials = degree.getSigla();
            this.nameDegreeCurricularPlan = degreeCurricularPlan.getName();
            this.infoExecutionYear = (InfoExecutionYear) Cloner.get(executionYear);

            if (!exists)
                cursoExecucaoPersistente.delete(executionDegree);

            this.infoExecutionDegree = (InfoExecutionDegree) Cloner.get(executionDegree);

            sp.confirmarTransaccao();

        } catch (ExcepcaoPersistencia excepcao) {
            try {
                sp.cancelarTransaccao();
            } catch (ExcepcaoPersistencia ex) {
                fail("ligarSuportePersistente: cancelarTransaccao: " + ex);
            }
            fail("ligarSuportePersistente: confirmarTransaccao: " + excepcao);
        }
    }

}