package ServidorAplicacao.Servicos.teacher;

import DataBeans.InfoCurriculum;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;

/**
 * @author Nuno Correia 
 * @author Ricardo Rodrigues
 * 
 */
public class EditProgramTest extends ProgramBelongsExecutionCourse {

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
		return "etc/datasets/testEditProgramDataSet.xml";
	}

	protected String getExpectedDataSetFilePath() {
		return "etc/datasets/testExpectedEditProgramDataSet.xml";
	}

	protected String getExpectedNewCurriculumDataSetFilePath() {
		return "etc/datasets/testExpectedNewCurriculumProgramDataSet.xml";
	}

	protected String[] getAuthorizedUser() {

		String[] args = { "user", "pass", getApplication()};
		return args;
	}

	protected String[] getUnauthorizedUser() {

		String[] args = { "julia", "pass", getApplication()};
		return args;
	}

	protected String[] getNonTeacherUser() {

		String[] args = { "fiado", "pass", getApplication()};
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
			String[] args = getAuthorizedUser();
			IUserView userView = authenticateUser(args);

			gestor.executar(userView, getNameOfServiceToBeTested(), getAuthorizeArguments());

			// verificar as alteracoes da bd
			compareDataSet(getExpectedDataSetFilePath());

		} catch (FenixServiceException ex) {
			fail("testSuccessfullEditProgram" + ex);
		} catch (Exception ex) {
			fail("testSuccessfullEditProgram error on compareDataSet" + ex);
		}
	}
	
//	public void testSuccessfullEditProgramWithNoCurriculum() {
//
//		System.out.println("Starting: testSuccessfullEditProgramWithNoCurriculum");
//		try {
//			String[] args = getAuthorizedUser();
//			IUserView userView = authenticateUser(args);
//
//			gestor.executar(
//				userView,
//				getNameOfServiceToBeTested(),
//				getTestEvaluationMethodCurricularCourseWithNoCurriculumArguments());
//
//			// verificar as alteracoes da bd
//			compareDataSet(getExpectedNewCurriculumDataSetFilePath());
//
//			System.out.println("Finished: testSuccessfullEditProgramWithNoCurriculum");
//		} catch (FenixServiceException ex) {
//			fail("EditProgramTest" + ex);
//		} catch (Exception ex) {
//			fail("EditProgramTest error on compareDataSet" + ex);
//		}
//	}

}