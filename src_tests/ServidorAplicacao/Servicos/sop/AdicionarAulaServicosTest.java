/*
 * AdicionarAulaServicosTest.java
 * JUnit based test
 *
 * Created on 26 de Outubro de 2002, 19:45
 */

package net.sourceforge.fenixedu.applicationTier.Servicos.sop;

/**
 * 
 * @author tfc130
 */
import java.util.Calendar;

import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

import junit.framework.Test;
import junit.framework.TestSuite;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoShiftServiceResult;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IAula;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.ISala;
import net.sourceforge.fenixedu.domain.ITurno;
import net.sourceforge.fenixedu.applicationTier.Servicos.TestCaseNeedAuthorizationServices;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IAulaPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ISalaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.util.DiaSemana;

public class AdicionarAulaServicosTest extends TestCaseNeedAuthorizationServices {

    private InfoLesson infoLesson = null;

    private InfoShift infoShift = null;

    public AdicionarAulaServicosTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(AdicionarAulaServicosTest.class);

        return suite;
    }

    protected void setUp() {
        super.setUp();
    }

    protected void tearDown() {
        super.tearDown();
    }

    protected String getNameOfServiceToBeTested() {
        return "AdicionarAula";
    }

    // TODO This class must extend class TestCaseCreateServices so this method
    // will be gonne
    protected boolean needsAuthorization() {
        return true;
    }

    // write new non-existing turnoAula
    public void testCreateNonExistingTurnoAula() {

        DiaSemana diaSemana = null;
        Calendar inicio = null;
        Calendar fim = null;
        diaSemana = new DiaSemana(DiaSemana.SEGUNDA_FEIRA);
        inicio = Calendar.getInstance();
        inicio.set(Calendar.HOUR_OF_DAY, 8);
        inicio.set(Calendar.MINUTE, 0);
        inicio.set(Calendar.SECOND, 0);
        fim = Calendar.getInstance();
        fim.set(Calendar.HOUR_OF_DAY, 9);
        fim.set(Calendar.MINUTE, 30);
        fim.set(Calendar.SECOND, 0);

        this.ligarSuportePersistente("turno2", diaSemana, inicio, fim);

        Object argsCriarTurnoAula[] = { this.infoShift, this.infoLesson };

        Object result = null;
        try {
            result = ServiceManagerServiceFactory.executeService(_userView,
                    getNameOfServiceToBeTested(), argsCriarTurnoAula);
            assertTrue("testCreateNonExistingTurnoAula", ((InfoShiftServiceResult) result).isSUCESS());
        } catch (Exception ex) {
            System.out.println(ex.toString());
            fail("testCreateNonExistingTurnoAula:");
        }
    }

    // write new non-existing turnoAula but carga-horaria maxima ja foi atingida
    public void testCreateNonExistingTurnoAula_HoursLimitReached() {

        DiaSemana diaSemana1 = null;
        DiaSemana diaSemana2 = null;
        Calendar inicio = null;
        Calendar fim1 = null;
        Calendar fim2 = null;
        diaSemana1 = new DiaSemana(DiaSemana.TERCA_FEIRA);
        diaSemana2 = new DiaSemana(DiaSemana.QUARTA_FEIRA);
        inicio = Calendar.getInstance();
        inicio.set(Calendar.HOUR_OF_DAY, 8);
        inicio.set(Calendar.MINUTE, 0);
        inicio.set(Calendar.SECOND, 0);
        fim1 = Calendar.getInstance();
        fim1.set(Calendar.HOUR_OF_DAY, 9);
        fim1.set(Calendar.MINUTE, 30);
        fim1.set(Calendar.SECOND, 0);
        fim2 = Calendar.getInstance();
        fim2.set(Calendar.HOUR_OF_DAY, 9);
        fim2.set(Calendar.MINUTE, 30);
        fim2.set(Calendar.SECOND, 0);

        this.ligarSuportePersistente("turno3", diaSemana1, inicio, fim1);
        Object argsCriarTurnoAula1[] = { this.infoShift, this.infoLesson };

        this.ligarSuportePersistente("turno3", diaSemana2, inicio, fim2);
        Object argsCriarTurnoAula2[] = { this.infoShift, this.infoLesson };

        Object result = null;
        try {
            result = ServiceManagerServiceFactory.executeService(_userView,
                    getNameOfServiceToBeTested(), argsCriarTurnoAula1);
            assertTrue("testCreateNonExistingTurnoAula_HoursLimitReached",
                    ((InfoShiftServiceResult) result).isSUCESS());
            result = ServiceManagerServiceFactory.executeService(_userView,
                    getNameOfServiceToBeTested(), argsCriarTurnoAula2);
            assertTrue("testCreateNonExistingTurnoAula_HoursLimitReached",
                    ((InfoShiftServiceResult) result).getMessageType() == new InfoShiftServiceResult(
                            InfoShiftServiceResult.PRATICAL_HOURS_LIMIT_REACHED).getMessageType());
        } catch (Exception ex) {
            fail("testCreateNonExistingTurnoAula_HoursLimitReached: " + ex);
        }
    }

    // write new non-existing turnoAula but carga-horaria maxima excedida
    public void testCreateNonExistingTurnoAula_HourLimitExceeded() {

        DiaSemana diaSemana1 = null;
        DiaSemana diaSemana2 = null;
        Calendar inicio1 = null;
        Calendar inicio2 = null;
        Calendar fim1 = null;
        Calendar fim2 = null;
        diaSemana1 = new DiaSemana(DiaSemana.TERCA_FEIRA);
        diaSemana2 = new DiaSemana(DiaSemana.QUARTA_FEIRA);
        inicio1 = Calendar.getInstance();
        inicio1.set(Calendar.HOUR_OF_DAY, 13);
        inicio1.set(Calendar.MINUTE, 30);
        inicio1.set(Calendar.SECOND, 0);
        inicio2 = Calendar.getInstance();
        inicio2.set(Calendar.HOUR_OF_DAY, 8);
        inicio2.set(Calendar.MINUTE, 0);
        inicio2.set(Calendar.SECOND, 0);
        fim1 = Calendar.getInstance();
        fim1.set(Calendar.HOUR_OF_DAY, 14);
        fim1.set(Calendar.MINUTE, 0);
        fim1.set(Calendar.SECOND, 0);
        fim2 = Calendar.getInstance();
        fim2.set(Calendar.HOUR_OF_DAY, 9);
        fim2.set(Calendar.MINUTE, 30);
        fim2.set(Calendar.SECOND, 0);

        this.ligarSuportePersistente("turno3", diaSemana1, inicio1, fim1);
        Object argsCriarTurnoAula1[] = { this.infoShift, this.infoLesson };

        this.ligarSuportePersistente("turno3", diaSemana2, inicio2, fim2);
        Object argsCriarTurnoAula2[] = { this.infoShift, this.infoLesson };

        Object result = null;
        try {
            result = ServiceManagerServiceFactory.executeService(_userView,
                    getNameOfServiceToBeTested(), argsCriarTurnoAula1);
            assertTrue("testCreateNonExistingTurnoAula_HourLimitExceeded",
                    ((InfoShiftServiceResult) result).isSUCESS());
            result = ServiceManagerServiceFactory.executeService(_userView,
                    getNameOfServiceToBeTested(), argsCriarTurnoAula2);
            assertTrue("testCreateNonExistingTurnoAula_HourLimitExceeded",
                    ((InfoShiftServiceResult) result).getMessageType() == new InfoShiftServiceResult(
                            InfoShiftServiceResult.PRATICAL_HOURS_LIMIT_EXCEEDED).getMessageType());
        } catch (Exception ex) {
            fail("testCreateNonExistingTurnoAula_HourLimitExceeded:" + ex);
        }
    }

    //	write existing turnoAula
    public void testCreateExistingTurnoAula() {

        DiaSemana diaSemana = null;
        Calendar inicio = null;
        Calendar fim = null;
        diaSemana = new DiaSemana(DiaSemana.SEGUNDA_FEIRA);
        inicio = Calendar.getInstance();
        inicio.set(Calendar.HOUR_OF_DAY, 12);
        inicio.set(Calendar.MINUTE, 30);
        inicio.set(Calendar.SECOND, 0);
        fim = Calendar.getInstance();
        fim.set(Calendar.HOUR_OF_DAY, 13);
        fim.set(Calendar.MINUTE, 0);
        fim.set(Calendar.SECOND, 0);

        this.ligarSuportePersistente("turno1", diaSemana, inicio, fim);
        Object argsCriarTurnoAula[] = { this.infoShift, this.infoLesson };

        Object result = null;
        try {
            result = ServiceManagerServiceFactory.executeService(_userView,
                    getNameOfServiceToBeTested(), argsCriarTurnoAula);
            fail("testCreateExistingTurnoAula:");
        } catch (Exception ex) {
            assertNull("testCreateExistingTurnoAula", result);
        }
    }

    private void ligarSuportePersistente(String nomeTurno, DiaSemana diaSemana, Calendar inicio,
            Calendar fim) {

        ISuportePersistente sp = null;

        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            sp.iniciarTransaccao();

            IPersistentExecutionYear ieyp = sp.getIPersistentExecutionYear();
            IExecutionYear iey = ieyp.readExecutionYearByName("2002/2003");

            IPersistentExecutionPeriod iepp = sp.getIPersistentExecutionPeriod();
            IExecutionPeriod iep = iepp.readByNameAndExecutionYear("2º Semestre", iey);

            ISalaPersistente isp = sp.getISalaPersistente();
            ISala is = isp.readByName("Ga1");

            IAulaPersistente iap = sp.getIAulaPersistente();
            IAula ia = iap.readByDiaSemanaAndInicioAndFimAndSala(diaSemana, inicio, fim, is, iep);

            String nomeDisciplinaExecucao = null;
            if (nomeTurno.equals("turno1") || nomeTurno.equals("turno2")) {
                nomeDisciplinaExecucao = "TFCI";
            } else if (nomeTurno.equals("turno3")) {
                nomeDisciplinaExecucao = "TFCII";
            }

            IPersistentExecutionCourse idep = sp.getIPersistentExecutionCourse();
            IExecutionCourse ide = idep.readByExecutionCourseInitialsAndExecutionPeriod(
                    nomeDisciplinaExecucao, iep);

            ITurnoPersistente itp = sp.getITurnoPersistente();
            ITurno it = itp.readByNameAndExecutionCourse(nomeTurno, ide);

            this.infoLesson = Cloner.copyILesson2InfoLesson(ia);
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