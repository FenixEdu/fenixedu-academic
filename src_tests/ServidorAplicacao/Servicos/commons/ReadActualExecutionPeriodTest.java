package ServidorAplicacao.Servicos.commons;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.ExecutionPeriod;
import Dominio.ExecutionYear;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear; 
import ServidorAplicacao.Servicos.TestCaseServicos;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author João Mota
 *
 */
public class ReadActualExecutionPeriodTest extends TestCaseServicos {

	/**
	 * Constructor for SelectShiftsTest.
	 * @param testName
	 */
	public ReadActualExecutionPeriodTest(String testName) {
		super(testName);
	}
	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(ReadActualExecutionPeriodTest.class);
		return suite;
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}

	/**
	 * FIXME: The OJB method doesn't quite work ... it returns the first
	 * execution period on the database, it doesn't return the Actual one ...
	 * 
	 * The test only checks if the service returns something ...
	 * 
	 */

	public void testReadActualExecutionPeriod() {
		Object result = null;

		//Empty database	
		try {
			SuportePersistenteOJB.getInstance().iniciarTransaccao();
			SuportePersistenteOJB.getInstance().getIPersistentExecutionPeriod().deleteAll();
			SuportePersistenteOJB.getInstance().confirmarTransaccao();
		} catch (ExcepcaoPersistencia excepcao) {
			fail("Exception when setUp");
		}
		try {
			result =
				_gestor.executar(_userView, "ReadActualExecutionPeriod", null);
		} catch (Exception ex) {
			fail("testReadAll: no Shifts to read");
		}
		assertNull(result);
		
//		assertEquals(
//			"testReadAll: no Shifts to read aqui",
//			0,
//			((List) result).size());

		// 1 class in database
		
		try {
			_suportePersistente.iniciarTransaccao();
			IExecutionYear executionYear = new ExecutionYear("2002/2003");
			persistentExecutionYear.writeExecutionYear(executionYear);
			IExecutionPeriod executionPeriod = new ExecutionPeriod("2 semestre", executionYear);
		    persistentExecutionPeriod.writeExecutionPeriod(executionPeriod);
			_suportePersistente.confirmarTransaccao();
		} catch (ExcepcaoPersistencia excepcao) {
			fail("Exception when setUp");
		}

		try {
			result =
				_gestor.executar(_userView, "ReadActualExecutionPeriod", null);
			assertEquals(
				"testReadAll: Actual Period",
				1,
				((List) result).size());
		} catch (Exception ex) {
			fail("testReadAll: Actual Period");
		}
	}
}
