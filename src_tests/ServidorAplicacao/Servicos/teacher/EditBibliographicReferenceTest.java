package net.sourceforge.fenixedu.applicationTier.Servicos.teacher;

import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.Autenticacao;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.Servicos.ServiceNeedsAuthenticationTestCase;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 *  
 */
public class EditBibliographicReferenceTest extends ServiceNeedsAuthenticationTestCase {

    String author = "Shari Pfleeger";

    String title = "Software Engineering: Theory and Practice";

    String reference = "Recomended bibliografy";

    String year = "2002";

    Boolean optional = new Boolean(false);

    /**
     * @param testName
     */
    public EditBibliographicReferenceTest(String testName) {
        super(testName);
    }

    /**
     * @see ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices#getNameOfServiceToBeTested()
     */
    protected String getNameOfServiceToBeTested() {
        return "EditBibliographicReference";
    }

    protected String getDataSetFilePath() {
        return "etc/datasets/servicos/teacher/testEditBibliographicReferenceDataSet.xml";
    }

    protected String getRecomendedExpectedDataSetFilePath() {
        return "etc/datasets/servicos/teacher/testExpectedEditRecomendedBibliographicReferenceDataSet.xml";
    }

    protected String getOptionalExpectedDataSetFilePath() {
        return "etc/datasets/servicos/teacher/testExpectedEditOptionalBibliographicReferenceDataSet.xml";
    }

    protected String[] getAuthenticatedAndAuthorizedUser() {

        String[] args = { "user", "pass", getApplication() };
        return args;
    }

    protected String[] getAuthenticatedAndUnauthorizedUser() {

        String[] args = { "julia", "pass", getApplication() };
        return args;
    }

    protected String[] getNotAuthenticatedUser() {

        String[] args = { "fiado", "pass", getApplication() };
        return args;
    }

    protected Object[] getAuthorizeArguments() {

        Integer executionCourseCode = new Integer(24);
        Integer bibliographicReferenceCode = new Integer(1);

        Object[] args = { executionCourseCode, bibliographicReferenceCode, title, author, reference,
                year, optional };

        return args;
    }

    protected Object[] getTestBibliographicReferenceSuccessfullArguments() {

        Integer executionCourseCode = new Integer(24);
        Integer bibliographicReferenceCode = new Integer(1);

        Object[] args = { executionCourseCode, bibliographicReferenceCode, title, author, reference,
                year, optional };

        return args;
    }

    protected Object[] getTestBibliographicReferenceUnsuccessfullArguments() {

        Integer executionCourseCode = new Integer(24);
        Integer bibliographicReferenceCode = new Integer(123);

        Object[] args = { executionCourseCode, bibliographicReferenceCode, title, author, reference,
                year, optional };

        return args;
    }

    protected String getApplication() {
        return Autenticacao.EXTRANET;
    }

    public void testEditRecomendedBibliographicReferenceByAuthenticatedAndAuthorizedUser() {

        try {

            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView userView = authenticateUser(args);

            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(),
                    getAuthorizeArguments());

            // verificar as alteracoes da bd
            compareDataSet(getRecomendedExpectedDataSetFilePath());

        } catch (FenixServiceException ex) {
            fail("testSuccessfullEditRecomendedBibliographicReference" + ex);
        } catch (Exception ex) {
            fail("testSuccessfullEditRecomendedBibliographicReference error on compareDataSet" + ex);
        }
    }

    public void testEditOptionalBibliographicReferenceAuthenticatedAndAuthorizedUser() {

        try {
            author = "Shari Pfleeger";
            title = "Software Engineering: Theory and Practice";
            reference = "Optional bibliografy";
            year = "2002";
            optional = new Boolean(true);
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView userView = authenticateUser(args);

            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(),
                    getAuthorizeArguments());

            // verificar as alteracoes da bd
            compareDataSet(getOptionalExpectedDataSetFilePath());

        } catch (FenixServiceException ex) {
            fail("testSuccessfullEditOptionalBibliographicReference" + ex);
        } catch (Exception ex) {
            fail("testSuccessfullEditOptionalBibliographicReference error on compareDataSet" + ex);
        }
    }

    public void testEditBibliographicReferenceNotBelongsExecutionCourse() {

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

}