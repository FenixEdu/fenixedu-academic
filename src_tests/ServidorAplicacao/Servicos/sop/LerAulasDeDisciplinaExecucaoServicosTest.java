/*
 * LerAulasDeDisciplinaExecucaoServicosTest.java JUnit based test
 * 
 * Created on 27 de Outubro de 2002, 23:34
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

public class LerAulasDeDisciplinaExecucaoServicosTest extends TestCaseReadServices {

    private InfoExecutionCourse infoExecutionCourse = null;

    public LerAulasDeDisciplinaExecucaoServicosTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(LerAulasDeDisciplinaExecucaoServicosTest.class);

        return suite;
    }

    protected void setUp() {
        super.setUp();
    }

    protected void tearDown() {
        super.tearDown();
    }

    protected String getNameOfServiceToBeTested() {
        return "LerAulasDeDisciplinaExecucao";
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {

        ligarSuportePersistente(true);

        Object argsLerAulas[] = { this.infoExecutionCourse };

        return argsLerAulas;
    }

    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {

        ligarSuportePersistente(false);

        Object argsLerAulas[] = { this.infoExecutionCourse };

        return argsLerAulas;
    }

    protected int getNumberOfItemsToRetrieve() {
        return 6;
    }

    protected Object getObjectToCompare() {
        return null;
    }

    protected boolean needsAuthorization() {
        return true;
    }

    private void ligarSuportePersistente(boolean existing) {

        ISuportePersistente sp = null;

        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            sp.iniciarTransaccao();

            IPersistentExecutionYear ieyp = sp.getIPersistentExecutionYear();
            IExecutionYear iey = ieyp.readExecutionYearByName("2002/2003");

            IPersistentExecutionPeriod iepp = sp.getIPersistentExecutionPeriod();
            IExecutionPeriod iep = iepp.readByNameAndExecutionYear("2º Semestre", iey);

            IPersistentExecutionCourse idep = sp.getIPersistentExecutionCourse();
            IExecutionCourse ide = null;

            if (existing) {
                ide = idep.readByExecutionCourseInitialsAndExecutionPeriod("TFCI", iep);
            } else {
                ide = new ExecutionCourse("NOME", "SIGLA", new Double(1.5), new Double(1.5), new Double(
                        1.5), new Double(1.5), iep);
            }

            this.infoExecutionCourse = (InfoExecutionCourse) Cloner.get(ide);

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