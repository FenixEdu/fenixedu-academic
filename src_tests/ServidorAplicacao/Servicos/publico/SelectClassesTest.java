package net.sourceforge.fenixedu.applicationTier.Servicos.publico;

import java.util.List;

import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

import junit.framework.Test;
import junit.framework.TestSuite;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ICurso;
import net.sourceforge.fenixedu.domain.ICursoExecucao;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.ITurma;
import net.sourceforge.fenixedu.applicationTier.Servicos.TestCaseServicos;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ICursoPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurmaPersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

/**
 * @author jmota
 *  
 */
public class SelectClassesTest extends TestCaseServicos {

    private InfoClass infoClass = null;

    /**
     * Constructor for SelectClassesTest.
     */
    public SelectClassesTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(SelectClassesTest.class);

        return suite;
    }

    protected void setUp() {
        super.setUp();
    }

    protected void tearDown() {
        super.tearDown();
    }

    public void testReadByDegreeAndOtherStuff() {

        Object argsSelectClasses[] = new Object[1];
        Object result = null;

        //		4 classes in database
        prepareTestCase(true);

        argsSelectClasses[0] = this.infoClass;

        try {
            result = ServiceManagerServiceFactory.executeService(_userView, "SelectClasses",
                    argsSelectClasses);
            assertEquals("testReadByDegreeAndOtherStuff: 4classes in db", 4, ((List) result).size());
        } catch (Exception ex) {
            fail("testReadByDegreeAndOtherStuff: 4 classes in db");
        }

        //Empty database
        prepareTestCase(false);
        argsSelectClasses[0] = this.infoClass;

        try {
            result = ServiceManagerServiceFactory.executeService(null, "SelectClasses",
                    argsSelectClasses);
            assertEquals("testReadByDegreeAndOtherStuff: no classes to read", 0, ((List) result).size());
        } catch (Exception ex) {
            fail("testReadByDegreeAndOtherStuff: no classes to read");
        }

    }

    private void prepareTestCase(boolean classesExist) {

        ISuportePersistente sp = null;

        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            sp.iniciarTransaccao();

            IPersistentExecutionYear persistentExecutionYear = sp.getIPersistentExecutionYear();
            IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
            assertNotNull(executionYear);

            IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();
            IExecutionPeriod executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear(
                    "2º Semestre", executionYear);
            assertNotNull(executionPeriod);

            ICursoPersistente cursoPersistente = sp.getICursoPersistente();
            ICurso degree = cursoPersistente.readBySigla("LEIC");
            assertNotNull(degree);

            IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = sp
                    .getIPersistentDegreeCurricularPlan();
            IDegreeCurricularPlan degreeCurricularPlan = persistentDegreeCurricularPlan
                    .readByNameAndDegree("plano1", degree);
            assertNotNull(degreeCurricularPlan);

            IPersistentExecutionDegree cursoExecucaoPersistente = sp.getIPersistentExecutionDegree();
            ICursoExecucao executionDegree = cursoExecucaoPersistente
                    .readByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan, executionYear);
            assertNotNull(executionDegree);

            ITurmaPersistente turmaPersistente = sp.getITurmaPersistente();
            ITurma class1 = turmaPersistente.readByNameAndExecutionDegreeAndExecutionPeriod("10501",
                    executionDegree, executionPeriod);
            assertNotNull(class1);

            //			if (!classesExist)
            //				turmaPersistente.deleteAll(); method deleted- too dangerous

            this.infoClass = InfoClass.newInfoFromDomain(class1);

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