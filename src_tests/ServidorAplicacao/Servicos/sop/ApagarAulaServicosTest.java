/*
 * ApagarAulaServicosTest.java
 * JUnit based test
 *
 * Created on 26 de Outubro de 2002, 15:07
 */

package ServidorAplicacao.Servicos.sop;

/**
 *
 * @author tfc130
 */
import java.util.Calendar;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.KeyLesson;
import DataBeans.RoomKey;
import ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices;
import Util.DiaSemana;

public class ApagarAulaServicosTest extends TestCaseDeleteAndEditServices {

	public ApagarAulaServicosTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(ApagarAulaServicosTest.class);

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
		return "ApagarAula";
	}

	/**
	 * @see ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices#getArgumentsOfServiceToBeTestedSuccessfuly()
	 */
	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {

		DiaSemana diaSemana = null;
		Calendar inicio = null;
		Calendar fim = null;
		diaSemana = new DiaSemana(DiaSemana.SEGUNDA_FEIRA);
		inicio = Calendar.getInstance();
		inicio.set(Calendar.HOUR_OF_DAY, 8);
		inicio.set(Calendar.MINUTE, 0);
		inicio.set(Calendar.SECOND, 0);
		fim = Calendar.getInstance();
		fim.set(Calendar.HOUR_OF_DAY, 9);
		fim.set(Calendar.MINUTE, 30);
		fim.set(Calendar.SECOND, 0);

		RoomKey keySala = new RoomKey("Ga1");

		Object argsDeleteAula[] = new Object[1];
		argsDeleteAula[0] = new KeyLesson(diaSemana, inicio, fim, keySala);

		return argsDeleteAula;
	}

	/**
	 * @see ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices#getArgumentsOfServiceToBeTestedUnsuccessfuly()
	 */
	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {

		DiaSemana diaSemana = null;
		Calendar inicio = null;
		Calendar fim = null;
		diaSemana = new DiaSemana(DiaSemana.DOMINGO);
		inicio = Calendar.getInstance();
		inicio.set(Calendar.HOUR_OF_DAY, 8);
		inicio.set(Calendar.MINUTE, 0);
		inicio.set(Calendar.SECOND, 0);
		fim = Calendar.getInstance();
		fim.set(Calendar.HOUR_OF_DAY, 9);
		fim.set(Calendar.MINUTE, 30);
		fim.set(Calendar.SECOND, 0);

		RoomKey keySala = new RoomKey("Ga1");

		Object argsDeleteAula[] = new Object[1];
		argsDeleteAula[0] = new KeyLesson(diaSemana, inicio, fim, keySala);

		return argsDeleteAula;
	}
/*
	// delete existing aula
	public void testDeleteExistingAula() {

		DiaSemana diaSemana = null;
		Calendar inicio = null;
		Calendar fim = null;
		diaSemana = new DiaSemana(DiaSemana.SEGUNDA_FEIRA);
		inicio = Calendar.getInstance();
		inicio.set(Calendar.HOUR_OF_DAY, 8);
		inicio.set(Calendar.MINUTE, 0);
		inicio.set(Calendar.SECOND, 0);
		fim = Calendar.getInstance();
		fim.set(Calendar.HOUR_OF_DAY, 9);
		fim.set(Calendar.MINUTE, 30);
		fim.set(Calendar.SECOND, 0);

		RoomKey keySala = new RoomKey("Ga1");

		Object argsDeleteAula[] = new Object[1];
		argsDeleteAula[0] = new KeyLesson(diaSemana, inicio, fim, keySala);

		Object result = null;
		try {
			result = _gestor.executar(_userView, getNameOfServiceToBeTested(), argsDeleteAula);
			assertEquals("testDeleteNonExistingAula", Boolean.TRUE.booleanValue(), ((Boolean) result).booleanValue());
		} catch (Exception ex) {
			fail("testDeleteNonExistingAula");
		}
	}

	// delete non-existing aula
	public void testDeleteNonExistingAula() {

		DiaSemana diaSemana = null;
		Calendar inicio = null;
		Calendar fim = null;
		diaSemana = new DiaSemana(DiaSemana.DOMINGO);
		inicio = Calendar.getInstance();
		inicio.set(Calendar.HOUR_OF_DAY, 8);
		inicio.set(Calendar.MINUTE, 0);
		inicio.set(Calendar.SECOND, 0);
		fim = Calendar.getInstance();
		fim.set(Calendar.HOUR_OF_DAY, 9);
		fim.set(Calendar.MINUTE, 30);
		fim.set(Calendar.SECOND, 0);

		RoomKey keySala = new RoomKey("Ga1");

		Object argsDeleteAula[] = new Object[1];
		argsDeleteAula[0] = new KeyLesson(diaSemana, inicio, fim, keySala);

		Object result = null;
		try {
			result = _gestor.executar(_userView, getNameOfServiceToBeTested(), argsDeleteAula);
			assertEquals("testDeleteNonExistingAula", Boolean.FALSE.booleanValue(), ((Boolean) result).booleanValue());
		} catch (Exception ex) {
			fail("testDeleteNonExistingAula");
		}
	}
*/
}