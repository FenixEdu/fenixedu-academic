/*
 * Created on 25/Set/2003
 *
 */
package ServidorAplicacao.Servicos.teacher;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

import Dominio.Metadata;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Susana Fernandes
 */
public class CountMetadatasByExecutionCourseTest extends ServiceNeedsAuthenticationTestCase
{

	public CountMetadatasByExecutionCourseTest(String testName)
	{
		super(testName);
	}

	protected String getDataSetFilePath()
	{
		return "etc/datasets/servicos/teacher/testReadMetadatasByTestTestDataSet.xml";
	}

	protected String getNameOfServiceToBeTested()
	{
		return "CountMetadatasByExecutionCourse";
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
		Object[] args = { executionCourseId };
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
			Integer count =
				(Integer) ServiceManagerServiceFactory.executeService(
					userView,
					getNameOfServiceToBeTested(),
					args);

			PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();
			Criteria criteria = new Criteria();
			criteria.addEqualTo("keyExecutionCourse", args[0]);
			Query queryCriteria = new QueryByCriteria(Metadata.class, criteria);
			int metadataNumber = (int) broker.getCount(queryCriteria);
			broker.close();
			if (count.intValue() != metadataNumber)
				fail("CountMetadatasByExecutionCourseTest " + "wrong number of metadatas");

		}
		catch (FenixServiceException ex)
		{
			fail("CountMetadatasByExecutionCourseTest " + ex);
		}
		catch (Exception ex)
		{
			fail("CountMetadatasByExecutionCourseTest " + ex);
		}
	}
}
