/*
 * AdicionarAulaServicosTest.java
 * JUnit based test
 *
 * Created on 26 de Outubro de 2002, 19:45
 */

package ServidorAplicacao.Servicos.sop;

/**
 *
 * @author tfc130
 */
import java.util.Calendar;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoShiftServiceResult;
import DataBeans.RoomKey;
import DataBeans.ShiftAndLessonKeys;
import ServidorAplicacao.Servicos.TestCaseServicos;
import Util.DiaSemana;

public class AdicionarAulaServicosTest extends TestCaseServicos {

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

	// write turnoAula by unauthorized user
	public void testUnauthorizedCreateTurnoAula() {

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
		fim.set(Calendar.MINUTE, 0);
		fim.set(Calendar.SECOND, 0);

		RoomKey keySala = new RoomKey("Ga1");
		Object argsCriarTurnoAula[] = new Object[1];
		argsCriarTurnoAula[0] = new ShiftAndLessonKeys("turno1", diaSemana, inicio, fim, keySala);
		Object result = null;
		try {
			result = _gestor.executar(_userView2, "AdicionarAula", argsCriarTurnoAula);
			fail("testUnauthorizedCreateTurnoAula");
		} catch (Exception ex) {
			assertNull("testUnauthorizedCreateTurnoAula", result);
		}
	}

	// write existing turnoAula
	public void testCreateExistingTurnoAula() {

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
		fim.set(Calendar.MINUTE, 0);
		fim.set(Calendar.SECOND, 0);

		RoomKey keySala = new RoomKey("Ga1");
		Object argsCriarTurnoAula[] = new Object[1];
		argsCriarTurnoAula[0] = new ShiftAndLessonKeys("turno1", diaSemana, inicio, fim, keySala);

		Object result = null;
		try {
			result = _gestor.executar(_userView, "AdicionarAula", argsCriarTurnoAula);
			fail("testCreateExistingTurnoAula:");
		} catch (Exception ex) {
			assertNull("testCreateExistingTurnoAula", result);
		}
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

		RoomKey keySala = new RoomKey("Ga1");
		Object argsCriarTurnoAula[] = new Object[1];
		argsCriarTurnoAula[0] = new ShiftAndLessonKeys("turno2", diaSemana, inicio, fim, keySala);

		Object result = null;
		try {
			result = _gestor.executar(_userView, "AdicionarAula", argsCriarTurnoAula);
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

		RoomKey keySala = new RoomKey("Ga1");

		Object argsCriarTurnoAula1[] = new Object[1];
		Object argsCriarTurnoAula2[] = new Object[1];
		argsCriarTurnoAula1[0] = new ShiftAndLessonKeys("turno3", diaSemana1, inicio,  fim1, keySala);
		argsCriarTurnoAula2[0] = new ShiftAndLessonKeys("turno3", diaSemana2, inicio,  fim2, keySala);

		Object result = null;
		try {
			result = _gestor.executar(_userView, "AdicionarAula", argsCriarTurnoAula1);
			assertTrue("testCreateNonExistingTurnoAula_HoursLimitReached", ((InfoShiftServiceResult) result).isSUCESS());
			result = _gestor.executar(_userView, "AdicionarAula", argsCriarTurnoAula2);
			assertTrue("testCreateNonExistingTurnoAula_HoursLimitReached", ((InfoShiftServiceResult) result).getMessageType() == new InfoShiftServiceResult(InfoShiftServiceResult.PRATICAL_HOURS_LIMIT_REACHED).getMessageType());
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

		RoomKey keySala = new RoomKey("Ga1");

		Object argsCriarTurnoAula1[] = new Object[1];
		Object argsCriarTurnoAula2[] = new Object[1];

		argsCriarTurnoAula1[0] = new ShiftAndLessonKeys("turno3", diaSemana1, inicio1, fim1, keySala);
		argsCriarTurnoAula2[0] = new ShiftAndLessonKeys("turno3", diaSemana2, inicio2, fim2, keySala);
	    
	    Object result = null; 
	      try {
			result = _gestor.executar(_userView, "AdicionarAula", argsCriarTurnoAula1);
			assertTrue("testCreateNonExistingTurnoAula_HourLimitExceeded", ((InfoShiftServiceResult) result).isSUCESS());
			result = _gestor.executar(_userView, "AdicionarAula", argsCriarTurnoAula2);
			assertTrue("testCreateNonExistingTurnoAula_HourLimitExceeded", ((InfoShiftServiceResult) result).getMessageType() == new InfoShiftServiceResult(InfoShiftServiceResult.PRATICAL_HOURS_LIMIT_EXCEEDED).getMessageType());
	      } catch (Exception ex) {
	      	fail("testCreateNonExistingTurnoAula_HourLimitExceeded:" + ex);
	      }
	  }
}
