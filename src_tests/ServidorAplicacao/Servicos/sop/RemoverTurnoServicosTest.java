/*
 * RemoverTurnoServicosTest.java
 * JUnit based test
 *
 * Created on 26 de Outubro de 2002, 17:04
 */

package ServidorAplicacao.Servicos.sop;

/**
 *
 * @author tfc130
 */
import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.ClassAndShiftKeys;
import ServidorAplicacao.Servicos.TestCaseServicos;

public class RemoverTurnoServicosTest extends TestCaseServicos {
	public RemoverTurnoServicosTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(RemoverTurnoServicosTest.class);

		return suite;
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}

	// remove Turno by unauthorized user
	public void testUnauthorizedRemoveTurno() {
		Object argsRemoverTurno[] = new Object[1];
		argsRemoverTurno[0] = new ClassAndShiftKeys("turma1", "turno1");

		Object result = null;
		try {
			result =
				_gestor.executar(_userView2, "RemoverTurno", argsRemoverTurno);
			fail("testUnauthorizedRemoveTurno");
		} catch (Exception ex) {
			assertNull("testUnauthorizedRemoveTurno", result);
		}
	}

	public void testRemoverTurno() {
		Object argsRemoverTurno[] = new Object[1];
		argsRemoverTurno[0] = new ClassAndShiftKeys("turma1", "turno1");

		Object result = null;
		try {
			result =
				_gestor.executar(_userView, "RemoverTurno", argsRemoverTurno);
			assertEquals(
				"testRemoverTurno",
				Boolean.TRUE.booleanValue(),
				((Boolean) result).booleanValue());
		} catch (Exception ex) {
			fail("testRemoverTurno");
		}
	}

}
