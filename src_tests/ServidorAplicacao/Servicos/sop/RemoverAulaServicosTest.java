/*
 * RemoverAulaServicosTest.java
 * JUnit based test
 *
 * Created on 26 de Outubro de 2002, 12:21
 */

package ServidorAplicacao.Servicos.sop;

/**
 *
 * @author tfc130
 */
import java.util.Calendar;

import framework.factory.ServiceManagerServiceFactory;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoLesson;
import DataBeans.InfoShift;
import DataBeans.util.Cloner;
import Dominio.Aula;
import Dominio.IAula;
import Dominio.ICurso;
import Dominio.IExecutionCourse;
import Dominio.IExecutionYear;
import Dominio.ISala;
import Dominio.ITurno;
import Dominio.Turno;
import ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IAulaPersistente;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.DiaSemana;
import Util.TipoAula;

public class RemoverAulaServicosTest
	extends TestCaseNeedAuthorizationServices {
	private InfoLesson infoLesson = null;
	private InfoShift infoShift = null;

	public RemoverAulaServicosTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(RemoverAulaServicosTest.class);

		return suite;
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}

	protected String getNameOfServiceToBeTested() {
		return "RemoverAula";
	}

	protected boolean needsAuthorization() {
		return true;
	}

	public void testRemoveLessonExisting() {
		this.ligarSuportePersistente(true);

		Object argsRemoveLesson[] = { this.infoLesson, this.infoShift };

		Object result = null;
		try {
			result =
				ServiceManagerServiceFactory.executeService(
					_userView,
					getNameOfServiceToBeTested(),
					argsRemoveLesson);
			assertEquals(
				"testRemoverAula",
				Boolean.TRUE.booleanValue(),
				((Boolean) result).booleanValue());
		} catch (Exception ex) {
			assertNull("testUnauthorizedRemoveAula", result);
		}
	}

	public void testRemoveUnexistingLessonExisting() {
		this.ligarSuportePersistente(false);

		Object argsRemoveLesson[] = { this.infoLesson, this.infoShift };

		Object result = null;
		try {
			result =
				ServiceManagerServiceFactory.executeService(
					_userView,
					getNameOfServiceToBeTested(),
					argsRemoveLesson);
			assertEquals(
				"testRemoverAula",
				Boolean.TRUE.booleanValue(),
				((Boolean) result).booleanValue());
		} catch (Exception ex) {
			assertNull("testUnauthorizedRemoveAula", result);
		}
	}

	private void ligarSuportePersistente(boolean existing) {

		ISuportePersistente sp = null;

		try {
			sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();

			IPersistentExecutionYear persistentExecutionYear =
				sp.getIPersistentExecutionYear();
			IExecutionYear executionYear =
				persistentExecutionYear.readExecutionYearByName("2002/2003");
			assertNotNull(executionYear);

			ICursoPersistente persistentDegree = sp.getICursoPersistente();
			ICurso degree = persistentDegree.readBySigla("LEIC");
			assertNotNull(degree);

			DiaSemana weekDay = new DiaSemana(DiaSemana.SEGUNDA_FEIRA);

			Calendar startTime = Calendar.getInstance();
			startTime.set(Calendar.HOUR_OF_DAY, 8);
			startTime.set(Calendar.MINUTE, 00);
			startTime.set(Calendar.SECOND, 00);
			Calendar endTime = Calendar.getInstance();
			endTime.set(Calendar.HOUR_OF_DAY, 9);
			endTime.set(Calendar.MINUTE, 30);
			endTime.set(Calendar.SECOND, 00);

			ISala room = sp.getISalaPersistente().readByName("Ga1");
			assertNotNull(room);

			IAula lesson = null;
			IAulaPersistente persistentLesson = sp.getIAulaPersistente();

			IPersistentExecutionCourse persistentExecutionCourse =
				sp.getIPersistentExecutionCourse();
			IExecutionCourse executionCourse =
				persistentExecutionCourse
					.readBySiglaAndAnoLectivoAndSiglaLicenciatura(
					"TFCI",
					"2002/2003",
					"LEIC");
			assertNotNull(executionCourse);

			ITurnoPersistente persistentShift = sp.getITurnoPersistente();
			ITurno shift = null;

			if (existing) {
				lesson =
					persistentLesson.readByDiaSemanaAndInicioAndFimAndSala(
						weekDay,
						startTime,
						endTime,
						room,
						executionCourse.getExecutionPeriod());
				assertNotNull(lesson);
				shift =
					persistentShift.readByNameAndExecutionCourse(
						"turno1",
						executionCourse);
				assertNotNull(shift);

			} else {
				lesson = new Aula();
				lesson.setDiaSemana(new DiaSemana(DiaSemana.DOMINGO));
				lesson.setFim(endTime);
				lesson.setInicio(startTime);
				lesson.setSala(room);
				lesson.setTipo(new TipoAula(TipoAula.DUVIDAS));
				lesson.setDisciplinaExecucao(executionCourse);
				shift =
					new Turno(
						"desc",
						new TipoAula(TipoAula.RESERVA),
						new Integer(1000),
						executionCourse);

			}

			this.infoLesson = Cloner.copyILesson2InfoLesson(lesson);
			this.infoShift = Cloner.copyShift2InfoShift(shift);

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
