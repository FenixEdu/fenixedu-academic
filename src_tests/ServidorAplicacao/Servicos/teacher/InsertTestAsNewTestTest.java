/*
 * Created on 26/Ago/2003
 *
 */
package ServidorAplicacao.Servicos.teacher;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

import Dominio.ITest;
import Dominio.Test;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Susana Fernandes
 */
public class InsertTestAsNewTestTest extends ServiceNeedsAuthenticationTestCase
{

	public InsertTestAsNewTestTest(String testName)
	{
		super(testName);
	}

	protected String getDataSetFilePath()
	{
		return "etc/datasets/servicos/teacher/testInsertDistributedTestDataSet.xml";
	}

	protected String getNameOfServiceToBeTested()
	{
		return "InsertTestAsNewTest";
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
		Integer testId = new Integer(108);
		Object[] args = { executionCourseId, testId };
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

			criteria = new Criteria();
			criteria.addEqualTo("idInternal", args[1]);
			Query queryCriteria = new QueryByCriteria(Test.class, criteria);
			ITest oldTest = (ITest) broker.getObjectByQuery(queryCriteria);

			criteria = new Criteria();
			criteria.addOrderBy("idInternal", false);
			queryCriteria = new QueryByCriteria(Test.class, criteria);
			ITest newTest = (ITest) broker.getObjectByQuery(queryCriteria);
			broker.close();

			assertEquals(oldTest.getTitle().concat(" (Duplicado)"), newTest.getTitle());
			assertEquals(oldTest.getInformation(), newTest.getInformation());
			assertEquals(oldTest.getNumberOfQuestions(), newTest.getNumberOfQuestions());
			//assertEquals(oldTest.getTestScope().getIdInternal(), newTest.getTestScope().getIdInternal());

		}
		catch (FenixServiceException ex)
		{
			fail("InsertTestAsNewTestTest " + ex);
		}
		catch (Exception ex)
		{
			fail("InsertTestAsNewTestTest " + ex);
		}
	}
}
