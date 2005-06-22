package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.IEnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrolmentEvaluation;
import net.sourceforge.fenixedu.util.EnrolmentEvaluationState;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author Ângela
 */

public class EnrolmentEvaluationOJB extends PersistentObjectOJB implements
        IPersistentEnrolmentEvaluation {


    public IEnrolmentEvaluation readEnrolmentEvaluationByEnrolmentAndEnrolmentEvaluationTypeAndGrade(
            Integer enrolmentId, EnrolmentEvaluationType evaluationType, String grade)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("enrolment.idInternal", enrolmentId);
        criteria.addEqualTo("enrolmentEvaluationType", evaluationType);
        if (grade == null) {
            criteria.addIsNull("grade");
        } else {
            criteria.addEqualTo("grade", grade);
        }

        IEnrolmentEvaluation enrolmentEvaluation = (IEnrolmentEvaluation) queryObject(
                EnrolmentEvaluation.class, criteria);

        return enrolmentEvaluation;
    }

    public List readEnrolmentEvaluationByEnrolmentEvaluationState(Integer enrolmentId,
            EnrolmentEvaluationState evaluationState) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("enrolment.idInternal", enrolmentId);
        criteria.addEqualTo("enrolmentEvaluationState", evaluationState);

        List examsWithRepetition = queryList(EnrolmentEvaluation.class, criteria);
        return examsWithRepetition;
    }


    public IEnrolmentEvaluation readEnrolmentEvaluationByEnrolmentAndEnrolmentEvaluationTypeAndGradeAndWhenAlteredDate(
            Integer enrolmentId, EnrolmentEvaluationType evaluationType, String grade, Date whenAltered)
            throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("enrolment.idInternal", enrolmentId);
        criteria.addEqualTo("enrolmentEvaluationType", evaluationType);
        criteria.addEqualTo("grade", grade);
        criteria.addEqualTo("when", whenAltered);

        IEnrolmentEvaluation enrolmentEvaluation = (IEnrolmentEvaluation) queryObject(
                EnrolmentEvaluation.class, criteria);

        return enrolmentEvaluation;
    }

    public List readAlreadySubmitedMarks(List enrolmentIds) throws ExcepcaoPersistencia {
        Criteria finalCriteria = new Criteria();
        Criteria firstCriteria = new Criteria();
        Criteria secondCriteria = new Criteria();

        Criteria markNotEmptyCriteria = new Criteria();
        Criteria markNotNullCriteria = new Criteria();

        markNotEmptyCriteria.addNotEqualTo("grade", "");

        markNotNullCriteria.addNotNull("grade");

        Criteria markCriteria = new Criteria();
        markCriteria.addAndCriteria(markNotNullCriteria);
        markCriteria.addOrCriteria(markNotEmptyCriteria);

        firstCriteria.addEqualTo("enrolmentEvaluationState", EnrolmentEvaluationState.FINAL_OBJ);

        Criteria anulledCriteria = new Criteria();
        anulledCriteria.addEqualTo("enrolmentEvaluationState", EnrolmentEvaluationState.ANNULED_OBJ);
        firstCriteria.addOrCriteria(anulledCriteria);

        secondCriteria.addEqualTo("enrolmentEvaluationState", EnrolmentEvaluationState.TEMPORARY_OBJ);
        secondCriteria.addAndCriteria(markCriteria);

        firstCriteria.addOrCriteria(secondCriteria);

        finalCriteria.addIn("enrolment.idInternal", enrolmentIds);
        finalCriteria.addAndCriteria(firstCriteria);

        return queryList(EnrolmentEvaluation.class, finalCriteria);
    }
    

    public IEnrolmentEvaluation readEnrolmentEvaluationByEnrolmentEvaluationStateAndType(
            Integer enrolmentId, EnrolmentEvaluationState temporary_obj,
            EnrolmentEvaluationType enrolmentEvaluationType) throws ExcepcaoPersistencia{
        Criteria criteria = new Criteria();
        criteria.addEqualTo("enrolment.idInternal", enrolmentId);
        criteria.addEqualTo("enrolmentEvaluationState", temporary_obj);
        criteria.addEqualTo("enrolmentEvaluationType", enrolmentEvaluationType);
        
        return (IEnrolmentEvaluation) queryObject(EnrolmentEvaluation.class, criteria);
    }
}