/*
 * Created on 7/Out/2003
 *  
 */
package ServidorAplicacao.Servicos;

import junit.framework.AssertionFailedError;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Luis Egidio, lmre@mega.ist.utl.pt Nuno Ochoa, nmgo@mega.ist.utl.pt
 *  
 */
public abstract class ServiceNeedsAuthenticationTestCase extends
        ServiceTestCase {

    protected IUserView userView = null;

    protected IUserView userView2 = null;

    protected IUserView userView3 = null;

    protected ServiceNeedsAuthenticationTestCase(String name) {
        super(name);
    }

    protected void setUp() {
        super.setUp();
        this.userView = authenticateUser(getAuthenticatedAndAuthorizedUser());
        this.userView2 = authenticateUser(getAuthenticatedAndUnauthorizedUser());
        this.userView3 = authenticateUser(getNotAuthenticatedUser());

    }

    public void testAuthorizedUser() {
        Object serviceArguments[] = getAuthorizeArguments();

        try {
            Object result = ServiceManagerServiceFactory.executeService(
                    this.userView, getNameOfServiceToBeTested(),
                    serviceArguments);
            assertAuthorizedResult(result);
        } catch (FenixServiceException ex) {
            ex.printStackTrace();
            System.out
                    .println("testNonAuthenticatedUser was UNSUCCESSFULY runned by service: "
                            + getNameOfServiceToBeTested());
            fail("Unable to run service: " + getNameOfServiceToBeTested());
        } catch (AssertionFailedError ex) {
            fail(ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Test class:" + this.getClass().getName()
                    + "Unable to run service: " + getNameOfServiceToBeTested());
        }
    }

    public void testUnauthorizedUser() {
        Object serviceArguments[] = getAuthorizeArguments();
        try {
            ServiceManagerServiceFactory.executeService(this.userView2,
                    getNameOfServiceToBeTested(), serviceArguments);
            fail(this.getClass().getName() + ": Service "
                    + getNameOfServiceToBeTested()
                    + ": fail testUnauthorizedUser");
        } catch (NotAuthorizedException ex) {
            // sucessfull service execution
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Test class:" + this.getClass().getName()
                    + "Unable to run service: " + getNameOfServiceToBeTested());
        }
    }

    /**
     * This method by default does nothing. This should be used to assert the
     * result from an authorized service call.
     * 
     * @param result
     *            Represents the result from service execution.
     */
    protected void assertAuthorizedResult(Object result) {
    }

   public void testNonAuthenticatedUser() {
        Object serviceArguments[] = getAuthorizeArguments();

        try {
            ServiceManagerServiceFactory.executeService(this.userView3,
                    getNameOfServiceToBeTested(), serviceArguments);
            fail(this.getClass().getName() + ": Service "
                    + getNameOfServiceToBeTested()
                    + "fail testNonAuthenticatedUser");
        } catch (NotAuthorizedException ex) {
            // sucessfull service execution
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Unable to run service: " + getNameOfServiceToBeTested());
        }
    }

    protected IUserView authenticateUser(String[] arguments) {
        SuportePersistenteOJB.resetInstance();
        String args[] = arguments;

        try {
            return (IUserView) ServiceManagerServiceFactory.executeService(
                    null, "Autenticacao", args);
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Authenticating User!" + ex);
            return null;
        }
    }

    protected abstract String getNameOfServiceToBeTested();

    protected abstract String getDataSetFilePath();

    protected abstract String[] getAuthenticatedAndAuthorizedUser();

    protected abstract String[] getAuthenticatedAndUnauthorizedUser();

    protected abstract String[] getNotAuthenticatedUser();

    protected abstract Object[] getAuthorizeArguments();

    protected abstract String getApplication();
}