package ServidorAplicacao.Servicos.teacher;

import DataBeans.InfoCurricularCourse;
import DataBeans.InfoCurriculum;
import DataBeans.InfoEvaluationMethod;
import DataBeans.InfoExecutionCourse;
import DataBeans.util.Cloner;
import Dominio.CurricularCourse;
import Dominio.DisciplinaExecucao;
import Dominio.ICurricularCourse;
import Dominio.IDisciplinaExecucao;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentCurriculum;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 * 
 */
public class EditEvaluationTest
	extends EvaluationMethodBelongsExecutionCourse {

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
		return "etc/testEditEvaluationMethodDataSet.xml";
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

		String[] args = { "jccm", "pass", getApplication()};
		return args;
	}

	protected Object[] getAuthorizeArguments() {

		// escolher nrs correctos
		Integer executionCourseCode = new Integer(24);
		Integer curricularCourseCode = new Integer(123);

		String evaluationMethodPT = "Evaluation Methods in Portuguese";
		String evaluationMethodEN = "Evaluation Methods in English";

		InfoCurriculum infoCurriculum = new InfoCurriculum();

		infoCurriculum.setIdInternal(curricularCourseCode);
		infoCurriculum.setEvaluationElements(evaluationMethodPT);
		infoCurriculum.setEvaluationElements(evaluationMethodEN);

		Object[] args =
			{ executionCourseCode, curricularCourseCode, infoCurriculum };
			
		return args;
	}

	protected Object[] getTestEvaluationMethodSuccessfullArguments() {

		// escolher nrs correctos
		Integer executionCourseCode = new Integer(24);
		Integer curricularCourseCode = new Integer(123);

		String evaluationMethodPT = "Evaluation Methods in Portuguese";
		String evaluationMethodEN = "Evaluation Methods in English";

		InfoCurriculum infoCurriculum = new InfoCurriculum();

		infoCurriculum.setIdInternal(curricularCourseCode);
		infoCurriculum.setEvaluationElements(evaluationMethodPT);
		infoCurriculum.setEvaluationElements(evaluationMethodEN);

		Object[] args =
			{ executionCourseCode, curricularCourseCode, infoCurriculum };
			
		return args;
	}

	protected Object[] getTestEvaluationMethodUnsuccessfullArguments() {

		// escolher nrs incorrectos
		Integer executionCourseCode = new Integer(24);	
		Integer curricularCourseCode = new Integer(123);

		String evaluationMethodPT = "Evaluation Methods in Portuguese";
		String evaluationMethodEN = "Evaluation Methods in English";

		InfoCurriculum infoCurriculum = new InfoCurriculum();
				
		infoCurriculum.setIdInternal(curricularCourseCode);
		infoCurriculum.setEvaluationElements(evaluationMethodPT);
		infoCurriculum.setEvaluationElements(evaluationMethodEN);		

		Object[] args =
			{
				executionCourseCode,
				curricularCourseCode,
				infoCurriculum
			};
		return args;
	}

	protected String getApplication() {
		return Autenticacao.EXTRANET;
	}

	public void testSuccessfull() {

		try {
			String[] args = getAuthorizedUser();
			IUserView userView = authenticateUser(args);

			gestor.executar(
				userView,
				getNameOfServiceToBeTested(),
				getAuthorizeArguments());

			// verificar as alteracoes da bd
			compareDataSet(getDataSetFilePath());
		}
		catch (FenixServiceException ex) {
			fail("EditEvaluationTest" + ex);
		}
		catch (Exception ex) {
			fail("EditEvaluatioTest error on compareDataSet" + ex);
		}
	}

	//#############################################################################################################
//	/**
//	 * @see ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices#getArgumentsOfServiceToBeTestedUnsuccessfuly()
//	 */
//	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
//		return null;
//	}
//
//	/**
//	 * @see ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices#getArgumentsOfServiceToBeTestedSuccessfuly()
//	 */
//	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
//		ISuportePersistente sp = null;
//		InfoExecutionCourse infoExecutionCourse = null;
//		try {
//			sp = SuportePersistenteOJB.getInstance();
//			IDisciplinaExecucaoPersistente persistentExecutionCourse =
//				sp.getIDisciplinaExecucaoPersistente();
//
//			sp.iniciarTransaccao();
//			IDisciplinaExecucao executionCourse =
//				(IDisciplinaExecucao) persistentExecutionCourse.readByOId(
//					new DisciplinaExecucao(new Integer(26)),
//					false);
//			sp.confirmarTransaccao();
//
//			infoExecutionCourse =
//				Cloner.copyIExecutionCourse2InfoExecutionCourse(
//					executionCourse);
//
//		}
//		catch (ExcepcaoPersistencia e) {
//			System.out.println("failed setting up the test data");
//			e.printStackTrace();
//		}
//		InfoEvaluationMethod infoEvaluation = new InfoEvaluationMethod();
//		infoEvaluation.setEvaluationElements("evaluationElements");
//		infoEvaluation.setEvaluationElementsEn("evaluationElementsEn");
//		//infoEvaluation.setInfoExecutionCourse(infoExecutionCourse);
//
//		Object[] args = { new Integer(26), infoEvaluation };
//		return args;
//	}
//
//	/**
//	 * This method must return 'true' if the service needs authorization to be runned and 'false' otherwise.
//	 */
//	protected boolean needsAuthorization() {
//		return true;
//	}
}