package ServidorPersistente.OJB;

/**
 * @author Tânia Pousão
 */
import java.util.ArrayList;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.EvaluationExecutionCourse;
import Dominio.ExamExecutionCourse;
import Dominio.IEvaluation;
import Dominio.IEvalutionExecutionCourse;
import Dominio.IExam;
import Dominio.IExecutionCourse;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEvaluationExecutionCourse;
import ServidorPersistente.exceptions.ExistingPersistentException;

public class EvaluationExecutionCourseOJB
    extends ObjectFenixOJB
    implements IPersistentEvaluationExecutionCourse
{

    public IEvalutionExecutionCourse readBy(IEvaluation evaluation, IExecutionCourse executionCourse)
        throws ExcepcaoPersistencia
    {

        if (evaluation instanceof IExam)
        {
            Criteria crit = new Criteria();
            crit.addEqualTo("exam.season", ((IExam) evaluation).getSeason());
            crit.addEqualTo("executionCourse.sigla", executionCourse.getSigla());
            crit.addEqualTo(
                "executionCourse.executionPeriod.name",
                executionCourse.getExecutionPeriod().getName());
            crit.addEqualTo(
                "executionCourse.executionPeriod.executionYear.year",
                executionCourse.getExecutionPeriod().getExecutionYear().getYear());
            return (IEvalutionExecutionCourse) queryObject(ExamExecutionCourse.class, crit);

        }
        else
        {
            return null;
        }
    }

    public List readByExecutionCourse(IExecutionCourse executionCourse) throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        crit.addEqualTo("executionCourse.sigla", executionCourse.getSigla());
        crit.addEqualTo(
            "executionCourse.executionPeriod.name",
            executionCourse.getExecutionPeriod().getName());
        crit.addEqualTo(
            "executionCourse.executionPeriod.executionYear.year",
            executionCourse.getExecutionPeriod().getExecutionYear().getYear());
        return queryList(ExamExecutionCourse.class, crit);

    }

    public List readAll() throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        crit.addOrderBy("executionCourse.sigla", true);
        return queryList(ExamExecutionCourse.class, crit);
    }

    public void lockWrite(IEvalutionExecutionCourse evalutionExecutionCourseToWrite)
        throws ExcepcaoPersistencia, ExistingPersistentException
    {

        IEvalutionExecutionCourse evalutionExecutionCourseFromDB = null;

        if (evalutionExecutionCourseToWrite == null)
            return;

        //read evaluation
        evalutionExecutionCourseFromDB =
            this.readBy(
                evalutionExecutionCourseToWrite.getEvaluation(),
                evalutionExecutionCourseToWrite.getExecutionCourse());

        //if (evaluation not in database) then write it
        if (evalutionExecutionCourseFromDB == null)
        {

            super.lockWrite(evalutionExecutionCourseToWrite);
            tx.commit();
            tx.begin();

            IEvaluation evaluation = evalutionExecutionCourseToWrite.getEvaluation();
            IExecutionCourse executionCourse = evalutionExecutionCourseToWrite.getExecutionCourse();

            List associatedExecutionCourses =
                evalutionExecutionCourseToWrite.getEvaluation().getAssociatedExecutionCourses();
            List associatedEvaluations =
                evalutionExecutionCourseToWrite.getExecutionCourse().getAssociatedExams();

            if (associatedEvaluations == null)
            {
                associatedEvaluations = new ArrayList();
                executionCourse.setAssociatedExams(associatedEvaluations);
            }
            associatedEvaluations.add(evaluation);

            if (associatedExecutionCourses == null)
            {
                associatedExecutionCourses = new ArrayList();
                evaluation.setAssociatedExecutionCourses(associatedExecutionCourses);
            }
            associatedExecutionCourses.add(executionCourse);
        }

        // else if (evaluation is mapped to the database then write any
        // existing changes)
        else if (
            (evalutionExecutionCourseToWrite instanceof EvaluationExecutionCourse)
                && ((EvaluationExecutionCourse) evalutionExecutionCourseFromDB).getIdInternal().equals(
                    ((EvaluationExecutionCourse) evalutionExecutionCourseToWrite).getIdInternal()))
        {
            evalutionExecutionCourseFromDB.setEvaluation(
                evalutionExecutionCourseToWrite.getEvaluation());
            evalutionExecutionCourseFromDB.setExecutionCourse(
                evalutionExecutionCourseToWrite.getExecutionCourse());
            // else throw an AlreadyExists exception.
        }
        else
            throw new ExistingPersistentException();
    }

    public void delete(IEvaluation evaluation) throws ExcepcaoPersistencia
    {
        List evaluationsExecutionCourses = readByCriteria(evaluation);
        for (int i = 0; i < evaluationsExecutionCourses.size(); i++)
        {
            IEvalutionExecutionCourse evalutionExecutionCourse =
                (IEvalutionExecutionCourse) evaluationsExecutionCourses.get(i);
            super.delete(evalutionExecutionCourse);
        }
    }

    public void delete(IEvalutionExecutionCourse evalutionExecutionCourse) throws ExcepcaoPersistencia
    {
        super.delete(evalutionExecutionCourse);
    }

}
