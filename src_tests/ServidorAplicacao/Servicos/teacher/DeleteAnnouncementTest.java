package net.sourceforge.fenixedu.applicationTier.Servicos.teacher;

import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.Autenticacao;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;

/**
 * @author Barbosa
 * @author Pica
 */
public class DeleteAnnouncementTest extends AnnouncementBelongsToExecutionCourseTest {

    /**
     * @param testName
     */
    public DeleteAnnouncementTest(java.lang.String testName) {
        super(testName);
    }

    protected String getNameOfServiceToBeTested() {
        return "DeleteAnnouncementService";
    }

    protected String getDataSetFilePath() {
        return "etc/datasets/servicos/teacher/testDeleteAnnouncementDataSet.xml";
    }

    protected String getExpectedDataSetFilePath() {
        return "etc/datasets/servicos/teacher/testExpectedDeleteAnnouncementDataSet.xml";
    }

    protected String getExpectedUnsuccessfullDataSetFilePath() {
        return "etc/datasets/servicos/teacher/testExpectedDeleteAnnouncementUnsuccefullDataSet.xml";
    }

    /*
     * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getApplication()
     */
    protected String getApplication() {
        return Autenticacao.EXTRANET;
    }

    /*
     * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getAuthorizedUser()
     */
    protected String[] getAuthenticatedAndAuthorizedUser() {
        String[] args = { "user", "pass", getApplication() };
        return args;
    }

    /*
     * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getUnauthorizedUser()
     */
    protected String[] getAuthenticatedAndUnauthorizedUser() {
        String[] args = { "julia", "pass", getApplication() };
        return args;
    }

    /*
     * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getNonTeacherUser()
     */
    protected String[] getNotAuthenticatedUser() {
        String[] args = { "jccm", "pass", getApplication() };
        return args;
    }

    /*
     * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getAuthorizeArguments()
     */
    protected Object[] getAuthorizeArguments() {
        Integer infoExecutionCourseCode = new Integer(24);
        Integer announcementCode = new Integer(2);

        Object[] args = { infoExecutionCourseCode, announcementCode };
        return args;
    }

    /*
     * @see ServidorAplicacao.Servicos.teacher.AnnouncementBelongsToExecutionCourseTest#getAnnouncementUnsuccessfullArguments()
     */
    protected Object[] getAnnouncementUnsuccessfullArguments() {
        Integer infoExecutionCourseCode = new Integer(24);
        Integer announcementCode = new Integer(3);

        Object[] args = { infoExecutionCourseCode, announcementCode };
        return args;
    }

    /**
     * 
     * Start of the tests
     *  
     */

    /**
     * Delete and announcement successfully
     */
    public void testDeleteAnnouncementSuccefull() {
        try {
            Integer infoExecutionCourseCode = new Integer(24);
            Integer announcementCode = new Integer(1);
            Object[] argserv = { infoExecutionCourseCode, announcementCode };

            IUserView arguser = authenticateUser(getAuthenticatedAndAuthorizedUser());

            //Run the service
            ServiceManagerServiceFactory.executeService(arguser, getNameOfServiceToBeTested(), argserv);

            //Verify if the announcement was correctly deleted
            compareDataSetUsingExceptedDataSetTablesAndColumns(getExpectedDataSetFilePath());

        } catch (NotAuthorizedException ex) {
            fail("Deleting an announcement of a Site " + ex);
        } catch (FenixServiceException ex) {
            fail("Deleting an announcement of a Site " + ex);
        } catch (Exception ex) {
            fail("Deleting an announcument of a Site " + ex);
        }
    }

    /**
     * The announcement to be deleted doesn't exist
     */
    public void testDeleteAnnouncementUnsuccefull() {
        try {
            Integer infoExecutionCourseCode = new Integer(24);
            Integer announcementCode = new Integer(12121212);
            Object[] argserv = { infoExecutionCourseCode, announcementCode };

            IUserView arguser = authenticateUser(getAuthenticatedAndAuthorizedUser());

            //Run the service
            ServiceManagerServiceFactory.executeService(arguser, getNameOfServiceToBeTested(), argserv);

        } catch (NotAuthorizedException ex) {
            /*
             * The filter verifies that the announcement doesn't exist and
             * returns and exception
             */
            compareDataSetUsingExceptedDataSetTablesAndColumns(getExpectedUnsuccessfullDataSetFilePath());
        } catch (FenixServiceException ex) {
            fail("Deleting an announcement of a Site " + ex);
        } catch (Exception ex) {
            fail("Deleting an announcument of a Site " + ex);
        }
    }
}