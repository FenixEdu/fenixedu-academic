package ServidorAplicacao.Servico.teacher;

import DataBeans.InfoEvaluationMethod;
import Dominio.CurricularCourse;
import Dominio.EvaluationMethod;
import Dominio.ICurricularCourse;
import Dominio.IEvaluationMethod;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentEvaluationMethod;
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

	public boolean run(
		Integer infoExecutionCourseCode,
		Integer infoCurricularCourseCode,
		InfoEvaluationMethod infoEvaluationNew)
		throws FenixServiceException {

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentCurricularCourse persistentCurricularCourse =
				sp.getIPersistentCurricularCourse();
			IPersistentEvaluationMethod persistentEvaluation =
				sp.getIPersistentEvaluationMethod();

			ICurricularCourse curricularCourse =
				(ICurricularCourse) persistentCurricularCourse.readByOId(
						new CurricularCourse(infoCurricularCourseCode),
						false);

			IEvaluationMethod evaluation = null;
			evaluation =
				persistentEvaluation.readByCurricularCourse(curricularCourse);

		

			if (evaluation != null) {
				persistentEvaluation.lockWrite(evaluation);
				evaluation.setCurricularCourse(curricularCourse);
				evaluation.setEvaluationElements(
					infoEvaluationNew.getEvaluationElements());
				evaluation.setEvaluationElementsEn(
					infoEvaluationNew.getEvaluationElementsEn());
			} else {
				evaluation =
					new EvaluationMethod(
						curricularCourse,
						infoEvaluationNew.getEvaluationElements(),
						infoEvaluationNew.getEvaluationElementsEn());
				persistentEvaluation.lockWrite(evaluation);
			}

		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
		return true;
	}
}