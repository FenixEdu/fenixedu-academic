/*
 * LerTurnoServicosTest.java
 * JUnit based test
 *
 * Created on 26 de Outubro de 2002, 17:19
 */

package ServidorAplicacao.Servicos.sop;

/**
 *
 * @author tfc130
 */
import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoDegree;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoShift;
import DataBeans.ShiftKey;
import ServidorAplicacao.Servicos.TestCaseServicos;

public class LerTurnoServicosTest extends TestCaseServicos {
	public LerTurnoServicosTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(LerTurnoServicosTest.class);

		return suite;
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}

	// read turno by unauthorized user
	public void testUnauthorizedReadTurno() {
		Object argsLerTurno[] = new Object[1];
		argsLerTurno[0] = new ShiftKey("turno1", null);

		Object result = null;
		try {
			result = _gestor.executar(_userView2, "LerTurno", argsLerTurno);
			fail("testUnauthorizedReadTurno");
		} catch (Exception ex) {
			assertNull("testUnauthorizedReadTurno", result);
		}
	}

	public void testReadExistingTurno() {
		// read existing Turno
		Object argsLerTurno[] = new Object[1];
		argsLerTurno[0] = new ShiftKey(_turno1.getNome(), null);

		Object result = null;
		try {
			InfoDegree infoLicenciatura =
				new InfoDegree(
					_turno1
						.getDisciplinaExecucao()
						.getLicenciaturaExecucao()
						.getCurso()
						.getSigla(),
					_turno1
						.getDisciplinaExecucao()
						.getLicenciaturaExecucao()
						.getCurso()
						.getNome());
			InfoExecutionDegree infoLicenciaturaExecucao =
				new InfoExecutionDegree(
					_turno1
						.getDisciplinaExecucao()
						.getLicenciaturaExecucao()
						.getAnoLectivo(),
					infoLicenciatura);
			InfoExecutionCourse infoDisciplinaExecucao =
				new InfoExecutionCourse(
					_turno1.getDisciplinaExecucao().getNome(),
					_turno1.getDisciplinaExecucao().getSigla(),
					_turno1.getDisciplinaExecucao().getPrograma(),
					infoLicenciaturaExecucao,
					_turno1.getDisciplinaExecucao().getTheoreticalHours(),
					_turno1.getDisciplinaExecucao().getPraticalHours(),
					_turno1.getDisciplinaExecucao().getTheoPratHours(),
					_turno1.getDisciplinaExecucao().getLabHours());
			InfoShift infoTurno =
				new InfoShift(
					_turno1.getNome(),
					_turno1.getTipo(),
					_turno1.getLotacao(),
					infoDisciplinaExecucao);
			result = _gestor.executar(_userView, "LerTurno", argsLerTurno);
			assertEquals("testReadExistingTurno", infoTurno, result);
		} catch (Exception ex) {
			fail("testReadExistingTurno");
		}
	}

	public void testReadUnexistingTurno() {
		// read unexisting Turno
		Object argsLerTurno[] = new Object[1];
		argsLerTurno[0] = new ShiftKey("TurnoInexistente", null);

		Object result = null;
		try {
			result = _gestor.executar(_userView, "LerTurno", argsLerTurno);
			assertNull("testReadExistingTurno", result);
		} catch (Exception ex) {
			fail("testReadExistingTurno");
		}
	}

}
