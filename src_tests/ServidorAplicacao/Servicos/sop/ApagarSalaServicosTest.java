/*
 * ApagarSalaServicosTest.java
 * JUnit based test
 *
 * Created on 24 de Outubro de 2002, 15:46
 */

package ServidorAplicacao.Servicos.sop;

/**
 *
 * @author tfc130
 */
import junit.framework.*;
import DataBeans.*;
import ServidorAplicacao.Servicos.*;

public class ApagarSalaServicosTest extends TestCaseServicos {
	public ApagarSalaServicosTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(ApagarSalaServicosTest.class);

		return suite;
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}

	// unauthorized delete sala
	public void testUnauthorizedDeleteSala() {
		Object argsDeleteSala[] = new Object[1];
		argsDeleteSala[0] = new RoomKey(new String("Ga1"));

		Object result = null;
		try {
			result = _gestor.executar(_userView2, "ApagarSala", argsDeleteSala);
			fail("testUnauthorizedDeleteSala");
		} catch (Exception ex) {
			assertNull("testUnauthorizedDeleteSala", result);
		}
	}

	// delete existing sala
	public void testDeleteExistingSala() {
		Object argsDeleteSala[] = new Object[1];
		argsDeleteSala[0] = new RoomKey(new String("Ga1"));

		Object result = null;
		try {
			result = _gestor.executar(_userView, "ApagarSala", argsDeleteSala);
			assertEquals(
				"testDeleteExistingSala",
				Boolean.TRUE.booleanValue(),
				((Boolean) result).booleanValue());
		} catch (Exception ex) {
			fail("testDeleteExistingSala");
			System.out.println("Serviço não executado: " + ex);
		}
	}

	// delete non-existing sala
	public void testDeleteNonExistingSala() {
		Object argsDeleteSala[] = new Object[1];
		argsDeleteSala[0] = new RoomKey(new String("Ga2"));

		Object result = null;
		try {
			result = _gestor.executar(_userView, "ApagarSala", argsDeleteSala);
			assertEquals(
				"testDeleteNonExistingSala",
				Boolean.FALSE.booleanValue(),
				((Boolean) result).booleanValue());
		} catch (Exception ex) {
			fail("testDeleteNonExistingSala");
			System.out.println("Serviço não executado: " + ex);
		}
	}

}
