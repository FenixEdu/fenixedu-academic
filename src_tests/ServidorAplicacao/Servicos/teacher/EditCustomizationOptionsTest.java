package net.sourceforge.fenixedu.applicationTier.Servicos.teacher;

import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.dataTransferObject.InfoSite;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.Autenticacao;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.Servicos.ServiceNeedsAuthenticationTestCase;

/**
 * @author Barbosa
 * @author Pica
 */
public class EditCustomizationOptionsTest extends ServiceNeedsAuthenticationTestCase {
    /**
     * @param testName
     */
    public EditCustomizationOptionsTest(java.lang.String testName) {
        super(testName);
    }

    protected String getNameOfServiceToBeTested() {
        return "EditCustomizationOptions";
    }

    protected String getDataSetFilePath() {
        return "etc/datasets/servicos/teacher/testEditCustomizationOptionsDataSet.xml";
    }

    protected String getExpectedDataSetFilePath() {
        return "etc/datasets/servicos/teacher/testExpectedEditCustomizationOptionsDataSet.xml";
    }

    protected String getExpectedUnsucesfullDataSetFilePath() {
        return "etc/datasets/servicos/teacher/testExpectedEditCustomizationOptionsUnsuccefullDataSet.xml";
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
        InfoSite infoSiteNew = new InfoSite();
        infoSiteNew.setAlternativeSite("site");
        infoSiteNew.setMail("mail");
        infoSiteNew.setInitialStatement("inicio");
        infoSiteNew.setIntroduction("introducao");

        Object[] args = { infoExecutionCourseCode, infoSiteNew };
        return args;
    }

    /**
     * 
     * Start of the tests
     *  
     */

    /**
     * Edit Customization Option Succefully
     */
    public void testEditCustomizationOptionsSuccefull() {
        try {
            Integer infoExecutionCourseCode = new Integer(24);
            InfoSite infoSiteNew = new InfoSite();
            infoSiteNew.setAlternativeSite("site");
            infoSiteNew.setMail("mail");
            Object[] argserv = { infoExecutionCourseCode, infoSiteNew };

            IUserView arguser = authenticateUser(getAuthenticatedAndAuthorizedUser());

            ServiceManagerServiceFactory.executeService(arguser, getNameOfServiceToBeTested(), argserv);

            //Verify the changes
            compareDataSetUsingExceptedDataSetTablesAndColumns(getExpectedDataSetFilePath());

        } catch (NotAuthorizedException ex) {
            fail("Editing Customization Options of a Site " + ex);
        } catch (FenixServiceException ex) {
            fail("Editing Customization Options of a Site " + ex);
        } catch (Exception ex) {
            fail("Editing Customization Options of a Site " + ex);
        }
    }

    /**
     * The execution course doesn't exist, the edit customization operation
     * cannot be made
     */
    public void testEditCustomizationOptionsOfInvalidCourse() {
        try {
            Integer infoExecutionCourseCode = new Integer(34343434);
            InfoSite infoSiteNew = new InfoSite();
            infoSiteNew.setAlternativeSite("site");
            infoSiteNew.setMail("mail");
            infoSiteNew.setInitialStatement("inicio");
            infoSiteNew.setIntroduction("introducao");
            Object[] argserv = { infoExecutionCourseCode, infoSiteNew };

            IUserView arguser = authenticateUser(getAuthenticatedAndAuthorizedUser());

            //Run the service
            ServiceManagerServiceFactory.executeService(arguser, getNameOfServiceToBeTested(), argserv);

        } catch (NotAuthorizedException ex) {
            /*
             * The filter returns an exception. No changes are made in the DB
             */
            compareDataSetUsingExceptedDataSetTableColumns(getExpectedUnsucesfullDataSetFilePath());
        } catch (FenixServiceException ex) {
            fail("Editing Customization Options of a Site " + ex);
        } catch (Exception ex) {
            fail("Editing Customization Options of a Site " + ex);
        }
    }
}