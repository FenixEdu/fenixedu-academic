/*
 * Created on 8/Set/2003
 *
 */
package ServidorAplicacao.Servicos.student;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoDistributedTest;
import DataBeans.InfoQuestion;
import DataBeans.InfoStudent;
import DataBeans.InfoStudentTestQuestion;
import DataBeans.util.Cloner;
import DataBeans.util.CopyUtils;
import Dominio.DistributedTest;
import Dominio.IDistributedTest;
import Dominio.IStudent;
import Dominio.IStudentTestQuestion;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servicos.TestCaseReadServices;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Susana Fernandes
 */
public class ReadStudentTestTest extends TestCaseReadServices
{

	public ReadStudentTestTest(java.lang.String testName)
	{
		super(testName);
	}

	public static void main(java.lang.String[] args)
	{
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite()
	{
		TestSuite suite = new TestSuite(ReadStudentTestTest.class);

		return suite;
	}

	protected void setUp()
	{
		super.setUp();
	}

	protected void tearDown()
	{
		super.tearDown();
	}

	protected String getNameOfServiceToBeTested()
	{
		return "ReadStudentTest";
	}

	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly()
	{
		return null;
	}

	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly()
	{
		String userName = new String("L48283");
		Integer distributedTestId = new Integer(2);
		String path = new String("e:\\eclipse\\workspace\\fenix-exams2\\build\\standalone\\");

		Object[] args = { userName, distributedTestId, new Boolean(true), path };
		return args;

	}

	protected int getNumberOfItemsToRetrieve()
	{
		return 6;
	}

	protected Object getObjectToCompare()
	{
		Object[] args = getArgumentsOfServiceToBeTestedSuccessfuly();
		List infoStudentTestQuestionList = new ArrayList();
		try
		{
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			List distributedTestList = new ArrayList();
			sp.iniciarTransaccao();
			IStudent student = sp.getIPersistentStudent().readByUsername((String) args[0]);
			assertNotNull("student is null", student);

			IDistributedTest distributedTest =
				(IDistributedTest) sp.getIPersistentDistributedTest().readByOId(
					new DistributedTest((Integer) args[1]),
					false);

			assertNotNull("DistributedTest is null", distributedTest);
			List studentTestQuestionList =
				sp.getIPersistentStudentTestQuestion().readByStudentAndDistributedTest(
					student,
					distributedTest);
			sp.confirmarTransaccao();

			Iterator it = studentTestQuestionList.iterator();

			while (it.hasNext())
			{
				infoStudentTestQuestionList.add(
					copyIStudentTestQuestion2InfoStudentTestQuestion((IStudentTestQuestion) it.next()));
			}

		}
		catch (Exception ex)
		{
			fail("ReadStudentTestTest " + ex);
		}
		return infoStudentTestQuestionList;
	}

	protected boolean needsAuthorization()
	{
		return true;
	}
	protected String[] getArgsForAuthorizedUser()
	{
		String argsAutenticacao3[] = { "l48283", "pass", getApplication()};
		return argsAutenticacao3;
	}

	protected String[] getArgsForNotAuthorizedUser()
	{
		String argsAutenticacao4[] = { "d2543", "pass", getApplication()};
		return argsAutenticacao4;
	}

	protected String getDataSetFilePath()
	{
		return "etc/datasets/servicos/student/testReadStudentTestsToDoDataSet.xml";
	}

	public String getApplication()
	{
		return Autenticacao.EXTRANET;
	}

	public static InfoStudentTestQuestion copyIStudentTestQuestion2InfoStudentTestQuestion(IStudentTestQuestion studentTestQuestion)
	{
		InfoStudentTestQuestion infoStudentTestQuestion = new InfoStudentTestQuestion();
		infoStudentTestQuestion.setIdInternal(studentTestQuestion.getIdInternal());
		infoStudentTestQuestion.setOptionShuffle(studentTestQuestion.getOptionShuffle());
		infoStudentTestQuestion.setOldResponse(studentTestQuestion.getOldResponse());
		infoStudentTestQuestion.setTestQuestionOrder(studentTestQuestion.getTestQuestionOrder());
		infoStudentTestQuestion.setTestQuestionValue(studentTestQuestion.getTestQuestionValue());
		infoStudentTestQuestion.setTestQuestionMark(studentTestQuestion.getTestQuestionMark());
		InfoDistributedTest infoDistributedTest = new InfoDistributedTest();
		try
		{
			CopyUtils.copyProperties(infoDistributedTest, studentTestQuestion.getDistributedTest());
		}
		catch (Exception e)
		{
			fail("ReadStudentTestTest " + "cloner");
		}

		InfoStudent infoStudent = Cloner.copyIStudent2InfoStudent(studentTestQuestion.getStudent());
		InfoQuestion infoQuestion = Cloner.copyIQuestion2InfoQuestion(studentTestQuestion.getQuestion());
		infoStudentTestQuestion.setDistributedTest(infoDistributedTest);
		infoStudentTestQuestion.setStudent(infoStudent);
		infoStudentTestQuestion.setQuestion(infoQuestion);
		return infoStudentTestQuestion;
	}

}

//extends ServiceNeedsStudentAuthenticationTestCase
//{
//
//	public ReadStudentTestTest(String testName)
//	{
//		super(testName);
//	}
//
//	protected String getDataSetFilePath()
//	{
//		return "etc/datasets/servicos/student/testReadStudentTestTestDataSet.xml";
//	}
//
//	protected String getNameOfServiceToBeTested()
//	{
//		return "ReadStudentTest";
//	}
//
//	protected String[] getAuthenticatedAndAuthorizedUser()
//	{
//
//		String[] args = { "L48283", "pass", getApplication()};
//		return args;
//	}
//
//	protected String[] getAuthenticatedAndUnauthorizedUser()
//	{
//
//		String[] args = { "D2543", "pass", getApplication()};
//		return args;
//	}
//
//	protected String[] getNotAuthenticatedUser()
//	{
//
//		String[] args = { "D2543", "pass", getApplication()};
//		return args;
//	}
//
//	protected Object[] getAuthorizeArguments()
//	{
//		String userName = new String("L48283");
//		Integer distributedTestId = new Integer(2);
//		String path = new String("e:\\eclipse\\workspace\\fenix-exams2\\build\\standalone\\");
//
//		Object[] args = { userName, distributedTestId, new Boolean(true), path };
//		return args;
//	}
//
//	protected String getApplication()
//	{
//		return Autenticacao.EXTRANET;
//	}
//
//	public void testSuccessfull()
//	{
//		try
//		{
//			IUserView userView = authenticateUser(getAuthenticatedAndAuthorizedUser());
//			Object[] args = getAuthorizeArguments();
//
//			List serviceStudentTestQuestionList =
//				(List) ServiceManagerServiceFactory.executeService(
//					userView,
//					getNameOfServiceToBeTested(),
//					args);
//
//			PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();
//
//			Criteria criteria = new Criteria();
//			criteria.addEqualTo("person.username", args[0]);
//			Query queryCriteria = new QueryByCriteria(Student.class, criteria);
//			IStudent student = (IStudent) broker.getObjectByQuery(queryCriteria);
//
//			criteria = new Criteria();
//			criteria.addEqualTo("keyDistributedTest", args[1]);
//			criteria.addEqualTo("keyStudent", student.getIdInternal());
//			criteria.addOrderBy("testQuestionOrder", true);
//			queryCriteria = new QueryByCriteria(StudentTestQuestion.class, criteria);
//			List studentTestQuestionList = (List) broker.getCollectionByQuery(queryCriteria);
//			broker.close();
//
//			assertEquals(studentTestQuestionList.size(), serviceStudentTestQuestionList.size());
//			int i = 0;
//			Iterator it = serviceStudentTestQuestionList.iterator();
//			while (it.hasNext())
//			{
//				InfoStudentTestQuestion infoServiceStudentTestQuestion =
//					(InfoStudentTestQuestion) it.next();
//				InfoStudentTestQuestion infoStudentTestQuestion =
//					copyIStudentTestQuestion2InfoStudentTestQuestion(
//						(IStudentTestQuestion) studentTestQuestionList.get(i));
//				assertEquals(infoServiceStudentTestQuestion, infoStudentTestQuestion);
//				i++;
//			}
//
//		}
//		catch (FenixServiceException ex)
//		{
//			fail("ReadStudentTestTest " + ex);
//		}
//		catch (Exception ex)
//		{
//			fail("ReadStudentTestTest " + ex);
//		}
//	}
//
//	public static InfoStudentTestQuestion copyIStudentTestQuestion2InfoStudentTestQuestion(IStudentTestQuestion studentTestQuestion)
//	{
//		InfoStudentTestQuestion infoStudentTestQuestion = new InfoStudentTestQuestion();
//		infoStudentTestQuestion.setIdInternal(studentTestQuestion.getIdInternal());
//		infoStudentTestQuestion.setOptionShuffle(studentTestQuestion.getOptionShuffle());
//		infoStudentTestQuestion.setResponse(studentTestQuestion.getResponse());
//		infoStudentTestQuestion.setTestQuestionOrder(studentTestQuestion.getTestQuestionOrder());
//		infoStudentTestQuestion.setTestQuestionValue(studentTestQuestion.getTestQuestionValue());
//		infoStudentTestQuestion.setTestQuestionMark(studentTestQuestion.getTestQuestionMark());
//		InfoDistributedTest infoDistributedTest = new InfoDistributedTest();
//		try
//		{
//			CopyUtils.copyProperties(infoDistributedTest, studentTestQuestion.getDistributedTest());
//		}
//		catch (Exception e)
//		{
//			fail("ReadStudentDistributedTestTest " + "cloner");
//		}
//
//		InfoStudent infoStudent = Cloner.copyIStudent2InfoStudent(studentTestQuestion.getStudent());
//		InfoQuestion infoQuestion = Cloner.copyIQuestion2InfoQuestion(studentTestQuestion.getQuestion());
//		infoStudentTestQuestion.setDistributedTest(infoDistributedTest);
//		infoStudentTestQuestion.setStudent(infoStudent);
//		infoStudentTestQuestion.setQuestion(infoQuestion);
//		return infoStudentTestQuestion;
//	}
//}
