/*
 * LerTurmaServicosTest.java
 * JUnit based test
 *
 * Created on 26 de Outubro de 2002, 11:47
 */

package ServidorAplicacao.Servicos.sop;

/**
 *
 * @author tfc130
 */
import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.ClassKey;
import DataBeans.InfoClass;
import DataBeans.InfoDegree;
import ServidorAplicacao.Servicos.TestCaseServicos;

public class LerTurmaServicosTest extends TestCaseServicos {
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

	// read turma by unauthorized user
	public void testUnauthorizedReadTurma() {
		Object argsLerTurma[] = new Object[1];
		argsLerTurma[0] = new ClassKey("turma1");

		Object result = null;
		try {
			result = _gestor.executar(_userView2, "LerTurma", argsLerTurma);
			fail("testUnauthorizedReadTurmas");
		} catch (Exception ex) {
			assertNull("testUnauthorizedReadTurmas", result);
		}
	}

	public void testReadExistingTurma() {
		// read existing Turma
		Object argsLerTurma[] = new Object[1];
		argsLerTurma[0] = new ClassKey(_turma1.getNome());

		Object result = null;
		try {
			InfoDegree infoLicenciatura =
				new InfoDegree(
					_turma1.getLicenciatura().getSigla(),
					_turma1.getLicenciatura().getNome());
			InfoClass infoTurma =
				new InfoClass(
					_turma1.getNome(),
					_turma1.getSemestre(),
					_turma1.getAnoCurricular(),
					infoLicenciatura);
			result = _gestor.executar(_userView, "LerTurma", argsLerTurma);
			assertEquals("testReadExistingTurma", infoTurma, result);
		} catch (Exception ex) {
			fail("testReadExistingTurma");
		}
	}

	public void testReadUnexistingTurma() {
		// read unexisting Turma
		Object argsLerTurma[] = new Object[1];
		argsLerTurma[0] = new ClassKey("TurmaInexistente");

		Object result = null;
		try {
			result = _gestor.executar(_userView, "LerTurma", argsLerTurma);
			assertNull("testReadExistingTurma", result);
		} catch (Exception ex) {
			fail("testReadExistingTurma");
		}
	}

}
