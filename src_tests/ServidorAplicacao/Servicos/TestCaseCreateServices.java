package net.sourceforge.fenixedu.applicationTier.Servicos;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

/**
 * @author dcs-rjao
 * 
 * Created on 24/Fev/2003
 */
public abstract class TestCaseCreateServices extends TestCaseNeedAuthorizationServices {

    public TestCaseCreateServices(String testName) {
        super(testName);
    }

    protected void setUp() {
        super.setUp();
    }

    protected void tearDown() {
        super.tearDown();
    }

    //	create existing object with several situations
    public void testUnsuccessfulExecutionsOfCreateService() {

        HashMap hashMap = getArgumentListOfServiceToBeTestedUnsuccessfuly();
        if (hashMap != null) {

            Object result = null;
            Set keys = hashMap.keySet();
            Iterator keysIterator = keys.iterator();

            while (keysIterator.hasNext()) {

                String key = (String) keysIterator.next();
                List listArgs = (List) hashMap.get(key);
                Object[] args = listArgs.toArray();

                try {
                    result = ServiceManagerServiceFactory.executeService(_userView,
                            getNameOfServiceToBeTested(), args);
                    System.out.println("testUnsuccessfulExecutionsOfCreateService [" + key
                            + "] was UNSUCCESSFULY run by class: " + this.getClass().getName());
                    fail("testUnsuccessfulExecutionsOfCreateService");
                } catch (Exception ex) {
                    assertNull("testUnsuccessfulExecutionsOfCreateService", result);
                    System.out.println("EXCEPCAO" + ex);
                    System.out.println("testUnsuccessfulExecutionsOfCreateService [" + key
                            + "] was SUCCESSFULY run by class: " + this.getClass().getName());
                }
            }
        }
    }

    //	create existing object
    public void testUnsuccessfulExecutionOfCreateService() {

        Object[] args = getArgumentsOfServiceToBeTestedUnsuccessfuly();

        if (args != null) {
            Object result = null;
            try {
                result = ServiceManagerServiceFactory.executeService(_userView,
                        getNameOfServiceToBeTested(), args);
                System.out
                        .println("testUnsuccessfulExecutionOfCreateService was UNSUCCESSFULY run by class: "
                                + this.getClass().getName());
                fail("testUnsuccessfulExecutionOfCreateService");
            } catch (Exception ex) {
                assertNull("testUnsuccessfulExecutionOfCreateService", result);
                System.out
                        .println("testUnsuccessfulExecutionOfCreateService was SUCCESSFULY run by class: "
                                + this.getClass().getName());
            }
        }
    }

    //	create non-existing object
    public void testSuccessfulExecutionOfCreateService() {

        Object[] args = getArgumentsOfServiceToBeTestedSuccessfuly();

        if (args != null) {
            Object result = null;
            try {

                result = ServiceManagerServiceFactory.executeService(_userView,
                        getNameOfServiceToBeTested(), args);
                assertEquals("testSuccessfulExecutionOfCreateService", Boolean.TRUE.booleanValue(),
                        ((Boolean) result).booleanValue());
                System.out
                        .println("testSuccessfulExecutionOfCreateService was SUCCESSFULY run by class: "
                                + this.getClass().getName());
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out
                        .println("testSuccessfulExecutionOfCreateService was UNSUCCESSFULY run by class: "
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
    protected abstract Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly();

    /**
     * This method must return the service arguments that makes it execute
     * correctly. This method must return null if not to be used.
     */
    protected abstract Object[] getArgumentsOfServiceToBeTestedSuccessfuly();

    /**
     * This method must return the service arguments that makes it fail it's
     * execution. This method is to be called when it is intended to test
     * multiple situations of failure. This method must return null if not to be
     * used.
     */
    protected abstract HashMap getArgumentListOfServiceToBeTestedUnsuccessfuly();

    /**
     * This method must return 'true' if the service needs authorization to be
     * runned and 'false' otherwise.
     */
    protected boolean needsAuthorization() {
        return true;
    }

}