/*
 * Created on Jun 15, 2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servicos.grant.contract;

import java.text.SimpleDateFormat;

import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContract;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContractRegime;
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
public class EditGrantContractRegimeTest extends ServiceNeedsAuthenticationTestCase {

    /**
     * @param name
     */
    public EditGrantContractRegimeTest(String name) {
        super(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.ServiceTestCase#getNameOfServiceToBeTested()
     */
    protected String getNameOfServiceToBeTested() {
        return "EditGrantContractRegime";
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.ServiceTestCase#getDataSetFilePath()
     */
    protected String getDataSetFilePath() {
        return "etc/datasets_templates/servicos/grant/contract/testEditGrantContractRegimeDataSet.xml";
    }

    private String getExpectedEditDataSetFilePath() {
        return "etc/datasets_templates/servicos/grant/contract/testEditGrantContractRegimeExpectedDataSet.xml";
    }

    private String getExpectedCreateDataSetFilePath() {
        return "etc/datasets_templates/servicos/grant/contract/testCreateGrantContractRegimeExpectedDataSet.xml";
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

        InfoGrantContractRegime infoGrantContractRegime = new InfoGrantContractRegime();
        infoGrantContractRegime.setState(new Integer(1));
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            infoGrantContractRegime.setDateBeginContract(sdf.parse("10-10-2222"));
            infoGrantContractRegime.setDateEndContract(sdf.parse("10-10-3333"));
            infoGrantContractRegime.setDateSendDispatchCC(sdf.parse("08-10-2003"));
            infoGrantContractRegime.setDateSendDispatchCD(sdf.parse("08-10-2003"));
            infoGrantContractRegime.setDateDispatchCC(sdf.parse("08-10-2003"));
            infoGrantContractRegime.setDateDispatchCD(sdf.parse("08-10-2003"));
        } catch (java.text.ParseException e) {
        }

        InfoGrantContract infoGrantContract = new InfoGrantContract();
        infoGrantContract.setIdInternal(new Integer(1));
        infoGrantContractRegime.setInfoGrantContract(infoGrantContract);

        InfoTeacher infoTeacher = new InfoTeacher();
        infoTeacher.setIdInternal(new Integer(2));
        infoGrantContractRegime.setInfoTeacher(infoTeacher);

        Object[] args = { infoGrantContractRegime };
        return args;
    }

    protected Object[] getAuthorizeArgumentsEdit() {

        InfoGrantContractRegime infoGrantContractRegime = new InfoGrantContractRegime();
        infoGrantContractRegime.setIdInternal(new Integer(1));
        infoGrantContractRegime.setState(new Integer(1));
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            infoGrantContractRegime.setDateBeginContract(sdf.parse("11-11-2003"));
            infoGrantContractRegime.setDateEndContract(sdf.parse("10-10-2004"));
            infoGrantContractRegime.setDateSendDispatchCC(sdf.parse("08-10-2003"));
            infoGrantContractRegime.setDateSendDispatchCD(sdf.parse("08-10-2003"));
            infoGrantContractRegime.setDateDispatchCC(sdf.parse("08-10-2003"));
            infoGrantContractRegime.setDateDispatchCD(sdf.parse("08-10-2003"));
        } catch (java.text.ParseException e) {
        }

        InfoGrantContract infoGrantContract = new InfoGrantContract();
        infoGrantContract.setIdInternal(new Integer(1));
        infoGrantContractRegime.setInfoGrantContract(infoGrantContract);

        InfoTeacher infoTeacher = new InfoTeacher();
        infoTeacher.setIdInternal(new Integer(2));
        infoGrantContractRegime.setInfoTeacher(infoTeacher);

        Object[] args = { infoGrantContractRegime };
        return args;
    }

    protected Object[] getUnauthorizeArguments(boolean edit) {
        InfoGrantContractRegime infoGrantContractRegime = new InfoGrantContractRegime();

        if (edit) {
            InfoTeacher infoTeacher = new InfoTeacher();
            infoTeacher.setIdInternal(new Integer(100));
            infoGrantContractRegime.setInfoTeacher(infoTeacher);
        } else {
            infoGrantContractRegime.setIdInternal(new Integer(1));
        }
        infoGrantContractRegime.setState(new Integer(1));
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            infoGrantContractRegime.setDateBeginContract(sdf.parse("11-11-2003"));
            infoGrantContractRegime.setDateEndContract(sdf.parse("10-10-2004"));
            infoGrantContractRegime.setDateSendDispatchCC(sdf.parse("08-10-2003"));
            infoGrantContractRegime.setDateSendDispatchCD(sdf.parse("08-10-2003"));
            infoGrantContractRegime.setDateDispatchCC(sdf.parse("08-10-2003"));
            infoGrantContractRegime.setDateDispatchCD(sdf.parse("08-10-2003"));
        } catch (java.text.ParseException e) {
        }

        InfoGrantContract infoGrantContract = new InfoGrantContract();
        infoGrantContract.setIdInternal(new Integer(1));
        infoGrantContractRegime.setInfoGrantContract(infoGrantContract);

        Object[] args = { infoGrantContractRegime };
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
     * Grant ContractRegime Creation Successfull
     */
    public void testCreateGrantContractRegimeSuccessfull() {
        try {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getAuthorizeArguments();

            ServiceManagerServiceFactory.executeService(id, getNameOfServiceToBeTested(), args2);
            compareDataSetUsingExceptedDataSetTableColumns(getExpectedCreateDataSetFilePath());
            System.out.println(getNameOfServiceToBeTested()
                    + " was SUCCESSFULY runned by test: testCreateGrantContractRegimeSuccessfull");
        } catch (FenixServiceException e) {
            fail("Creating a new GrantContractRegime successfull " + e);
        } catch (Exception e) {
            fail("Creating a new GrantContractRegime successfull " + e);
        }
    }

    /*
     * Grant ContractRegime Creation Unsuccessfull: existing grant part
     */
    public void testCreateGrantContractRegimeUnsuccessfull() {
        try {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getUnauthorizeArguments(false);

            ServiceManagerServiceFactory.executeService(id, getNameOfServiceToBeTested(), args2);

        } catch (ExistingServiceException e) {
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
            System.out.println(getNameOfServiceToBeTested()
                    + " was SUCCESSFULY runned by test: testCreateGrantContractRegimeUnsuccessfull");
        } catch (FenixServiceException e) {
            fail("Creating a new GrantContractRegime unsuccessfull " + e);
        } catch (Exception e) {
            fail("Creating a new GrantContractRegime unsuccessfull " + e);
        }
    }

    /*
     * Grant ContractRegime Edition Successfull
     */
    public void testEditGrantContractRegimeSuccessfull() {
        try {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getAuthorizeArgumentsEdit();

            ServiceManagerServiceFactory.executeService(id, getNameOfServiceToBeTested(), args2);

            compareDataSetUsingExceptedDataSetTableColumns(getExpectedEditDataSetFilePath());
            System.out.println(getNameOfServiceToBeTested()
                    + " was SUCCESSFULY runned by test: testEditGrantContractRegimeSuccessfull");
        } catch (FenixServiceException e) {
            fail("Editing a GrantContractRegime successfull " + e);
        } catch (Exception e) {
            fail("Editing a GrantContractRegime successfull " + e);
        }
    }

    /*
     * Grant ContractRegime Edition Unsuccessfull: invalid values
     */
    public void testEditGrantContractRegimeUnsuccessfull() {
        try {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getUnauthorizeArguments(true);

            ServiceManagerServiceFactory.executeService(id, getNameOfServiceToBeTested(), args2);
        } catch (ExistingServiceException e) {
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
            System.out.println(getNameOfServiceToBeTested()
                    + " was SUCCESSFULY runned by test: testEditGrantContractRegimeUnsuccessfull");
        } catch (FenixServiceException e) {
            fail("Editing a GrantContractRegime unsuccessfull " + e);
        } catch (Exception e) {
            fail("Editing a GrantContractRegime unsuccessfull " + e);
        }
    }
}