/*
 * Created on 13/Nov/2003
 *  
 */
package ServidorAplicacao.Servicos.teacher;

import framework.factory.ServiceManagerServiceFactory;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class DeleteCareerTest extends ServiceNeedsAuthenticationTestCase
{

    /**
	 *  
	 */
    public DeleteCareerTest(String testName)
    {
        super(testName);
    }

    protected String getDataSetFilePath()
    {
        return "etc/datasets/servicos/teacher/testDeleteCareerDataSet.xml";
    }

    protected String getNameOfServiceToBeTested()
    {
        return "DeleteCareer";
    }

    protected String[] getAuthenticatedAndAuthorizedUser()
    {

        String[] args = { "user", "pass", getApplication()};
        return args;
    }

    protected String[] getAuthenticatedAndUnauthorizedUser()
    {

        String[] args = { "julia", "pass", getApplication()};
        return args;
    }

    protected String[] getNotAuthenticatedUser()
    {

        String[] args = { "jccm", "pass", getApplication()};
        return args;
    }

    protected Object[] getAuthorizeArguments()
    {

        Object[] args = { new Integer(1)};
        return args;
    }

    protected String getApplication()
    {
        return Autenticacao.EXTRANET;
    }

    public void testDeleteProfessionalCareer()
    {

        try
        {
            Object[] args = { new Integer(1)};

            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args);
            
            compareDataSetUsingExceptedDataSetTableColumns("etc/datasets/servicos/teacher/testExpectedDeleteProfessionalCareerDataSet.xml");
        } catch (Exception ex)
        {
            fail("Deleting the ProfessionalCareer of a Teacher " + ex);
        }
    }

    public void testDeleteTeachingCareer()
    {

        try
        {
            Object[] args = { new Integer(2)};

            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args);
            
            compareDataSetUsingExceptedDataSetTableColumns("etc/datasets/servicos/teacher/testExpectedDeleteTeachingCareerDataSet.xml");
        } catch (Exception ex)
        {
            fail("Deleting the ProfessionalCareer of a Teacher " + ex);
        }
    }

}
