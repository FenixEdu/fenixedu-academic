/*
 * Created on 29/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servicos.manager;

import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servicos.TestCaseReadServices;

/**
 * @author lmac1
 */

public abstract class TestCaseManagerReadServices extends TestCaseReadServices {

    public TestCaseManagerReadServices(String testName) {
        super(testName);
    }

    /*
     * (non-Javadoc) read non-existing object
     */
    public void testUnsuccessfulExecutionOfReadService() {

        Object[] args = getArgumentsOfServiceToBeTestedUnsuccessfuly();

        if (args != null) {

            //			Object result; result =
            try {
                ServiceManagerServiceFactory.executeService(_userView, getNameOfServiceToBeTested(),
                        args);

            } catch (NonExistingServiceException ex) {
                System.out
                        .println("testUnsuccessfulExecutionOfReadService was SUCCESSFULY runned by class: "
                                + this.getClass().getName());
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out
                        .println("testUnSuccessfulExecutionOfReadService was UNSUCCESSFULY runned by class: "
                                + this.getClass().getName());
                fail("testUnSuccessfulExecutionOfReadService");
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseReadServices#getArgumentsOfServiceToBeTestedUnsuccessfuly()
     */
    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseReadServices#getArgumentsOfServiceToBeTestedSuccessfuly()
     */
    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseReadServices#getNumberOfItemsToRetrieve()
     */
    protected int getNumberOfItemsToRetrieve() {
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseReadServices#getObjectToCompare()
     */
    protected Object getObjectToCompare() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseServicos#getArgsForAuthorizedUser()
     */
    protected String[] getArgsForAuthorizedUser() {
        return new String[] { "manager", "pass", getApplication() };
    }

    protected boolean needsAuthorization() {
        return true;
    }

    protected abstract String getNameOfServiceToBeTested();

}