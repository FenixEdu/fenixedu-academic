/*
 * Created on Jun 17, 2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servicos.grant.contract;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.Autenticacao;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servicos.ServiceNeedsAuthenticationTestCase;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

/**
 * @author Pica
 * @author Barbosa
 */
public class ReadGrantContractRegimeByContractAndStateTest extends ServiceNeedsAuthenticationTestCase {

    /**
     * @param name
     */
    public ReadGrantContractRegimeByContractAndStateTest(String name) {
        super(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.ServiceTestCase#getNameOfServiceToBeTested()
     */
    protected String getNameOfServiceToBeTested() {
        return "ReadGrantContractRegimeByContractAndState";
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.ServiceTestCase#getDataSetFilePath()
     */
    protected String getDataSetFilePath() {
        return "etc/datasets_templates/servicos/grant/contract/testReadGrantContractRegimeByContractAndStateDataSet.xml";
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

        Integer contractId = new Integer(1);
        Integer state = new Integer(1);
        Object[] args = { contractId, state };
        return args;
    }

    protected Object[] getAuthorizeArgumentsDesactiveContractRegime() {

        Integer contractId = new Integer(1);
        Integer state = new Integer(0);
        Object[] args = { contractId, state };
        return args;
    }

    protected Object[] getUnauthorizeArguments() {

        Integer contractId = new Integer(666);
        Integer state = new Integer(0);
        Object[] args = { contractId, state };
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
     * Read active GrantContractRegime Successfull
     */
    public void testReadActiveGrantContractRegimeSuccessfull() {
        try {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getAuthorizeArguments();

            List result = (List) ServiceManagerServiceFactory.executeService(id,
                    getNameOfServiceToBeTested(), args2);

            //Check the read result
            if (result == null || result.size() != 1)
                fail("Reading active GrantContractRegime Successfull: invalid grant ContractRegime read!");

            //Verify unchanged database
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
            System.out
                    .println("testReadGrantContractRegimeByContractAndStateSuccessfull was SUCCESSFULY runned by: "
                            + getNameOfServiceToBeTested());
        } catch (FenixServiceException e) {
            fail("Reading a GrantContractRegime by contract and state" + e);
        } catch (Exception e) {
            fail("Reading a GrantContractRegime by contract and state" + e);
        }
    }

    /*
     * Read deactive GrantContractRegime Successfull
     */
    public void testReadDesactiveGrantContractRegimeSuccessfull() {
        try {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getAuthorizeArgumentsDesactiveContractRegime();

            List result = (List) ServiceManagerServiceFactory.executeService(id,
                    getNameOfServiceToBeTested(), args2);

            //Check the read result
            if (result == null || result.size() != 2)
                fail("Reading desactive GrantContractRegime Successfull: invalid grant ContractRegime read!");

            //Verify unchanged database
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
            System.out
                    .println("testReadGrantContractRegimeByContractAndStateSuccessfull was SUCCESSFULY runned by: "
                            + getNameOfServiceToBeTested());
        } catch (FenixServiceException e) {
            fail("Reading a GrantContractRegime by contract and state" + e);
        } catch (Exception e) {
            fail("Reading a GrantContractRegime by contract and state" + e);
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

            List result = (List) ServiceManagerServiceFactory.executeService(id,
                    getNameOfServiceToBeTested(), args2);

            //Check the read result
            if (result != null && result.size() != 0)
                fail("Reading a GrantContractRegime by contract and stateUnsuccessfull: grant ContractRegime should not exist!");

            //Verify unchanged database
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
            System.out
                    .println("testReadContractRegimeByContractAndState Unsuccessfull was SUCCESSFULY runned by: "
                            + getNameOfServiceToBeTested());
        } catch (FenixServiceException e) {
            fail("Reading a GrantContractRegime by contract and state Unsuccessfull " + e);
        } catch (Exception e) {
            fail("Reading a GrantContractRegime by contract and state Unsuccessfull " + e);
        }
    }

}