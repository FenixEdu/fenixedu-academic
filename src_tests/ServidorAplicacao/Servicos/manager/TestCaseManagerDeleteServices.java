/*
 * Created on 2/Set/2003
 */
package ServidorAplicacao.Servicos.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import framework.factory.ServiceManagerServiceFactory;

import ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices;

/**
 * @author lmac1
 */

public abstract class TestCaseManagerDeleteServices extends TestCaseNeedAuthorizationServices
{

    public TestCaseManagerDeleteServices(String testName)
    {
        super(testName);
    }

    //	try to delete object that can´t be deleted	
    public void testUnsuccessfulExecutionOfDeleteService()
    {

        Object[] args = { getArgumentsOfServiceToBeTestedUnSuccessfuly()};

        try
        {
            Object result = ServiceManagerServiceFactory.executeService(_userView, getNameOfServiceToBeTested(), args);

            List comparatorArgument = new ArrayList(expectedActionErrorsArguments().size());
            Iterator iter = expectedActionErrorsArguments().iterator();
            while (iter.hasNext())
            {
                comparatorArgument.add(iter.next());
            }
            assertEquals("testUnsuccessfulExecutionOfDeleteService", comparatorArgument, result);
            System.out.println(
                "testUnsuccessfulExecutionOfDeleteService was SUCCESSFULY runned by class: "
                    + this.getClass().getName());
        } catch (Exception ex)
        {
            ex.printStackTrace();
            System.out.println(
                "testUnsuccessfulExecutionOfDeleteService was UNSUCCESSFULY runned by class: "
                    + this.getClass().getName());
            fail("testUnsuccessfulExecutionOfDeleteService");
        }
    }

    //	delete existing object and non-existing object
    public void testSuccessfulExecutionOfDeleteService()
    {

        Object[] args = { getArgumentsOfServiceToBeTestedSuccessfuly()};

        try
        {
            Object result = ServiceManagerServiceFactory.executeService(_userView, getNameOfServiceToBeTested(), args);
            List comparatorArgument = new ArrayList();
            assertEquals("testSuccessfulExecutionOfDeleteService", comparatorArgument, result);
            System.out.println(
                "testSuccessfulExecutionOfDeleteService was SUCCESSFULY runned by class: "
                    + this.getClass().getName());

        } catch (Exception ex)
        {
            ex.printStackTrace();
            System.out.println(
                "testSuccessfulExecutionOfDeleteService was UNSUCCESSFULY runned by class: "
                    + this.getClass().getName());
            fail("testSuccessfulExecutionOfDeleteService");
        }
    }

    //	returns a list with the strings used to create the action errors
    protected abstract List expectedActionErrorsArguments();

    //	returns a list with the id of an object that can´t be deleted
    protected abstract List getArgumentsOfServiceToBeTestedUnSuccessfuly();

    //	returns a list with the id of an object that can be deleted and the id of an object that doesn´t exist
    protected abstract List getArgumentsOfServiceToBeTestedSuccessfuly();

    /* (non-Javadoc)
     * @see ServidorAplicacao.Servicos.TestCaseServicos#getArgsForAuthorizedUser()
     */
    protected String[] getArgsForAuthorizedUser()
    {
        return new String[] { "manager", "pass", getApplication()};
    }

    protected boolean needsAuthorization()
    {
        return true;
    }

    protected abstract String getNameOfServiceToBeTested();

}
