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
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentDistributedTest;
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
				"there is no distributedTest with id=25",
				distributedTest);
			persistentDistributedTest.delete(distributedTest);
			persistentSuport.confirmarTransaccao();
			persistentSuport.iniciarTransaccao();
			IDisciplinaExecucao executrionCourse =
				new DisciplinaExecucao(new Integer(26));
			executrionCourse =
				(IDisciplinaExecucao) persistentSuport
					.getIDisciplinaExecucaoPersistente()
					.readByOId(executrionCourse, false);
			assertNotNull(
				"there is no executionCourse with id=26",
				executrionCourse);
			List result =
				persistentDistributedTest.readByExecutionCourse(
					executrionCourse);
			assertNotNull(
				"there is no distributed test for this executionCourse",
				result);
			assertEquals("wrong number of questions", 2, result.size());
			persistentSuport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			fail("exception: ExcepcaoPersistencia");
		}
	}
}
