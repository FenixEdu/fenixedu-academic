/*
 * Created on Jun 15, 2004
 *
 */
package ServidorAplicacao.Servicos.grant.contract;

import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Pica
 * @author Barbosa
 */
public class DeleteGrantContractTest extends ServiceNeedsAuthenticationTestCase {

    /**
     * @param name
     */
    public DeleteGrantContractTest(String name) {
        super(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.ServiceTestCase#getNameOfServiceToBeTested()
     */
    protected String getNameOfServiceToBeTested() {
        return "DeleteGrantContract";
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.ServiceTestCase#getDataSetFilePath()
     */
    protected String getDataSetFilePath() {
        return "etc/datasets_templates/servicos/grant/contract/testDeleteGrantContractDataSet.xml";
    }

    protected String getExpectedDataSetFilePath() {
        return "etc/datasets_templates/servicos/grant/contract/testDeleteGrantContractExpectedDataSet.xml";
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

        Integer idInternal = new Integer(2);
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
     * Delete a GrantContract Successfull
     */
    public void testDeleteGrantContractSuccessfull() {
        try {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getAuthorizeArguments();

            ServiceManagerServiceFactory.executeService(id, getNameOfServiceToBeTested(), args2);

            //Verify changed database
            compareDataSetUsingExceptedDataSetTableColumns(getExpectedDataSetFilePath());
            System.out.println("testDeleteGrantContractSuccessfull was SUCCESSFULY runned by: "
                    + getNameOfServiceToBeTested());
        } catch (FenixServiceException e) {
            fail("Deleting a GrantContract " + e);
        } catch (Exception e) {
            fail("Deleting a GrantContract " + e);
        }
    }

    /*
     * Delete a GrantContract Unsuccessfull
     */
    public void testDeleteGrantContractUnsuccessfull() {
        try {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getUnauthorizeArguments();

            ServiceManagerServiceFactory.executeService(id, getNameOfServiceToBeTested(), args2);

            fail("Deleting a GrantContract Unsuccessfull: grant Contract should not exist do te deleted!");

        } catch (FenixServiceException e) {
            //Verify unchanged database
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
            System.out.println("testDeleteGrantContractUnsuccessfull was SUCCESSFULY runned by: "
                    + getNameOfServiceToBeTested());
        } catch (Exception e) {
            fail("Delete a GrantContract Unsuccessfull " + e);
        }
    }

}