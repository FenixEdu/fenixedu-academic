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

public class EditObjectivesTest extends ObjectivesBelongsExecutionCourse {

	/**
	 * @param testName
	 */
	public EditObjectivesTest(String testName) {
		super(testName);
	}

	/**
	 * @see ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices#getNameOfServiceToBeTested()
	 */
	protected String getNameOfServiceToBeTested() {
		return "EditObjectives";
	}

	protected String getDataSetFilePath() {
		return "etc/datasets/testEditObjectivesDataSet.xml";
	}

	protected String getExpectedDataSetFilePath() {
		return "etc/datasets/testExpectedEditObjectivesDataSet.xml";
	}

	protected String getExpectedNewCurriculumDataSetFilePath() {
		return "etc/datasets/testExpectedNewCurriculumObjectivesDataSet.xml";
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

		String GeneralObjectivesPT = "General Objectives in Portuguese";
		String GeneralObjectivesEN = "General Objectives in English";
		String OperationalObjectivesPT = "Operational Objectives in Portuguese";
		String OperationalObjectivesEN = "Operational Objectives in English";
		
		InfoCurriculum infoCurriculum = new InfoCurriculum();

		infoCurriculum.setIdInternal(curricularCourseCode);
		infoCurriculum.setGeneralObjectives(GeneralObjectivesPT);
		infoCurriculum.setGeneralObjectivesEn(GeneralObjectivesEN);
		infoCurriculum.setOperacionalObjectives(OperationalObjectivesPT);
		infoCurriculum.setOperacionalObjectivesEn(OperationalObjectivesEN);

		Object[] args = { executionCourseCode, curricularCourseCode, infoCurriculum };

		return args;
	}

	protected Object[] getTestObjectivesSuccessfullArguments() {

		Integer executionCourseCode = new Integer(24);
		Integer curricularCourseCode = new Integer(14);

		String GeneralObjectivesPT = "General Objectives in Portuguese";
		String GeneralObjectivesEN = "General Objectives in English";
		String OperationalObjectivesPT = "Operational Objectives in Portuguese";
		String OperationalObjectivesEN = "Operational Objectives in English";
		
		InfoCurriculum infoCurriculum = new InfoCurriculum();

		infoCurriculum.setIdInternal(curricularCourseCode);
		infoCurriculum.setGeneralObjectives(GeneralObjectivesPT);
		infoCurriculum.setGeneralObjectivesEn(GeneralObjectivesEN);
		infoCurriculum.setOperacionalObjectives(OperationalObjectivesPT);
		infoCurriculum.setOperacionalObjectivesEn(OperationalObjectivesEN);

		Object[] args = { executionCourseCode, curricularCourseCode, infoCurriculum };

		return args;
	}

	protected Object[] getTestObjectivesUnsuccessfullArguments() {

		Integer executionCourseCode = new Integer(24);
		Integer curricularCourseCode = new Integer(123);

		String GeneralObjectivesPT = "General Objectives in Portuguese";
		String GeneralObjectivesEN = "General Objectives in English";
		String OperationalObjectivesPT = "Operational Objectives in Portuguese";
		String OperationalObjectivesEN = "Operational Objectives in English";
		
		InfoCurriculum infoCurriculum = new InfoCurriculum();

		infoCurriculum.setIdInternal(curricularCourseCode);
		infoCurriculum.setGeneralObjectives(GeneralObjectivesPT);
		infoCurriculum.setGeneralObjectivesEn(GeneralObjectivesEN);
		infoCurriculum.setOperacionalObjectives(OperationalObjectivesPT);
		infoCurriculum.setOperacionalObjectivesEn(OperationalObjectivesEN);

		Object[] args = { executionCourseCode, curricularCourseCode, infoCurriculum };
		return args;
	}

	protected Object[] getTestObjectivesCurricularCourseWithNoCurriculumArguments() {

		Integer executionCourseCode = new Integer(26);
		Integer curricularCourseCode = new Integer(16);

		String GeneralObjectivesPT = "General Objectives in Portuguese";
		String GeneralObjectivesEN = "General Objectives in English";
		String OperationalObjectivesPT = "Operational Objectives in Portuguese";
		String OperationalObjectivesEN = "Operational Objectives in English";
		
		InfoCurriculum infoCurriculum = new InfoCurriculum();

		infoCurriculum.setIdInternal(curricularCourseCode);
		infoCurriculum.setGeneralObjectives(GeneralObjectivesPT);
		infoCurriculum.setGeneralObjectivesEn(GeneralObjectivesEN);
		infoCurriculum.setOperacionalObjectives(OperationalObjectivesPT);
		infoCurriculum.setOperacionalObjectivesEn(OperationalObjectivesEN);

		Object[] args = { executionCourseCode, curricularCourseCode, infoCurriculum };
		return args;
	}

	protected String getApplication() {
		return Autenticacao.EXTRANET;
	}

	public void testSuccessfull() {

		try {
			String[] args = getAuthorizedUser();
			IUserView userView = authenticateUser(args);

			gestor.executar(userView, getNameOfServiceToBeTested(), getAuthorizeArguments());

			// verificar as alteracoes da bd
			compareDataSet(getExpectedDataSetFilePath());

		} catch (FenixServiceException ex) {
			fail("EditObjectivesTest" + ex);
		} catch (Exception ex) {
			fail("EditObjectivesTest error on compareDataSet" + ex);
		}
	}
	
//	public void testSuccessfullEditObjectivesWithNoCurriculum() {
//
//		System.out.println("Starting: testSuccessfullEditObjectivesWithNoCurriculum");
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
//			System.out.println("Finished: testSuccessfullEditObjectivesWithNoCurriculum");
//		} catch (FenixServiceException ex) {
//			fail("EditObjectivesTest" + ex);
//		} catch (Exception ex) {
//			fail("EditObjectivesTest error on compareDataSet" + ex);
//		}
//	}
}