/*
 * EditarSalaServicosTest.java
 * JUnit based test
 *
 * Created on 27 de Outubro de 2002, 19:58
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
import ServidorAplicacao.Servicos.TestCaseServicosWithAuthorization;
import Util.TipoSala;

public class EditarSalaServicosTest extends TestCaseServicosWithAuthorization {

	public EditarSalaServicosTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(EditarSalaServicosTest.class);

		return suite;
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}

	/**
	 * @see ServidorAplicacao.Servicos.TestCaseServicosWithAuthorization#getNameOfServiceToBeTested()
	 */
	protected String getNameOfServiceToBeTested() {
		return "EditarSala";
	}

	// edit existing sala
	public void testEditExistingSala() {

		Object argsEditarSala[] = new Object[2];
		argsEditarSala[0] = new RoomKey("Ga1");
		argsEditarSala[1] = new InfoRoom(new String("Ga1"), new String("Pavilhilhão Central"), new Integer(1), new TipoSala(TipoSala.ANFITEATRO), new Integer(150), new Integer(25));

		Object result = null;
		try {
			result = _gestor.executar(_userView, getNameOfServiceToBeTested(), argsEditarSala);
			assertEquals("testEditNonExistingSala", Boolean.TRUE.booleanValue(), ((Boolean) result).booleanValue());
		} catch (Exception ex) {
			fail("testEditNonExistingSala");
		}
	}

	// edit non-existing sala
	public void testEditarNonExistingSala() {

		Object argsEditarSala[] = new Object[2];
		argsEditarSala[0] = new RoomKey("Ga4");
		argsEditarSala[1] = new InfoRoom(new String("Ga4"), new String("Pavilhilhão Central"), new Integer(1), new TipoSala(TipoSala.ANFITEATRO), new Integer(100), new Integer(50));

		Object result = null;
		try {
			result = _gestor.executar(_userView, getNameOfServiceToBeTested(), argsEditarSala);
			assertEquals("testEditNonExistingSala", Boolean.FALSE.booleanValue(), ((Boolean) result).booleanValue());
		} catch (Exception ex) {
			fail("testEditNonExistingSala");
		}
	}
}