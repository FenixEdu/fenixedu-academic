package net.sourceforge.fenixedu.applicationTier.Servicos.publico;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;

import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
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
public class ReadCurricularCourseListOfExecutionCourseTest extends TestCaseServicos {

    private InfoExecutionCourse infoExecutionCourse = null;

    /**
     * Constructor for SelectClassesTest.
     */
    public ReadCurricularCourseListOfExecutionCourseTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ReadCurricularCourseListOfExecutionCourseTest.class);

        return suite;
    }

    protected void setUp() {
        super.setUp();
    }

    protected void tearDown() {
        super.tearDown();
    }

    public void testReadAll() {

        Object argsReadCurricularCourseListOfExecutionCourse[] = new Object[1];
        Object result = null;
        //execution course with 1 curricularCourses associated
        this.prepareTestCase(true);
        argsReadCurricularCourseListOfExecutionCourse[0] = this.infoExecutionCourse;

        System.out.println(infoExecutionCourse);
        try {
            result = ServiceManagerServiceFactory.executeService(_userView,
                    "ReadCurricularCourseListOfExecutionCourse",
                    argsReadCurricularCourseListOfExecutionCourse);
            assertEquals("testReadAll: 1 curricularCourses of executionCourse", 1, ((List) result)
                    .size());

        } catch (Exception ex) {
            fail("testReadAll: executionCourse with 1 curricularCourses: " + ex);
        }

        // Empty database - no curricularCourses of selected executionCourse
        this.prepareTestCase(false);
        try {
            result = ServiceManagerServiceFactory.executeService(_userView,
                    "ReadCurricularCourseListOfExecutionCourse",
                    argsReadCurricularCourseListOfExecutionCourse);
            assertEquals("testReadAll: no curricularCourses of executionCourse", 0, ((List) result)
                    .size());
        } catch (Exception ex) {
            fail("testReadAll: no curricularCourses of executionCourse: " + ex);
        }
    }

    private void prepareTestCase(boolean hasCurricularCourses) {
        ISuportePersistente sp = null;
        try {
            sp = SuportePersistenteOJB.getInstance();
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

            if (!hasCurricularCourses) {
                PersistenceBroker pb = PersistenceBrokerFactory.defaultPersistenceBroker();
                // sp.getIPersistentCurricularCourse().deleteAll(); no longer
                // suported- too dangerous
                pb.removeFromCache(executionCourse);
                //executionCourse.setAssociatedCurricularCourses(null);
            }

            this.infoExecutionCourse = (InfoExecutionCourse) Cloner.get(executionCourse);
            sp.confirmarTransaccao();
        } catch (ExcepcaoPersistencia excepcao) {
            try {
                sp.cancelarTransaccao();
            } catch (ExcepcaoPersistencia ex) {
                fail("ligarSuportePersistente: cancelarTransaccao: " + ex);
            }
            System.out.println("44");
            fail("ligarSuportePersistente: confirmarTransaccao: " + excepcao);
        }
    }

}