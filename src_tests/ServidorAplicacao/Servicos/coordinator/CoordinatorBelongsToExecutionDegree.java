/*
 * Created on 6/Nov/2003
 *
 */
package ServidorAplicacao.Servicos.coordinator;

import framework.factory.ServiceManagerServiceFactory;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;

/**
 * fenix-head ServidorAplicacao.Servicos.coordinator
 * 
 * @author João Mota 6/Nov/2003
 *  
 */
public abstract class CoordinatorBelongsToExecutionDegree extends ServiceNeedsAuthenticationTestCase {

    /**
     * @param name
     */
    protected CoordinatorBelongsToExecutionDegree(String name) {
        super(name);
    }

    protected abstract String[] getAuthenticatedAndAuthorizedUser();

    protected abstract String[] getAuthenticatedAndUnauthorizedUser();

    protected abstract String[] getNotAuthenticatedUser();

    protected abstract String getNameOfServiceToBeTested();

    protected abstract Object[] getAuthorizeArguments();

    protected abstract Object[] getNonAuthorizeArguments();

    protected abstract String getDataSetFilePath();

    protected abstract String getApplication();

    protected void setUp() {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        //
    }

    public void testNonCoordinatorUser() {
        Object serviceArguments[] = getAuthorizeArguments();
        try {
            ServiceManagerServiceFactory.executeService(userView3, getNameOfServiceToBeTested(),
                    serviceArguments);
            System.out.println("testNonCoordinatorUser was UNSUCCESSFULY runned by service: "
                    + getNameOfServiceToBeTested());
            fail(getNameOfServiceToBeTested() + "fail testNonCoordinatorUser");

        } catch (NotAuthorizedException ex) {
            System.out.println("testNonCoordinatorUser was SUCCESSFULY runned by service: "
                    + getNameOfServiceToBeTested());
        } catch (Exception ex) {
            System.out.println("testNonCoordinatorUser was UNSUCCESSFULY runned by service: "
                    + getNameOfServiceToBeTested());
            fail("Unable to run service: " + getNameOfServiceToBeTested());
        }
    }

    public void testUnauthorizedUser() {
        Object serviceArguments[] = getAuthorizeArguments();
        try {
            ServiceManagerServiceFactory.executeService(userView2, getNameOfServiceToBeTested(),
                    serviceArguments);
            System.out.println("testUnauthorizedUser was UNSUCCESSFULY runned by service: "
                    + getNameOfServiceToBeTested());
            fail(getNameOfServiceToBeTested() + "fail testUnauthorizedUser");
        } catch (NotAuthorizedException ex) {
            System.out.println("testUnauthorizedUser was SUCCESSFULY runned by service: "
                    + getNameOfServiceToBeTested());
        } catch (Exception ex) {
            System.out.println("testUnauthorizedUser was UNSUCCESSFULY runned by service: "
                    + getNameOfServiceToBeTested());
            fail("Unable to run service: " + getNameOfServiceToBeTested());

        }
    }

    public void testCoordinatorNotBelongsToExecutionDegree() {
        Object serviceArguments[] = getNonAuthorizeArguments();
        try {
            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(),
                    serviceArguments);
            System.out
                    .println("testCoordinatorNotBelongsToExecutionDegree was UNSUCCESSFULY runned by service: "
                            + getNameOfServiceToBeTested());
            fail(getNameOfServiceToBeTested() + "fail testCoordinatorNotBelongsToExecutionDegree");
        } catch (NotAuthorizedException ex) {
            System.out
                    .println("testCoordinatorNotBelongsToExecutionDegree was SUCCESSFULY runned by service: "
                            + getNameOfServiceToBeTested());
        } catch (Exception ex) {
            System.out
                    .println("testCoordinatorNotBelongsToExecutionDegree was UNSUCCESSFULY runned by service: "
                            + getNameOfServiceToBeTested());
            fail("Unable to run service: " + getNameOfServiceToBeTested());

        }
    }

}