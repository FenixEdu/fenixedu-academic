package ServidorAplicacao.Servicos.teacher;

import framework.factory.ServiceManagerServiceFactory;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;

/**
 * @author Barbosa
 * @author Pica
 */
public class EditAnnouncementTest extends AnnouncementBelongsToExecutionCourseTest
{
	/**
	 * @param testName
	 */
	public EditAnnouncementTest(String testName)
	{
		super(testName);
	}

	protected String getNameOfServiceToBeTested()
	{
		return "EditAnnouncementService";
	}
	
	protected String getDataSetFilePath()
	{
		return "etc/datasets/servicos/teacher/testEditAnnouncementDataSet.xml";
	}
	
	protected String getExpectedDataSetFilePath()
	{
		return "etc/datasets/servicos/teacher/testExpectedEditAnnouncementDataSet.xml";
	}
	/*
	 * @see ServidorAplicacao.Servicos.teacher.AnnouncementBelongsToExecutionCourseTest#getExpectedUnsuccefullDataSetFilePath()
	 */
	protected String getExpectedUnsuccessfullDataSetFilePath()
	{
		return "etc/datasets/servicos/teacher/testExpectedEditAnnouncementUnsuccefullDataSet.xml";
	}
	/*
	 * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getApplication()
	 */
	protected String getApplication()
	{
		return Autenticacao.EXTRANET;
	}
	/*
	 * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getAuthorizedUser()
	 */
	protected String[] getAuthenticatedAndAuthorizedUser()
	{
		String[] args = { "user", "pass", getApplication()};
		return args;
	}
	/*
	 * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getUnauthorizedUser()
	 */
	protected String[] getAuthenticatedAndUnauthorizedUser()
	{
		String[] args = { "julia", "pass", getApplication()};
		return args;
	}
	/*
	 * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getNonTeacherUser()
	 */
	protected String[] getNotAuthenticatedUser()
	{
		String[] args = { "jccm", "pass", getApplication()};
		return args;
	}
	/*
	 * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getAuthorizeArguments()
	 */
	protected Object[] getAuthorizeArguments()
	{
		Integer infoExecutionCourseCode = new Integer(24);
		Integer announcementCode = new Integer(2);
		String announcementNewTitle = "Titulo-teste-teste";
		String announcementNewInformation = "Informacao-teste-teste";

		Object[] args =
			{
				infoExecutionCourseCode,
				announcementCode,
				announcementNewTitle,
				announcementNewInformation };
		return args;
	}

	/*
	 * @see ServidorAplicacao.Servicos.teacher.AnnouncementBelongsToExecutionCourseTest#getTestAnnouncementUnsuccessfullArguments()
	 */
	protected Object[] getAnnouncementUnsuccessfullArguments()
	{
		Integer infoExecutionCourseCode = new Integer(24);
		Integer announcementCode = new Integer(3);
		String announcementNewTitle = "Titulo-teste-teste";
		String announcementNewInformation = "Informacao-teste-teste";

		Object[] args =
			{
				infoExecutionCourseCode,
				announcementCode,
				announcementNewTitle,
				announcementNewInformation };
		return args;
	}

	/**
	 * 
	 * Start of the tests
	 *
	 */

	/**
	 * Edit an Announcement Successfully
	 */
	public void testEditAnnouncementSuccefull()
	{
		try
		{
			Integer infoExecutionCourseCode = new Integer(24);
			Integer announcementCode = new Integer(1);
			String announcementNewTitle = "NewTitle";
			String announcementNewInformation = "NewInformation";
			Object[] argserv =
				{
					infoExecutionCourseCode,
					announcementCode,
					announcementNewTitle,
					announcementNewInformation };

			IUserView arguser = authenticateUser(getAuthenticatedAndAuthorizedUser());

			//Run the service
			ServiceManagerServiceFactory.executeService(arguser, getNameOfServiceToBeTested(), argserv);

			//Verify is the announcement was edited
			compareDataSetUsingExceptedDataSetTablesAndColumns(getExpectedDataSetFilePath());

		} catch (FenixServiceException ex)
		{
			fail("Editing an announcement of a Site " + ex);
		} catch (Exception ex)
		{
			fail("Editing an announcument of a Site " + ex);
		}
	}

	/**
	 * Try do edit a announcement that doesn't exist
	 */
	public void testEditAnnouncementUnsuccefull()
	{
		try
		{
			Integer infoExecutionCourseCode = new Integer(24);
			Integer announcementCode = new Integer(12121212);
			String announcementNewTitle = "Novo titulo";
			String announcementNewInformation = "Nova Informacao";
			Object[] argserv =
				{
					infoExecutionCourseCode,
					announcementCode,
					announcementNewTitle,
					announcementNewInformation };

			IUserView arguser = authenticateUser(getAuthenticatedAndAuthorizedUser());

			//Run the service
			ServiceManagerServiceFactory.executeService(arguser, getNameOfServiceToBeTested(), argserv);

		} catch (NotAuthorizedException ex)
		{
			//The announcement doesn't exist.. so the filter return an exception
			compareDataSetUsingExceptedDataSetTablesAndColumns(getExpectedUnsuccessfullDataSetFilePath());
		} catch (FenixServiceException ex)
		{
			fail("Editing an announcement of a Site " + ex);
		} catch (Exception ex)
		{
			fail("Editing an announcument of a Site " + ex);
		}
	}
}