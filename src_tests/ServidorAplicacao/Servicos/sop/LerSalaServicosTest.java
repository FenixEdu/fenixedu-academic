/*
 * LerSalaServicosTest.java
 * JUnit based test
 *
 * Created on 26 de Outubro de 2002, 11:23
 */

package ServidorAplicacao.Servicos.sop;

/**
 *
 * @author tfc130
 */
import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoRoom;
import DataBeans.RoomKey;
import ServidorAplicacao.Servicos.TestCaseServicos;

public class LerSalaServicosTest extends TestCaseServicos {
	public LerSalaServicosTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(LerSalaServicosTest.class);

		return suite;
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}

	// read sala by unauthorized user
	public void testUnauthorizedReadSala() {
		Object argsLerSala[] = new Object[1];
		argsLerSala[0] = new RoomKey("Ga1");

		Object result = null;
		try {
			result = _gestor.executar(_userView2, "LerSala", argsLerSala);
			fail("testUnauthorizedReadSala");
		} catch (Exception ex) {
			assertNull("testUnauthorizedReadSala", result);
		}
	}

	public void testReadExistingSala() {
		// read existing Sala
		Object argsLerSala[] = new Object[1];
		argsLerSala[0] = new RoomKey("Ga1");

		Object result = null;
		try {
			result = _gestor.executar(_userView, "LerSala", argsLerSala);
			InfoRoom infoSala =
				new InfoRoom(
					_sala1.getNome(),
					_sala1.getEdificio(),
					_sala1.getPiso(),
					_sala1.getTipo(),
					_sala1.getCapacidadeNormal(),
					_sala1.getCapacidadeExame());
			assertEquals("testReadExistingSala", infoSala, result);
		} catch (Exception ex) {
			System.out.println("E a excepcao foi...");
			fail("testReadExistingSala " + ex);
		}
	}

	public void testReadUnexistingSala() {
		// read unexisting Sala
		Object argsLerSala[] = new Object[1];
		argsLerSala[0] = new RoomKey("Ga6");

		Object result = null;
		try {
			result = _gestor.executar(_userView, "LerSala", argsLerSala);
			assertNull("testReadExistingSala", result);
		} catch (Exception ex) {
			fail("testReadExistingSala");
		}
	}

}
