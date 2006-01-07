package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrolmentEvaluation;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;
import net.sourceforge.fenixedu.util.EnrolmentEvaluationState;

import org.apache.commons.collections.Predicate;

public class EnrolmentEvaluationVO extends VersionedObjectsBase implements IPersistentEnrolmentEvaluation{


	    public EnrolmentEvaluation readEnrolmentEvaluationByEnrolmentAndEnrolmentEvaluationTypeAndGrade(
	            Integer enrolmentId, EnrolmentEvaluationType evaluationType, String grade)
	            throws ExcepcaoPersistencia {
			
			Enrolment enrolment = (Enrolment)readByOID(Enrolment.class, enrolmentId);
			if (enrolment != null){
				
				List<EnrolmentEvaluation> enrolmentEvaluations = enrolment.getEvaluations();
				
				for (EnrolmentEvaluation enrolmentEvaluation : enrolmentEvaluations) {
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
			
			Enrolment enrolment = (Enrolment)readByOID(Enrolment.class, enrolmentId);
			if (enrolment != null){
				List<EnrolmentEvaluation> enrolmentEvaluations = enrolment.getEvaluations();
				
				return (List)CollectionUtils.select(enrolmentEvaluations, new Predicate(){
					public boolean evaluate(Object obj){
						return ((EnrolmentEvaluation)obj).getEnrolmentEvaluationState().equals(evaluationState);
					}
				});
			}
			return new ArrayList();
	    }

		
		
	    public EnrolmentEvaluation readByUnique(Date whenAlter, Integer enrolmentId)
	            throws ExcepcaoPersistencia {
			
			Enrolment enrolment = (Enrolment)readByOID(Enrolment.class, enrolmentId);
			if (enrolment != null){
				
				List<EnrolmentEvaluation> enrolmentEvaluations = enrolment.getEvaluations();
				
				for (EnrolmentEvaluation enrolmentEvaluation : enrolmentEvaluations) {
					if (enrolmentEvaluation.getWhen().equals(whenAlter)){
						return enrolmentEvaluation;
					}
				}
			}
			
			return null;	      
	    }

		
		
	    public EnrolmentEvaluation readEnrolmentEvaluationByEnrolmentAndEnrolmentEvaluationTypeAndGradeAndWhenAlteredDate(
	            Integer enrolmentId, EnrolmentEvaluationType evaluationType, String grade, Date whenAltered)
	            throws ExcepcaoPersistencia {

			Enrolment enrolment = (Enrolment)readByOID(Enrolment.class, enrolmentId);
			
			if (enrolment != null){
				List<EnrolmentEvaluation> enrolmentEvaluations = enrolment.getEvaluations();
				
				for (EnrolmentEvaluation enrolmentEvaluation : enrolmentEvaluations) {
					if (enrolmentEvaluation.getEnrolmentEvaluationType().equals(evaluationType) &&
						enrolmentEvaluation.getGrade().equals(grade) &&
						enrolmentEvaluation.getWhen().equals(whenAltered))
						return enrolmentEvaluation;
				}
			}
			return null;
	    }

		
		
	    public List readAlreadySubmitedMarks(List enrolmentIds) throws ExcepcaoPersistencia {
			
			Collection<EnrolmentEvaluation> evaluations = readAll(EnrolmentEvaluation.class);
			Collection<EnrolmentEvaluation> filteredEvaluations = new ArrayList();
			
			for(EnrolmentEvaluation evaluation : evaluations){
				if (enrolmentIds.contains(evaluation.getEnrolment().getIdInternal()))
					filteredEvaluations.add(evaluation);
			}
			
			return (List)CollectionUtils.select(filteredEvaluations, new Predicate(){
					public boolean evaluate(Object obj){
						EnrolmentEvaluation eval = (EnrolmentEvaluation)obj;
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
	    

		
	    public EnrolmentEvaluation readEnrolmentEvaluationByEnrolmentEvaluationStateAndType(
	            Integer enrolmentId, EnrolmentEvaluationState temporary_obj,
	            EnrolmentEvaluationType enrolmentEvaluationType) throws ExcepcaoPersistencia{
			
			Enrolment enrolment = (Enrolment)readByOID(Enrolment.class, enrolmentId);
			if (enrolment != null){
				
				List<EnrolmentEvaluation> enrolmentEvaluations = enrolment.getEvaluations();
				
				for (EnrolmentEvaluation enrolmentEvaluation : enrolmentEvaluations) {
					if (enrolmentEvaluation.getEnrolmentEvaluationState().equals(temporary_obj) &&
						enrolmentEvaluation.getEnrolmentEvaluationType().equals(enrolmentEvaluationType))
						return enrolmentEvaluation;
				}
			}
			
			return null;
	    }
}
