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
public class ReadAllGrantPaymentEntitiesByClassNameTest extends ServiceNeedsAuthenticationTestCase {

    /**
     * @param name
     */
    public ReadAllGrantPaymentEntitiesByClassNameTest(String name) {
        super(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.ServiceTestCase#getNameOfServiceToBeTested()
     */
    protected String getNameOfServiceToBeTested() {
        return "ReadAllGrantPaymentEntitiesByClassName";
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.ServiceTestCase#getDataSetFilePath()
     */
    protected String getDataSetFilePath() {
        return "etc/datasets_templates/servicos/grant/contract/testReadGrantPaymentEntityDataSet.xml";
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

        String classname = "Dominio.grant.contract.GrantCostCenter";
        Object[] args = { classname };
        return args;
    }

    protected Object[] getAuthorizeArgumentsGrantProject() {

        String classname = "Dominio.grant.contract.GrantProject";
        Object[] args = { classname };
        return args;
    }

    protected Object[] getUnauthorizeArguments() {

        String classname = "bogus";
        Object[] args = { classname };
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
     * Read a GrantPaymentEntity Successfull (Cost Centers)
     */
    public void testReadAllGrantPaymentEntitiesByClassNameSuccessfull1() {
        try {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getAuthorizeArguments();

            List result = (List) ServiceManagerServiceFactory.executeService(id,
                    getNameOfServiceToBeTested(), args2);

            //Check the read result
            if (result == null || result.size() != 2)
                fail("Reading a AllGrantPaymentEntityByClassName (Cost Center) Successfull: invalid grant payment entity read!");

            //Verify unchanged database
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
            System.out
                    .println("testReadAllGrantPaymentEntityByClassName (CostCenter) was SUCCESSFULY runned by: "
                            + getNameOfServiceToBeTested());
        } catch (FenixServiceException e) {
            fail("Reading a AllGrantPaymentEntityByClassName " + e);
        } catch (Exception e) {
            fail("Reading a AllGrantPaymentEntityByClassName " + e);
        }
    }

    /*
     * Read a GrantPaymentEntity Successfull (Projects)
     */
    public void testReadAllGrantPaymentEntitiesByClassNameSuccessfull2() {
        try {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getAuthorizeArgumentsGrantProject();

            List result = (List) ServiceManagerServiceFactory.executeService(id,
                    getNameOfServiceToBeTested(), args2);

            //Check the read result
            if (result == null && result.size() != 3)
                fail("Reading a AllGrantPaymentEntityByClassName (Projects) Successfull: invalid grant payment entity read!");

            //Verify unchanged database
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
            System.out
                    .println("testReadAllGrantPaymentEntityByClassName (Projects) was SUCCESSFULY runned by: "
                            + getNameOfServiceToBeTested());
        } catch (FenixServiceException e) {
            fail("Reading a AllGrantPaymentEntityByClassName " + e);
        } catch (Exception e) {
            fail("Reading a AllGrantPaymentEntityByClassName " + e);
        }
    }

    /*
     * Read a GrantPaymentEntity Unsuccessfull
     */
    public void testReadGrantPaymentEntityUnsuccessfull() {
        try {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getUnauthorizeArguments();

            ServiceManagerServiceFactory.executeService(id, getNameOfServiceToBeTested(), args2);

            fail("Reading a GrantPaymentEntity Unsuccessfull: grant payment entity should not exist!");

        } catch (FenixServiceException e) {
            //Verify unchanged database
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
            System.out
                    .println("test ReadAllGrantPaymentEntityByClassName Unsuccessful was SUCCESSFULY runned by: "
                            + getNameOfServiceToBeTested());
        } catch (Exception e) {
            fail("Reading AllGrantPaymentEntityByClassName Unsuccessfull " + e);
        }
    }

}