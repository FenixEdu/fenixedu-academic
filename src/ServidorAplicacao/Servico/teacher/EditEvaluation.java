package ServidorAplicacao.Servico.teacher;

import DataBeans.InfoEvaluationMethod;
import Dominio.DisciplinaExecucao;
import Dominio.EvaluationMethod;
import Dominio.IDisciplinaExecucao;
import Dominio.IEvaluationMethod;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
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
		Integer infoEvaluationMethodCode,
		InfoEvaluationMethod infoEvaluationMethod)
		throws FenixServiceException{

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			
			IDisciplinaExecucao executionCourse = new DisciplinaExecucao();
			executionCourse.setIdInternal(infoExecutionCourseCode); 
				
			IPersistentEvaluationMethod persistentEvaluationMethod = sp.getIPersistentEvaluationMethod();
			IEvaluationMethod evaluationMethod = persistentEvaluationMethod.readByIdExecutionCourse(executionCourse);

			if(evaluationMethod == null) {
				evaluationMethod = new EvaluationMethod();
				
				evaluationMethod.setKeyExecutionCourse(infoExecutionCourseCode);
								
				IDisciplinaExecucaoPersistente persistenteExecutionCourse = sp.getIDisciplinaExecucaoPersistente();
				evaluationMethod.setExecutionCourse((IDisciplinaExecucao) persistenteExecutionCourse.readByOId(executionCourse, false));				
			} 
			
			evaluationMethod.setEvaluationElements(infoEvaluationMethod.getEvaluationElements());
			evaluationMethod.setEvaluationElementsEn(infoEvaluationMethod.getEvaluationElementsEn());
			persistentEvaluationMethod.lockWrite(evaluationMethod);					
			
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
		return true;
	}
	
//	public boolean run(
//		Integer infoExecutionCourseCode,
//		Integer infoCurricularCourseCode,
//		InfoCurriculum infoCurriculumNew)
//		throws FenixServiceException{
//
//		try {
//			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
//			IPersistentCurricularCourse persistentCurricularCourse =
//				sp.getIPersistentCurricularCourse();
//
//			ICurricularCourse curricularCourse =
//				(ICurricularCourse) persistentCurricularCourse.readByOId(new CurricularCourse(infoCurricularCourseCode),false);
//
//			IPersistentCurriculum persistentCurriculum =
//				sp.getIPersistentCurriculum();
//
//			ICurriculum curriculum = persistentCurriculum.readCurriculumByCurricularCourse(curricularCourse);
//		
//			if (curriculum != null) {		
//				curriculum.setCurricularCourse(curricularCourse);
//				curriculum.setEvaluationElements(infoCurriculumNew.getEvaluationElements());
//				curriculum.setEvaluationElementsEn(infoCurriculumNew.getEvaluationElementsEn());
//				persistentCurriculum.lockWrite(curriculum);
//					
//			} else {
//				curriculum = new Curriculum();
//				curriculum.setCurricularCourse(curricularCourse);
//				curriculum.setEvaluationElements(infoCurriculumNew.getEvaluationElements());
//				curriculum.setEvaluationElementsEn(infoCurriculumNew.getEvaluationElementsEn());
//				persistentCurriculum.lockWrite(curriculum);
//			}
//			
//		} catch (ExcepcaoPersistencia e) {
//			throw new FenixServiceException(e);
//		}
//		return true;
//	}
}