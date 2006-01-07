package net.sourceforge.fenixedu.persistenceTier;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType;
import net.sourceforge.fenixedu.util.EnrolmentEvaluationState;

/**
 * @author Ângela
 */

public interface IPersistentEnrolmentEvaluation extends IPersistentObject {

    public EnrolmentEvaluation readEnrolmentEvaluationByEnrolmentAndEnrolmentEvaluationTypeAndGrade(
            Integer enrolmentId, EnrolmentEvaluationType evaluationType, String grade)
           throws ExcepcaoPersistencia;

    public List readEnrolmentEvaluationByEnrolmentEvaluationState(Integer enrolmentId,
            EnrolmentEvaluationState evaluationState) throws ExcepcaoPersistencia;

    public EnrolmentEvaluation readEnrolmentEvaluationByEnrolmentAndEnrolmentEvaluationTypeAndGradeAndWhenAlteredDate(
            Integer enrolmentId, EnrolmentEvaluationType evaluationType, String grade, Date whenAltered)
            throws ExcepcaoPersistencia;

    public List readAlreadySubmitedMarks(List enrolmentIds) throws ExcepcaoPersistencia;

    public EnrolmentEvaluation readEnrolmentEvaluationByEnrolmentEvaluationStateAndType(Integer enrolmentId, EnrolmentEvaluationState temporary_obj, EnrolmentEvaluationType enrolmentEvaluationType) throws ExcepcaoPersistencia;
}