/*
 * Created on Jun 4, 2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servicos.grant.contract;

import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantPaymentEntity;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.Autenticacao;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servicos.ServiceNeedsAuthenticationTestCase;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

/**
 * @author Pica
 * @author Barbosa
 */
public class ReadPaymentEntityByNumberAndClassTest extends ServiceNeedsAuthenticationTestCase {

    /**
     * @param name
     */
    public ReadPaymentEntityByNumberAndClassTest(String name) {
        super(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.ServiceTestCase#getNameOfServiceToBeTested()
     */
    protected String getNameOfServiceToBeTested() {
        return "ReadPaymentEntityByNumberAndClass";
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

        String classname = GrantCostCenter.class.getName();
        String number = "1";
        Object[] args = { number, classname };
        return args;
    }

    protected Object[] getAuthorizeArgumentsGrantProject() {

        String classname = GrantProject.class.getName();
        String number = "2";
        Object[] args = { number, classname };
        return args;
    }

    protected Object[] getUnauthorizeArgumentsProject() {

        String classname = GrantProject.class.getName();
        String number = "666";
        Object[] args = { number, classname };
        return args;
    }

    protected Object[] getUnauthorizeArgumentsCostCenter() {

        String classname = GrantCostCenter.class.getName();
        String number = "666";
        Object[] args = { number, classname };
        return args;
    }

    protected Object[] getUnauthorizeArgumentsInvalidClass() {

        String classname = "bogus";
        String number = "1";
        Object[] args = { number, classname };
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
     * Read a GrantPaymentEntity Successfull (Cost Center)
     */
    public void testReadGrantPaymentEntitySuccessfull1() {
        try {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getAuthorizeArguments();

            InfoGrantPaymentEntity result = (InfoGrantPaymentEntity) ServiceManagerServiceFactory
                    .executeService(id, getNameOfServiceToBeTested(), args2);

            //Check the read result
            if (!(result.getNumber().equals("1") && result.getOjbConcreteClass().equals(
                    GrantCostCenter.class.getName())))
                fail("Reading a GrantPaymentEntity Successfull: invalid grant payment entity read!");

            //Verify unchanged database
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
            System.out.println("testReadGrantPaymentEntitySuccessfull was SUCCESSFULY runned by: "
                    + getNameOfServiceToBeTested());
        } catch (FenixServiceException e) {
            fail("Reading a GrantPaymentEntity " + e);
        } catch (Exception e) {
            fail("Reading a GrantPaymentEntity " + e);
        }
    }

    /*
     * Read a GrantPaymentEntity Successfull (Project)
     */
    public void testReadGrantPaymentEntitySuccessfull2() {
        try {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getAuthorizeArgumentsGrantProject();

            InfoGrantPaymentEntity result = (InfoGrantPaymentEntity) ServiceManagerServiceFactory
                    .executeService(id, getNameOfServiceToBeTested(), args2);

            //Check the read result
            if (!result.getNumber().equals("2")
                    || !result.getOjbConcreteClass().equals(GrantProject.class.getName()))
                fail("Reading a GrantPaymentEntity Successfull: invalid grant payment entity read!");

            //Verify unchanged database
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
            System.out.println("testReadGrantPaymentEntitySuccessfull was SUCCESSFULY runned by: "
                    + getNameOfServiceToBeTested());
        } catch (FenixServiceException e) {
            fail("Reading a GrantPaymentEntity " + e);
        } catch (Exception e) {
            fail("Reading a GrantPaymentEntity " + e);
        }
    }

    /*
     * Read a GrantPaymentEntity Unsuccessfull
     */
    public void testReadGrantPaymentEntityUnsuccessfull1() {
        try {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getUnauthorizeArgumentsProject();

            InfoGrantPaymentEntity result = (InfoGrantPaymentEntity) ServiceManagerServiceFactory
                    .executeService(id, getNameOfServiceToBeTested(), args2);

            //Check the read result
            if (result != null)
                fail("Reading a GrantPaymentEntity Unsuccessfull: grant payment entity should not exist!");

            //Verify unchanged database
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
            System.out.println("testReadGrantPaymentEntityUnsuccessfull was SUCCESSFULY runned by: "
                    + getNameOfServiceToBeTested());
        } catch (FenixServiceException e) {
            fail("Reading a GrantPaymentEntity Unsuccessfull " + e);
        } catch (Exception e) {
            fail("Reading a GrantPaymentEntity Unsuccessfull " + e);
        }
    }

    /*
     * Read a GrantPaymentEntity Unsuccessfull
     */
    public void testReadGrantPaymentEntityUnsuccessfull2() {
        try {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getUnauthorizeArgumentsCostCenter();

            InfoGrantPaymentEntity result = (InfoGrantPaymentEntity) ServiceManagerServiceFactory
                    .executeService(id, getNameOfServiceToBeTested(), args2);

            //Check the read result
            if (result != null)
                fail("Reading a GrantPaymentEntity Unsuccessfull: grant payment entity should not exist!");

            //Verify unchanged database
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
            System.out.println("testReadGrantPaymentEntityUnsuccessfull was SUCCESSFULY runned by: "
                    + getNameOfServiceToBeTested());
        } catch (FenixServiceException e) {
            fail("Reading a GrantPaymentEntity Unsuccessfull " + e);
        } catch (Exception e) {
            fail("Reading a GrantPaymentEntity Unsuccessfull " + e);
        }
    }

    /*
     * Read a GrantPaymentEntity Unsuccessfull
     */
    public void testReadGrantPaymentEntityUnsuccessfull3() {
        try {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getUnauthorizeArgumentsInvalidClass();

            InfoGrantPaymentEntity result = (InfoGrantPaymentEntity) ServiceManagerServiceFactory
                    .executeService(id, getNameOfServiceToBeTested(), args2);

            if (result != null)
                fail("Reading a GrantPaymentEntity Unsuccessfull: grant payment entity should not exist!");

            //Verify unchanged database
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
            System.out.println("testReadGrantPaymentEntityUnsuccessfull was SUCCESSFULY runned by: "
                    + getNameOfServiceToBeTested());

        } catch (FenixServiceException e) {
            fail("Reading a GrantPaymentEntity Unsuccessfull " + e);
        } catch (Exception e) {
            fail("Reading a GrantPaymentEntity Unsuccessfull " + e);
        }
    }

}