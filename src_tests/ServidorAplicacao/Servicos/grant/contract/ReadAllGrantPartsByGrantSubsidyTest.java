/*
 * Created on Jun 4, 2004
 *
 */
package ServidorAplicacao.Servicos.grant.contract;

import java.util.List;

import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Pica
 * @author Barbosa
 */
public class ReadAllGrantPartsByGrantSubsidyTest extends
        ServiceNeedsAuthenticationTestCase {

    /**
     * @param name
     */
    public ReadAllGrantPartsByGrantSubsidyTest(String name) {
        super(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.ServiceTestCase#getNameOfServiceToBeTested()
     */
    protected String getNameOfServiceToBeTested() {
        return "ReadAllGrantPartsByGrantSubsidy";
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.ServiceTestCase#getDataSetFilePath()
     */
    protected String getDataSetFilePath() {
        return "etc/datasets_templates/servicos/grant/contract/testReadGrantPartDataSet.xml";
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
     * Read a GrantPart Successfull
     */
    public void testReadAllGrantPartsBySubsidySuccessfull() {
        try {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getAuthorizeArguments();

            List result = (List) ServiceManagerServiceFactory.executeService(
                    id, getNameOfServiceToBeTested(), args2);

            //Check the read result
            if (!(result != null && result.size() == 3))
                fail("Reading all GrantPart by subsidy Successfull: invalid grant part read!");

            //Verify unchanged database
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
            System.out
                    .println("testReadAllGrantPartBySubsidySuccessfull was SUCCESSFULY runned by: "
                            + getNameOfServiceToBeTested());
        } catch (FenixServiceException e) {
            fail("Reading all GrantPart by subsidy " + e);
        } catch (Exception e) {
            fail("Reading all GrantPart by subsidy" + e);
        }
    }

    /*
     * Read a GrantPart Unsuccessfull
     */
    public void testReadAllGrantPartsBySubsidyUnsuccessfull() {
        try {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getUnauthorizeArguments();

            List result = (List)ServiceManagerServiceFactory
                    .executeService(id, getNameOfServiceToBeTested(), args2);

            //Check the read result
            if (result != null && result.size() != 0)
                    fail("Reading all GrantPart by subsidy Unsuccessfull: grant Part should not exist!");

            //Verify unchanged database
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
            System.out
                    .println("testReadAllGrantPartsBySubsidyUnsuccessfull was SUCCESSFULY runned by: "
                            + getNameOfServiceToBeTested());
        } catch (FenixServiceException e) {
            fail("Reading All GrantParts By Subsidy Unsuccessfull " + e);
        } catch (Exception e) {
            fail("Reading All GrantParts By Subsidy Unsuccessfull " + e);
        }
    }

}