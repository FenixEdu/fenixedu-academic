/*
 * Created on 15/Out/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servicos.teacher;

import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.Servicos.ServiceNeedsAuthenticationTestCase;

/**
 * @author Ricardo Rodrigues
 * @author Nuno Correia
 *  
 */
public abstract class ProgramBelongsExecutionCourse extends ServiceNeedsAuthenticationTestCase {

    /**
     * @param name
     */
    public ProgramBelongsExecutionCourse(String name) {
        super(name);
    }

    public void testProgramExecutionCourse() {

        Object serviceArguments[] = getTestProgramSuccessfullArguments();

        //		Object result = null;

        try {
            //			result =
            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(),
                    serviceArguments);
            System.out.println("testProgramBelongsExecutionCourse was SUCCESSFULY runned by service: "
                    + getNameOfServiceToBeTested());

        } catch (NotAuthorizedException ex) {
            fail(getNameOfServiceToBeTested() + "fail testProgramBelongsExecutionCourse");
        } catch (Exception ex) {
            fail(getNameOfServiceToBeTested() + "fail testProgramBelongsExecutionCourse");
        }
    }

    public void testProgramNotBelongsExecutionCourse() {

        Object serviceArguments[] = getTestProgramUnsuccessfullArguments();

        try {
            //			result =
            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(),
                    serviceArguments);
            fail(getNameOfServiceToBeTested() + "fail testProgramNotBelongsExecutionCourse");
        } catch (NotAuthorizedException ex) {

            System.out
                    .println("testProgramNotBelongsExecutionCourse was SUCCESSFULY runned by service: "
                            + getNameOfServiceToBeTested());
        } catch (Exception ex) {
            fail(getNameOfServiceToBeTested() + "fail testProgramNotBelongsExecutionCourse");
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

    protected abstract Object[] getTestProgramSuccessfullArguments();

    protected abstract Object[] getTestProgramUnsuccessfullArguments();

}