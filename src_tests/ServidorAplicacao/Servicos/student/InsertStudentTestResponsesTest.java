/*
 * Created on 9/Set/2003
 *
 */
package ServidorAplicacao.Servicos.student;

import java.util.Iterator;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoSiteDistributedTests;
import Dominio.DistributedTest;
import Dominio.IDistributedTest;
import Dominio.IStudent;
import Dominio.IStudentTestLog;
import Dominio.IStudentTestQuestion;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servicos.TestCaseReadServices;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Susana Fernandes
 */
public class InsertStudentTestResponsesTest extends TestCaseReadServices
{

	public InsertStudentTestResponsesTest(java.lang.String testName)
	{
		super(testName);
	}

	public static void main(java.lang.String[] args)
	{
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite()
	{
		TestSuite suite = new TestSuite(InsertStudentTestResponsesTest.class);

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
		return "InsertStudentTestResponses";
	}

	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly()
	{
		return null;
	}

	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly()
	{
		String userName = new String("L48816");
		Integer distributedTestId = new Integer(189);
		String[] options = { "0", "0", "0", "0", "0", "0", "0", "0", "0", "2", "1" };
		//Integer distributedTestId = new Integer(2);
		//String[] options = { "0", "0", "0", "0", "2", "1" };
		String path = new String("e:\\eclipse\\workspace\\fenix-exams2\\build\\standalone\\");
		Object[] args = { userName, distributedTestId, options, path };
		return args;

	}

	protected int getNumberOfItemsToRetrieve()
	{
		return 0;
	}

	protected Object getObjectToCompare()
	{
		InfoSiteDistributedTests infoSiteDistributedTests = new InfoSiteDistributedTests();
		try
		{
			Object[] args = getArgumentsOfServiceToBeTestedSuccessfuly();
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			sp.iniciarTransaccao();

			IStudent student = sp.getIPersistentStudent().readByUsername((String) args[0]);
			assertNotNull("student is null", student);

			IDistributedTest distributedTest =
				(IDistributedTest) sp.getIPersistentDistributedTest().readByOId(
					new DistributedTest((Integer) args[1]),
					false);
			List studentTestQuestionList =
				(List) sp.getIPersistentStudentTestQuestion().readByStudentAndDistributedTest(
					student,
					distributedTest);

			List studentTestLogList =
				(List) sp.getIPersistentStudentTestLog().readByStudentAndDistributedTest(
					student,
					distributedTest);
			sp.confirmarTransaccao();

			Iterator it = studentTestQuestionList.iterator();
			assertEquals(studentTestQuestionList.size(), 6);
			assertEquals(
				((IStudentTestLog) studentTestLogList.get(studentTestLogList.size() - 1)).getEvent(),
				"Submeter Teste;0;0;0;0;0;0;0;0;0;2;1;");
			//			assertEquals(
			//				((IStudentTestLog) studentTestLogList.get(studentTestLogList.size() - 1)).getEvent(),
			//				"Submeter Teste;0;0;0;0;2;1;");
			while (it.hasNext())
			{
				IStudentTestQuestion studentTestQuestion = (IStudentTestQuestion) it.next();
				assertEquals(studentTestQuestion.getStudent(), student);
				assertEquals(studentTestQuestion.getDistributedTest().getIdInternal(), args[1]);
				assertEquals(
					studentTestQuestion.getResponse(),
					((String[]) args[2])[studentTestQuestion.getTestQuestionOrder().intValue() - 1]);
			}
		}
		catch (Exception ex)
		{
			fail("InsertStudentTestResponsesTest " + ex);
		}
		return new Boolean(true);
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
		return "etc/datasets/servicos/student/testInsertStudentTestResponsesDataSet.xml";
	}

	public String getApplication()
	{
		return Autenticacao.EXTRANET;
	}

}

//extends ServiceNeedsStudentAuthenticationTestCase
//{
//
//	public InsertStudentTestResponsesTest(String testName)
//	{
//		super(testName);
//	}
//
//	protected String getDataSetFilePath()
//	{
//		return "etc/datasets/servicos/student/testReadStudentTestsToDoDataSet.xml";
//	}
//
//	protected String getNameOfServiceToBeTested()
//	{
//		return "InsertStudentTestResponses";
//	}
//
//	protected String[] getAuthenticatedAndAuthorizedUser()
//	{
//
//		String[] args = { "L48816", "pass", getApplication()};
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
//		String userName, Integer distributedTestId, String[] options, String path)
//		String userName = new String("L48816");
//		Integer distributedTestId = new Integer(2);
//		String[] options = { "0", "0", "0", "0", "2", "1" };
//		String path = new String("e:\\eclipse\\workspace\\fenix-exams2\\build\\standalone\\");
//		Object[] args = { userName, distributedTestId, options, path };
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
//
//		try
//		{
//			IUserView userView = authenticateUser(getAuthenticatedAndAuthorizedUser());
//			Object[] args = getAuthorizeArguments();
//
//			ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args);
//
//			PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();
//
//			Criteria criteria = new Criteria();
//
//			criteria = new Criteria();
//			criteria.addEqualTo("person.username", args[0]);
//			Query queryCriteria = new QueryByCriteria(Student.class, criteria);
//			IStudent student = (IStudent) broker.getObjectByQuery(queryCriteria);
//
//			criteria = new Criteria();
//			criteria.addEqualTo("keyStudent", student.getIdInternal());
//			criteria.addEqualTo("keyDistributedTest", args[1]);
//			queryCriteria = new QueryByCriteria(Student.class, criteria);
//			List studentTestQuestionList = (List) broker.getCollectionByQuery(queryCriteria);
//
//			criteria = new Criteria();
//			criteria.addEqualTo("keyStudent", student.getIdInternal());
//			criteria.addOrderBy("date", false);
//			queryCriteria = new QueryByCriteria(Student.class, criteria);
//			IStudentTestLog studentTestLog = (IStudentTestLog) broker.getObjectByQuery(queryCriteria);
//			broker.close();
//
//			Iterator it = studentTestQuestionList.iterator();
//			assertEquals(studentTestQuestionList.size(), 6);
//			assertEquals(studentTestLog.getEvent(), "Submeter Teste;0;0;0;0;2;1;");
//			while (it.hasNext())
//			{
//				IStudentTestQuestion studentTestQuestion = (IStudentTestQuestion) it.next();
//				assertEquals(studentTestQuestion.getStudent(), student);
//				assertEquals(studentTestQuestion.getDistributedTest().getIdInternal(), args[1]);
//				assertEquals(
//					studentTestQuestion.getResponse(),
//					((String[]) args[2])[studentTestQuestion.getTestQuestionOrder().intValue() - 1]);
//			}
//
//		}
//		catch (FenixServiceException ex)
//		{
//			fail("InsertStudentTestResponsesTest " + ex);
//		}
//		catch (Exception ex)
//		{
//			fail("InsertStudentTestResponsesTest " + ex);
//		}
//	}
//}
