package ServidorAplicacao.Servicos.teacher;

import DataBeans.InfoEvaluationMethod;
import DataBeans.InfoExecutionCourse;
import DataBeans.util.Cloner;
import Dominio.DisciplinaExecucao;
import Dominio.IDisciplinaExecucao;
import ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Fernanda Quitério
 * 
 */
public class EditEvaluationTest extends TestCaseDeleteAndEditServices {

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

	/**
	 * @see ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices#getArgumentsOfServiceToBeTestedUnsuccessfuly()
	 */
	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		return null;
	}

	/**
	 * @see ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices#getArgumentsOfServiceToBeTestedSuccessfuly()
	 */
	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		ISuportePersistente sp = null;
		InfoExecutionCourse infoExecutionCourse = null;
		try {
			sp = SuportePersistenteOJB.getInstance();
			IDisciplinaExecucaoPersistente persistentExecutionCourse = sp.getIDisciplinaExecucaoPersistente();
			
			sp.iniciarTransaccao();
			IDisciplinaExecucao executionCourse =
				(IDisciplinaExecucao) persistentExecutionCourse.readByOId(new DisciplinaExecucao(new Integer(26)), false);
			sp.confirmarTransaccao();
			
			infoExecutionCourse = Cloner.copyIExecutionCourse2InfoExecutionCourse(executionCourse);

		} catch (ExcepcaoPersistencia e) {
			System.out.println("failed setting up the test data");
			e.printStackTrace();
		}
		InfoEvaluationMethod infoEvaluation = new InfoEvaluationMethod();
		infoEvaluation.setEvaluationElements("evaluationElements");
		infoEvaluation.setEvaluationElementsEn("evaluationElementsEn");
		infoEvaluation.setInfoExecutionCourse(infoExecutionCourse);

		Object[] args = { new Integer(26), infoEvaluation };
		return args;
	}

	/**
	 * This method must return 'true' if the service needs authorization to be runned and 'false' otherwise.
	 */
	protected boolean needsAuthorization() {
		return true;
	}
}