/*
 * CriarAulaServicosTest.java
 * JUnit based test
 *
 * Created on 26 de Outubro de 2002, 15:51
 */

package ServidorAplicacao.Servicos.sop;

/**
 *
 * @author tfc130
 */
import junit.framework.*;
import Util.*;
import ServidorAplicacao.Servicos.*;
import DataBeans.*;
import DataBeans.util.Cloner;

public class CriarAulaServicosTest extends TestCaseServicos {
	public CriarAulaServicosTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(CriarAulaServicosTest.class);

		return suite;
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}

	// write aula by unauthorized user
	public void testUnauthorizedCreateAula() {
		InfoRoom infoSala = Cloner.copyRoom2InfoRoom(_sala1);
		InfoExecutionCourse infoDisciplinaExecucao =
			Cloner.copyIExecutionCourse2InfoExecutionCourse(
				_disciplinaExecucao1);
		Object argsCriarAula[] = new Object[1];
		argsCriarAula[0] =
			new InfoLesson(
				_diaSemana1,
				_inicio,
				_fim,
				_tipoAula,
				infoSala,
				infoDisciplinaExecucao);

		Object result = null;
		try {
			result = _gestor.executar(_userView2, "CriarAula", argsCriarAula);
			fail("testUnauthorizedCreateAula");
		} catch (Exception ex) {
			// all is ok
			assertNull("testUnauthorizedCreateAula", result);
		}
	}

	// write new existing aula
	public void testCreateExistingAula() {
		InfoRoom infoSala = Cloner.copyRoom2InfoRoom(_sala1);
		InfoExecutionCourse infoDisciplinaExecucao =
			Cloner.copyIExecutionCourse2InfoExecutionCourse(
				_disciplinaExecucao1);

		Object argsCriarAula[] = new Object[1];
		argsCriarAula[0] =
			new InfoLesson(
				_diaSemana1,
				_inicio,
				_fim,
				_tipoAula,
				infoSala,
				infoDisciplinaExecucao);

		Object result = null;
		try {
			result = _gestor.executar(_userView, "CriarAula", argsCriarAula);
			fail("testCreateExistingAula");
		} catch (Exception ex) {
			assertNull("testCreateExistingAula", result);
		}
	}

	// write new non-existing aula
	public void testCreateNonExistingAula() {
		InfoRoom infoSala = Cloner.copyRoom2InfoRoom(_sala1);
		InfoExecutionCourse infoDisciplinaExecucao =
			Cloner.copyIExecutionCourse2InfoExecutionCourse(
				_disciplinaExecucao1);

		Object argsCriarAula[] = new Object[1];
		argsCriarAula[0] =
			new InfoLesson(
				new DiaSemana(DiaSemana.SEXTA_FEIRA),
				_inicio,
				_fim,
				_tipoAula,
				infoSala,
				infoDisciplinaExecucao);

		Object result = null;
		try {
			result = _gestor.executar(_userView, "CriarAula", argsCriarAula);
			assertTrue(
				"testCreateNonExistingAula",
				((InfoLessonServiceResult) result).isSUCESS());
		} catch (Exception ex) {
			fail("testCreateNonExistingAula");
		}
	}

}
