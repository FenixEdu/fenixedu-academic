/*
 * Created on Oct 29, 2003
 *
 */
package ServidorAplicacao.Servicos.teacher;

import java.util.Iterator;
import java.util.List;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

import Dominio.IQuestion;
import Dominio.IStudentTestQuestion;
import Dominio.Question;
import Dominio.StudentTestQuestion;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;
import Util.TestQuestionChangesType;
import Util.TestQuestionStudentsChangesType;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Susana Fernandes
 *
 */
public class ChangeStudentTestQuestionTest extends ServiceNeedsAuthenticationTestCase
{

	public ChangeStudentTestQuestionTest(String testName)
	{
		super(testName);
	}

	protected String getDataSetFilePath()
	{
		return "etc/datasets/servicos/teacher/testEditDistributedTestDataSet.xml";
	}

	protected String getExpectedDataSetFilePath()
	{
		return "etc/datasets/servicos/teacher/testExpectedChangeStudentTestQuestionTestDataSet.xml";
	}

	protected String getNameOfServiceToBeTested()
	{
		return "ChangeStudentTestQuestion";
	}

	protected String[] getAuthenticatedAndAuthorizedUser()
	{

		String[] args = { "D2543", "pass", getApplication()};
		return args;
	}

	protected String[] getAuthenticatedAndUnauthorizedUser()
	{

		String[] args = { "L48283", "pass", getApplication()};
		return args;
	}

	protected String[] getNotAuthenticatedUser()
	{

		String[] args = { "L48283", "pass", getApplication()};
		return args;
	}

	protected Object[] getAuthorizeArguments()
	{
		Integer executionCourseId = new Integer(34882);
		Integer distributedTestId = new Integer(207);
		Integer oldQuestionId = new Integer(9343);
		Integer newMetadataId = null;
		Integer studentId = new Integer(3701);
		TestQuestionChangesType testQuestionChangesType = new TestQuestionChangesType(1);
		Boolean delete = new Boolean(true);
		TestQuestionStudentsChangesType testQuestionStudentsChangesType =
			new TestQuestionStudentsChangesType(1);
		String path = new String("e:\\eclipse\\workspace\\fenix\\build\\standalone\\");

		Object[] args =
			{
				executionCourseId,
				distributedTestId,
				oldQuestionId,
				newMetadataId,
				studentId,
				testQuestionChangesType,
				delete,
				testQuestionStudentsChangesType,
				path };
		return args;
	}

	protected String getApplication()
	{
		return Autenticacao.EXTRANET;
	}

	public void testSuccessfull()
	{

		try
		{
			IUserView userView = authenticateUser(getAuthenticatedAndAuthorizedUser());
			Object[] args = getAuthorizeArguments();
			ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args);
			PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();
			Criteria criteria = new Criteria();
			criteria.addEqualTo("keyDistributedTest", args[1]);
			Query queryCriteria = new QueryByCriteria(StudentTestQuestion.class, criteria);
			List studentTestQuestionList = (List) broker.getCollectionByQuery(queryCriteria);
			broker.close();

			assertEquals(studentTestQuestionList.size(), 1);
			Iterator it = studentTestQuestionList.iterator();
			while (it.hasNext())
			{
				IStudentTestQuestion studentTestQuestion = (IStudentTestQuestion) it.next();
				assertEquals(studentTestQuestion.getKeyStudent(), args[4]);
				assertEquals(studentTestQuestion.getKeyQuestion(), new Integer(9342));
			}
			if (((Boolean) args[6]).booleanValue() == true)
			{
				broker = PersistenceBrokerFactory.defaultPersistenceBroker();
				criteria = new Criteria();
				criteria.addEqualTo("idInternal", args[2]);
				queryCriteria = new QueryByCriteria(Question.class, criteria);
				IQuestion question = (IQuestion) broker.getObjectByQuery(queryCriteria);
				broker.close();
				if (question != null)
					if (question.getVisibility().booleanValue() != false)
						fail("ChangeStudentTestQuestionTest " + "Não apagou a pergunta antiga");
			}

		}
		catch (FenixServiceException ex)
		{
			fail("ChangeStudentTestQuestionTest " + ex);
		}
		catch (Exception ex)
		{
			fail("ChangeStudentTestQuestionTest " + ex);
		}
	}
}