/*
 * Created on Jun 4, 2004
 *
 */
package ServidorAplicacao.Servicos.grant.contract;

import DataBeans.grant.contract.InfoGrantPart;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Pica
 * @author Barbosa
 */
public class ReadGrantPartTest extends ServiceNeedsAuthenticationTestCase {

    /**
     * @param name
     */
    public ReadGrantPartTest(String name) {
        super(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.ServiceTestCase#getNameOfServiceToBeTested()
     */
    protected String getNameOfServiceToBeTested() {
        return "ReadGrantPart";
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
    public void testReadGrantPartSuccessfull() {
        try {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getAuthorizeArguments();

            InfoGrantPart result = (InfoGrantPart) ServiceManagerServiceFactory
                    .executeService(id, getNameOfServiceToBeTested(), args2);

            //Check the read result
            Integer grantPartId = new Integer(1);
            if (!result.getIdInternal().equals(grantPartId))
                    fail("Reading a GrantPart Successfull: invalid grant part read!");

            //Verify unchanged database
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
            System.out
                    .println("testReadGrantPartSuccessfull was SUCCESSFULY runned by: "
                            + getNameOfServiceToBeTested());
        } catch (FenixServiceException e) {
            fail("Reading a GrantPart " + e);
        } catch (Exception e) {
            fail("Reading a GrantPart " + e);
        }
    }

    /*
     * Read a GrantPart Unsuccessfull
     */
    public void testReadGrantPartUnsuccessfull() {
        try {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getUnauthorizeArguments();

            InfoGrantPart result = (InfoGrantPart) ServiceManagerServiceFactory
                    .executeService(id, getNameOfServiceToBeTested(), args2);

            //Check the read result
            if (result != null)
                    fail("Reading a GrantPart Unsuccessfull: grant Part should not exist!");

            //Verify unchanged database
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
            System.out
                    .println("testReadGrantPartUnsuccessfull was SUCCESSFULY runned by: "
                            + getNameOfServiceToBeTested());
        } catch (FenixServiceException e) {
            fail("Reading a GrantPartUnsuccessfull " + e);
        } catch (Exception e) {
            fail("Reading a GrantPart Unsuccessfull " + e);
        }
    }

}