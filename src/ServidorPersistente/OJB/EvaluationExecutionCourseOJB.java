package ServidorPersistente.OJB;

/**
 * @author Tânia Pousão
 */
import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.ExamExecutionCourse;
import Dominio.IEvaluation;
import Dominio.IEvalutionExecutionCourse;
import Dominio.IExam;
import Dominio.IExecutionCourse;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEvaluationExecutionCourse;

public class EvaluationExecutionCourseOJB extends ObjectFenixOJB implements
        IPersistentEvaluationExecutionCourse
{

    public IEvalutionExecutionCourse readBy(IEvaluation evaluation, IExecutionCourse executionCourse)
            throws ExcepcaoPersistencia
    {

        if (evaluation instanceof IExam)
        {
            Criteria crit = new Criteria();
            crit.addEqualTo("exam.season", ((IExam) evaluation).getSeason());
            crit.addEqualTo("executionCourse.sigla", executionCourse.getSigla());
            crit.addEqualTo("executionCourse.executionPeriod.name", executionCourse.getExecutionPeriod()
                    .getName());
            crit.addEqualTo("executionCourse.executionPeriod.executionYear.year", executionCourse
                    .getExecutionPeriod().getExecutionYear().getYear());
            return (IEvalutionExecutionCourse) queryObject(ExamExecutionCourse.class, crit);

        }
       
            return null;
        
    }

    public List readByExecutionCourse(IExecutionCourse executionCourse) throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        crit.addEqualTo("executionCourse.sigla", executionCourse.getSigla());
        crit.addEqualTo("executionCourse.executionPeriod.name", executionCourse.getExecutionPeriod()
                .getName());
        crit.addEqualTo("executionCourse.executionPeriod.executionYear.year", executionCourse
                .getExecutionPeriod().getExecutionYear().getYear());
        return queryList(ExamExecutionCourse.class, crit);

    }

    public List readAll() throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        return queryList(ExamExecutionCourse.class, crit, "executionCourse.sigla", true);
    }

    public void delete(IEvaluation evaluation) throws ExcepcaoPersistencia
    {
        List evaluationsExecutionCourses = readByCriteria(evaluation);
        for (int i = 0; i < evaluationsExecutionCourses.size(); i++)
        {
            IEvalutionExecutionCourse evalutionExecutionCourse = (IEvalutionExecutionCourse) evaluationsExecutionCourses
                    .get(i);
            super.delete(evalutionExecutionCourse);
        }
    }

    public void delete(IEvalutionExecutionCourse evalutionExecutionCourse) throws ExcepcaoPersistencia
    {
        super.delete(evalutionExecutionCourse);
    }

}