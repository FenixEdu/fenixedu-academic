/*
 * Created on 03/Dec/2003
 *
 */

package ServidorAplicacao.Servicos.grant.owner;

import framework.factory.ServiceManagerServiceFactory;
import DataBeans.grant.owner.InfoGrantOwner;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;

/**
 * @author  Barbosa
 * @author  Pica
 *
 */

public class ReadGrantOwnerByPersonTest
    extends ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase
{

    /**
     * @param testName
     */
    public ReadGrantOwnerByPersonTest(java.lang.String testName)
    {
        super(testName);
    }

    /*
    *  (non-Javadoc)
    * @see ServiceNeedsAuthenticationTestCase#getApplication()
    */
    protected String getApplication()
    {
        return Autenticacao.INTRANET;
    }

    protected String getNameOfServiceToBeTested()
    {
        return "ReadGrantByPersonOwner";
    }

    protected String getDataSetFilePath()
    {
        return "etc/datasets/servicos/grant/owner/testReadGrantOwnerDataSet.xml";
    }

    /*
     *  (non-Javadoc)
     * @see ServiceNeedsAuthenticationTestCase#getAuthenticatedAndAuthorizedUser()
     */
    protected String[] getAuthenticatedAndAuthorizedUser()
    {
        String[] args = { "16", "pass", getApplication()};
        return args;
    }
    /*
     *  (non-Javadoc)
     * @see ServiceNeedsAuthenticationTestCase#getAuthenticatedAndUnauthorizedUser()
     */
    protected String[] getAuthenticatedAndUnauthorizedUser()
    {
        String[] args = { "julia", "pass", getApplication()};
        return args;
    }

    /*
    	 *  (non-Javadoc)
    	 * @see ServiceNeedsAuthenticationTestCase#getNonAuthenticatedUser()
    	 */
    protected String[] getNotAuthenticatedUser()
    {
        String[] args = { "fiado", "pass", getApplication()};
        return args;
    }

    /*
     *  (non-Javadoc)
     * @see ServiceNeedsAuthenticationTestCase#getAuthorizeArguments()
     */
    protected Object[] getAuthorizeArguments()
    {
        Integer idInternal = new Integer(15);
        Object[] args = { idInternal };
        return args;
    }

    protected Object[] getUnauthorizeArguments()
    {
        Integer idInternal = new Integer(69);
        Object[] args = { idInternal };
        return args;
    }

    /************  Inicio dos testes ao serviço**************/

    /*
     * Read a GrantOwner Successfull
     */
    public void testReadGrantOwnerByPersonSuccessfull()
    {
        try
        {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getAuthorizeArguments();

            InfoGrantOwner result =
                (InfoGrantOwner) ServiceManagerServiceFactory.executeService(
                    id,
                    getNameOfServiceToBeTested(),
                    args2);

            //Check the read result
            Integer grantOwnerId = new Integer(2);
            if (!result.getIdInternal().equals(grantOwnerId))
                fail("Reading a GrantOwnerByPerson Successfull: invalid grant owner read!");

            //Verify unchanged database
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
            System.out.println(
                "testReadGrantOwnerByPersonSuccessfull was SUCCESSFULY runned by: "
                    + getNameOfServiceToBeTested());
        } catch (FenixServiceException e)
        {
            fail("Reading a GrantOwnerByPerson Successfull " + e);
        } catch (Exception e)
        {
            fail("Reading a GrantOwnerByPerson Successfull " + e);
        }
    }

    /*
     * Read a GrantOwner Unsuccessfull
     */
    public void testReadGrantOwnerByPersonUnsuccessfull()
    {
        try
        {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getUnauthorizeArguments();

            InfoGrantOwner result =
                (InfoGrantOwner) ServiceManagerServiceFactory.executeService(
                    id,
                    getNameOfServiceToBeTested(),
                    args2);

            //Check the read result
            if (result != null)
                fail("Reading a GrantOwnerByPerson Unsuccessfull: grant owner should not exist!");

            //Verify unchanged database
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
            System.out.println(
                "testReadGrantOwnerByPersonUnsuccessfull was SUCCESSFULY runned by: "
                    + getNameOfServiceToBeTested());
        } catch (FenixServiceException e)
        {
            fail("Reading a GrantOwnerByPerson Unsuccessfull " + e);
        } catch (Exception e)
        {
            fail("Reading a GrantOwnerByPerson Unsuccessfull " + e);
        }
    }
}