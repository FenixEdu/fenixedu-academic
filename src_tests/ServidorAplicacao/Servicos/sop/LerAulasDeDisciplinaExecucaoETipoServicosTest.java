/*
 * LerAulasDeDisciplinaExecucaoETipoServicosTest.java
 * JUnit based test
 *
 * Created on 28 de Outubro de 2002, 18:36
 */

package ServidorAplicacao.Servicos.sop;

/**
 *
 * @author tfc130
 */
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.ExecutionCourseKeyAndLessonType;
import ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices;
import Util.TipoAula;

public class LerAulasDeDisciplinaExecucaoETipoServicosTest extends TestCaseNeedAuthorizationServices {

	public LerAulasDeDisciplinaExecucaoETipoServicosTest(
		java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite =
			new TestSuite(LerAulasDeDisciplinaExecucaoETipoServicosTest.class);

		return suite;
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}

	/**
	 * @see ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices#getNameOfServiceToBeTested()
	 */
	protected String getNameOfServiceToBeTested() {
		return "LerAulasDeDisciplinaExecucaoETipo";
	}

	// read existing aulas
	public void testReadExistingAulas() {
		Object argsLerAulas[] = new Object[1];
		argsLerAulas[0] =
			new ExecutionCourseKeyAndLessonType(
				new TipoAula(TipoAula.TEORICA),
				_disciplinaExecucao1.getSigla());

		Object result = null;
		try {
			result =
				_gestor.executar(
					_userView,
					getNameOfServiceToBeTested(),
					argsLerAulas);
			assertEquals("testLerExistingAulas", 1, ((List) result).size());
		} catch (Exception ex) {
			fail("testLerExistingAulas");
		}
	}

	// read non-existing aulas
	public void testReadNonExistingAulas() {
		Object argsLerAulas[] = new Object[1];
		argsLerAulas[0] =
			new ExecutionCourseKeyAndLessonType(
				new TipoAula(TipoAula.PRATICA),
				_disciplinaExecucao2.getSigla());

		Object result = null;
		try {
			result =
				_gestor.executar(
					_userView,
					getNameOfServiceToBeTested(),
					argsLerAulas);
			assertTrue("testLerNonExistingAulas", ((List) result).isEmpty());
		} catch (Exception ex) {
			fail("testLerNonExistingAulas");
		}
	}
}
