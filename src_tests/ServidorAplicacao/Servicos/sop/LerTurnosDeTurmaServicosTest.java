/*
 * LerTurnosDeTurmaServicosTest.java
 * JUnit based test
 *
 * Created on 28 de Outubro de 2002, 20:31
 */

package ServidorAplicacao.Servicos.sop;

/**
 *
 * @author tfc130
 */
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.ClassKey;
import ServidorAplicacao.Servicos.TestCaseServicos;

public class LerTurnosDeTurmaServicosTest extends TestCaseServicos {
	public LerTurnosDeTurmaServicosTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(LerTurnosDeTurmaServicosTest.class);

		return suite;
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}

	// read turmas by unauthorized user
	public void testUnauthorizedReadTurnosDeTurma() {
		Object argsLerTurnosDeTurma[] = new Object[1];
		argsLerTurnosDeTurma[0] = new ClassKey("turma1");

		Object result = null;
		try {
			result =
				_gestor.executar(
					_userView2,
					"LerTurnosDeTurma",
					argsLerTurnosDeTurma);
			fail("testUnauthorizedReadTurnosDeTurma");
		} catch (Exception ex) {
			assertNull("testUnauthorizedReadTurnosDeTurma", result);
		}
	}

	// read new existing turnos de turma
	public void testReadExistingTurnosDeTurma() {
		Object argsLerTurnosDeTurma[] = new Object[1];
		argsLerTurnosDeTurma[0] = new ClassKey("turma1");

		Object result = null;
		try {
			result =
				_gestor.executar(
					_userView,
					"LerTurnosDeTurma",
					argsLerTurnosDeTurma);
			assertEquals(
				"testReadExistingTurnosDeTurma",
				1,
				((List) result).size());
		} catch (Exception ex) {
			fail("testReadExistingTurnosDeTurma");
		}
	}

	// read new non-existing turnos de turma
	public void testReadNonExistingTurnosDeTurma() {
		Object argsLerTurnosDeTurma[] = new Object[1];
		argsLerTurnosDeTurma[0] = new ClassKey("turma2");

		Object result = null;
		try {
			result =
				_gestor.executar(
					_userView,
					"LerTurnosDeTurma",
					argsLerTurnosDeTurma);
			assertTrue(
				"testReadExistingTurnosDeTurma",
				((List) result).isEmpty());
		} catch (Exception ex) {
			fail("testReadExistingTurnosDeTurma");
		}
	}

}
