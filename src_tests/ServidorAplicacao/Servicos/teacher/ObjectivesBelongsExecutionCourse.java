/*
 * Created on 15/Out/2003
 */
package ServidorAplicacao.Servicos.teacher;

import framework.factory.ServiceManagerServiceFactory;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 *  
 */
public abstract class ObjectivesBelongsExecutionCourse extends ServiceNeedsAuthenticationTestCase {

    /**
     * @param name
     */
    public ObjectivesBelongsExecutionCourse(String name) {
        super(name);
    }

    public void testObjectivesExecutionCourse() {

        Object serviceArguments[] = getTestObjectivesSuccessfullArguments();

        try {

            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(),
                    serviceArguments);
            System.out
                    .println("testObjectivesBelongsExecutionCourse was SUCCESSFULY runned by service: "
                            + getNameOfServiceToBeTested());

        } catch (NotAuthorizedException ex) {
            fail(getNameOfServiceToBeTested() + "fail testObjectivesBelongsExecutionCourse");
        } catch (Exception ex) {
            fail(getNameOfServiceToBeTested() + "fail testObjectivesBelongsExecutionCourse");
        }
    }

    public void testObjectivesNotBelongsExecutionCourse() {

        Object serviceArguments[] = getTestObjectivesUnsuccessfullArguments();

        try {
            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(),
                    serviceArguments);
            fail(getNameOfServiceToBeTested() + "fail testObjectivesNotBelongsExecutionCourse");
        } catch (NotAuthorizedException ex) {

            System.out
                    .println("testObjectivesNotBelongsExecutionCourse was SUCCESSFULY runned by service: "
                            + getNameOfServiceToBeTested());
        } catch (Exception ex) {
            fail(getNameOfServiceToBeTested() + "fail testObjectivesNotBelongsExecutionCourse");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getAuthorizedUser()
     */
    protected abstract String[] getAuthorizedUser();

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getUnauthorizedUser()
     */
    protected abstract String[] getUnauthorizedUser();

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getNonTeacherUser()
     */
    protected abstract String[] getNonTeacherUser();

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getNameOfServiceToBeTested()
     */
    protected abstract String getNameOfServiceToBeTested();

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getAuthorizeArguments()
     */
    protected abstract Object[] getAuthorizeArguments();

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.ServiceTestCase#getDataSetFilePath()
     */
    protected abstract String getDataSetFilePath();

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getApplication()
     */
    protected abstract String getApplication();

    protected abstract Object[] getTestObjectivesSuccessfullArguments();

    protected abstract Object[] getTestObjectivesUnsuccessfullArguments();

}