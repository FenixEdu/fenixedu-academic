package ServidorAplicacao.Servicos.teacher;

import DataBeans.InfoCurriculum;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 *  
 */
public class EditEvaluationTest extends ServiceNeedsAuthenticationTestCase
{

    /**
	 * @param testName
	 */
    public EditEvaluationTest(String testName)
    {
        super(testName);
    }

    /**
	 * @see ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices#getNameOfServiceToBeTested()
	 */
    protected String getNameOfServiceToBeTested()
    {
        return "EditEvaluation";
    }

    protected String getDataSetFilePath()
    {
        return "etc/datasets/servicos/teacher/testEditEvaluationMethodDataSet.xml";
    }

    protected String getExpectedDataSetFilePath()
    {
        return "etc/datasets/servicos/teacher/testExpectedEditEvaluationMethodDataSet.xml";
    }

    protected String getExpectedNewCurriculumDataSetFilePath()
    {
        return "etc/datasets/servicos/teacher/testExpectedNewCurriculumEvaluatioMethodDataSet.xml";
    }

    protected String[] getAuthenticatedAndAuthorizedUser()
    {

        String[] args = { "user", "pass", getApplication()};
        return args;
    }

    protected String[] getAuthenticatedAndUnauthorizedUser()
    {

        String[] args = { "julia", "pass", getApplication()};
        return args;
    }

    protected String[] getNotAuthenticatedUser()
    {

        String[] args = { "fiado", "pass", getApplication()};
        return args;
    }

    protected Object[] getAuthorizeArguments()
    {

        Integer executionCourseCode = new Integer(24);
        Integer curricularCourseCode = new Integer(14);

        InfoCurriculum infoCurriculum = new InfoCurriculum();

        infoCurriculum.setIdInternal(curricularCourseCode);

        Object[] args = { executionCourseCode, curricularCourseCode, infoCurriculum };

        return args;
    }

    protected Object[] getTestEvaluationMethodSuccessfullArguments()
    {

        Integer executionCourseCode = new Integer(24);
        Integer curricularCourseCode = new Integer(14);

        InfoCurriculum infoCurriculum = new InfoCurriculum();

        infoCurriculum.setIdInternal(curricularCourseCode);

        Object[] args = { executionCourseCode, curricularCourseCode, infoCurriculum };

        return args;
    }

    protected Object[] getTestEvaluationMethodUnsuccessfullArguments()
    {

        Integer executionCourseCode = new Integer(24);
        Integer curricularCourseCode = new Integer(123);

        InfoCurriculum infoCurriculum = new InfoCurriculum();

        infoCurriculum.setIdInternal(curricularCourseCode);

        Object[] args = { executionCourseCode, curricularCourseCode, infoCurriculum };
        return args;
    }

    protected Object[] getTestEvaluationMethodCurricularCourseWithNoCurriculumArguments()
    {

        Integer executionCourseCode = new Integer(26);
        Integer curricularCourseCode = new Integer(16);

        InfoCurriculum infoCurriculum = new InfoCurriculum();

        infoCurriculum.setIdInternal(curricularCourseCode);

        Object[] args = { executionCourseCode, curricularCourseCode, infoCurriculum };
        return args;
    }

    protected String getApplication()
    {
        return Autenticacao.EXTRANET;
    }

    public void testEditEvaluationMethod()
    {

        try
        {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView userView = authenticateUser(args);

            gestor.executar(userView, getNameOfServiceToBeTested(), getAuthorizeArguments());

            // verificar as alteracoes da bd
            compareDataSet(getExpectedDataSetFilePath());

        } catch (FenixServiceException ex)
        {
            fail("EditEvaluationTest" + ex);
        } catch (Exception ex)
        {
            fail("EditEvaluationTest error on compareDataSet" + ex);
        }
    }

    public void testEditEvaluationMethodWithNoCurriculum()
    {

        System.out.println("Starting: testSuccessfullEditEvaluationMethodWithNoCurriculum");
        try
        {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView userView = authenticateUser(args);

            gestor.executar(
                userView,
                getNameOfServiceToBeTested(),
                getTestEvaluationMethodCurricularCourseWithNoCurriculumArguments());

            // verificar as alteracoes da bd
            compareDataSet(getExpectedNewCurriculumDataSetFilePath());

            System.out.println("Finished: testSuccessfullEditEvaluationMethodWithNoCurriculum");
        } catch (FenixServiceException ex)
        {
            fail("EditEvaluationTest" + ex);
        } catch (Exception ex)
        {
            fail("EditEvaluationTest error on compareDataSet" + ex);
        }
    }

    public void testEvaluationMethodNotBelongsExecutionCourse()
    {

        Object serviceArguments[] = getTestEvaluationMethodUnsuccessfullArguments();

        try
        {
            gestor.executar(userView, getNameOfServiceToBeTested(), serviceArguments);
            fail(getNameOfServiceToBeTested() + "fail testEvaluationMethodNotBelongsExecutionCourse");
        } catch (NotAuthorizedException ex)
        {

            System.out.println(
                "testEvaluationMethodNotBelongsExecutionCourse was SUCCESSFULY runned by service: "
                    + getNameOfServiceToBeTested());
        } catch (Exception ex)
        {
            fail(getNameOfServiceToBeTested() + "fail testEvaluationMethodNotBelongsExecutionCourse");
        }
    }
}