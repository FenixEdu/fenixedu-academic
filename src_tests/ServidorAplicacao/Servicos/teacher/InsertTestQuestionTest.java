/*
 * Created on 11/Ago/2003
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

import Dominio.ITestQuestion;
import Dominio.TestQuestion;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Susana Fernandes
 */
public class InsertTestQuestionTest extends ServiceNeedsAuthenticationTestCase
{

	public InsertTestQuestionTest(String testName)
	{
		super(testName);
	}

	protected String getDataSetFilePath()
	{
		return "etc/datasets/servicos/teacher/testInsertTestQuestionTestDataSet.xml";
	}

	protected String getExpectedDataSetFilePath()
	{
		return "etc/datasets/servicos/teacher/testExpectedInsertDistributedTestDataSet.xml";
	}

	protected String getNameOfServiceToBeTested()
	{
		return "InsertTestQuestion";
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
		Integer testId = new Integer(23);
		Integer metadataId = new Integer(3);
		Integer questionOrder = new Integer(0);
		Integer questionValue = new Integer(2);
		String path = new String("e:\\eclipse\\workspace\\fenix\\build\\standalone\\");

		Object[] args = { executionCourseId, testId, metadataId, questionOrder, questionValue, path };
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
			criteria.addEqualTo("keyTest", args[1]);
			Query queryCriteria = new QueryByCriteria(TestQuestion.class, criteria);
			List testQuestionList = (List) broker.getCollectionByQuery(queryCriteria);
			broker.close();

			//ver se os dados estão correctos
			assertEquals(testQuestionList.size(), 1);
			Iterator it = testQuestionList.iterator();
			while (it.hasNext())
			{
				ITestQuestion testQuestion = (ITestQuestion) it.next();
				//pergunta inserida
				if (testQuestion.getQuestion().getKeyMetadata().equals(args[2]))
				{
					if (args[3].equals(new Integer(-1)))
						assertEquals(testQuestion.getTestQuestionOrder(), new Integer(1));
					else
						assertEquals(testQuestion.getTestQuestionOrder(), new Integer(((Integer)args[3]).intValue()+1));
					if (args[4] != null)
						assertEquals(testQuestion.getTestQuestionValue(), args[4]);
				}
				else
					fail("Insert Test Question : Inseriu mal");
			}
		}
		catch (FenixServiceException ex)
		{
			fail("Insert Test Question " + ex);
		}
		catch (Exception ex)
		{
			fail("Insert Test Question " + ex);
		}
	}
}
