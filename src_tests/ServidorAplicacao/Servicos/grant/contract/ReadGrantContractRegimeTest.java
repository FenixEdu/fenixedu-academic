/*
 * Created on Jun 15, 2004
 */
package ServidorAplicacao.Servicos.grant.contract;

import DataBeans.grant.contract.InfoGrantContractRegime;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Pica
 * @author Barbosa
 */
public class ReadGrantContractRegimeTest extends ServiceNeedsAuthenticationTestCase {
    /**
     * @param name
     */
    public ReadGrantContractRegimeTest(String name) {
        super(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.ServiceTestCase#getNameOfServiceToBeTested()
     */
    protected String getNameOfServiceToBeTested() {
        return "ReadGrantContractRegime";
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.ServiceTestCase#getDataSetFilePath()
     */
    protected String getDataSetFilePath() {
        return "etc/datasets_templates/servicos/grant/contract/testReadGrantContractRegimeDataSet.xml";
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
     * Read a GrantContractRegime Successfull
     */
    public void testReadGrantContractRegimeSuccessfull() {
        try {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getAuthorizeArguments();

            InfoGrantContractRegime result = (InfoGrantContractRegime) ServiceManagerServiceFactory
                    .executeService(id, getNameOfServiceToBeTested(), args2);

            //Check the read result
            Integer grantContractRegimeId = new Integer(1);
            if (!result.getIdInternal().equals(grantContractRegimeId))
                fail("Reading a GrantContractRegime Successfull: invalid grant ContractRegime read!");

            //Verify unchanged database
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
            System.out.println("testReadGrantContractRegimeSuccessfull was SUCCESSFULY runned by: "
                    + getNameOfServiceToBeTested());
        } catch (FenixServiceException e) {
            fail("Reading a GrantContractRegime " + e);
        } catch (Exception e) {
            fail("Reading a GrantContractRegime " + e);
        }
    }

    /*
     * Read a GrantContractRegime Unsuccessfull
     */
    public void testReadGrantContractRegimeUnsuccessfull() {
        try {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getUnauthorizeArguments();

            InfoGrantContractRegime result = (InfoGrantContractRegime) ServiceManagerServiceFactory
                    .executeService(id, getNameOfServiceToBeTested(), args2);

            //Check the read result
            if (result != null)
                fail("Reading a GrantContractRegime Unsuccessfull: grant ContractRegime should not exist!");

            //Verify unchanged database
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
            System.out.println("testReadContractRegimeTypeUnsuccessfull was SUCCESSFULY runned by: "
                    + getNameOfServiceToBeTested());
        } catch (FenixServiceException e) {
            fail("Reading a GrantContractRegime Unsuccessfull " + e);
        } catch (Exception e) {
            fail("Reading a GrantContractRegime Unsuccessfull " + e);
        }
    }

}