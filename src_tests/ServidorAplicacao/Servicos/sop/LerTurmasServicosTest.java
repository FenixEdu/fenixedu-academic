/*
 * LerTurmasServicosTest.java
 * JUnit based test
 *
 * Created on 28 de Outubro de 2002, 20:07
 */

package ServidorAplicacao.Servicos.sop;

/**
 *
 * @author tfc130
 */
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.CurricularYearAndSemesterAndInfoExecutionDegree;
import DataBeans.InfoClass;
import DataBeans.InfoDegree;
import DataBeans.InfoExecutionDegree;
import ServidorAplicacao.Servicos.TestCaseServicos;
import ServidorPersistente.ExcepcaoPersistencia;

public class LerTurmasServicosTest extends TestCaseServicos {
	public LerTurmasServicosTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(LerTurmasServicosTest.class);

		return suite;
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}

	// read turmas by unauthorized user
	public void testUnauthorizedReadTurmas() {
		Object argsLerTurmas[] = new Object[1];
		InfoDegree iL =
			new InfoDegree(
				_turma1.getLicenciatura().getSigla(),
				_turma1.getLicenciatura().getNome());
		InfoExecutionDegree iLE = new InfoExecutionDegree("2002/03", iL);
		argsLerTurmas[0] =
			new CurricularYearAndSemesterAndInfoExecutionDegree(
				new Integer(1),
				_turma1.getSemestre(),
				iLE);
		Object result = null;

		try {
			result = _gestor.executar(_userView2, "LerTurmas", argsLerTurmas);
			fail("testUnauthorizedReadTurmas");
		} catch (Exception ex) {
			assertNull("testUnauthorizedReadTurmas", result);
		}
	}

	public void testReadAll() {
		Object argsLerTurmas[] = new Object[1];
		InfoDegree iL =
			new InfoDegree(
				_turma1.getLicenciatura().getSigla(),
				_turma1.getLicenciatura().getNome());
		InfoExecutionDegree iLE = new InfoExecutionDegree("2002/03", iL);
		argsLerTurmas[0] =
			new CurricularYearAndSemesterAndInfoExecutionDegree(
				new Integer(1),
				_turma1.getSemestre(),
				iLE);
		InfoClass infoturma1 =
			new InfoClass(_turma1.getNome(), _turma1.getSemestre(),_turma1.getAnoCurricular(), iL);
		Object result = null;

		// Nao ha turmas na BD
		try {
			_suportePersistente.iniciarTransaccao();
			_turmaPersistente.deleteAll();
			_suportePersistente.confirmarTransaccao();
		} catch (ExcepcaoPersistencia excepcao) {
			fail("Exception when setUp");
		}

		try {
			result = _gestor.executar(_userView, "LerTurmas", argsLerTurmas);
			assertEquals(
				"testReadAll: nao ha turmas para ler",
				0,
				((List) result).size());
		} catch (Exception ex) {
			fail("testReadAll: nao ha turmas para ler");
		}

		// Ha 2 turmas na BD
		try {
			_suportePersistente.iniciarTransaccao();
			_turmaPersistente.lockWrite(_turma1);
			//_turmaPersistente.lockWrite(_turma2);
			_suportePersistente.confirmarTransaccao();
		} catch (ExcepcaoPersistencia excepcao) {
			fail("Exception when setUp");
		}

		try {
			result = _gestor.executar(_userView, "LerTurmas", argsLerTurmas);
			assertEquals(
				"testReadAll: ha 1 turmas para ler",
				((List) result).size(),
				1);
			assertTrue(
				"testReadAll: turma lida e _turma1",
				((List) result).contains(infoturma1));
		} catch (Exception ex) {
			fail("testReadAll");
		}
	}

}
