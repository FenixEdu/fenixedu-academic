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
import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.RoomKey;
import ServidorAplicacao.Servicos.TestCaseServicosWithAuthorization;

public class ApagarSalaServicosTest extends TestCaseServicosWithAuthorization {

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

		super.testUnauthorizedExecutionOfService("ApagarSala");

//		Object argsDeleteSala[] = new Object[1];
//		argsDeleteSala[0] = new RoomKey(new String("Ga1"));
//
//		Object result = null;
//		try {
//			result = _gestor.executar(_userView2, "ApagarSala", argsDeleteSala);
//			fail("testUnauthorizedDeleteSala");
//		} catch (Exception ex) {
//			assertNull("testUnauthorizedDeleteSala", result);
//		}
	}

	// delete existing sala
	public void testDeleteExistingSala() {

		Object argsDeleteSala[] = new Object[1];
		argsDeleteSala[0] = new RoomKey(new String("Ga3"));

		Object result = null;
		try {
			result = _gestor.executar(_userView, "ApagarSala", argsDeleteSala);
			assertEquals("testDeleteExistingSala", Boolean.TRUE.booleanValue(), ((Boolean) result).booleanValue());
		} catch (Exception ex) {
			fail("testDeleteExistingSala");
		}
	}

	// delete non-existing sala
	public void testDeleteNonExistingSala() {

		Object argsDeleteSala[] = new Object[1];
		argsDeleteSala[0] = new RoomKey(new String("Ga4"));

		Object result = null;
		try {
			result = _gestor.executar(_userView, "ApagarSala", argsDeleteSala);
			assertEquals("testDeleteNonExistingSala", Boolean.FALSE.booleanValue(), ((Boolean) result).booleanValue());
		} catch (Exception ex) {
			fail("testDeleteNonExistingSala");
		}
	}

	// delete existing sala with associations
	public void testDeleteExistingSalaWithAssociations() {

		Object argsDeleteSala[] = new Object[1];
		argsDeleteSala[0] = new RoomKey(new String("Ga1"));

		Object result = null;
		try {
			result = _gestor.executar(_userView, "ApagarSala", argsDeleteSala);
			assertEquals("testDeleteExistingSala", Boolean.FALSE.booleanValue(), ((Boolean) result).booleanValue());
		} catch (Exception ex) {
			fail("testDeleteExistingSala");
		}
	}

}
