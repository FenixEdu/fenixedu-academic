package net.sourceforge.fenixedu.applicationTier.Servicos;

import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

/**
 * @author dcs-rjao at 21/Fev/2003
 * 
 * Created on 24/Fev/2003
 */
abstract public class TestCaseNeedAuthorizationServices extends TestCaseServicos {

    public TestCaseNeedAuthorizationServices(String testName) {
        super(testName);
    }

    protected void setUp() {
        super.setUp();
    }

    protected void tearDown() {
        super.tearDown();
    }

    public void testUnauthorizedExecutionOfService() {

        Object serviceArguments[] = { null };

        Object result = null;

        if (needsAuthorization()) {
            try {
                result = ServiceManagerServiceFactory.executeService(_userView2,
                        getNameOfServiceToBeTested(), serviceArguments);
                System.out
                        .println("testUnauthorizedExecutionOfService was UNSUCCESSFULY runned by class: "
                                + this.getClass().getName());
                fail(this.getClass().getName()
                        + " : testUnauthorizedExecutionOfService - Service Name: "
                        + getNameOfServiceToBeTested());
            } catch (Exception ex) {
                assertNull(this.getClass().getName() + " : testUnauthorizedExecutionOfService", result);
                System.out
                        .println("testUnauthorizedExecutionOfService was SUCCESSFULY runned by class: "
                                + this.getClass().getName());
            }
        }
    }

    /**
     * This method must return a String with the name of the service to be
     * tested.
     */
    protected abstract String getNameOfServiceToBeTested();

    /**
     * This method must return 'true' if the service needs authorization to be
     * runned and 'false' otherwise.
     */
    protected abstract boolean needsAuthorization();
}