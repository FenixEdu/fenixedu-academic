/*
 * Created on 11/Ago/2003
 *
 */

package ServidorAplicacao.Servicos.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

import Dominio.ITest;
import Dominio.ITestQuestion;
import Dominio.Test;
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
		return "etc/datasets/servicos/teacher/testEditTestTestDataSet.xml";
	}

	protected String getNameOfServiceToBeTested()
	{
		return "InsertTestQuestion";
	}

	protected String[] getAuthenticatedAndAuthorizedUser()
	{
		String[] args =
		{"D3673", "pass", getApplication()};
		return args;
	}

	protected String[] getAuthenticatedAndUnauthorizedUser()
	{
		String[] args =
		{"L46730", "pass", getApplication()};
		return args;
	}

	protected String[] getNotAuthenticatedUser()
	{
		String[] args =
		{"L46730", "pass", getApplication()};
		return args;
	}

	protected Object[] getAuthorizeArguments()
	{
		Integer executionCourseId = new Integer(34033);
		Integer testId = new Integer(110);
		String[] metadataId =
		{new String("133"), new String("134")};
		//String[] metadataId = {"133"};
		Integer questionOrder = new Integer(0);
		Integer questionValue = new Integer(2);
		String path = new String("e:\\eclipse-m7\\workspace\\fenix\\build\\standalone\\");
		Object[] args =
		//{executionCourseId, testId, metadataId, questionOrder,
		// questionValue, path};
		{executionCourseId, testId, metadataId, null, null, path};
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

			criteria = new Criteria();
			criteria.addEqualTo("idInternal", args[1]);
			queryCriteria = new QueryByCriteria(Test.class, criteria);
			ITest test = (ITest) broker.getObjectByQuery(queryCriteria);

			broker.close();
			//ver se os dados estão correctos
			List metadatasIds = new ArrayList();
			CollectionUtils.addAll(metadatasIds, (String[]) args[2]);

			assertEquals(testQuestionList.size(), metadatasIds.size());
			Iterator it = testQuestionList.iterator();

			while (it.hasNext())
			{
				ITestQuestion testQuestion = (ITestQuestion) it.next();
				//pergunta inseridfa
				if (metadatasIds.contains(testQuestion.getQuestion().getKeyMetadata().toString()))
				{
					if (args[3] != null && !args[3].equals(new Integer(-1)))
						assertEquals(testQuestion.getTestQuestionOrder(), args[3]);
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
