package ServidorAplicacao.Servico.teacher;

import DataBeans.InfoEvaluationMethod;
import Dominio.ExecutionCourse;
import Dominio.EvaluationMethod;
import Dominio.IExecutionCourse;
import Dominio.IEvaluationMethod;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
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
			
			IExecutionCourse executionCourse = new ExecutionCourse();
			executionCourse.setIdInternal(infoExecutionCourseCode); 
				
			IPersistentEvaluationMethod persistentEvaluationMethod = sp.getIPersistentEvaluationMethod();
			IEvaluationMethod evaluationMethod = persistentEvaluationMethod.readByIdExecutionCourse(executionCourse);

			if(evaluationMethod == null) {
				evaluationMethod = new EvaluationMethod();
				
				evaluationMethod.setKeyExecutionCourse(infoExecutionCourseCode);
								
				IPersistentExecutionCourse persistenteExecutionCourse = sp.getIDisciplinaExecucaoPersistente();
				evaluationMethod.setExecutionCourse((IExecutionCourse) persistenteExecutionCourse.readByOId(executionCourse, false));				
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