/*
 * LerLicenciaturaExecucaoDeLicenciaturaAndSemestreServicosTest.java
 * JUnit based test
 *
 * Created on 24 de Novembro de 2002, 19:35
 */

package ServidorAplicacao.Servicos.sop;

/**
 *
 * @author tfc130
 */
import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.DegreeKey;
import DataBeans.InfoExecutionDegree;
import ServidorAplicacao.Servicos.TestCaseServicos;

public class LerLicenciaturaExecucaoDeLicenciaturaServicosTest
	extends TestCaseServicos {
	public LerLicenciaturaExecucaoDeLicenciaturaServicosTest(
		java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite =
			new TestSuite(
				LerLicenciaturaExecucaoDeLicenciaturaServicosTest.class);

		return suite;
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}

	// read licenciaturaExecucao by unauthorized user
	// no Authorization is required
	public void testUnauthorizedReadLicenciaturaExecucao() {
		Object argsLerLicenciaturaExecucao[] = new Object[1];
		argsLerLicenciaturaExecucao[0] = new DegreeKey(new String("LEIC"));

		Object result = null;
		try {
			result =
				_gestor.executar(
					_userView2,
					"LerLicenciaturaExecucaoDeLicenciatura",
					argsLerLicenciaturaExecucao);
			assertNotNull("testUnauthorizedReadLicenciaturaExecucao", result);
		} catch (Exception ex) {
			fail("testUnauthorizedReadLicenciaturaExecucao");
		}
	}

	public void testRead() {
		Object argsLerLicenciaturaExecucao[] = new Object[1];
		argsLerLicenciaturaExecucao[0] = new DegreeKey(_curso2.getSigla());

		Object result = null;
		try {
			result =
				_gestor.executar(
					_userView,
					"LerLicenciaturaExecucaoDeLicenciatura",
					argsLerLicenciaturaExecucao);
			assertNull(
				"testReadLicenciaturaExecucao: nao ha licenciaturasexecucao para ler",
				result);
		} catch (Exception ex) {
			fail("testReadLicenciaturaExecucao: nao ha licenciaturasexecucao para ler");
		}

		argsLerLicenciaturaExecucao[0] = new DegreeKey(_curso1.getSigla());
		try {
			result =
				_gestor.executar(
					_userView,
					"LerLicenciaturaExecucaoDeLicenciatura",
					argsLerLicenciaturaExecucao);
			assertEquals(
				"testReadLicenciaturaExecucao:",
				_cursoExecucao1.getAnoLectivo(),
				((InfoExecutionDegree) result).getAnoLectivo());
			assertEquals(
				"testReadLicenciaturaExecucao:",
				_cursoExecucao1.getCurso().getSigla(),
				((InfoExecutionDegree) result)
					.getInfoLicenciatura()
					.getSigla());
		} catch (Exception ex) {
			fail("testReadLicenciaturaExecucao:");
		}
	}
}
