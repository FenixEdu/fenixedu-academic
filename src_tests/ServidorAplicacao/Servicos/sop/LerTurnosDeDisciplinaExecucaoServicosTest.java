/*
 * LerTurnosDeDisciplinaExecucaoServicosTest.java JUnit based test
 * 
 * Created on 01 de Dezembro de 2002, 18:31
 */

package net.sourceforge.fenixedu.applicationTier.Servicos.sop;

/**
 * @author tfc130
 */
import junit.framework.Test;
import junit.framework.TestSuite;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.applicationTier.Servicos.TestCaseReadServices;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

public class LerTurnosDeDisciplinaExecucaoServicosTest extends TestCaseReadServices {

    private InfoExecutionCourse infoExecutionCourse = null;

    public LerTurnosDeDisciplinaExecucaoServicosTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(LerTurnosDeDisciplinaExecucaoServicosTest.class);

        return suite;
    }

    protected void setUp() {
        super.setUp();
    }

    protected void tearDown() {
        super.tearDown();
    }

    protected String getNameOfServiceToBeTested() {
        return "LerTurnosDeDisciplinaExecucao";
    }

    protected int getNumberOfItemsToRetrieve() {
        return 10;
    }

    protected Object getObjectToCompare() {
        return null;
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {

        this.ligarSuportePersistente(true);

        Object argsLerTurnos[] = { this.infoExecutionCourse };

        return argsLerTurnos;
    }

    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {

        this.ligarSuportePersistente(false);

        Object argsLerTurnos[] = { this.infoExecutionCourse };

        return argsLerTurnos;
    }

    private void ligarSuportePersistente(boolean existing) {

        ISuportePersistente sp = null;

        try {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            IPersistentExecutionYear persistentExecutionYear = sp.getIPersistentExecutionYear();
            IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
            assertNotNull(executionYear);

            IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();
            IExecutionPeriod executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear(
                    "2º Semestre", executionYear);
            assertNotNull(executionPeriod);

            IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();
            IExecutionCourse executionCourse = null;

            if (existing) {
                executionCourse = persistentExecutionCourse
                        .readBySiglaAndAnoLectivoAndSiglaLicenciatura("TFCI", "2002/2003", "LEIC");
                assertNotNull(executionCourse);
            } else {
                executionCourse = new ExecutionCourse("desc", "desc", new Double(1), new Double(2),
                        new Double(3), new Double(4), executionPeriod);
            }

            this.infoExecutionCourse = (InfoExecutionCourse) Cloner.get(executionCourse);

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