/*
 * AdicionarTurnoServicosTest.java JUnit based test
 * 
 * Created on 26 de Outubro de 2002, 12:54
 */

package net.sourceforge.fenixedu.applicationTier.Servicos.sop;

/**
 * @author tfc130
 */
import java.util.HashMap;

import junit.framework.Test;
import junit.framework.TestSuite;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ICurso;
import net.sourceforge.fenixedu.domain.ICursoExecucao;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.ITurma;
import net.sourceforge.fenixedu.domain.ITurno;
import net.sourceforge.fenixedu.applicationTier.Servicos.TestCaseCreateServices;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ICursoPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurmaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

public class AdicionarTurnoServicosTest extends TestCaseCreateServices {

    private InfoClass infoClass = null;

    private InfoShift infoShift = null;

    public AdicionarTurnoServicosTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(AdicionarTurnoServicosTest.class);

        return suite;
    }

    protected void setUp() {
        super.setUp();
    }

    protected void tearDown() {
        super.tearDown();
    }

    protected String getNameOfServiceToBeTested() {
        return "AdicionarTurno";
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {

        this.ligarSuportePersistente(false);
        Object argsCriarTurmaTurno[] = { this.infoClass, this.infoShift };

        return argsCriarTurmaTurno;
    }

    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {

        this.ligarSuportePersistente(true);
        Object argsCriarTurmaTurno[] = { this.infoClass, this.infoShift };

        return argsCriarTurmaTurno;
    }

    protected HashMap getArgumentListOfServiceToBeTestedUnsuccessfuly() {
        return null;
    }

    private void ligarSuportePersistente(boolean existing) {

        ISuportePersistente sp = null;

        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            sp.iniciarTransaccao();

            ICursoPersistente icp = sp.getICursoPersistente();
            ICurso ic = icp.readBySigla("LEIC");

            IPersistentDegreeCurricularPlan ipccp = sp.getIPersistentDegreeCurricularPlan();
            IDegreeCurricularPlan ipcc = ipccp.readByNameAndDegree("plano1", ic);

            IPersistentExecutionYear ieyp = sp.getIPersistentExecutionYear();
            IExecutionYear iey = ieyp.readExecutionYearByName("2002/2003");

            IPersistentExecutionDegree icep = sp.getIPersistentExecutionDegree();
            ICursoExecucao ice = icep.readByDegreeCurricularPlanAndExecutionYear(ipcc, iey);

            IPersistentExecutionPeriod iepp = sp.getIPersistentExecutionPeriod();
            IExecutionPeriod iep = iepp.readByNameAndExecutionYear("2º Semestre", iey);

            ITurmaPersistente turmaPersistente = sp.getITurmaPersistente();
            ITurma turma = turmaPersistente.readByNameAndExecutionDegreeAndExecutionPeriod("turma413",
                    ice, iep);

            IPersistentExecutionCourse idep = sp.getIPersistentExecutionCourse();
            IExecutionCourse ide = idep.readByExecutionCourseInitialsAndExecutionPeriod("TFCI", iep);

            ITurnoPersistente itp = sp.getITurnoPersistente();
            ITurno it = null;
            if (existing) {
                it = itp.readByNameAndExecutionCourse("turno453", ide);
            } else {
                it = itp.readByNameAndExecutionCourse("turno1", ide);
            }

            this.infoClass = InfoClass.newInfoFromDomain(turma);
            this.infoShift = (InfoShift) Cloner.get(it);

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