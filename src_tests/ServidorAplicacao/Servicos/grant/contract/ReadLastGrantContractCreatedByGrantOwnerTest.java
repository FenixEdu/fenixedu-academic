/*
 * Created on Jun 16, 2004
 */
package ServidorAplicacao.Servicos.grant.contract;

import DataBeans.grant.contract.InfoGrantContract;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;
import framework.factory.ServiceManagerServiceFactory;


/**
 * @author Pica
 * @author Barbosa
 */
public class ReadLastGrantContractCreatedByGrantOwnerTest
		extends
			ServiceNeedsAuthenticationTestCase
{

	/**
	 * @param name
	 */
	public ReadLastGrantContractCreatedByGrantOwnerTest(String name)
	{
		super(name);
	}
	
	    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.ServiceTestCase#getNameOfServiceToBeTested()
     */
    protected String getNameOfServiceToBeTested() {
        return "ReadLastGrantContractCreatedByGrantOwner";
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.ServiceTestCase#getDataSetFilePath()
     */
    protected String getDataSetFilePath() {
        return "etc/datasets_templates/servicos/grant/contract/testReadLastGrantContractCreatedByGrantOwnerDataSet.xml";
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServiceNeedsAuthenticationTestCase#getAuthenticatedAndAuthorizedUser()
     */
    protected String[] getAuthenticatedAndAuthorizedUser() {
        String[] args = { "16", "pass", getApplication() };
        return args;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServiceNeedsAuthenticationTestCase#getAuthenticatedAndUnauthorizedUser()
     */
    protected String[] getAuthenticatedAndUnauthorizedUser() {
        String[] args = { "julia", "pass", getApplication() };
        return args;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServiceNeedsAuthenticationTestCase#getNonAuthenticatedUser()
     */
    protected String[] getNotAuthenticatedUser() {
        String[] args = { "fiado", "pass", getApplication() };
        return args;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getAuthorizeArguments()
     */
    protected Object[] getAuthorizeArguments() {

        Integer idInternal = new Integer(1);
        Object[] args = { idInternal };
        return args;
    }

    protected Object[] getUnauthorizeArguments() {

        Integer idInternal = new Integer(666);
        Object[] args = { idInternal };
        return args;
    }

    
    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getApplication()
     */
    protected String getApplication() {
        return Autenticacao.INTRANET;
    }

    /***************************************************************************
     * 
     * Begining of the tests
     *  
     *
     */
    
    /*
     * Read the Last GrantContract by Grant Owner Successfull
     */
    public void testReadLastGrantContractByGrantOwnerSuccessfull() {
        try {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getAuthorizeArguments();

            InfoGrantContract result = (InfoGrantContract) ServiceManagerServiceFactory
                    .executeService(id, getNameOfServiceToBeTested(), args2);

            //Check the read result
            Integer grantContractId = new Integer(2);
            Integer grantContractNumber = new Integer(2);
            if (!result.getIdInternal().equals(grantContractId) || !result.getContractNumber().equals(grantContractNumber))
                    fail("Reading a GrantContract Successfull: invalid grant contract read!");

            //Verify unchanged database
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
            System.out
                    .println("testReadLastGrantContractCreatedByGrantOwnerTest Successfull was SUCCESSFULY runned by: "
                            + getNameOfServiceToBeTested());
        } catch (FenixServiceException e) {
            fail("Reading LastGrantContractCreatedByGrantOwner " + e);
        } catch (Exception e) {
            fail("Reading LastGrantContractCreatedByGrantOwner " + e);
        }
    }

    /*
     * Read the Last GrantContract By GrantOwner Unsuccessfull (unKnowned grant owner)
     */
    public void testReadLastGrantContractCreatedByGrantOwnerUnsuccessfull() {
        try {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getUnauthorizeArguments();

            InfoGrantContract result = (InfoGrantContract) ServiceManagerServiceFactory
                    .executeService(id, getNameOfServiceToBeTested(), args2);

            //Check the read result
            if (result != null && result.getIdInternal() != null)
                    fail("Reading LastGrantContractCreatedByGrantOwner Unsuccessfull: grant contract should not exist!");

            //Verify unchanged database
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
            System.out
                    .println("testReadLastGrantContractCreatedByGrantOwner was SUCCESSFULY runned by: "
                            + getNameOfServiceToBeTested());
        } catch (FenixServiceException e) {
            fail("Reading LastGrantContractCreatedByGrantOwner Unsuccessfull " + e);
        } catch (Exception e) {
            fail("Reading LastGrantContractCreatedByGrantOwner Unsuccessfull " + e);
        }
    }	

}
