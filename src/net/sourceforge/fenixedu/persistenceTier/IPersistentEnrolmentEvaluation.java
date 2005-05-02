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

    public void delete(IEnrolmentEvaluation enrolmentEvaluation) throws ExcepcaoPersistencia;

    public List readAll() throws ExcepcaoPersistencia;

    public IEnrolmentEvaluation readEnrolmentEvaluationByEnrolmentAndEnrolmentEvaluationTypeAndGrade(
            IEnrolment enrolment, EnrolmentEvaluationType evaluationType, String grade)
            throws ExcepcaoPersistencia;

    public List readEnrolmentEvaluationByEnrolmentEvaluationState(IEnrolment enrolment,
            EnrolmentEvaluationState evaluationState) throws ExcepcaoPersistencia;

    public List readEnrolmentEvaluationByEnrolment(IEnrolment enrolment) throws ExcepcaoPersistencia;

    public IEnrolmentEvaluation readEnrolmentEvaluationByEnrolmentAndEnrolmentEvaluationTypeAndGradeAndWhenAlteredDate(
            IEnrolment enrolment, EnrolmentEvaluationType evaluationType, String grade, Date whenAltered)
            throws ExcepcaoPersistencia;

    public List readAlreadySubmitedMarks(List enrolmentIds) throws ExcepcaoPersistencia;

    /**
     * @param enrolment
     * @param temporary_obj
     * @param enrolmentEvaluationType
     * @return
     */
    public IEnrolmentEvaluation readEnrolmentEvaluationByEnrolmentEvaluationStateAndType(IEnrolment enrolment, EnrolmentEvaluationState temporary_obj, EnrolmentEvaluationType enrolmentEvaluationType) throws ExcepcaoPersistencia;
}