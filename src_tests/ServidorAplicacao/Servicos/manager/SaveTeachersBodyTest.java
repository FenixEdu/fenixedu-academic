/*
 * Created on 2/Out/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servicos.manager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

import net.sourceforge.fenixedu.applicationTier.Servicos.TestCaseNeedAuthorizationServices;

/**
 * @author lmac1
 */
public class SaveTeachersBodyTest extends TestCaseNeedAuthorizationServices {

    public SaveTeachersBodyTest(String testName) {
        super(testName);
    }

    public void testUnsuccessfulExecutionOfService() {

        //		Object[] args = getArgumentsOfServiceToBeTestedUnsuccessfuly();
        List responsibleTeachersIds = new ArrayList(1);
        List professorShipTeachersIds = null;

        responsibleTeachersIds.add(new Integer(2));

        Object[] args = { responsibleTeachersIds, professorShipTeachersIds, new Integer(100) };

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

        //		Object[] args = getArgumentsOfServiceToBeTestedSuccessfuly();
        List responsibleTeachersIds = new ArrayList(1);
        List professorShipTeachersIds = new ArrayList();

        responsibleTeachersIds.add(new Integer(2));

        Object[] args = { responsibleTeachersIds, professorShipTeachersIds, new Integer(24) };

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
    protected String getNameOfServiceToBeTested() {
        return "SaveTeachersBody";
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