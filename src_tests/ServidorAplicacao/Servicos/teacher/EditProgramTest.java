package ServidorAplicacao.Servicos.teacher;

import framework.factory.ServiceManagerServiceFactory;
import DataBeans.InfoCurriculum;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 *  
 */
public class EditProgramTest extends ServiceNeedsAuthenticationTestCase {

    /**
     * @param testName
     */
    public EditProgramTest(String testName) {
        super(testName);
    }

    /**
     * @see ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices#getNameOfServiceToBeTested()
     */
    protected String getNameOfServiceToBeTested() {
        return "EditProgram";
    }

    protected String getDataSetFilePath() {
        return "etc/datasets/servicos/teacher/testEditProgramDataSet.xml";
    }

    protected String getExpectedDataSetFilePath() {
        return "etc/datasets/servicos/teacher/testExpectedEditProgramDataSet.xml";
    }

    protected String getExpectedNewCurriculumDataSetFilePath() {
        return "etc/datasets/servicos/teacher/testExpectedNewCurriculumProgramDataSet.xml";
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
        Integer curricularCourseCode = new Integer(14);

        String programPT = "Program in Portuguese";
        String programEN = "Program in English";

        InfoCurriculum infoCurriculum = new InfoCurriculum();

        infoCurriculum.setIdInternal(curricularCourseCode);
        infoCurriculum.setProgram(programPT);
        infoCurriculum.setProgramEn(programEN);

        Object[] args = { executionCourseCode, curricularCourseCode, infoCurriculum };

        return args;
    }

    protected Object[] getTestProgramSuccessfullArguments() {

        Integer executionCourseCode = new Integer(24);
        Integer curricularCourseCode = new Integer(14);

        String programPT = "Program in Portuguese";
        String programEN = "Program in English";

        InfoCurriculum infoCurriculum = new InfoCurriculum();

        infoCurriculum.setIdInternal(curricularCourseCode);
        infoCurriculum.setProgram(programPT);
        infoCurriculum.setProgramEn(programEN);

        Object[] args = { executionCourseCode, curricularCourseCode, infoCurriculum };

        return args;
    }

    protected Object[] getTestProgramUnsuccessfullArguments() {

        Integer executionCourseCode = new Integer(24);
        Integer curricularCourseCode = new Integer(123);

        String programPT = "Program in Portuguese";
        String programEN = "Program in English";

        InfoCurriculum infoCurriculum = new InfoCurriculum();

        infoCurriculum.setIdInternal(curricularCourseCode);
        infoCurriculum.setProgram(programPT);
        infoCurriculum.setProgramEn(programEN);

        Object[] args = { executionCourseCode, curricularCourseCode, infoCurriculum };
        return args;
    }

    protected Object[] getTestProgramCurricularCourseWithNoCurriculumArguments() {

        Integer executionCourseCode = new Integer(26);
        Integer curricularCourseCode = new Integer(16);

        String programPT = "Program in Portuguese";
        String programEN = "Program in English";

        InfoCurriculum infoCurriculum = new InfoCurriculum();

        infoCurriculum.setIdInternal(curricularCourseCode);
        infoCurriculum.setProgram(programPT);
        infoCurriculum.setProgramEn(programEN);

        Object[] args = { executionCourseCode, curricularCourseCode, infoCurriculum };
        return args;
    }

    protected String getApplication() {
        return Autenticacao.EXTRANET;
    }

    public void testSuccessfullEditProgram() {

        try {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView userView = authenticateUser(args);

            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(),
                    getAuthorizeArguments());

            // verificar as alteracoes da bd
            compareDataSet(getExpectedDataSetFilePath());

        } catch (FenixServiceException ex) {
            fail("testSuccessfullEditProgram" + ex);
        } catch (Exception ex) {
            fail("testSuccessfullEditProgram error on compareDataSet" + ex);
        }
    }

    public void testSuccessfullEditProgramWithNoCurriculum() {

        System.out.println("Starting: testSuccessfullEditProgramWithNoCurriculum");
        try {
            String[] args = getAuthenticatedAndAuthorizedUser();
            IUserView userView = authenticateUser(args);

            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(),
                    getTestProgramCurricularCourseWithNoCurriculumArguments());

            // verificar as alteracoes da bd
            compareDataSet(getExpectedNewCurriculumDataSetFilePath());

            System.out.println("Finished: testSuccessfullEditProgramWithNoCurriculum");
        } catch (FenixServiceException ex) {
            fail("EditProgramTest" + ex);
        } catch (Exception ex) {
            fail("EditProgramTest error on compareDataSet" + ex);
        }
    }

}