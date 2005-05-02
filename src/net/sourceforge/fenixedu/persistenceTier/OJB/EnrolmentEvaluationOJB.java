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


    public void delete(IEnrolmentEvaluation enrolmentEvaluation) throws ExcepcaoPersistencia {
        try {
            super.delete(enrolmentEvaluation);
        } catch (ExcepcaoPersistencia ex) {
            throw ex;
        }
    }

    public List readAll() throws ExcepcaoPersistencia {
        return queryList(EnrolmentEvaluation.class, new Criteria());
    }

    public IEnrolmentEvaluation readEnrolmentEvaluationByEnrolmentAndEnrolmentEvaluationTypeAndGrade(
            IEnrolment enrolment, EnrolmentEvaluationType evaluationType, String grade)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("enrolment.idInternal", enrolment.getIdInternal());
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

    public List readEnrolmentEvaluationByEnrolment(IEnrolment enrolment) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("enrolment.idInternal", enrolment.getIdInternal());
        List result = queryList(EnrolmentEvaluation.class, criteria);
        return result;
    }

    public List readEnrolmentEvaluationByEnrolmentEvaluationState(IEnrolment enrolment,
            EnrolmentEvaluationState evaluationState) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("enrolment.idInternal", enrolment.getIdInternal());
        criteria.addEqualTo("enrolmentEvaluationState", evaluationState);

        List examsWithRepetition = queryList(EnrolmentEvaluation.class, criteria);
        return examsWithRepetition;
    }

    public IEnrolmentEvaluation readByUnique(Date whenAlter, IEnrolment enrolment)
            throws ExcepcaoPersistencia {
        try {
            Criteria criteria = new Criteria();
            criteria.addEqualTo("enrolment.idInternal", enrolment.getIdInternal());
            criteria.addEqualTo("when", whenAlter);
            IEnrolmentEvaluation evaluationsWithRepetition = (IEnrolmentEvaluation) queryObject(
                    EnrolmentEvaluation.class, criteria);

            return evaluationsWithRepetition;
        } catch (ExcepcaoPersistencia e) {
            throw e;
        }
    }

    public IEnrolmentEvaluation readEnrolmentEvaluationByEnrolmentAndEnrolmentEvaluationTypeAndGradeAndWhenAlteredDate(
            IEnrolment enrolment, EnrolmentEvaluationType evaluationType, String grade, Date whenAltered)
            throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("enrolment.idInternal", enrolment.getIdInternal());
        //		criteria.addEqualTo("enrolmentEvaluationType",
        // evaluationType.getType());
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
    
    /* (non-Javadoc)
     * @see ServidorPersistente.IPersistentEnrolmentEvaluation#readEnrolmentEvaluationByEnrolmentEvaluationStateAndType(Dominio.IEnrollment, Util.EnrolmentEvaluationState, Util.EnrolmentEvaluationType)
     */
    public IEnrolmentEvaluation readEnrolmentEvaluationByEnrolmentEvaluationStateAndType(
            IEnrolment enrolment, EnrolmentEvaluationState temporary_obj,
            EnrolmentEvaluationType enrolmentEvaluationType) throws ExcepcaoPersistencia{
        Criteria criteria = new Criteria();
        criteria.addEqualTo("enrolment.idInternal", enrolment.getIdInternal());
        criteria.addEqualTo("enrolmentEvaluationState", temporary_obj);
        criteria.addEqualTo("enrolmentEvaluationType", enrolmentEvaluationType);
        
        return (IEnrolmentEvaluation) queryObject(EnrolmentEvaluation.class, criteria);
    }
}