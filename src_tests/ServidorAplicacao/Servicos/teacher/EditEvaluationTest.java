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
public class EditEvaluationTest extends EvaluationMethodBelongsExecutionCourse {

	/**
	 * @param testName
	 */
	public EditEvaluationTest(String testName) {
		super(testName);
	}

	/**
	 * @see ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices#getNameOfServiceToBeTested()
	 */
	protected String getNameOfServiceToBeTested() {
		return "EditEvaluation";
	}

	protected String getDataSetFilePath() {
		return "etc/datasets/testEditEvaluationMethodDataSet.xml";
	}

	protected String getExpectedDataSetFilePath() {
		return "etc/datasets/testExpectedEditEvaluationMethodDataSet.xml";
	}

	protected String getExpectedNewCurriculumDataSetFilePath() {
		return "etc/datasets/testExpectedNewCurriculumEvaluatioMethodDataSet.xml";
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

		String evaluationMethodPT = "Evaluation Methods in Portuguese";
		String evaluationMethodEN = "Evaluation Methods in English";

		InfoCurriculum infoCurriculum = new InfoCurriculum();

		infoCurriculum.setIdInternal(curricularCourseCode);
		infoCurriculum.setEvaluationElements(evaluationMethodPT);
		infoCurriculum.setEvaluationElementsEn(evaluationMethodEN);

		Object[] args = { executionCourseCode, curricularCourseCode, infoCurriculum };

		return args;
	}

	protected Object[] getTestEvaluationMethodSuccessfullArguments() {

		Integer executionCourseCode = new Integer(24);
		Integer curricularCourseCode = new Integer(14);

		String evaluationMethodPT = "Evaluation Methods in Portuguese";
		String evaluationMethodEN = "Evaluation Methods in English";

		InfoCurriculum infoCurriculum = new InfoCurriculum();

		infoCurriculum.setIdInternal(curricularCourseCode);
		infoCurriculum.setEvaluationElements(evaluationMethodPT);
		infoCurriculum.setEvaluationElementsEn(evaluationMethodEN);

		Object[] args = { executionCourseCode, curricularCourseCode, infoCurriculum };

		return args;
	}

	protected Object[] getTestEvaluationMethodUnsuccessfullArguments() {

		Integer executionCourseCode = new Integer(24);
		Integer curricularCourseCode = new Integer(123);

		String evaluationMethodPT = "Evaluation Methods in Portuguese";
		String evaluationMethodEN = "Evaluation Methods in English";

		InfoCurriculum infoCurriculum = new InfoCurriculum();

		infoCurriculum.setIdInternal(curricularCourseCode);
		infoCurriculum.setEvaluationElements(evaluationMethodPT);
		infoCurriculum.setEvaluationElementsEn(evaluationMethodEN);

		Object[] args = { executionCourseCode, curricularCourseCode, infoCurriculum };
		return args;
	}

	protected Object[] getTestEvaluationMethodCurricularCourseWithNoCurriculumArguments() {

		Integer executionCourseCode = new Integer(26);
		Integer curricularCourseCode = new Integer(16);

		String evaluationMethodPT = "Evaluation Methods in Portuguese";
		String evaluationMethodEN = "Evaluation Methods in English";

		InfoCurriculum infoCurriculum = new InfoCurriculum();

		infoCurriculum.setIdInternal(curricularCourseCode);
		infoCurriculum.setEvaluationElements(evaluationMethodPT);
		infoCurriculum.setEvaluationElementsEn(evaluationMethodEN);

		Object[] args = { executionCourseCode, curricularCourseCode, infoCurriculum };
		return args;
	}

	protected String getApplication() {
		return Autenticacao.EXTRANET;
	}

	public void testSuccessfullEditEvaluationMethod() {

		try {
			String[] args = getAuthorizedUser();
			IUserView userView = authenticateUser(args);

			gestor.executar(userView, getNameOfServiceToBeTested(), getAuthorizeArguments());

			// verificar as alteracoes da bd
			compareDataSet(getExpectedDataSetFilePath());

		} catch (FenixServiceException ex) {
			fail("EditEvaluationTest" + ex);
		} catch (Exception ex) {
			fail("EditEvaluationTest error on compareDataSet" + ex);
		}
	}

	public void testSuccessfullEditEvaluationMethodWithNoCurriculum() {

		System.out.println("Starting: testSuccessfullEditEvaluationMethodWithNoCurriculum");
		try {
			String[] args = getAuthorizedUser();
			IUserView userView = authenticateUser(args);

			gestor.executar(
				userView,
				getNameOfServiceToBeTested(),
				getTestEvaluationMethodCurricularCourseWithNoCurriculumArguments());

			// verificar as alteracoes da bd
			compareDataSet(getExpectedNewCurriculumDataSetFilePath());

			System.out.println("Finished: testSuccessfullEditEvaluationMethodWithNoCurriculum");
		} catch (FenixServiceException ex) {
			fail("EditEvaluationTest" + ex);
		} catch (Exception ex) {
			fail("EditEvaluationTest error on compareDataSet" + ex);
		}
	}
}