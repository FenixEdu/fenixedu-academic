/*
 * Created on Jun 4, 2004
 *
 */
package ServidorAplicacao.Servicos.grant.contract;

import DataBeans.InfoTeacher;
import DataBeans.grant.contract.InfoGrantCostCenter;
import DataBeans.grant.contract.InfoGrantPart;
import DataBeans.grant.contract.InfoGrantPaymentEntity;
import DataBeans.grant.contract.InfoGrantSubsidy;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;
import framework.factory.ServiceManagerServiceFactory;


/**
 * @author Pica
 * @author Barbosa
 */
public class EditGrantPartTest extends ServiceNeedsAuthenticationTestCase {

    /**
     * @param name
     */
    public EditGrantPartTest(String name) {
        super(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.ServiceTestCase#getNameOfServiceToBeTested()
     */
    protected String getNameOfServiceToBeTested() {
        return "EditGrantPart";
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.ServiceTestCase#getDataSetFilePath()
     */
    protected String getDataSetFilePath() {
        return "etc/datasets_templates/servicos/grant/contract/testEditGrantPartDataSet.xml";
    }

    private String getExpectedEditDataSetFilePath() {
        return "etc/datasets_templates/servicos/grant/contract/testEditGrantPartExpectedDataSet.xml";
    }

    private String getExpectedCreateDataSetFilePath() {
        return "etc/datasets_templates/servicos/grant/contract/testCreateGrantPartExpectedDataSet.xml";
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
        InfoGrantPart infoGrantPart = new InfoGrantPart();
        infoGrantPart.setPercentage(new Integer(1));
        
        InfoGrantSubsidy infoGrantSubsidy = new InfoGrantSubsidy();
        infoGrantSubsidy.setIdInternal(new Integer(1));
        infoGrantPart.setInfoGrantSubsidy(infoGrantSubsidy);
        
        InfoTeacher infoTeacher = new InfoTeacher();
        infoTeacher.setTeacherNumber(new Integer(6));
        infoGrantPart.setInfoResponsibleTeacher(infoTeacher);
        
        InfoGrantPaymentEntity infoGrantPaymentEntity = new InfoGrantCostCenter();
        infoGrantPaymentEntity.setIdInternal(new Integer(4));
        infoGrantPart.setInfoGrantPaymentEntity(infoGrantPaymentEntity);
        
        Object[] args = { infoGrantPart };
        return args;
    }

    protected Object[] getAuthorizeArgumentsEdit() {
        InfoGrantPart infoGrantPart = new InfoGrantPart();
        infoGrantPart.setPercentage(new Integer(66));
        infoGrantPart.setIdInternal(new Integer(3));
        
        InfoGrantSubsidy infoGrantSubsidy = new InfoGrantSubsidy();
        infoGrantSubsidy.setIdInternal(new Integer(1));
        infoGrantPart.setInfoGrantSubsidy(infoGrantSubsidy);
        
        InfoTeacher infoTeacher = new InfoTeacher();
        infoTeacher.setTeacherNumber(new Integer(6));
        infoGrantPart.setInfoResponsibleTeacher(infoTeacher);
        
        InfoGrantPaymentEntity infoGrantPaymentEntity = new InfoGrantCostCenter();
        infoGrantPaymentEntity.setIdInternal(new Integer(4));
        infoGrantPart.setInfoGrantPaymentEntity(infoGrantPaymentEntity);

        Object[] args = { infoGrantPart };
        return args;
    }

    protected Object[] getUnauthorizeArguments(boolean edit) {
        InfoGrantPart infoGrantPart = new InfoGrantPart();
        infoGrantPart.setPercentage(new Integer(20));
        
        
        InfoGrantSubsidy infoGrantSubsidy = new InfoGrantSubsidy();
        if(edit)
        {
            infoGrantSubsidy.setIdInternal(new Integer(444));    
            infoGrantPart.setIdInternal(new Integer(1));
        }
        else
        {
            infoGrantSubsidy.setIdInternal(new Integer(444));
        }
        infoGrantPart.setInfoGrantSubsidy(infoGrantSubsidy);
        
        InfoTeacher infoTeacher = new InfoTeacher();
        infoTeacher.setTeacherNumber(new Integer(1));
        infoGrantPart.setInfoResponsibleTeacher(infoTeacher);
        
        InfoGrantPaymentEntity infoGrantPaymentEntity = new InfoGrantCostCenter();
        infoGrantPaymentEntity.setIdInternal(new Integer(1));
        infoGrantPart.setInfoGrantPaymentEntity(infoGrantPaymentEntity);
        
        Object[] args = { infoGrantPart };
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
     * Grant Part Creation Successfull
     */
    public void testCreateGrantPartSuccessfull() {
        try {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getAuthorizeArguments();

            ServiceManagerServiceFactory.executeService(id,
                    getNameOfServiceToBeTested(), args2);
            compareDataSetUsingExceptedDataSetTableColumns(getExpectedCreateDataSetFilePath());
            System.out
                    .println(getNameOfServiceToBeTested()
                            + " was SUCCESSFULY runned by test: testCreateGrantPartSuccessfull");
        } catch (FenixServiceException e) {
            fail("Creating a new GrantPart successfull " + e);
        } catch (Exception e) {
            fail("Creating a new GrantPart successfull " + e);
        }
    }

    /*
     * Grant Part Creation Unsuccessfull: existing grant part
     */
    public void testCreateGrantPartUnsuccessfull() {
        try {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getUnauthorizeArguments(false);

            ServiceManagerServiceFactory.executeService(id,
                    getNameOfServiceToBeTested(), args2);

        } catch (ExistingServiceException e) {
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
            System.out
                    .println(getNameOfServiceToBeTested()
                            + " was SUCCESSFULY runned by test: testCreateGrantPartUnsuccessfull");
        } catch (FenixServiceException e) {
            fail("Creating a new GrantPart unsuccessfull " + e);
        } catch (Exception e) {
            fail("Creating a new GrantPart unsuccessfull " + e);
        }
    }

    /*
     * Grant Part Edition Successfull
     */
    public void testEditGrantPartSuccessfull() {
        try {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getAuthorizeArgumentsEdit();

            ServiceManagerServiceFactory.executeService(id,
                    getNameOfServiceToBeTested(), args2);

            compareDataSetUsingExceptedDataSetTableColumns(getExpectedEditDataSetFilePath());
            System.out
                    .println(getNameOfServiceToBeTested()
                            + " was SUCCESSFULY runned by test: testEditGrantPartSuccessfull");
        } catch (FenixServiceException e) {
            fail("Editing a GrantPart successfull " + e);
        } catch (Exception e) {
            fail("Editing a GrantPart successfull " + e);
        }
    }

    /*
     * Grant Part Edition Unsuccessfull: existing grant type
     */
    public void testEditGrantPartUnsuccessfull() {
        try {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getUnauthorizeArguments(true);

            ServiceManagerServiceFactory.executeService(id,
                    getNameOfServiceToBeTested(), args2);
        } catch (ExistingServiceException e) {
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
            System.out
                    .println(getNameOfServiceToBeTested()
                            + " was SUCCESSFULY runned by test: testEditGrantPartUnsuccessfull");
        } catch (FenixServiceException e) {
            fail("Editing a GrantPart unsuccessfull " + e);
        } catch (Exception e) {
            fail("Editing a GrantPart unsuccessfull " + e);
        }
    }

}
