/*
 * Created on Jun 15, 2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servicos.grant.contract;

import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContract;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantOrientationTeacher;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantType;
import net.sourceforge.fenixedu.dataTransferObject.grant.owner.InfoGrantOwner;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.Autenticacao;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servicos.ServiceNeedsAuthenticationTestCase;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

/**
 * @author Pica
 * @author Barbosa
 */
public class EditGrantContractTest extends ServiceNeedsAuthenticationTestCase {

    /**
     * @param name
     */
    public EditGrantContractTest(String name) {
        super(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.ServiceTestCase#getNameOfServiceToBeTested()
     */
    protected String getNameOfServiceToBeTested() {
        return "EditGrantContract";
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.ServiceTestCase#getDataSetFilePath()
     */
    protected String getDataSetFilePath() {
        return "etc/datasets_templates/servicos/grant/contract/testEditGrantContractDataSet.xml";
    }

    private String getExpectedCreateDataSetFilePath() {
        return "etc/datasets_templates/servicos/grant/contract/testCreateGrantContractExpectedDataSet.xml";
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
        InfoGrantContract infoGrantContract = new InfoGrantContract();
        infoGrantContract.setContractNumber(new Integer(22));

        InfoGrantOwner infoGrantOwner = new InfoGrantOwner();
        infoGrantOwner.setIdInternal(new Integer(2));
        infoGrantContract.setGrantOwnerInfo(infoGrantOwner);

        InfoGrantType infoGrantType = new InfoGrantType();
        infoGrantType.setIdInternal(new Integer(1));
        infoGrantContract.setGrantTypeInfo(infoGrantType);

        InfoGrantOrientationTeacher infoGrantOrientationTeacher = new InfoGrantOrientationTeacher();
        InfoTeacher infoTeacher = new InfoTeacher();
        infoTeacher.setIdInternal(new Integer(2));
        infoGrantOrientationTeacher.setOrientationTeacherInfo(infoTeacher);
        infoGrantContract.setGrantOrientationTeacherInfo(infoGrantOrientationTeacher);

        Object[] args = { infoGrantContract };
        return args;
    }

    protected Object[] getAuthorizeArgumentsEdit() {
        InfoGrantContract infoGrantContract = new InfoGrantContract();
        infoGrantContract.setIdInternal(new Integer(1));
        infoGrantContract.setContractNumber(new Integer(12));

        InfoGrantOwner infoGrantOwner = new InfoGrantOwner();
        infoGrantOwner.setIdInternal(new Integer(1));
        infoGrantContract.setGrantOwnerInfo(infoGrantOwner);

        InfoGrantType infoGrantType = new InfoGrantType();
        infoGrantType.setIdInternal(new Integer(2));
        infoGrantContract.setGrantTypeInfo(infoGrantType);

        InfoGrantOrientationTeacher infoGrantOrientationTeacher = new InfoGrantOrientationTeacher();
        infoGrantOrientationTeacher.setIdInternal(new Integer(1));
        infoGrantContract.setGrantOrientationTeacherInfo(infoGrantOrientationTeacher);

        Object[] args = { infoGrantContract };
        return args;
    }

    protected Object[] getUnauthorizeArguments(boolean edit) {
        InfoGrantContract infoGrantContract = new InfoGrantContract();

        InfoGrantType infoGrantType = new InfoGrantType();
        if (edit) {
            infoGrantContract.setIdInternal(new Integer(666));
            infoGrantType.setIdInternal(new Integer(1));
        } else {
            infoGrantType.setIdInternal(new Integer(2222));
        }
        infoGrantContract.setGrantTypeInfo(infoGrantType);

        infoGrantContract.setContractNumber(new Integer(22));

        InfoGrantOwner infoGrantOwner = new InfoGrantOwner();
        infoGrantOwner.setIdInternal(new Integer(2));
        infoGrantContract.setGrantOwnerInfo(infoGrantOwner);

        InfoGrantOrientationTeacher infoGrantOrientationTeacher = new InfoGrantOrientationTeacher();
        InfoTeacher infoTeacher = new InfoTeacher();
        infoTeacher.setIdInternal(new Integer(2));
        infoGrantOrientationTeacher.setOrientationTeacherInfo(infoTeacher);
        infoGrantContract.setGrantOrientationTeacherInfo(infoGrantOrientationTeacher);

        Object[] args = { infoGrantContract };
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
     * Grant Contract Creation Successfull
     */
    public void testCreateGrantContractSuccessfull() {
        try {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView id = authenticateUser(args);
            Object[] args2 = getAuthorizeArguments();

            ServiceManagerServiceFactory.executeService(id, getNameOfServiceToBeTested(), args2);
            compareDataSetUsingExceptedDataSetTableColumns(getExpectedCreateDataSetFilePath());
            System.out.println(getNameOfServiceToBeTested()
                    + " was SUCCESSFULY runned by test: testCreateGrantContractSuccessfull");
        } catch (FenixServiceException e) {
            fail("Creating a new GrantContract successfull " + e);
        } catch (Exception e) {
            fail("Creating a new GrantContract successfull " + e);
        }
    }
    /*
     * Grant Contract Creation Unsuccessfull: existing grant part
     */
    //        public void testCreateGrantContractUnsuccessfull() {
    //            try {
    //                String[] args = getAuthenticatedAndAuthorizedUser();
    //                IUserView id = authenticateUser(args);
    //                Object[] args2 = getUnauthorizeArguments(false);
    //    
    //                ServiceManagerServiceFactory.executeService(id,
    //                        getNameOfServiceToBeTested(), args2);
    //    
    //            } catch (ExistingServiceException e) {
    //                compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
    //                System.out
    //                        .println(getNameOfServiceToBeTested()
    //                                + " was SUCCESSFULY runned by test:
    // testCreateGrantContractUnsuccessfull");
    //            } catch (FenixServiceException e) {
    //                fail("Creating a new GrantContract unsuccessfull " + e);
    //            } catch (Exception e) {
    //                fail("Creating a new GrantContract unsuccessfull " + e);
    //            }
    //        }
    /*
     * Grant Contract Edition Successfull
     */
    //    public void testEditGrantContractSuccessfull() {
    //        try {
    //            String[] args = getAuthenticatedAndAuthorizedUser();
    //            IUserView id = authenticateUser(args);
    //            Object[] args2 = getAuthorizeArgumentsEdit();
    //
    //            ServiceManagerServiceFactory.executeService(id,
    //                    getNameOfServiceToBeTested(), args2);
    //
    //            compareDataSetUsingExceptedDataSetTableColumns(getExpectedEditDataSetFilePath());
    //            System.out
    //                    .println(getNameOfServiceToBeTested()
    //                            + " was SUCCESSFULY runned by test: testEditGrantContractSuccessfull");
    //        } catch (FenixServiceException e) {
    //            fail("Editing a GrantContract successfull " + e);
    //        } catch (Exception e) {
    //            fail("Editing a GrantContract successfull " + e);
    //        }
    //    }
    /*
     * Grant Contract Edition Unsuccessfull: invalid values
     */
    //        public void testEditGrantContractUnsuccessfull() {
    //            try {
    //                String[] args = getAuthenticatedAndAuthorizedUser();
    //                IUserView id = authenticateUser(args);
    //                Object[] args2 = getUnauthorizeArguments(true);
    //    
    //                ServiceManagerServiceFactory.executeService(id,
    //                        getNameOfServiceToBeTested(), args2);
    //            } catch (ExistingServiceException e) {
    //                compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
    //                System.out
    //                        .println(getNameOfServiceToBeTested()
    //                                + " was SUCCESSFULY runned by test: testEditGrantContractUnsuccessfull");
    //            } catch (FenixServiceException e) {
    //                fail("Editing a GrantContract unsuccessfull " + e);
    //            } catch (Exception e) {
    //                fail("Editing a GrantContract unsuccessfull " + e);
    //            }
    //        }
}