package ServidorPersistente.OJB;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.ojb.broker.query.Criteria;

import Dominio.EnrolmentEvaluation;
import Dominio.IEnrolment;
import Dominio.IEnrolmentEvaluation;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEnrolmentEvaluation;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.EnrolmentEvaluationState;
import Util.EnrolmentEvaluationType;

/**
 * @author Ângela
 *  
 */

public class EnrolmentEvaluationOJB extends ObjectFenixOJB implements IPersistentEnrolmentEvaluation
{

    public void lockWrite(IEnrolmentEvaluation enrolmentEvaluationToWrite)
        throws ExcepcaoPersistencia, ExistingPersistentException
    {

        IEnrolmentEvaluation enrolmentEvaluationFromDB = null;

        // If there is nothing to write, simply return.
        if (enrolmentEvaluationToWrite == null)
        {
            return;
        }

        // Read Enrolment from database.

        enrolmentEvaluationFromDB =
            this.readByUnique(
                enrolmentEvaluationToWrite.getWhen(),
                enrolmentEvaluationToWrite.getEnrolment());

        //		enrolmentEvaluationFromDB =
        //			(IEnrolmentEvaluation)
        // this.readEnrolmentEvaluationByEnrolmentEvaluationTypeAndGrade(
        //				enrolmentEvaluationToWrite.getEnrolment(),
        //				new
        // EnrolmentEvaluationType(enrolmentEvaluationToWrite.getEnrolmentEvaluationType().getType()),
        //				enrolmentEvaluationToWrite.getGrade());
        // If Enrolment is not in database, then write it.
        if (enrolmentEvaluationFromDB == null)
        {
            super.lockWrite(enrolmentEvaluationToWrite);
            // else If the Enrolment is mapped to the database, then write any
            // existing changes.
        } else if (
            (enrolmentEvaluationToWrite instanceof EnrolmentEvaluation)
                && ((EnrolmentEvaluation) enrolmentEvaluationFromDB).getIdInternal().equals(
                    ((EnrolmentEvaluation) enrolmentEvaluationToWrite).getIdInternal()))
        {
            super.lockWrite(enrolmentEvaluationToWrite);
            // else Throw an already existing exception
        } else
            throw new ExistingPersistentException();
    }

    public void delete(IEnrolmentEvaluation enrolmentEvaluation) throws ExcepcaoPersistencia
    {
        try
        {
            super.delete(enrolmentEvaluation);
        } catch (ExcepcaoPersistencia ex)
        {
            throw ex;
        }
    }

    public List readAll() throws ExcepcaoPersistencia
    {
        return queryList(EnrolmentEvaluation.class, new Criteria());
    }

