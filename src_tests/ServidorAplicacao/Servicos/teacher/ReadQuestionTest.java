/*
 * Created on 12/Ago/2003
 *  
 */
package ServidorAplicacao.Servicos.teacher;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoQuestion;
import DataBeans.InfoSiteQuestion;
import DataBeans.SiteView;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Susana Fernandes
 */
public class ReadQuestionTest extends ServiceNeedsAuthenticationTestCase
{

	public ReadQuestionTest(String testName)
	{
		super(testName);
	}

	protected String getDataSetFilePath()
	{
		return "etc/datasets/servicos/teacher/testReadQuestionTestDataSet.xml";
	}

	protected String getNameOfServiceToBeTested()
	{
		return "ReadQuestion";
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
		Integer metadataId = new Integer(3);
		Integer questionId = new Integer(448);
		String path = new String("e:\\eclipse\\workspace\\fenix\\build\\standalone\\");

		Object[] args = { executionCourseId, metadataId, questionId, path };
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

			InfoSiteQuestion bodyComponent = (InfoSiteQuestion) siteView.getComponent();
			InfoExecutionCourse infoExecutionCourse = bodyComponent.getExecutionCourse();
			assertEquals(infoExecutionCourse.getIdInternal(), args[0]);
			InfoQuestion infoQuestion = bodyComponent.getInfoQuestion();
			assertEquals(infoQuestion.getInfoMetadata().getIdInternal(), args[1]);
			if (!args[2].equals(new Integer(-1)))
				assertEquals(infoQuestion.getIdInternal(), args[2]);

			compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
		}
		catch (FenixServiceException ex)
		{
			fail("Read Question " + ex);
		}
		catch (Exception ex)
		{
			fail("Read Question " + ex);
		}
	}
}
