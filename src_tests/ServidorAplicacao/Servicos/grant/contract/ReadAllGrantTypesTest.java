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
public class ReadAllGrantTypesTest extends ServiceNeedsAuthenticationTestCase {

    /**
     * @param name
     */
    public ReadAllGrantTypesTest(String name) {
        super(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.ServiceTestCase#getNameOfServiceToBeTested()
     */
    protected String getNameOfServiceToBeTested() {
        return "ReadAllGrantTypes";
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.ServiceTestCase#getDataSetFilePath()
     */
    protected String getDataSetFilePath() {
        return "etc/datasets_templates/servicos/grant/contract/testReadGrantTypeDataSet.xml";
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
        Object[] args = { };
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
     * Read all GrantTypes Successfull
     */
    public void testReadAllGrantTypesSuccessfull() {
        try {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getAuthorizeArguments();

            List result = (List) ServiceManagerServiceFactory
                    .executeService(id, getNameOfServiceToBeTested(), args2);

            //Check the read result
            if (result == null && result.size() != 2)
                    fail("Reading all GrantTypes Successfull: invalid grant type read!");
            
            //TODO.. verificar o que ele leu...

            //Verify unchanged database
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
            System.out
                    .println("testReadAllGrantTypesSuccessfull was SUCCESSFULY runned by: "
                            + getNameOfServiceToBeTested());
        } catch (FenixServiceException e) {
            fail("Reading all GrantTypes " + e);
        } catch (Exception e) {
            fail("Reading all GrantTypes " + e);
        }
    }

}
