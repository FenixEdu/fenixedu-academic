/*
 * Created on 26/Ago/2003
 *
 */
package ServidorPersistente.OJB;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.DisciplinaExecucao;
import Dominio.DistributedTest;
import Dominio.IDisciplinaExecucao;
import Dominio.IDistributedTest;
import Dominio.ITest;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentDistributedTest;
import ServidorPersistente.IPersistentTest;
import ServidorPersistente.ISuportePersistente;

/**
 * @author Susana Fernandes
 */
public class DistributedTestOJBTest extends TestCaseOJB {

	public DistributedTestOJBTest(String testName) {
		super(testName);

	}

	public static Test suite() {
		TestSuite suite = new TestSuite(DistributedTestOJBTest.class);
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
			IPersistentDistributedTest persistentDistributedTest =
				persistentSuport.getIPersistentDistributedTest();
			List result =
				persistentDistributedTest.readByExecutionCourse(
					executionCourse);
			assertNotNull(
				"there are no distributed tests for this executionCourse",
				result);

			assertEquals("wrong number of distributed tests", 3, result.size());
			persistentSuport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			fail("exception: ExcepcaoPersistencia");
		}
	}

	public void testReadByTest() {
		System.out.println("2-> Test ReadByTest");
		try {
			ISuportePersistente persistentSuport =
				SuportePersistenteOJB.getInstance();
			persistentSuport.iniciarTransaccao();
			IPersistentTest persistentTest =
				persistentSuport.getIPersistentTest();
			ITest test = new Dominio.Test(new Integer(3));
			test = (ITest) persistentTest.readByOId(test, false);
			assertNotNull("there is no test with id=3", test);
			IPersistentDistributedTest persistentDistributedTest =
				persistentSuport.getIPersistentDistributedTest();
			List result = persistentDistributedTest.readByTest(test);
			assertNotNull(
				"there are no distributed tests for this test",
				result);

			assertEquals("wrong number of distributed tests", 3, result.size());
			persistentSuport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			fail("exception: ExcepcaoPersistencia");
		}
	}

	public void testDeleteByTest() {
		System.out.println("3-> Test DeleteByTest");
		try {
			ISuportePersistente persistentSuport =
				SuportePersistenteOJB.getInstance();
			persistentSuport.iniciarTransaccao();
			IPersistentTest persistentTest =
				persistentSuport.getIPersistentTest();
			ITest test = new Dominio.Test(new Integer(3));
			test = (ITest) persistentTest.readByOId(test, false);
			assertNotNull("there is no test with id=3", test);
			IPersistentDistributedTest persistentDistributedTest =
				persistentSuport.getIPersistentDistributedTest();
			persistentDistributedTest.deleteByTest(test);

			persistentSuport.confirmarTransaccao();
			persistentSuport.iniciarTransaccao();

			List result = persistentDistributedTest.readByTest(test);
			assertNotNull(
				"there are no distributed tests for this test",
				result);
			assertEquals("wrong number of distributed tests", 0, result.size());
			persistentSuport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			fail("exception: ExcepcaoPersistencia");
		}
	}

	public void testDelete() {
		System.out.println("4-> Test Delete");
		try {
			ISuportePersistente persistentSuport =
				SuportePersistenteOJB.getInstance();
			persistentSuport.iniciarTransaccao();
			IPersistentDistributedTest persistentDistributedTest =
				persistentSuport.getIPersistentDistributedTest();
			IDistributedTest distributedTest =
				new DistributedTest(new Integer(25));
			assertNotNull(
				"there is no test question with id=8",
				distributedTest);
			persistentDistributedTest.delete(distributedTest);
			persistentSuport.confirmarTransaccao();
			persistentSuport.iniciarTransaccao();
			IPersistentTest persistentTest =
				persistentSuport.getIPersistentTest();
			ITest test = new Dominio.Test(new Integer(3));
			assertNotNull("there is no test with id=3", test);
			List result = persistentDistributedTest.readByTest(test);
			assertNotNull("there is no distributed test for this Test", result);
			assertEquals("wrong number of questions", 2, result.size());
			persistentSuport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			fail("exception: ExcepcaoPersistencia");
		}
	}
}
