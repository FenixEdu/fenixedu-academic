/*
 * Created on 13/Nov/2003
 *  
 */
package ServidorAplicacao.Servicos.teacher;

import DataBeans.InfoTeacher;
import DataBeans.teacher.InfoCareer;
import DataBeans.teacher.InfoCategory;
import DataBeans.teacher.InfoExternalActivity;
import DataBeans.teacher.InfoProfessionalCareer;
import DataBeans.teacher.InfoTeachingCareer;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class EditExternalActivityTest extends ServiceNeedsAuthenticationTestCase
{

    public EditExternalActivityTest(String testName)
    {
        super(testName);
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servicos.ServiceTestCase#getNameOfServiceToBeTested()
	 */
    protected String getNameOfServiceToBeTested()
    {
        return "EditExternalActivity";
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servicos.ServiceTestCase#getDataSetFilePath()
	 */
    protected String getDataSetFilePath()
    {
        return "etc/datasets/servicos/teacher/testEditExternalActivityDataSet.xml";
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getAuthenticatedAndAuthorizedUser()
	 */
    protected String[] getAuthenticatedAndAuthorizedUser()
    {
        String[] args = { "user", "pass", getApplication()};
        return args;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getAuthenticatedAndUnauthorizedUser()
	 */
    protected String[] getAuthenticatedAndUnauthorizedUser()
    {
        String[] args = { "manager", "pass", getApplication()};
        return args;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getNotAuthenticatedUser()
	 */
    protected String[] getNotAuthenticatedUser()
    {
        String[] args = { "jccm", "pass", getApplication()};
        return args;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getAuthorizeArguments()
	 */
    protected Object[] getAuthorizeArguments()
    {
        Integer externalActivityId = new Integer(1);

        InfoExternalActivity infoExternalActivity = new InfoExternalActivity();
        infoExternalActivity.setIdInternal(externalActivityId);
        infoExternalActivity.setActivity("brincar");

        InfoTeacher infoTeacher = new InfoTeacher();
        infoTeacher.setIdInternal(new Integer(1));

        infoExternalActivity.setInfoTeacher(infoTeacher);

        Object[] args = { externalActivityId, infoExternalActivity };
        return args;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getApplication()
	 */
    protected String getApplication()
    {
        return Autenticacao.EXTRANET;
    }

    public void testCreateNewExternalActivity()
    {
        try
        {
            Boolean result = null;
            InfoExternalActivity infoExternalActivity = new InfoExternalActivity();
            infoExternalActivity.setActivity("viajar");

            InfoTeacher infoTeacher = new InfoTeacher();
            infoTeacher.setIdInternal(new Integer(1));

            infoExternalActivity.setInfoTeacher(infoTeacher);
            Object[] args = { null, infoExternalActivity };

            result = (Boolean) gestor.executar(userView, getNameOfServiceToBeTested(), args);

            assertTrue(result.booleanValue());
            compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/teacher/testExpectedCreateExternalActivityDataSet.xml");
        } catch (Exception ex)
        {
            fail("Creating a new external activity " + ex);
        }
    }

    public void testEditExistingExternalActivity()
    {
        try
        {
            Boolean result = null;
            Integer externalActivityId = new Integer(1);
            InfoExternalActivity infoExternalActivity = new InfoExternalActivity();
            infoExternalActivity.setIdInternal(externalActivityId);
            infoExternalActivity.setActivity("passear");

            InfoTeacher infoTeacher = new InfoTeacher();
            infoTeacher.setIdInternal(new Integer(1));

            infoExternalActivity.setInfoTeacher(infoTeacher);
            Object[] args = { externalActivityId, infoExternalActivity };

            result = (Boolean) gestor.executar(userView, getNameOfServiceToBeTested(), args);

            assertTrue(result.booleanValue());
            compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/teacher/testExpectedEditExternalActivityDataSet.xml");
        } catch (Exception ex)
        {
            fail("Editing an existing external activity " + ex);
        }
    }
}
