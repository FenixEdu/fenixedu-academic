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
import framework.factory.ServiceManagerServiceFactory;
import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.RoomKey;
import ServidorAplicacao.Servico.exceptions.notAuthorizedServiceDeleteException;
import ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices;

public class ApagarSalaServicosTest extends TestCaseDeleteAndEditServices {

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

	protected String getNameOfServiceToBeTested() {
		return "ApagarSala";
	}

	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {

		Object argsDeleteSala[] = new Object[1];
		argsDeleteSala[0] = new RoomKey(new String("Ga3"));

		return argsDeleteSala;
	}

	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {

		Object argsDeleteSala[] = new Object[1];
		argsDeleteSala[0] = new RoomKey(new String("Ga4"));

		return argsDeleteSala;
	}

	// delete existing sala with associations
	public void testDeleteExistingSalaWithAssociations() {

		Object argsDeleteSala[] = new Object[1];
		argsDeleteSala[0] = new RoomKey(new String("Ga1"));

		try {
		
				ServiceManagerServiceFactory.executeService(
					_userView,
					getNameOfServiceToBeTested(),
					argsDeleteSala);

			fail("testDeleteExistingSala");
		} catch (notAuthorizedServiceDeleteException ex) {
			assertTrue(true);
		} catch (Exception ex) {
			fail("testDeleteExistingSala");
		}
	}
}