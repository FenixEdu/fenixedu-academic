/*
 * Created on 18/Fev/2004
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

import DataBeans.InfoDistributedTest;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoSiteStudentsTestMarks;
import DataBeans.InfoStudentTestQuestionMark;
import DataBeans.SiteView;
import DataBeans.util.Cloner;
import DataBeans.util.CopyUtils;
import Dominio.ExecutionCourse;
import Dominio.IDistributedTest;
import Dominio.IExecutionCourse;
import Dominio.IStudentTestQuestion;
import Dominio.StudentTestQuestion;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;
import framework.factory.ServiceManagerServiceFactory;

/**
 *
 * @author Susana Fernandes
 *
 */
public class ReadDistributedTestMarksTest extends ServiceNeedsAuthenticationTestCase
{
	public ReadDistributedTestMarksTest(String testName)
	{
		super(testName);
	}

	protected String getDataSetFilePath()
	{
		return "etc/datasets/servicos/teacher/testEditDistributedTestDataSet.xml";
	}

	protected String getNameOfServiceToBeTested()
	{
		return "ReadDistributedTestMarks";
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
		Integer distributedTestId = new Integer(1);
		Object[] args = { executionCourseId, distributedTestId };

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

			SiteView siteView =
				(SiteView) ServiceManagerServiceFactory.executeService(
					userView,
					getNameOfServiceToBeTested(),
					args);
			InfoSiteStudentsTestMarks infoSiteStudentsTestMarks =
				(InfoSiteStudentsTestMarks) siteView.getComponent();
			PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();
			Criteria criteria = new Criteria();
			criteria.addEqualTo("idInternal", args[0]);
			Query queryCriteria = new QueryByCriteria(ExecutionCourse.class, criteria);
			IExecutionCourse executionCourse = (IExecutionCourse) broker.getObjectByQuery(queryCriteria);

			criteria = new Criteria();
			criteria.addEqualTo("keyDistributedTest", args[1]);
			criteria.addOrderBy("keyStudent", true);
			criteria.addOrderBy("testQuestionOrder", true);
			queryCriteria = new QueryByCriteria(StudentTestQuestion.class, criteria);
			List studentTestQuestionList = (List) broker.getCollectionByQuery(queryCriteria);
			broker.close();
			assertEquals(
				studentTestQuestionList.size(),
				infoSiteStudentsTestMarks.getInfoStudentTestQuestionList().size());
			assertEquals(
				(InfoExecutionCourse) Cloner.get(executionCourse),
				infoSiteStudentsTestMarks.getExecutionCourse());
			assertEquals(
				copyIDistributedTest2InfoDistributedTest(
					((IStudentTestQuestion) studentTestQuestionList.get(0)).getDistributedTest()),
				infoSiteStudentsTestMarks.getInfoDistributedTest());
			Iterator it = infoSiteStudentsTestMarks.getInfoStudentTestQuestionList().iterator();
			int i = 0;
			while (it.hasNext())
			{
				InfoStudentTestQuestionMark infoStudentTestQuestionMark =
					(InfoStudentTestQuestionMark) it.next();
				IStudentTestQuestion studentTestQuestion =
					(IStudentTestQuestion) studentTestQuestionList.get(i);
				assertEquals(
					infoStudentTestQuestionMark,
					Cloner.copyIStudentTestQuestion2InfoStudentTestQuestionMark(studentTestQuestion));
				i++;
			}

		}
		catch (FenixServiceException ex)
		{
			fail("ReadDistributedTestMarksTest " + ex);
		}
		catch (Exception ex)
		{
			fail("ReadDistributedTestMarksTest " + ex);
		}
	}

	private static InfoDistributedTest copyIDistributedTest2InfoDistributedTest(IDistributedTest distributedTest)
	{
		InfoDistributedTest infoDistributedTest = new InfoDistributedTest();
		try
		{
			CopyUtils.copyProperties(infoDistributedTest, distributedTest);
		}
		catch (Exception e)
		{
			fail("ReadStudentTestsToDoTest " + "cloner");
		}
		return infoDistributedTest;
	}
}
