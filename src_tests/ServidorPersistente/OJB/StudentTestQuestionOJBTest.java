/*
 * Created on 8/Set/2003
 *
 */
package ServidorPersistente.OJB;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.DistributedTest;
import Dominio.IDistributedTest;
import Dominio.IStudent;
import Dominio.IStudentTestQuestion;
import Dominio.Student;
import Dominio.StudentTestQuestion;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;

/**
 * @author Susana Fernandes
 */
public class StudentTestQuestionOJBTest extends TestCaseOJB {
	/**
		 * @param testName
		 */
	public StudentTestQuestionOJBTest(String testName) {
		super(testName);

	}

	public static Test suite() {
		TestSuite suite = new TestSuite(StudentTestQuestionOJBTest.class);
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
			IStudent student = new Student(new Integer(11));
			student =
				(IStudent) persistentSuport.getIPersistentStudent().readByOId(
					student,
					false);
			assertNotNull("there is no student with id=11", student);

			IDistributedTest distributedTest =
				new DistributedTest(new Integer(1));
			distributedTest =
				(IDistributedTest) persistentSuport
					.getIPersistentDistributedTest()
					.readByOId(
					distributedTest,
					false);
			assertNotNull(
				"there is no distributedTest with id=1",
				distributedTest);

			List result =
				persistentSuport
					.getIPersistentStudentTestQuestion()
					.readByStudentAndDistributedTest(student, distributedTest);
			assertNotNull(
				"there are no student test question for this student and distributedTest",
				result);
			assertEquals(
				"wrong number of student test questions",
				2,
				result.size());
			persistentSuport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			fail("exception: ExcepcaoPersistencia");
		}
	}

	public void testReadByDistributedTest() {
		System.out.println("2-> Test ReadByDistributedTest");
		try {
			ISuportePersistente persistentSuport =
				SuportePersistenteOJB.getInstance();
			persistentSuport.iniciarTransaccao();
			IDistributedTest distributedTest =
				new DistributedTest(new Integer(1));
			distributedTest =
				(IDistributedTest) persistentSuport
					.getIPersistentDistributedTest()
					.readByOId(
					distributedTest,
					false);
			assertNotNull(
				"there is no distributedTest with id=1",
				distributedTest);
			List result =
				persistentSuport
					.getIPersistentStudentTestQuestion()
					.readByDistributedTest(
					distributedTest);
			assertNotNull(
				"there are no students tests questions for this distributedTest",
				result);
			assertEquals(
				"wrong number of student test questions",
				4,
				result.size());
			persistentSuport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			fail("exception: ExcepcaoPersistencia");
		}
	}

	public void testReadByStudent() {
		System.out.println("3-> Test ReadByStudent");
		try {
			ISuportePersistente persistentSuport =
				SuportePersistenteOJB.getInstance();
			persistentSuport.iniciarTransaccao();
			IStudent student = new Student(new Integer(11));
			student =
				(IStudent) persistentSuport.getIPersistentStudent().readByOId(
					student,
					false);
			assertNotNull("there is no student with id=11", student);

			List result =
				persistentSuport
					.getIPersistentStudentTestQuestion()
					.readByStudent(
					student);
			assertNotNull(
				"there are no students tests questions for this student",
				result);
			assertEquals(
				"wrong number of student test questions",
				3,
				result.size());
			persistentSuport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			fail("exception: ExcepcaoPersistencia");
		}
	}

	public void testDeleteByDistributedTest() {
		System.out.println("4-> Test DeleteByDistributedTest");
		try {
			ISuportePersistente persistentSuport =
				SuportePersistenteOJB.getInstance();
			persistentSuport.iniciarTransaccao();

			IDistributedTest distributedTest =
				new DistributedTest(new Integer(2));
			assertNotNull(
				"there is no distributed test with id=2",
				distributedTest);

			persistentSuport
				.getIPersistentStudentTestQuestion()
				.deleteByDistributedTest(
				distributedTest);

			persistentSuport.confirmarTransaccao();
			persistentSuport.iniciarTransaccao();
			List result =
				persistentSuport
					.getIPersistentStudentTestQuestion()
					.readByDistributedTest(
					distributedTest);
			assertNotNull(
				"there is no student test questions for this distributed test",
				result);
			assertEquals(
				"wrong number of student test questions",
				0,
				result.size());

			persistentSuport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			fail("exception: ExcepcaoPersistencia");
		}
	}

	public void testDelete() {
		System.out.println("5-> Test Delete");
		try {
			ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
			persistentSuport.iniciarTransaccao();
			
			IStudentTestQuestion studentTestQuestion = new StudentTestQuestion(new Integer(25));
			assertNotNull("there is no studentTestQuestion with id=25", studentTestQuestion);
			studentTestQuestion = (IStudentTestQuestion) persistentSuport.getIPersistentStudentTestQuestion().readByOId(studentTestQuestion, true);
			persistentSuport.getIPersistentStudentTestQuestion().delete(studentTestQuestion);
			persistentSuport.confirmarTransaccao();
			
			persistentSuport.iniciarTransaccao();
			IDistributedTest distributedTest =
							new DistributedTest(new Integer(2));
						assertNotNull(
							"there is no distributed test with id=2",
							distributedTest);
			List result = persistentSuport.getIPersistentStudentTestQuestion().readByDistributedTest(distributedTest);
			assertNotNull(
				"there is no student test questions for this distributed test",
				result);
			assertEquals(
				"wrong number of student test questions",
				1,
				result.size());

			persistentSuport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			fail("exception: ExcepcaoPersistencia");
		}
	}

}
