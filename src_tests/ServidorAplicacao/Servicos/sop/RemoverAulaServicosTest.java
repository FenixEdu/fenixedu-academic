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
import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.RoomKey;
import DataBeans.ShiftAndLessonKeys;
import ServidorAplicacao.Servicos.TestCaseServicos;

public class RemoverAulaServicosTest extends TestCaseServicos {
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

	// remove Aula by unauthorized user
	public void testUnauthorizedRemoveAula() {
		RoomKey keySala = new RoomKey(_sala1.getNome());
		Object argsRemoverAula[] = new Object[1];
		argsRemoverAula[0] =
			new ShiftAndLessonKeys(
				_turno1.getNome(),
				_aula1.getDiaSemana(),
				_aula1.getInicio(),
				_aula1.getFim(),
				keySala);

		Object result = null;
		try {
			result =
				_gestor.executar(_userView2, "RemoverAula", argsRemoverAula);
			fail("testUnauthorizedRemoveAula");
		} catch (Exception ex) {
			assertNull("testUnauthorizedRemoveAula", result);
		}
	}

	public void testRemoverAula() {
		RoomKey keySala = new RoomKey(_sala1.getNome());
		Object argsRemoverAula[] = new Object[1];
		argsRemoverAula[0] =
			new ShiftAndLessonKeys(
				_turno1.getNome(),
				_aula1.getDiaSemana(),
				_aula1.getInicio(),
				_aula1.getFim(),
				keySala);

		Object result = null;
		try {
			result =
				_gestor.executar(_userView, "RemoverAula", argsRemoverAula);
			assertEquals(
				"testRemoverAula",
				Boolean.TRUE.booleanValue(),
				((Boolean) result).booleanValue());
		} catch (Exception ex) {
			fail("testRemoverAula");
		}
	}

}
