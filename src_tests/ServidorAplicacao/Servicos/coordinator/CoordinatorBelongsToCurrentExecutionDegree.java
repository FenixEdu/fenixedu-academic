/*
 * Created on 6/Nov/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servicos.coordinator;

import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;

/**
 * fenix-head ServidorAplicacao.Servicos.coordinator
 * 
 * @author João Mota 6/Nov/2003
 *  
 */
public abstract class CoordinatorBelongsToCurrentExecutionDegree extends
        CoordinatorBelongsToExecutionDegree {
    IUserView userView4 = null;

    /**
     * @param name
     */
    public CoordinatorBelongsToCurrentExecutionDegree(String name) {
        super(name);
    }

    protected void setUp() {
        super.setUp();
        userView4 = authenticateUser(getAuthenticatedCoordinatorUserOfNotCurrentExecutionDegree());
    }

    protected abstract String[] getAuthenticatedCoordinatorUserOfNotCurrentExecutionDegree();

    protected abstract String[] getAuthenticatedAndAuthorizedUser();

    protected abstract String[] getAuthenticatedAndUnauthorizedUser();

    protected abstract String[] getNotAuthenticatedUser();

    protected abstract String getNameOfServiceToBeTested();

    protected abstract Object[] getAuthorizeArguments();

    protected abstract Object[] getNonAuthorizeArguments();

    protected abstract Object[] getNonAuthorizeArgumentsForNotCurrentExecutionDegree();

    protected abstract String getDataSetFilePath();

    protected abstract String getApplication();

    public void testCoordinatorIsNotCoordinatorOfCurrentExecutionDegree() {
        Object serviceArguments[] = getNonAuthorizeArgumentsForNotCurrentExecutionDegree();
        try {
            ServiceManagerServiceFactory.executeService(userView4, getNameOfServiceToBeTested(),
                    serviceArguments);
            System.out
                    .println("testCoordinatorIsNotCoordinatorOfCurrentExecutionDegree was UNSUCCESSFULY runned by service: "
                            + getNameOfServiceToBeTested());
            fail(getNameOfServiceToBeTested()
                    + "fail testCoordinatorIsNotCoordinatorOfCurrentExecutionDegree");
        } catch (NotAuthorizedException ex) {
            System.out
                    .println("testCoordinatorIsNotCoordinatorOfCurrentExecutionDegree was SUCCESSFULY runned by service: "
                            + getNameOfServiceToBeTested());
        } catch (Exception ex) {
            System.out
                    .println("testCoordinatorIsNotCoordinatorOfCurrentExecutionDegree was UNSUCCESSFULY runned by service: "
                            + getNameOfServiceToBeTested());
            fail("Unable to run service: " + getNameOfServiceToBeTested());

        }
    }

}