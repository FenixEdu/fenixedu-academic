/*
 * Created on 11/Ago/2003
 *
 */
package ServidorPersistente.OJB;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.IQuestion;
import Dominio.ITest;
import Dominio.ITestQuestion;
import Dominio.Question;
import Dominio.TestQuestion;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentQuestion;
import ServidorPersistente.IPersistentTest;
import ServidorPersistente.IPersistentTestQuestion;
import ServidorPersistente.ISuportePersistente;

/**
 * @author Susana Fernandes
 */
public class TestQuestionOJBTest extends TestCaseOJB {

	/**
	 * @param testName
	 */
	public TestQuestionOJBTest(String testName) {
		super(testName);

	}

	public static Test suite() {
		TestSuite suite = new TestSuite(TestQuestionOJBTest.class);
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

	public void testReadByTest() {
		System.out.println("1-> Test ReadByTest");
		try {
			ISuportePersistente persistentSuport =
				SuportePersistenteOJB.getInstance();
			persistentSuport.iniciarTransaccao();
			IPersistentTest persistentTest =
				persistentSuport.getIPersistentTest();
			ITest test = new Dominio.Test(new Integer(2));
			test = (ITest) persistentTest.readByOId(test, false);
			assertNotNull("there is no test with id=2", test);
			IPersistentTestQuestion persistentTestQuestion =
				persistentSuport.getIPersistentTestQuestion();
			List result = persistentTestQuestion.readByTest(test);
			assertNotNull("there are no questions for this test", result);

			assertEquals("wrong number of questions", 2, result.size());
			persistentSuport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			fail("exception: ExcepcaoPersistencia");
		}
	}

	public void testReadByTestAndQuestion() {
		System.out.println("2-> Test ReadByTestAndQuestion");
		try {
			ISuportePersistente persistentSuport =
				SuportePersistenteOJB.getInstance();
			persistentSuport.iniciarTransaccao();
			IPersistentTest persistentTest =
				persistentSuport.getIPersistentTest();
			ITest test = new Dominio.Test(new Integer(3));
			assertNotNull("there is no test with id=3", test);
			IPersistentQuestion persistentQuestion =
				persistentSuport.getIPersistentQuestion();
			IQuestion question = new Question(new Integer(9));
			assertNotNull("there is no question with id=9", question);
			IPersistentTestQuestion persistentTestQuestion =
				persistentSuport.getIPersistentTestQuestion();
			ITestQuestion testQuestion =
				persistentTestQuestion.readByTestAndQuestion(test, question);
			assertNotNull(
				"there is no Test Question for this Test and Question",
				testQuestion);
			persistentSuport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			fail("exception: ExcepcaoPersistencia");
		}
	}

	public void testReadByQuestion() {
		System.out.println("3-> Test ReadByQuestion");
		try {
			ISuportePersistente persistentSuport =
				SuportePersistenteOJB.getInstance();
			persistentSuport.iniciarTransaccao();
			IPersistentQuestion persistentQuestion =
				persistentSuport.getIPersistentQuestion();
			IQuestion question = new Question(new Integer(9));
			assertNotNull("there is no question with id=9", question);

			IPersistentTestQuestion persistentTestQuestion =
				persistentSuport.getIPersistentTestQuestion();
			List result = persistentTestQuestion.readByQuestion(question);
			assertNotNull(
				"there are no test questions with this question",
				result);
			assertEquals("wrong number of questions", 2, result.size());
			persistentSuport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			fail("exception: ExcepcaoPersistencia");
		}
	}

	public void testDeleteByTest() {
		System.out.println("4-> Test DeleteByTest");
		try {
			ISuportePersistente persistentSuport =
				SuportePersistenteOJB.getInstance();
			persistentSuport.iniciarTransaccao();
			IPersistentTest persistentTest =
				persistentSuport.getIPersistentTest();
			ITest test = new Dominio.Test(new Integer(3));
			assertNotNull("there is no test with id=3", test);
			IPersistentTestQuestion persistentTestQuestion =
				persistentSuport.getIPersistentTestQuestion();
			persistentTestQuestion.deleteByTest(test);
			persistentSuport.confirmarTransaccao();
			persistentSuport.iniciarTransaccao();
			List result = persistentTestQuestion.readByTest(test);
			assertNotNull("there is no question for this Test", result);
			assertEquals("wrong number of questions", 0, result.size());
			persistentSuport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			fail("exception: ExcepcaoPersistencia");
		}
	}

	public void testDelete() {
		System.out.println("5-> Test Delete");
		try {
			ISuportePersistente persistentSuport =
				SuportePersistenteOJB.getInstance();
			persistentSuport.iniciarTransaccao();
			IPersistentTestQuestion persistentTestQuestion =
				persistentSuport.getIPersistentTestQuestion();
			ITestQuestion testQuestion = new TestQuestion(new Integer(70));
			assertNotNull("there is no test question with id=70", testQuestion);
			persistentTestQuestion.delete(testQuestion);
			persistentSuport.confirmarTransaccao();
			persistentSuport.iniciarTransaccao();
			IPersistentTest persistentTest =
				persistentSuport.getIPersistentTest();
			ITest test = new Dominio.Test(new Integer(1));
			assertNotNull("there is no test with id=1", test);
			List result = persistentTestQuestion.readByTest(test);
			assertNotNull("there is no question for this Test", result);
			assertEquals("wrong number of questions", 0, result.size());
			persistentSuport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			fail("exception: ExcepcaoPersistencia");
		}
	}
}
