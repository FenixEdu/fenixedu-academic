package net.sourceforge.fenixedu.applicationTier.Servicos.publico;

import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import junit.framework.Test;
import junit.framework.TestSuite;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ICurso;
import net.sourceforge.fenixedu.domain.ICursoExecucao;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.applicationTier.Servicos.TestCaseServicos;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ICursoPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

/**
 * @author tfc130
 *  
 */
public class ReadExecutionCourseTest extends TestCaseServicos {

    private InfoExecutionCourse infoExecutionCourse = null;

    private InfoExecutionPeriod infoExecutionPeriod = null;

    private String code = null;

    /**
     * Constructor for SelectClassesTest.
     */
    public ReadExecutionCourseTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ReadExecutionCourseTest.class);

        return suite;
    }

    protected void setUp() {
        super.setUp();
    }

    protected void tearDown() {
        super.tearDown();
    }

    public void testReadAll() {

        Object argsReadExecutionCourse[] = new Object[2];

        Object result = null;

        // execution course exists in database
        this.prepareTestCase(true);

        argsReadExecutionCourse[0] = this.infoExecutionPeriod;
        argsReadExecutionCourse[1] = this.code;

        try {
            result = ServiceManagerServiceFactory.executeService(_userView, "ReadExecutionCourse",
                    argsReadExecutionCourse);
            assertTrue(((InfoExecutionCourse) result).equals(infoExecutionCourse));

        } catch (Exception ex) {
            fail("testReadAll: executionCourse with 1 curricularCourses: " + ex);
        }

        // executionCourse does not exist in database
        this.prepareTestCase(false);
        try {
            result = ServiceManagerServiceFactory.executeService(_userView, "ReadExecutionCourse",
                    argsReadExecutionCourse);
            assertNull("testReadAll: executionCourse does not exist in db", result);
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

            IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();
            IExecutionPeriod executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear(
                    "2º Semestre", executionYear);
            assertNotNull(executionPeriod);

            IPersistentExecutionCourse disciplinaExecucaoPersistente = sp
                    .getIPersistentExecutionCourse();
            IExecutionCourse executionCourse = disciplinaExecucaoPersistente
                    .readByExecutionCourseInitialsAndExecutionPeriod("TFCI", executionPeriod);
            assertNotNull(executionCourse);

            this.infoExecutionCourse = (InfoExecutionCourse) Cloner.get(executionCourse);

            if (!exists)
                disciplinaExecucaoPersistente.deleteExecutionCourse(executionCourse);

            this.infoExecutionPeriod = (InfoExecutionPeriod) Cloner.get(executionPeriod);
            this.code = executionCourse.getSigla();

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