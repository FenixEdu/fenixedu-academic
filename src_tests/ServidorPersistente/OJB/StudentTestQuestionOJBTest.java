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
import Dominio.IQuestion;
import Dominio.IStudent;
import Dominio.IStudentTestQuestion;
import Dominio.Question;
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
			IStudent student = new Student(new Integer(9));
			student =
				(IStudent) persistentSuport.getIPersistentStudent().readByOId(
					student,
					false);
			assertNotNull("there is no student with id=9", student);

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
				1,
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
				new DistributedTest(new Integer(2));
			distributedTest =
				(IDistributedTest) persistentSuport
					.getIPersistentDistributedTest()
					.readByOId(
					distributedTest,
					false);
			assertNotNull(
				"there is no distributedTest with id=2",
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
				5,
				result.size());
			persistentSuport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			fail("exception: ExcepcaoPersistencia");
		}
	}

	public void testReadByQuestion() {
		System.out.println("4-> Test ReadByQuestion");
		try {
			ISuportePersistente persistentSuport =
				SuportePersistenteOJB.getInstance();
			persistentSuport.iniciarTransaccao();

			IQuestion question = new Question(new Integer("4"));
			question =
				(IQuestion) persistentSuport
					.getIPersistentQuestion()
					.readByOId(
					question,
					false);

			List studentTestQuestions =
				persistentSuport
					.getIPersistentStudentTestQuestion()
					.readByQuestion(
					question);
			assertNotNull(
				"there is no studentTestQuestions whith this question",
				studentTestQuestions);
			assertEquals(
				"wrong number of student test questions",
				2,
				studentTestQuestions.size());
			persistentSuport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			fail("exception: ExcepcaoPersistencia");
		}
	}

	public void testReadByQuestionAndDistributedTest() {
		System.out.println("5-> Test ReadByQuestionAndDistributedTest");
		try {
			ISuportePersistente persistentSuport =
				SuportePersistenteOJB.getInstance();
			persistentSuport.iniciarTransaccao();
			IDistributedTest distributedTest =
				new DistributedTest(new Integer(3));
			distributedTest =
				(IDistributedTest) persistentSuport
					.getIPersistentDistributedTest()
					.readByOId(
					distributedTest,
					false);
			assertNotNull(
				"there is no distributedTest with id=3",
				distributedTest);
			IQuestion question = new Question(new Integer("10"));
			question =
				(IQuestion) persistentSuport
					.getIPersistentQuestion()
					.readByOId(
					question,
					false);
			assertNotNull("there is no question with id=10", question);

			List studentTestQuestions =
				persistentSuport
					.getIPersistentStudentTestQuestion()
					.readByQuestionAndDistributedTest(question, distributedTest);

			assertNotNull(
				"there is no studentTestQuestions whith this question",
				studentTestQuestions);
			assertEquals(
				"wrong number of student test questions",
				3,
				studentTestQuestions.size());
			persistentSuport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			fail("exception: ExcepcaoPersistencia");
		}
	}

	public void testReadByQuestionAndStudentAndDistributedTest() {
		System.out.println(
			"6-> Test ReadByQuestionAndStudentAndDistributedTest");
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
				new DistributedTest(new Integer(3));
			distributedTest =
				(IDistributedTest) persistentSuport
					.getIPersistentDistributedTest()
					.readByOId(
					distributedTest,
					false);
			assertNotNull(
				"there is no distributedTest with id=3",
				distributedTest);
			IQuestion question = new Question(new Integer("10"));
			question =
				(IQuestion) persistentSuport
					.getIPersistentQuestion()
					.readByOId(
					question,
					false);
			assertNotNull("there is no question with id=10", question);

			IStudentTestQuestion studentTestQuestion =
				persistentSuport
					.getIPersistentStudentTestQuestion()
					.readByQuestionAndStudentAndDistributedTest(
						question,
						student,
						distributedTest);

			assertNotNull(
				"there is no studentTestQuestion for this student, question and distributedTest",
				studentTestQuestion);

			persistentSuport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			fail("exception: ExcepcaoPersistencia");
		}
	}

	public void testReadStudentsByDistributedTest() {
		System.out.println("7-> Test ReadStudentsByDistributedTest");
		try {
			ISuportePersistente persistentSuport =
				SuportePersistenteOJB.getInstance();
			persistentSuport.iniciarTransaccao();
			IDistributedTest distributedTest =
				new DistributedTest(new Integer(3));
			distributedTest =
				(IDistributedTest) persistentSuport
					.getIPersistentDistributedTest()
					.readByOId(
					distributedTest,
					false);
			assertNotNull(
				"there is no distributedTest with id=3",
				distributedTest);
			List result =
				persistentSuport
					.getIPersistentStudentTestQuestion()
					.readStudentsByDistributedTest(distributedTest);
			assertNotNull(
				"there are no students for this distributedTest",
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

	public void testReadStudentTestQuestionsByDistributedTest() {
		System.out.println(
			"8-> Test ReadStudentTestQuestionsByDistributedTest");
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
					.readStudentTestQuestionsByDistributedTest(distributedTest);
			assertNotNull(
				"there are no students tests questions for this distributedTest",
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

	public void testDeleteByDistributedTest() {
		System.out.println("9-> Test DeleteByDistributedTest");
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
				"there is no distributed test with id=1",
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
		System.out.println("10-> Test Delete");
		try {
			ISuportePersistente persistentSuport =
				SuportePersistenteOJB.getInstance();
			persistentSuport.iniciarTransaccao();

			IStudentTestQuestion studentTestQuestion =
				new StudentTestQuestion(new Integer(1));
			assertNotNull(
				"there is no studentTestQuestion with id=1",
				studentTestQuestion);
			studentTestQuestion =
				(IStudentTestQuestion) persistentSuport
					.getIPersistentStudentTestQuestion()
					.readByOId(studentTestQuestion, true);
			persistentSuport.getIPersistentStudentTestQuestion().delete(
				studentTestQuestion);
			persistentSuport.confirmarTransaccao();

			persistentSuport.iniciarTransaccao();
			IDistributedTest distributedTest =
				new DistributedTest(new Integer(1));
			assertNotNull(
				"there is no distributed test with id=1",
				distributedTest);
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

}
