/*
 * Created on 19/Set/2003
 *
 */
package ServidorPersistente.OJB;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.DistributedTest;
import Dominio.IDistributedTest;
import Dominio.IStudent;
import Dominio.Student;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;

/**
 * @author Susana Fernandes
 */
public class StudentTestLogOJBTest extends TestCaseOJB {
	/**
		 * @param testName
		 */
	public StudentTestLogOJBTest(String testName) {
		super(testName);

	}

	public static Test suite() {
		TestSuite suite = new TestSuite(StudentTestLogOJBTest.class);
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

	public void testReadByStudentAndDistributedTest() {
		System.out.println("1-> Test ReadByStudentAndDistributedTest");
		try {
			ISuportePersistente persistentSuport =
				SuportePersistenteOJB.getInstance();
			persistentSuport.iniciarTransaccao();
			IStudent student = new Student(new Integer(9));
			student =
				(IStudent) persistentSuport.getIPersistentStudent().readByOId(
					student,
					false);
			assertNotNull("there is no student with id=9", student);

			IDistributedTest distributedTest =
				new DistributedTest(new Integer(27));
			distributedTest =
				(IDistributedTest) persistentSuport
					.getIPersistentDistributedTest()
					.readByOId(
					distributedTest,
					false);
			assertNotNull(
				"there is no distributedTest with id=27",
				distributedTest);

			List result =
				persistentSuport
					.getIPersistentStudentTestLog()
					.readByStudentAndDistributedTest(
					student,
					distributedTest);
			assertNotNull(
				"there are no student test question for this student and distributedTest",
				result);
			assertEquals(
				"wrong number of student test questions",
				10,
				result.size());
			persistentSuport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			fail("exception: ExcepcaoPersistencia");
		}
	}

}
