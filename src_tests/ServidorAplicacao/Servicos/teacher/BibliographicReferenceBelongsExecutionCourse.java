/*
 * Created on 15/Out/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servicos.teacher;

import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.Servicos.ServiceNeedsAuthenticationTestCase;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 *  
 */
public abstract class BibliographicReferenceBelongsExecutionCourse extends
        ServiceNeedsAuthenticationTestCase {

    public BibliographicReferenceBelongsExecutionCourse(String name) {
        super(name);
    }

    public void testBibliographicReferenceBelongsExecutionCourse() {

        Object serviceArguments[] = getTestBibliographicReferenceSuccessfullArguments();

        try {
            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(),
                    serviceArguments);
            System.out
                    .println("testBibliographicReferenceBelongsExecutionCourse was SUCCESSFULY runned by service: "
                            + getNameOfServiceToBeTested());

        } catch (NotAuthorizedException ex) {
            fail(getNameOfServiceToBeTested() + "fail testBibliographicReferenceBelongsExecutionCourse");
        } catch (Exception ex) {
            fail(getNameOfServiceToBeTested() + "fail testBibliographicReferenceBelongsExecutionCourse");
        }
    }

    public void testBibliographicReferenceNotBelongsExecutionCourse() {

        Object serviceArguments[] = getTestBibliographicReferenceUnsuccessfullArguments();

        try {
            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(),
                    serviceArguments);
            fail(getNameOfServiceToBeTested()
                    + "fail testBibliographicReferenceNotBelongsExecutionCourse");
        } catch (NotAuthorizedException ex) {

            System.out
                    .println("testBibliographicReferenceNotBelongsExecutionCourse was SUCCESSFULY runned by service: "
                            + getNameOfServiceToBeTested());
        } catch (Exception ex) {
            fail(getNameOfServiceToBeTested()
                    + "fail testBibliographicReferenceNotBelongsExecutionCourse");
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

    protected abstract Object[] getTestBibliographicReferenceSuccessfullArguments();

    protected abstract Object[] getTestBibliographicReferenceUnsuccessfullArguments();

}