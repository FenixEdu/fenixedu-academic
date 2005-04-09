package net.sourceforge.fenixedu.persistenceTier.OJB;

/**
 * @author Tânia Pousão
 */
import java.util.List;

import net.sourceforge.fenixedu.domain.ExamExecutionCourse;
import net.sourceforge.fenixedu.domain.IEvaluation;
import net.sourceforge.fenixedu.domain.IEvaluationExecutionCourse;
import net.sourceforge.fenixedu.domain.IExam;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEvaluationExecutionCourse;

import org.apache.ojb.broker.query.Criteria;

public class EvaluationExecutionCourseOJB extends PersistentObjectOJB implements
        IPersistentEvaluationExecutionCourse {

    public IEvaluationExecutionCourse readBy(IEvaluation evaluation, IExecutionCourse executionCourse)
            throws ExcepcaoPersistencia {

        if (evaluation instanceof IExam) {
            Criteria crit = new Criteria();
            crit.addEqualTo("exam.season", ((IExam) evaluation).getSeason());
            crit.addEqualTo("executionCourse.sigla", executionCourse.getSigla());
            crit.addEqualTo("executionCourse.executionPeriod.name", executionCourse.getExecutionPeriod()
                    .getName());
            crit.addEqualTo("executionCourse.executionPeriod.executionYear.year", executionCourse
                    .getExecutionPeriod().getExecutionYear().getYear());
            return (IEvaluationExecutionCourse) queryObject(ExamExecutionCourse.class, crit);

        }

        return null;

    }

    public List readByEvaluation(IEvaluation evaluation) throws ExcepcaoPersistencia {

        if (evaluation instanceof IExam) {
            Criteria crit = new Criteria();
            crit.addEqualTo("exam.idInternal", ((IExam) evaluation).getIdInternal());
            return queryList(ExamExecutionCourse.class, crit);

        }
        return null;

    }

    public List readByExecutionCourse(IExecutionCourse executionCourse) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("executionCourse.sigla", executionCourse.getSigla());
        crit.addEqualTo("executionCourse.executionPeriod.name", executionCourse.getExecutionPeriod()
                .getName());
        crit.addEqualTo("executionCourse.executionPeriod.executionYear.year", executionCourse
                .getExecutionPeriod().getExecutionYear().getYear());
        return queryList(ExamExecutionCourse.class, crit);

    }

    public List readAll() throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        return queryList(ExamExecutionCourse.class, crit, "executionCourse.sigla", true);
    }

    public void delete(IEvaluation evaluation) throws ExcepcaoPersistencia {
        List evaluationsExecutionCourses = readByEvaluation(evaluation);
        for (int i = 0; i < evaluationsExecutionCourses.size(); i++) {
            IEvaluationExecutionCourse evalutionExecutionCourse = (IEvaluationExecutionCourse) evaluationsExecutionCourses
                    .get(i);
            super.delete(evalutionExecutionCourse);
        }
    }

    public void delete(IEvaluationExecutionCourse evalutionExecutionCourse) throws ExcepcaoPersistencia {
        super.delete(evalutionExecutionCourse);
    }

}