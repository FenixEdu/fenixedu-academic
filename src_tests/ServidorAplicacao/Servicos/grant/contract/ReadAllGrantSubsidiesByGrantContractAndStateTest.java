/*
 * Created on Jun 16, 2004
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
public class ReadAllGrantSubsidiesByGrantContractAndStateTest
		extends ServiceNeedsAuthenticationTestCase
{
	/**
	 * @param name
	 */
	public ReadAllGrantSubsidiesByGrantContractAndStateTest(String name)
	{
		super(name);
	}

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.ServiceTestCase#getNameOfServiceToBeTested()
     */
    protected String getNameOfServiceToBeTested() {
        return "ReadAllGrantSubsidiesByGrantContractAndState";
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.ServiceTestCase#getDataSetFilePath()
     */
    protected String getDataSetFilePath() {
        return "etc/datasets_templates/servicos/grant/contract/testReadAllGrantSubsidiesByGrantContractAndStateDataSet.xml";
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
        Integer state = new Integer(1); //Active subsidies
        Object[] args = { idInternal , state};
        return args;
    }

    protected Object[] getAuthorizeArguments2() {

        Integer idInternal = new Integer(1);
        Integer state = new Integer(0); //Desactivated subsidies
        Object[] args = { idInternal , state};
        return args;
    }
    
    protected Object[] getUnauthorizeArguments() {

        Integer idInternal = new Integer(666); //Invalid Contract...
        Integer state = new Integer(0); //Desactivated subsidies
        Object[] args = { idInternal , state};
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
     * Read all GrantSubsidies actives by Contract Successfull
     */
    public void testReadAllGrantSubsidiesByGrantContractAndStateSuccessfull() {
        try {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getAuthorizeArguments();

            List result = (List) ServiceManagerServiceFactory
                    .executeService(id, getNameOfServiceToBeTested(), args2);

            //Check the read result
            if (result == null || result.size() != 1)
                    fail("Reading AllGrantSubsidiesByGrantContractAndState Successfull: invalid grant Subsidy read!");

            //Verify unchanged database
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
            System.out
                    .println("testReadAllGrantSubsidiesByGrantContractAndState Successfull was SUCCESSFULY runned by: "
                            + getNameOfServiceToBeTested());
        } catch (FenixServiceException e) {
            fail("Reading AllGrantSubsidiesByGrantContractAndState " + e);
        } catch (Exception e) {
            fail("Reading AllGrantSubsidiesByGrantContractAndState " + e);
        }
    }

    /*
     * Read all GrantSubsidies desactives by Contract Successfull
     */
    public void testReadAllGrantSubsidiesByGrantContractAndStateSuccessfull2() {
        try {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getAuthorizeArguments2();

            List result = (List) ServiceManagerServiceFactory
                    .executeService(id, getNameOfServiceToBeTested(), args2);

            //Check the read result
            if (result == null || result.size() != 2)
                    fail("Reading AllGrantSubsidiesByGrantContractAndState Successfull: invalid grant Subsidy read!");

            //Verify unchanged database
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
            System.out
                    .println("testReadAllGrantSubsidiesByGrantContractAndState Successfull was SUCCESSFULY runned by: "
                            + getNameOfServiceToBeTested());
        } catch (FenixServiceException e) {
            fail("Reading AllGrantSubsidiesByGrantContractAndState " + e);
        } catch (Exception e) {
            fail("Reading AllGrantSubsidiesByGrantContractAndState " + e);
        }
    }

    
    /*
     * Read all GrantSubsidies Unsuccessfull (invalid contract)
     */
    public void testReadAllGrantSubsidiesByGrantContractUnsuccessfull() {
        try {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getUnauthorizeArguments();

            List result = (List) ServiceManagerServiceFactory
                    .executeService(id, getNameOfServiceToBeTested(), args2);

            //Check the read result
            if (result != null && result.size() != 0)
                    fail("Reading AllGrantSubsidiesByGrantContractAndState Unsuccessfull: grant Subsidies should not exist!");

            //Verify unchanged database
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
            System.out
                    .println("testReadAllGrantSubsidiesByGrantContractAndState was SUCCESSFULY runned by: "
                            + getNameOfServiceToBeTested());
        } catch (FenixServiceException e) {
            fail("Reading AllGrantSubsidiesByGrantContractAndState Unsuccessfull " + e);
        } catch (Exception e) {
            fail("Reading AllGrantSubsidiesByGrantContractAndState Unsuccessfull " + e);
        }
    }	

}
