/*
 * LerTurmaServicosTest.java JUnit based test
 * 
 * Created on 26 de Outubro de 2002, 11:47
 */

package net.sourceforge.fenixedu.applicationTier.Servicos.sop;

/**
 * @author tfc130
 */
import junit.framework.Test;
import junit.framework.TestSuite;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.CursoExecucao;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.ICurso;
import net.sourceforge.fenixedu.domain.ICursoExecucao;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.applicationTier.Servicos.TestCaseReadServices;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ICursoPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

public class LerTurmaServicosTest extends TestCaseReadServices {

    private InfoExecutionDegree infoExecutionDegree = null;

    private InfoExecutionPeriod infoExecutionPeriod = null;

    public LerTurmaServicosTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(LerTurmaServicosTest.class);

        return suite;
    }

    protected void setUp() {
        super.setUp();
    }

    protected void tearDown() {
        super.tearDown();
    }

    protected String getNameOfServiceToBeTested() {
        return "LerTurma";
    }

    protected int getNumberOfItemsToRetrieve() {
        return 0;
    }

    protected Object getObjectToCompare() {
        InfoClass infoClass = new InfoClass();
        infoClass.setNome("10501");
        this.ligarSuportePersistente(true);

        infoClass.setInfoExecutionDegree(this.infoExecutionDegree);
        infoClass.setInfoExecutionPeriod(this.infoExecutionPeriod);

        return infoClass;
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {

        this.ligarSuportePersistente(true);

        Object argsLerTurma[] = { "10501", this.infoExecutionDegree, this.infoExecutionPeriod };

        return argsLerTurma;
    }

    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {

        this.ligarSuportePersistente(false);

        Object argsLerTurma[] = { "10501", this.infoExecutionDegree, this.infoExecutionPeriod };

        return argsLerTurma;
    }

    private void ligarSuportePersistente(boolean existing) {

        ISuportePersistente sp = null;

        try {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            ICursoPersistente persistentDegree = sp.getICursoPersistente();
            ICurso degree = persistentDegree.readBySigla("LEIC");
            assertNotNull(degree);

            IPersistentExecutionYear persistentExecutionYear = sp.getIPersistentExecutionYear();
            IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
            assertNotNull(executionYear);

            IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = sp
                    .getIPersistentDegreeCurricularPlan();
            IDegreeCurricularPlan degreeCurricularPlan = persistentDegreeCurricularPlan
                    .readByNameAndDegree("plano1", degree);
            assertNotNull(degreeCurricularPlan);

            IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();
            IExecutionPeriod executionPeriod = null;

            IPersistentExecutionDegree persistentExecutionDegree = sp.getIPersistentExecutionDegree();
            ICursoExecucao executionDegree = null;

            if (existing) {

                executionDegree = persistentExecutionDegree.readByDegreeCurricularPlanAndExecutionYear(
                        degreeCurricularPlan, executionYear);
                assertNotNull(executionDegree);

                executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear("2º Semestre",
                        executionYear);
                assertNotNull(executionPeriod);

            } else {

                executionYear = new ExecutionYear("desc");
                executionDegree = new CursoExecucao(executionYear, degreeCurricularPlan);

                executionPeriod = new ExecutionPeriod("desc", executionYear);
            }

            this.infoExecutionDegree = (InfoExecutionDegree) Cloner.get(executionDegree);
            this.infoExecutionPeriod = (InfoExecutionPeriod) Cloner.get(executionPeriod);

            sp.confirmarTransaccao();

        } catch (ExcepcaoPersistencia excepcao) {
            try {
                sp.cancelarTransaccao();
            } catch (ExcepcaoPersistencia ex) {
                fail("ligarSuportePersistente: cancelarTransaccao");
            }
            fail("ligarSuportePersistente: confirmarTransaccao");
        }
    }

}