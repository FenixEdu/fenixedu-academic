package ServidorAplicacao.Servicos.teacher;

import framework.factory.ServiceManagerServiceFactory;
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

public class EditObjectivesTest extends ServiceNeedsAuthenticationTestCase {

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
		return "etc/datasets/servicos/teacher/testEditObjectivesDataSet.xml";
	}

	protected String getExpectedDataSetFilePath() {
		return "etc/datasets/servicos/teacher/testExpectedEditObjectivesDataSet.xml";
	}

	protected String getExpectedNewCurriculumDataSetFilePath() {
		return "etc/datasets/servicos/teacher/testExpectedNewCurriculumObjectivesDataSet.xml";
	}

	protected String[] getAuthenticatedAndAuthorizedUser() {

		String[] args = { "user", "pass", getApplication()};
		return args;
	}

	protected String[] getAuthenticatedAndUnauthorizedUser() {

		String[] args = { "julia", "pass", getApplication()};
		return args;
	}

	protected String[] getNotAuthenticatedUser() {

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

	public void testEditObjectives() {

		try {
			String[] args = getAuthenticatedAndAuthorizedUser();
			IUserView userView = authenticateUser(args);

			ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), getAuthorizeArguments());

			// verificar as alteracoes da bd
			compareDataSet(getExpectedDataSetFilePath());

		} catch (FenixServiceException ex) {
			fail("EditObjectivesTest" + ex);
		} catch (Exception ex) {
			fail("EditObjectivesTest error on compareDataSet" + ex);
		}
	}

	public void testEditObjectivesWithNoCurriculum() {

		try {
			String[] args = getAuthenticatedAndAuthorizedUser();
			IUserView userView = authenticateUser(args);

			ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), getTestObjectivesCurricularCourseWithNoCurriculumArguments());

			// verificar as alteracoes da bd
			compareDataSet(getExpectedNewCurriculumDataSetFilePath());

		} catch (FenixServiceException ex) {
			fail("EditObjectivesTest" + ex);
		} catch (Exception ex) {
			fail("EditObjectivesTest error on compareDataSet" + ex);
		}
	}

	public void testObjectivesNotBelongsExecutionCourse() {

		Object serviceArguments[] = getTestObjectivesUnsuccessfullArguments();

		try {
			ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), serviceArguments);
			fail(getNameOfServiceToBeTested() + "fail testObjectivesNotBelongsExecutionCourse");
		} catch (NotAuthorizedException ex) {

			System.out.println(
				"testObjectivesNotBelongsExecutionCourse was SUCCESSFULY runned by service: " + getNameOfServiceToBeTested());
		} catch (Exception ex) {
			fail(getNameOfServiceToBeTested() + "fail testObjectivesNotBelongsExecutionCourse");
		}
	}
}