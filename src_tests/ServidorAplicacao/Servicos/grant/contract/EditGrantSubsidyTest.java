/*
 * Created on 22/Jan/2004
 *  
 */

package ServidorAplicacao.Servicos.grant.contract;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import DataBeans.grant.contract.InfoGrantContract;
import DataBeans.grant.contract.InfoGrantSubsidy;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Barbosa
 * @author Pica
 *  
 */

public class EditGrantSubsidyTest extends ServiceNeedsAuthenticationTestCase {

    /**
     * @param testName
     */
    public EditGrantSubsidyTest(String testName) {
        super(testName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServiceNeedsAuthenticationTestCase#getApplication()
     */
    protected String getApplication() {
        return Autenticacao.INTRANET;
    }

    protected String getNameOfServiceToBeTested() {
        return "EditGrantSubsidy";
    }

    protected String getDataSetFilePath() {
        return "etc/datasets_templates/servicos/grant/contract/testEditGrantSubsidyDataSet.xml";
    }

    private String getExpectedEditDataSetFilePath() {
        return "etc/datasets_templates/servicos/grant/contract/testEditGrantSubsidyExpectedDataSet.xml";
    }

    private String getExpectedCreateDataSetFilePath() {
        return "etc/datasets_templates/servicos/grant/contract/testCreateGrantSubsidyExpectedDataSet.xml";
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

    protected Object[] getArguments() {
        InfoGrantSubsidy infoGrantSubsidy = null;
        Object[] args = { infoGrantSubsidy };
        return args;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServiceNeedsAuthenticationTestCase#getAuthorizeArguments()
     */
    protected Object[] getAuthorizeArguments() {
        InfoGrantSubsidy infoGrantSubsidy = new InfoGrantSubsidy();
        infoGrantSubsidy.setState(new Integer(0));
        infoGrantSubsidy.setValue(new Double(10.0));
        infoGrantSubsidy.setTotalCost(new Double(100.0));
        infoGrantSubsidy.setValueFullName("quatro");

        InfoGrantContract infoGrantContract = new InfoGrantContract();
        infoGrantContract.setIdInternal(new Integer(3));
        infoGrantSubsidy.setInfoGrantContract(infoGrantContract);

        Object[] args = { infoGrantSubsidy };
        return args;
    }

    protected Object[] getAuthorizeArgumentsEdit() {

        InfoGrantSubsidy infoGrantSubsidy = new InfoGrantSubsidy();
        infoGrantSubsidy.setIdInternal(new Integer(4));
        infoGrantSubsidy.setState(new Integer(1));
        infoGrantSubsidy.setValue(new Double(666.0));
        infoGrantSubsidy.setTotalCost(new Double(6969.0));
        infoGrantSubsidy.setValueFullName("yeyeye");

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            infoGrantSubsidy.setDateBeginSubsidy(sdf.parse("10-10-2002"));
        } catch (ParseException e) {
        }

        InfoGrantContract infoGrantContract = new InfoGrantContract();
        infoGrantContract.setIdInternal(new Integer(3));
        infoGrantSubsidy.setInfoGrantContract(infoGrantContract);

        Object[] args = { infoGrantSubsidy };
        return args;
    }

    protected Object[] getUnauthorizeArguments(boolean edit) {
        InfoGrantSubsidy infoGrantSubsidy = new InfoGrantSubsidy();
        InfoGrantContract infoGrantContract = new InfoGrantContract();
        if (edit) {
            infoGrantSubsidy.setIdInternal(new Integer(888));
            infoGrantContract.setIdInternal(new Integer(3));
        } else {
            infoGrantContract.setIdInternal(new Integer(999));
        }
        infoGrantSubsidy.setInfoGrantContract(infoGrantContract);
        infoGrantSubsidy.setState(new Integer(0));
        infoGrantSubsidy.setValue(new Double(10.0));
        infoGrantSubsidy.setTotalCost(new Double(100.0));
        infoGrantSubsidy.setValueFullName("quatro");

        Object[] args = { infoGrantSubsidy };
        return args;
    }

    /** ********** Inicio dos testes ao serviço ************* */

    /*
     * Grant Subsidy Creation Successfull
     */
    public void testCreateNewGrantSubsidySuccessfull() {
        try {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getAuthorizeArguments();

            ServiceManagerServiceFactory.executeService(id, getNameOfServiceToBeTested(), args2);
            compareDataSetUsingExceptedDataSetTableColumns(getExpectedCreateDataSetFilePath());
            System.out.println(getNameOfServiceToBeTested()
                    + " was SUCCESSFULY runned by test: testCreateGrantSubsidySuccessfull");
        } catch (FenixServiceException e) {
            fail("Creating a new GrantSubsidy " + e);
        } catch (Exception e) {
            fail("Creating a new GrantSubsidy " + e);
        }
    }

    /*
     * Grant Subsidy Creation Unsuccessfull: existing grant Subsidy
     */
    public void testCreateGrantSubsidyUnsuccessfull() {
        try {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getUnauthorizeArguments(false);

            ServiceManagerServiceFactory.executeService(id, getNameOfServiceToBeTested(), args2);

        } catch (ExistingServiceException e) {
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
            System.out.println(getNameOfServiceToBeTested()
                    + " was SUCCESSFULY runned by test: testCreateGrantSubsidyUnsuccessfull");
        } catch (FenixServiceException e) {
            fail("Creating a new GrantSubsidy unsuccessfull " + e);
        } catch (Exception e) {
            fail("Creating a new GrantSubsidy unsuccessfull " + e);
        }
    }

    /*
     * Grant Subsidy Edition Successfull
     */
    public void testEditGrantSubsidySuccessfull() {
        try {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getAuthorizeArgumentsEdit();

            ServiceManagerServiceFactory.executeService(id, getNameOfServiceToBeTested(), args2);

            compareDataSetUsingExceptedDataSetTableColumns(getExpectedEditDataSetFilePath());
            System.out.println(getNameOfServiceToBeTested()
                    + " was SUCCESSFULY runned by test: testEditGrantSubsidySuccessfull");
        } catch (FenixServiceException e) {
            fail("Editing a GrantSubsidy successfull " + e);
        } catch (Exception e) {
            fail("Editing a GrantSubsidy successfull " + e);
        }
    }

    /*
     * Grant Subsidy Edition Unsuccessfull: existing grant type
     */
    public void testEditGrantSubsidyUnsuccessfull() {
        try {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getUnauthorizeArguments(true);

            ServiceManagerServiceFactory.executeService(id, getNameOfServiceToBeTested(), args2);
        } catch (ExistingServiceException e) {
            fail("Editing a GrantSubsidy unsuccessfull " + e);
        } catch (FenixServiceException e) {
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
            System.out.println(getNameOfServiceToBeTested()
                    + " was SUCCESSFULY runned by test: testEditGrantSubsidyUnsuccessfull");
        } catch (Exception e) {
            fail("Editing a GrantSubsidy unsuccessfull " + e);
        }
    }
}