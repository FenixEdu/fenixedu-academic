/*
 * Created on 11/Ago/2003
 *
 */
package ServidorPersistente.OJB;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.DisciplinaExecucao;
import Dominio.IDisciplinaExecucao;
import Dominio.ITest;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentTest;
import ServidorPersistente.ISuportePersistente;

/**
 * @author Susana Fernandes
 */
public class TestOJBTest extends TestCaseOJB {

	/**
	 * @param testName
	 */
	public TestOJBTest(String testName) {
		super(testName);

	}

	public static Test suite() {
		TestSuite suite = new TestSuite(TestOJBTest.class);
		return suite;
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	protected void setUp() {
		super.setUp();

	}

	protected void tearDown() {
		super.tearDown();
	}

	public void testReadByExecutionCourse() {
		System.out.println("1-> Test ReadByExecutionCourse");
		try {
			ISuportePersistente persistentSuport =
				SuportePersistenteOJB.getInstance();
			persistentSuport.iniciarTransaccao();
			IDisciplinaExecucaoPersistente persistentExecutionCourse =
				persistentSuport.getIDisciplinaExecucaoPersistente();
			IDisciplinaExecucao executionCourse =
				new DisciplinaExecucao(new Integer(26));
			executionCourse =
				(IDisciplinaExecucao) persistentExecutionCourse.readByOId(
					executionCourse,
					false);
			assertNotNull(
				"there is no executionCourse with id=26",
				executionCourse);
			IPersistentTest persistentTest =
				persistentSuport.getIPersistentTest();
			List result = persistentTest.readByExecutionCourse(executionCourse);
			assertNotNull(
				"there are no tests for this executionCourse",
				result);

			assertEquals("wrong number of tests", 3, result.size());
			persistentSuport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			fail("exception: ExcepcaoPersistencia");
		}
	}

	public void testDelete() {
		System.out.println("2-> Test Delete");
		try {
			ISuportePersistente persistentSuport =
				SuportePersistenteOJB.getInstance();
			persistentSuport.iniciarTransaccao();
			IPersistentTest persistentTest =
				persistentSuport.getIPersistentTest();
			ITest test = new Dominio.Test(new Integer(3));
			assertNotNull("there is no test with id=3", test);
			persistentTest.delete(test);
			persistentSuport.confirmarTransaccao();
			persistentSuport.iniciarTransaccao();
			IDisciplinaExecucaoPersistente persistentExecutionCourse =
				persistentSuport.getIDisciplinaExecucaoPersistente();
			IDisciplinaExecucao executionCourse =
				new DisciplinaExecucao(new Integer(26));
			executionCourse =
				(IDisciplinaExecucao) persistentExecutionCourse.readByOId(
					executionCourse,
					false);
			assertNotNull(
				"there is no executionCourse with id=26",
				executionCourse);
			List result =
				persistentTest.readByExecutionCourse(executionCourse);
			assertNotNull(
				"there are no test for this executionCourse",
				result);
			assertEquals("wrong number of test", 2, result.size());
			persistentSuport.confirmarTransaccao();

		} catch (ExcepcaoPersistencia e) {
			fail("exception: ExcepcaoPersistencia");
		}
	}
}
