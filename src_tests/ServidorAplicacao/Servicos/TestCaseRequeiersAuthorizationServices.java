/*
 * TestCaseRequeiersAuthorizationServices.java
 *
 * Created on 2003/04/06
 */

package net.sourceforge.fenixedu.applicationTier.Servicos;

import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

/**
 * @author Luis Cruz & Sara Ribeiro
 *  
 */
public abstract class TestCaseRequeiersAuthorizationServices extends TestCaseServices {

    public TestCaseRequeiersAuthorizationServices(String testName) {
        super(testName);
    }

    public void testUnauthorizedExecutionOfService() {
        result = null;
        try {
            result = ServiceManagerServiceFactory.executeService(userViewNotAuthorized,
                    getNameOfServiceToBeTested(), args);
            fail("Service was run with unauthorized user view.");
        } catch (Exception ex) {
            assertNull("Execution of Service with unauthorized user view returned a result: " + result,
                    result);
        }
    }

}