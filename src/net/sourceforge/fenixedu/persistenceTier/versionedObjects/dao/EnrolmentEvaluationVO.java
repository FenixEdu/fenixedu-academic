package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.IEnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrolmentEvaluation;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;
import net.sourceforge.fenixedu.util.EnrolmentEvaluationState;

import org.apache.commons.collections.Predicate;

public class EnrolmentEvaluationVO extends VersionedObjectsBase implements IPersistentEnrolmentEvaluation{


	    public IEnrolmentEvaluation readEnrolmentEvaluationByEnrolmentAndEnrolmentEvaluationTypeAndGrade(
	            Integer enrolmentId, EnrolmentEvaluationType evaluationType, String grade)
	            throws ExcepcaoPersistencia {
			
			IEnrolment enrolment = (IEnrolment)readByOID(Enrolment.class, enrolmentId);
			if (enrolment != null){
				
				List<IEnrolmentEvaluation> enrolmentEvaluations = enrolment.getEvaluations();
				
				for (IEnrolmentEvaluation enrolmentEvaluation : enrolmentEvaluations) {
					if (enrolmentEvaluation.getEnrolmentEvaluationType().equals(evaluationType) &&
							((grade == null && enrolmentEvaluation.getGrade() == null) || 
									(enrolmentEvaluation.getGrade() != null && enrolmentEvaluation.getGrade().equals(grade))))
						return enrolmentEvaluation;
				}
	    	}
			return null;
	    }


	    public List readEnrolmentEvaluationByEnrolmentEvaluationState(Integer enrolmentId,
	            final EnrolmentEvaluationState evaluationState) throws ExcepcaoPersistencia {
			
			IEnrolment enrolment = (IEnrolment)readByOID(Enrolment.class, enrolmentId);
			if (enrolment != null){
				List<IEnrolmentEvaluation> enrolmentEvaluations = enrolment.getEvaluations();
				
				return (List)CollectionUtils.select(enrolmentEvaluations, new Predicate(){
					public boolean evaluate(Object obj){
						return ((IEnrolmentEvaluation)obj).getEnrolmentEvaluationState().equals(evaluationState);
					}
				});
			}
			return new ArrayList();
	    }

		
		
	    public IEnrolmentEvaluation readByUnique(Date whenAlter, Integer enrolmentId)
	            throws ExcepcaoPersistencia {
			
			IEnrolment enrolment = (IEnrolment)readByOID(Enrolment.class, enrolmentId);
			if (enrolment != null){
				
				List<IEnrolmentEvaluation> enrolmentEvaluations = enrolment.getEvaluations();
				
				for (IEnrolmentEvaluation enrolmentEvaluation : enrolmentEvaluations) {
					if (enrolmentEvaluation.getWhen().equals(whenAlter)){
						return enrolmentEvaluation;
					}
				}
			}
			
			return null;	      
	    }

		
		
	    public IEnrolmentEvaluation readEnrolmentEvaluationByEnrolmentAndEnrolmentEvaluationTypeAndGradeAndWhenAlteredDate(
	            Integer enrolmentId, EnrolmentEvaluationType evaluationType, String grade, Date whenAltered)
	            throws ExcepcaoPersistencia {

			IEnrolment enrolment = (IEnrolment)readByOID(Enrolment.class, enrolmentId);
			
			if (enrolment != null){
				List<IEnrolmentEvaluation> enrolmentEvaluations = enrolment.getEvaluations();
				
				for (IEnrolmentEvaluation enrolmentEvaluation : enrolmentEvaluations) {
					if (enrolmentEvaluation.getEnrolmentEvaluationType().equals(evaluationType) &&
						enrolmentEvaluation.getGrade().equals(grade) &&
						enrolmentEvaluation.getWhen().equals(whenAltered))
						return enrolmentEvaluation;
				}
			}
			return null;
	    }

		
		
	    public List readAlreadySubmitedMarks(List enrolmentIds) throws ExcepcaoPersistencia {
			
			Collection<IEnrolmentEvaluation> evaluations = readAll(EnrolmentEvaluation.class);
			Collection<IEnrolmentEvaluation> filteredEvaluations = new ArrayList();
			
			for(IEnrolmentEvaluation evaluation : evaluations){
				if (enrolmentIds.contains(evaluation.getEnrolment().getIdInternal()))
					filteredEvaluations.add(evaluation);
			}
			
			return (List)CollectionUtils.select(filteredEvaluations, new Predicate(){
					public boolean evaluate(Object obj){
						IEnrolmentEvaluation eval = (IEnrolmentEvaluation)obj;
						return (eval.getEnrolmentEvaluationState().equals(EnrolmentEvaluationState.FINAL_OBJ) ||
								eval.getEnrolmentEvaluationState().equals(EnrolmentEvaluationState.ANNULED_OBJ) ||
									(eval.getEnrolmentEvaluationState().equals(EnrolmentEvaluationState.TEMPORARY_OBJ) &&
										(eval.getGrade() != null ||
										 !eval.getGrade().equals("")
										)
									)
							   );
					}
				});
	    }
	    

		
	    public IEnrolmentEvaluation readEnrolmentEvaluationByEnrolmentEvaluationStateAndType(
	            Integer enrolmentId, EnrolmentEvaluationState temporary_obj,
	            EnrolmentEvaluationType enrolmentEvaluationType) throws ExcepcaoPersistencia{
			
			IEnrolment enrolment = (IEnrolment)readByOID(Enrolment.class, enrolmentId);
			if (enrolment != null){
				
				List<IEnrolmentEvaluation> enrolmentEvaluations = enrolment.getEvaluations();
				
				for (IEnrolmentEvaluation enrolmentEvaluation : enrolmentEvaluations) {
					if (enrolmentEvaluation.getEnrolmentEvaluationState().equals(temporary_obj) &&
						enrolmentEvaluation.getEnrolmentEvaluationType().equals(enrolmentEvaluationType))
						return enrolmentEvaluation;
				}
			}
			
			return null;
	    }
}
