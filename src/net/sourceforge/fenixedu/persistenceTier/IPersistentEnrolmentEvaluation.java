package net.sourceforge.fenixedu.persistenceTier;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.IEnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType;
import net.sourceforge.fenixedu.util.EnrolmentEvaluationState;

/**
 * @author Ângela
 */

public interface IPersistentEnrolmentEvaluation extends IPersistentObject {

    public IEnrolmentEvaluation readEnrolmentEvaluationByEnrolmentAndEnrolmentEvaluationTypeAndGrade(
            Integer enrolmentId, EnrolmentEvaluationType evaluationType, String grade)
           throws ExcepcaoPersistencia;

    public List readEnrolmentEvaluationByEnrolmentEvaluationState(Integer enrolmentId,
            EnrolmentEvaluationState evaluationState) throws ExcepcaoPersistencia;

    public IEnrolmentEvaluation readEnrolmentEvaluationByEnrolmentAndEnrolmentEvaluationTypeAndGradeAndWhenAlteredDate(
            Integer enrolmentId, EnrolmentEvaluationType evaluationType, String grade, Date whenAltered)
            throws ExcepcaoPersistencia;

    public List readAlreadySubmitedMarks(List enrolmentIds) throws ExcepcaoPersistencia;

    public IEnrolmentEvaluation readEnrolmentEvaluationByEnrolmentEvaluationStateAndType(Integer enrolmentId, EnrolmentEvaluationState temporary_obj, EnrolmentEvaluationType enrolmentEvaluationType) throws ExcepcaoPersistencia;
}