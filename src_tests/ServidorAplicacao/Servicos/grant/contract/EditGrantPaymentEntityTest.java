/*
 * Created on Jun 4, 2004
 *
 */

package net.sourceforge.fenixedu.applicationTier.Servicos.grant.contract;

import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantCostCenter;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantPaymentEntity;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.Autenticacao;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servicos.ServiceNeedsAuthenticationTestCase;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

/**
 * @author Pica
 * @author Barbosa
 */
public class EditGrantPaymentEntityTest extends ServiceNeedsAuthenticationTestCase {

    /**
     * @param name
     */
    public EditGrantPaymentEntityTest(String name) {
        super(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.ServiceTestCase#getNameOfServiceToBeTested()
     */
    protected String getNameOfServiceToBeTested() {
        return "EditGrantPaymentEntity";
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.ServiceTestCase#getDataSetFilePath()
     */
    protected String getDataSetFilePath() {
        return "etc/datasets_templates/servicos/grant/contract/testEditGrantPaymentEntityDataSet.xml";
    }

    private String getExpectedEditDataSetFilePath() {
        return "etc/datasets_templates/servicos/grant/contract/testEditGrantPaymentEntityExpectedDataSet.xml";
    }

    private String getExpectedCreateDataSetFilePath() {
        return "etc/datasets_templates/servicos/grant/contract/testCreateGrantPaymentEntityExpectedDataSet.xml";
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
     * @see ServiceNeedsAuthenticationTestCase#getAuthorizeArguments()
     */
    protected Object[] getAuthorizeArguments() {
        InfoGrantPaymentEntity infoGrantPaymentEntity = null;
        infoGrantPaymentEntity = new InfoGrantCostCenter();
        infoGrantPaymentEntity.setDesignation("Hello");
        infoGrantPaymentEntity.setNumber("10");
        infoGrantPaymentEntity.setOjbConcreteClass("Dominio.grant.contract.GrantCostCenter");
        InfoTeacher infoTeacher = new InfoTeacher();
        infoTeacher.setIdInternal(new Integer(2));
        infoGrantPaymentEntity.setInfoResponsibleTeacher(infoTeacher);
        Object[] args = { infoGrantPaymentEntity };
        return args;
    }

    protected Object[] getAuthorizeArgumentsEdit() {
        InfoGrantCostCenter infoGrantCostCenter = new InfoGrantCostCenter();
        infoGrantCostCenter.setIdInternal(new Integer(1));
        infoGrantCostCenter.setDesignation("Cost Center 70");
        infoGrantCostCenter.setNumber("70");
        infoGrantCostCenter.setOjbConcreteClass("Dominio.grant.contract.GrantCostCenter");
        InfoTeacher infoTeacher = new InfoTeacher();
        infoTeacher.setIdInternal(new Integer(1));
        infoGrantCostCenter.setInfoResponsibleTeacher(infoTeacher);
        Object[] args = { infoGrantCostCenter };
        return args;
    }

    protected Object[] getUnauthorizeArgumentsCreate() {
        InfoGrantPaymentEntity infoGrantPaymentEntity = null;
        infoGrantPaymentEntity = new InfoGrantCostCenter();
        infoGrantPaymentEntity.setDesignation("Hello");
        infoGrantPaymentEntity.setNumber("70");
        infoGrantPaymentEntity.setOjbConcreteClass("Dominio.grant.contract.GrantCostCenter");
        InfoTeacher infoTeacher = new InfoTeacher();
        infoTeacher.setIdInternal(new Integer(10000));
        infoGrantPaymentEntity.setInfoResponsibleTeacher(infoTeacher);
        Object[] args = { infoGrantPaymentEntity };
        return args;
    }

    protected Object[] getUnauthorizeArgumentsEdit() {
        InfoGrantPaymentEntity infoGrantPaymentEntity = null;
        infoGrantPaymentEntity = new InfoGrantCostCenter();
        infoGrantPaymentEntity.setDesignation("Hello");
        infoGrantPaymentEntity.setIdInternal(new Integer(666));
        infoGrantPaymentEntity.setNumber("666");
        infoGrantPaymentEntity.setOjbConcreteClass("Dominio.grant.contract.GrantCostCenter");
        InfoTeacher infoTeacher = new InfoTeacher();
        infoTeacher.setIdInternal(new Integer(666));
        infoGrantPaymentEntity.setInfoResponsibleTeacher(infoTeacher);
        Object[] args = { infoGrantPaymentEntity };
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
     */
    /*
     * Grant Type Creation Successfull
     */
    public void testCreateGrantPaymentEntitySuccessfull() {
        try {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getAuthorizeArguments();
            ServiceManagerServiceFactory.executeService(id, getNameOfServiceToBeTested(), args2);
            compareDataSetUsingExceptedDataSetTableColumns(getExpectedCreateDataSetFilePath());
            System.out.println(getNameOfServiceToBeTested()
                    + " was SUCCESSFULY runned by test: testCreateGrantPaymentEntitySuccessfull");
        } catch (FenixServiceException e) {
            fail("Creating a new GrantPaymentEntity successfull " + e);
        } catch (Exception e) {
            fail("Creating a new GrantPaymentEntity successfull " + e);
        }
    }

    /*
     * Grant Type Creation Unsuccessfull: existing grant type
     */
    public void testCreateGrantPaymentEntityUnsuccessfull() {
        try {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getUnauthorizeArgumentsCreate();
            ServiceManagerServiceFactory.executeService(id, getNameOfServiceToBeTested(), args2);
        } catch (ExistingServiceException e) {
            fail("Creating a new GrantPaymentEntity unsuccessfull " + e);
        } catch (FenixServiceException e) {
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
            System.out.println(getNameOfServiceToBeTested()
                    + " was SUCCESSFULY runned by test: testCreateGrantPaymentEntityUnsuccessfull");
        } catch (Exception e) {
            fail("Creating a new GrantPaymentEntity unsuccessfull " + e);
        }
    }

    /*
     * Grant Type Edition Successfull
     */
    public void testEditGrantPaymentEntitySuccessfull() {
        try {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getAuthorizeArgumentsEdit();
            ServiceManagerServiceFactory.executeService(id, getNameOfServiceToBeTested(), args2);
            compareDataSetUsingExceptedDataSetTableColumns(getExpectedEditDataSetFilePath());
            System.out.println(getNameOfServiceToBeTested()
                    + " was SUCCESSFULY runned by test: testEditGrantPaymentEntitySuccessfull");
        } catch (FenixServiceException e) {
            fail("Editing a GrantPaymentEntity successfull " + e);
        } catch (Exception e) {
            fail("Editing a GrantPaymentEntity successfull " + e);
        }
    }

    /*
     * Grant Type Edition Unsuccessfull: existing grant type
     */
    public void testEditGrantPaymentEntityUnsuccessfull() {
        try {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getUnauthorizeArgumentsEdit();
            ServiceManagerServiceFactory.executeService(id, getNameOfServiceToBeTested(), args2);
        } catch (ExistingServiceException e) {
            fail("Editing a GrantPaymentEntity unsuccessfull " + e);
        } catch (FenixServiceException e) {
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
            System.out.println(getNameOfServiceToBeTested()
                    + " was SUCCESSFULY runned by test: testEditGrantPaymentEntityUnsuccessfull");
        } catch (Exception e) {
            fail("Editing a GrantPaymentEntity unsuccessfull " + e);
        }
    }
}