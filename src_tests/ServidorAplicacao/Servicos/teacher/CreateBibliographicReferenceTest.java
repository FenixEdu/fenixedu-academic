package net.sourceforge.fenixedu.applicationTier.Servicos.teacher;

import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.Autenticacao;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servicos.ServiceNeedsAuthenticationTestCase;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 *  
 */
public class CreateBibliographicReferenceTest extends ServiceNeedsAuthenticationTestCase {

    String author = "Shari Pfleeger";

    String title = "Software Engineering: Theory and Practice";

    String reference = "Recomended bibliography";

    String year = "2002";

    Boolean optional = new Boolean(false);

    /**
     * @param testName
     */
    public CreateBibliographicReferenceTest(String testName) {
        super(testName);
    }

    /**
     * @see ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices#getNameOfServiceToBeTested()
     */
    protected String getNameOfServiceToBeTested() {
        return "CreateBibliographicReference";
    }

    protected String getDataSetFilePath() {
        return "etc/datasets/servicos/teacher/testCreateBibliographicReferenceDataSet.xml";
    }

    protected String getRecomendedExpectedDataSetFilePath() {
        return "etc/datasets/servicos/teacher/testExpectedCreateRecomendedBibliographicReferenceDataSet.xml";
    }

    protected String getOptionalExpectedDataSetFilePath() {
        return "etc/datasets/servicos/teacher/testExpectedCreateOptionalBibliographicReferenceDataSet.xml";
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getAuthenticatedAndAuthorizedUser()
     */
    protected String[] getAuthenticatedAndAuthorizedUser() {
        String[] args = { "user", "pass", getApplication() };
        return args;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getAuthenticatedAndUnauthorizedUser()
     */
    protected String[] getAuthenticatedAndUnauthorizedUser() {
        String[] args = { "julia", "pass", getApplication() };
        return args;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getNotAuthenticatedUser()
     */
    protected String[] getNotAuthenticatedUser() {
        String[] args = { "fiado", "pass", getApplication() };
        return args;

    }

    protected Object[] getAuthorizeArguments() {

        Integer executionCourseCode = new Integer(24);

        Object[] args = { executionCourseCode, title, author, reference, year, optional };

        return args;
    }

    protected Object[] getTestBibliographicReferenceSuccessfullArguments() {

        Integer executionCourseCode = new Integer(24);

        Object[] args = { executionCourseCode, title, author, reference, year, optional };

        return args;
    }

    protected Object[] getTestBibliographicReferenceUnsuccessfullArguments() {

        Integer executionCourseCode = new Integer(242);

        Object[] args = { executionCourseCode, title, author, reference, year, optional };

        return args;
    }

    protected Object[] getTestExistingRecomendedBibliographicReferenceArguments() {

        Integer executionCourseCode = new Integer(24);
        author = "pedro";
        title = "xpto";
        reference = "ref";
        year = "2002";
        optional = new Boolean(false);

        Object[] args = { executionCourseCode, title, author, reference, year, optional };

        return args;
    }

    protected Object[] getTestExistingOptionalBibliographicReferenceArguments() {

        Integer executionCourseCode = new Integer(24);
        author = "rs";
        title = "ep";
        reference = "ref5";
        year = "2040";
        optional = new Boolean(true);

        Object[] args = { executionCourseCode, title, author, reference, year, optional };

        return args;
    }

    protected String getApplication() {
        return Autenticacao.EXTRANET;
    }

    public void testSuccessfullCreateRecomendedBibliographicReference() {

        try {

            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView userView = authenticateUser(args);

            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(),
                    getAuthorizeArguments());

            // verificar as alteracoes da bd
            compareDataSet(getRecomendedExpectedDataSetFilePath());
            System.out.println("testSuccessfullCreateRecomendedBibliographicReference "
                    + "was SUCCESSFULY runned by service: " + getNameOfServiceToBeTested());

        } catch (FenixServiceException ex) {
            fail("testSuccessfullCreateRecomendedBibliographicReference" + ex);
        } catch (Exception ex) {
            fail("testSuccessfullCreateRecomendedBibliographicReference error on compareDataSet" + ex);
        }
    }

    public void testUnuccessfullCreateExistingRecomendedBibliographicReference() {

        try {

            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView userView = authenticateUser(args);

            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(),
                    getTestExistingRecomendedBibliographicReferenceArguments());

            // verificar as alteracoes da bd
            //compareDataSet(getRecomendedExpectedDataSetFilePath());
            fail("testUnuccessfullCreateExistingRecomendedBibliographicReference: "
                    + "insertion of an existing bibliography");

        } catch (FenixServiceException ex) {
            System.out.println("testUnuccessfullCreateExistingRecomendedBibliographicReference "
                    + "was SUCCESSFULY runned by service: " + getNameOfServiceToBeTested());
        } catch (Exception ex) {
            fail("testUnuccessfullCreateExistingRecomendedBibliographicReference error on compareDataSet"
                    + ex);
        }
    }

    public void testSuccessfullCreateOptionalBibliographicReference() {

        author = "Shari Pfleeger";
        title = "Software Engineering: Theory and Practice";
        reference = "Optional bibliography";
        year = "2002";
        optional = new Boolean(true);

        try {

            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView userView = authenticateUser(args);

            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(),
                    getAuthorizeArguments());

            // verificar as alteracoes da bd
            compareDataSet(getOptionalExpectedDataSetFilePath());
            System.out.println("testSuccessfullCreateOptionalBibliographicReference "
                    + "was SUCCESSFULY runned by service: " + getNameOfServiceToBeTested());

        } catch (FenixServiceException ex) {
            fail("testSuccessfullCreateOptionalBibliographicReference" + ex);
        } catch (Exception ex) {
            fail("testSuccessfullCreateOptionalBibliographicReference error on compareDataSet" + ex);
        }
    }

    public void testUnsuccessfullCreateExistingOptionalBibliographicReference() {

        try {

            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView userView = authenticateUser(args);

            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(),
                    getTestExistingOptionalBibliographicReferenceArguments());

            // verificar as alteracoes da bd
            compareDataSet(getOptionalExpectedDataSetFilePath());
            fail("testUnsuccessfullCreateExistingOptionalBibliographicReference: "
                    + "insertion of an existing bibliography");

        } catch (FenixServiceException ex) {
            System.out.println("testUnsuccessfullCreateExistingOptionalBibliographicReference "
                    + "was SUCCESSFULY runned by service: " + getNameOfServiceToBeTested());
        } catch (Exception ex) {
            fail("testUnsuccessfullCreateExistingOptionalBibliographicReference error on compareDataSet"
                    + ex);
        }
    }

}