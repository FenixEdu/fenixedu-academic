/*
 * Created on 8/Out/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servicos.teacher;

import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.Servicos.ServiceNeedsAuthenticationTestCase;

/**
 * @author Leonor Almeida
 * @author Sérgio Montelobo
 *  
 */
public abstract class SummaryBelongsExecutionCourseTest extends ServiceNeedsAuthenticationTestCase {

    protected SummaryBelongsExecutionCourseTest(String name) {
        super(name);
    }

    public void testSummaryBelongsExecutionCourse() {

        Object serviceArguments[] = getAuthorizeArguments();

        try {
            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(),
                    serviceArguments);
            System.out.println("testSummaryBelongsExecutionCourse was SUCCESSFULY runned by service: "
                    + getNameOfServiceToBeTested());

        } catch (NotAuthorizedException ex) {
            fail(getNameOfServiceToBeTested() + " testSummaryBelongsExecutionCourse " + ex);
        } catch (Exception ex) {
            fail(getNameOfServiceToBeTested() + " testSummaryBelongsExecutionCourse " + ex);
        }
    }

    public void testSummaryNotBelongsExecutionCourse() {

        Object serviceArguments[] = getTestSummaryUnsuccessfullArguments();

        try {
            ServiceManagerServiceFactory.executeService(userView3, getNameOfServiceToBeTested(),
                    serviceArguments);
            fail(getNameOfServiceToBeTested() + " testSummaryNotBelongsExecutionCourse");
        } catch (NotAuthorizedException ex) {
            System.out.println("testItemNotBelongsExecutionCourse was SUCCESSFULY runned by service: "
                    + getNameOfServiceToBeTested());
        } catch (Exception ex) {
            fail(getNameOfServiceToBeTested() + " testSummaryNotBelongsExecutionCourse" + ex);
        }
    }

    protected abstract Object[] getAuthorizeArguments();

    protected abstract String[] getAuthenticatedAndAuthorizedUser();

    protected abstract String getDataSetFilePath();

    protected abstract String getNameOfServiceToBeTested();

    protected abstract String[] getNotAuthenticatedUser();

    protected abstract String[] getAuthenticatedAndUnauthorizedUser();

    protected abstract String getApplication();

    protected abstract Object[] getTestSummaryUnsuccessfullArguments();
}