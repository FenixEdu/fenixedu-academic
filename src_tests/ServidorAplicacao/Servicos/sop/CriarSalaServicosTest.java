
/*
 * CriarSalaServicosTest.java
 * JUnit based test
 *
 * Created on 24 de Outubro de 2002, 12:00
 */

package ServidorAplicacao.Servicos.sop;

/**
 *
 * @author tfc130
 */
import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoRoom;
import ServidorAplicacao.Servicos.TestCaseServicosWithAuthorization;
import Util.TipoSala;

public class CriarSalaServicosTest extends TestCaseServicosWithAuthorization {

	public CriarSalaServicosTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(CriarSalaServicosTest.class);

		return suite;
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}

	// unauthorized write sala
	public void testUnauthorizedCreateSala() {
		super.testUnauthorizedExecutionOfService("CriarSala");
	}

	// write existing sala
	public void testCreateExistingSala() {
		Object argsCriarSala[] = new Object[1];
		argsCriarSala[0] = new InfoRoom(new String("Ga1"), new String("Pavilhilhão Central"), new Integer(0), new TipoSala(TipoSala.ANFITEATRO), new Integer(100), new Integer(50));

		Object result = null;
		try {
			result = _gestor.executar(_userView, "CriarSala", argsCriarSala);
			fail("testCreateExistingSala");
		} catch (Exception ex) {
			assertNull("testCreateExistingSala", result);
		}
	}

	// write new non-existing sala
	public void testCreateNonExistingSala() {
		Object argsCriarSala[] = new Object[1];
		argsCriarSala[0] = new InfoRoom(new String("Ga4"), new String("Pavilhilhão Central"), new Integer(1), new TipoSala(TipoSala.ANFITEATRO), new Integer(100), new Integer(50));

		Object result = null;
		try {
			result = _gestor.executar(_userView, "CriarSala", argsCriarSala);
			assertEquals("testCreateNonExistingSala", Boolean.TRUE.booleanValue(), ((Boolean) result).booleanValue());
		} catch (Exception ex) {
			fail("testCreateNonExistingSala");
		}
	}
}