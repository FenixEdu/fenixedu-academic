package ServidorAplicacao.Servico.teacher;

import DataBeans.InfoEvaluation;
import Dominio.DisciplinaExecucao;
import Dominio.Evaluation;
import Dominio.IDisciplinaExecucao;
import Dominio.IEvaluation;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentEvaluation;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Fernanda Quitério
 *
 * 
 */
public class EditEvaluation implements IServico {

	private static EditEvaluation service = new EditEvaluation();
	public static EditEvaluation getService() {
		return service;
	}

	private EditEvaluation() {
	}
	public final String getNome() {
		return "EditEvaluation";
	}

	public boolean run(Integer infoExecutionCourseCode, InfoEvaluation infoEvaluationNew) throws FenixServiceException {

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IDisciplinaExecucaoPersistente persistentExecutionCourse = sp.getIDisciplinaExecucaoPersistente();
			IPersistentEvaluation persistentEvaluation = sp.getIPersistentEvaluation();

			IDisciplinaExecucao executionCourse =
				(IDisciplinaExecucao) persistentExecutionCourse.readByOId(new DisciplinaExecucao(infoExecutionCourseCode), false);

			IEvaluation evaluation = null;
			evaluation = persistentEvaluation.readByExecutionCourse(executionCourse);

			persistentEvaluation.lockWrite(evaluation);

			if (evaluation != null) {
				evaluation.setExecutionCourse(executionCourse);
				evaluation.setEvaluationElements(infoEvaluationNew.getEvaluationElements());
				evaluation.setEvaluationElementsEn(infoEvaluationNew.getEvaluationElementsEn());
			} else {
				evaluation =
					new Evaluation(
						executionCourse,
						infoEvaluationNew.getEvaluationElements(),
						infoEvaluationNew.getEvaluationElementsEn());
			}

		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
		return true;
	}
}