/*
 * Created on 3/Set/2003
 */
package ServidorAplicacao.Servicos.manager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import framework.factory.ServiceManagerServiceFactory;

import ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices;

/**
 * @author lmac1
 */
public abstract class TestCaseManagerInsertAndEditServices extends TestCaseNeedAuthorizationServices {

    public TestCaseManagerInsertAndEditServices(String testName) {
        super(testName);
    }

    //	test unsucessful execution of service in several situations
    public void testUnsuccessfulExecutionsOfService() {

        HashMap hashMap = getArgumentListOfServiceToBeTestedUnsuccessfuly();
        if (hashMap != null) {

            Set keys = hashMap.keySet();
            Iterator keysIterator = keys.iterator();

            while (keysIterator.hasNext()) {

                String key = (String) keysIterator.next();
                List listArgs = (List) hashMap.get(key);
                Object[] args = listArgs.toArray();

                try {
                    ServiceManagerServiceFactory.executeService(_userView, getNameOfServiceToBeTested(),
                            args);
                    System.out.println("testUnsuccessfulExecutionsOfService [" + key
                            + "] was UNSUCCESSFULY run by class: " + this.getClass().getName());
                    fail("testUnsuccessfulExecutionsOfService");
                } catch (Exception ex) {
                    System.out.println("EXCEPCAO" + ex);
                    System.out.println("testUnsuccessfulExecutionsOfService [" + key
                            + "] was SUCCESSFULY run by class: " + this.getClass().getName());
                }
            }
        }
    }

    public void testUnsuccessfulExecutionOfService() {

        Object[] args = getArgumentsOfServiceToBeTestedUnsuccessfuly();

        if (args != null) {

            try {
                ServiceManagerServiceFactory.executeService(_userView, getNameOfServiceToBeTested(),
                        args);
                System.out.println("testUnsuccessfulExecutionOfService was UNSUCCESSFULY run by class: "
                        + this.getClass().getName());
                fail("testUnsuccessfulExecutionOfService");
            } catch (Exception ex) {
                System.out.println("testUnsuccessfulExecutionOfService was SUCCESSFULY run by class: "
                        + this.getClass().getName());
            }
        }
    }

    public void testSuccessfulExecutionOfService() {

        Object[] args = getArgumentsOfServiceToBeTestedSuccessfuly();

        if (args != null) {

            try {
                ServiceManagerServiceFactory.executeService(_userView, getNameOfServiceToBeTested(),
                        args);
                System.out.println("testSuccessfulExecutionOfService was SUCCESSFULY run by class: "
                        + this.getClass().getName());
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("testSuccessfulExecutionOfService was UNSUCCESSFULY run by class: "
                        + this.getClass().getName());
                fail("testSuccessfulExecutionOfCreateService");
            }
        }
    }

    /**
     * This method must return a String with the name of the service to be
     * tested.
     */
    protected abstract String getNameOfServiceToBeTested();

    /**
     * This method must return the service arguments that makes it fail it's
     * execution. This method is to be called when it is intended to test a
     * single situation of failure. This method must return null if not to be
     * used.
     */
    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
        return null;
    }

    /**
     * This method must return the service arguments that makes it execute
     * correctly. This method must return null if not to be used.
     */
    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
        return null;
    }

    /**
     * This method must return the service arguments that makes it fail it's
     * execution. This method is to be called when it is intended to test
     * multiple situations of failure. This method must return null if not to be
     * used.
     */
    protected HashMap getArgumentListOfServiceToBeTestedUnsuccessfuly() {
        return null;
    }

    /**
     * This method must return 'true' if the service needs authorization to be
     * runned and 'false' otherwise.
     */
    protected boolean needsAuthorization() {
        return true;
    }

    protected String[] getArgsForAuthorizedUser() {
        return new String[] { "manager", "pass", getApplication() };
    }
}