    public IEnrolmentEvaluation readEnrolmentEvaluationByEnrolmentAndEnrolmentEvaluationTypeAndGrade(
        IEnrolment enrolment,
        EnrolmentEvaluationType evaluationType,
        String grade)
        throws ExcepcaoPersistencia
    {
        //        Criteria criteria = new Criteria();
        //
        //        criteria.addEqualTo(
        //            "enrolment.studentCurricularPlan.student.number",
        //            enrolment.getStudentCurricularPlan().getStudent().getNumber());
        //        criteria.addEqualTo(
        //            "enrolment.studentCurricularPlan.student.degreeType",
        //            enrolment.getStudentCurricularPlan().getStudent().getDegreeType());
        //        criteria.addEqualTo(
        //            "enrolment.studentCurricularPlan.currentState",
        //            enrolment.getStudentCurricularPlan().getCurrentState());
        //        criteria.addEqualTo(
        //            "enrolment.curricularCourseScope.curricularCourse.name",
        //            enrolment.getCurricularCourseScope().getCurricularCourse().getName());
        //        criteria.addEqualTo(
        //            "enrolment.curricularCourseScope.curricularCourse.code",
        //            enrolment.getCurricularCourseScope().getCurricularCourse().getCode());
        //        criteria.addEqualTo(
        //            "enrolment.curricularCourseScope.curricularCourse.degreeCurricularPlan.name",
        //            enrolment
        //                .getCurricularCourseScope()
        //                .getCurricularCourse()
        //                .getDegreeCurricularPlan()
        //                .getName());
        //        criteria.addEqualTo(
        //            "enrolment.curricularCourseScope.curricularCourse.degreeCurricularPlan.degree.nome",
        //            enrolment
        //                .getCurricularCourseScope()
        //                .getCurricularCourse()
        //                .getDegreeCurricularPlan()
        //                .getDegree()
        //                .getNome());
        //        criteria.addEqualTo(
        //            "enrolment.curricularCourseScope.curricularCourse.degreeCurricularPlan.degree.sigla",
        //            enrolment
        //                .getCurricularCourseScope()
        //                .getCurricularCourse()
        //                .getDegreeCurricularPlan()
        //                .getDegree()
        //                .getSigla());
        //        criteria.addEqualTo(
        //            "enrolment.curricularCourseScope.curricularSemester.semester",
        //            enrolment.getCurricularCourseScope().getCurricularSemester().getSemester());
        //        criteria.addEqualTo(
        //            "enrolment.curricularCourseScope.curricularSemester.curricularYear.year",
        //            enrolment.getCurricularCourseScope().getCurricularSemester().getCurricularYear().getYear());
        //        criteria.addEqualTo(
        //            "enrolment.curricularCourseScope.branch.degreeCurricularPlan.name",
        //            enrolment.getCurricularCourseScope().getBranch().getDegreeCurricularPlan().getName());
        //        criteria.addEqualTo(
        //            "enrolment.curricularCourseScope.branch.degreeCurricularPlan.degree.nome",
        //            enrolment
        //                .getCurricularCourseScope()
        //                .getBranch()
        //                .getDegreeCurricularPlan()
        //                .getDegree()
        //                .getNome());
        //        criteria.addEqualTo(
        //            "enrolment.curricularCourseScope.branch.degreeCurricularPlan.degree.sigla",
        //            enrolment
        //                .getCurricularCourseScope()
        //                .getBranch()
        //                .getDegreeCurricularPlan()
        //                .getDegree()
        //                .getSigla());
        //        criteria.addEqualTo(
        //            "enrolment.curricularCourseScope.branch.code",
        //            enrolment.getCurricularCourseScope().getBranch().getCode());
        //        criteria.addEqualTo("enrolmentEvaluationType",
        // evaluationType.getType());
        //        criteria.addEqualTo("grade", grade);
        //
        //        IEnrolmentEvaluation enrolmentEvaluation =
        //            (IEnrolmentEvaluation) queryObject(EnrolmentEvaluation.class,
        // criteria);
        //        return enrolmentEvaluation;

        Criteria criteria = new Criteria();
        criteria.addEqualTo("enrolmentKey", enrolment.getIdInternal());
        criteria.addEqualTo("enrolmentEvaluationType", evaluationType.getType());
        criteria.addEqualTo("grade", grade);

        IEnrolmentEvaluation enrolmentEvaluation =
            (IEnrolmentEvaluation) queryObject(EnrolmentEvaluation.class, criteria);

        return enrolmentEvaluation;
    }

    public List readEnrolmentEvaluationByEnrolment(IEnrolment enrolment) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("enrolmentKey", enrolment.getIdInternal());
        List result = queryList(EnrolmentEvaluation.class, criteria);
        return result;
    }

    public List readEnrolmentEvaluationByEnrolmentEvaluationState(
        IEnrolment enrolment,
        EnrolmentEvaluationState evaluationState)
        throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("enrolmentKey", enrolment.getIdInternal());
        criteria.addEqualTo("enrolmentEvaluationState", evaluationState.getState());

        List examsWithRepetition = queryList(EnrolmentEvaluation.class, criteria);
        return examsWithRepetition;
    }

    public IEnrolmentEvaluation readByUnique(Date whenAlter, IEnrolment enrolment)
        throws ExcepcaoPersistencia
    {
        try
        {
            Criteria criteria = new Criteria();
            criteria.addEqualTo("enrolmentKey", enrolment.getIdInternal());
            criteria.addEqualTo("when", whenAlter);
            IEnrolmentEvaluation evaluationsWithRepetition =
                (IEnrolmentEvaluation) queryObject(EnrolmentEvaluation.class, criteria);

            return evaluationsWithRepetition;
        } catch (ExcepcaoPersistencia e)
        {
            throw e;
        }
    }

    public IEnrolmentEvaluation readEnrolmentEvaluationByEnrolmentAndEnrolmentEvaluationTypeAndGradeAndWhenAlteredDate(
        IEnrolment enrolment,
        EnrolmentEvaluationType evaluationType,
        String grade,
        Date whenAltered)
        throws ExcepcaoPersistencia
    {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("enrolmentKey", enrolment.getIdInternal());
        criteria.addEqualTo("enrolmentEvaluationType", evaluationType.getType());
        criteria.addEqualTo("grade", grade);
        criteria.addEqualTo("when", whenAltered);

        IEnrolmentEvaluation enrolmentEvaluation =
            (IEnrolmentEvaluation) queryObject(EnrolmentEvaluation.class, criteria);

        return enrolmentEvaluation;
    }

    public List readAlreadySubmitedMarks(List enrolmentIds) throws ExcepcaoPersistencia
    {
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
}