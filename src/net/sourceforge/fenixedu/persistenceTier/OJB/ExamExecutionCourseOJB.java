/*
 * ExamExecutionCourseOJB.java
 * 
 * Created on 2003/03/29
 */

package net.sourceforge.fenixedu.persistenceTier.OJB;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
import java.util.List;

import net.sourceforge.fenixedu.domain.ExamExecutionCourse;
import net.sourceforge.fenixedu.domain.IExam;
import net.sourceforge.fenixedu.domain.IExamExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExamExecutionCourse;

import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;

public class ExamExecutionCourseOJB extends PersistentObjectOJB implements
        IPersistentExamExecutionCourse {

    public IExamExecutionCourse readBy(IExam exam, IExecutionCourse executionCourse)
            throws ExcepcaoPersistencia {

        Criteria crit = new Criteria();
        crit.addEqualTo("exam.season", exam.getSeason());
        crit.addEqualTo("executionCourse.sigla", executionCourse.getSigla());
        crit.addEqualTo("executionCourse.executionPeriod.name", executionCourse.getExecutionPeriod()
                .getName());
        crit.addEqualTo("executionCourse.executionPeriod.executionYear.year", executionCourse
                .getExecutionPeriod().getExecutionYear().getYear());
        return (IExamExecutionCourse) queryObject(ExamExecutionCourse.class, crit);

    }

    public List readByExam(IExam exam) throws ExcepcaoPersistencia {

        Criteria crit = new Criteria();
        crit.addEqualTo("exam.idInternal", exam.getIdInternal());

        return (List) queryList(ExamExecutionCourse.class, crit);

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
        QueryByCriteria queryByCriteria = new QueryByCriteria(ExamExecutionCourse.class, null);
        queryByCriteria.addOrderBy("executionCourse.sigla", true);
        queryByCriteria.addOrderBy("exam.season", true);
        return queryList(queryByCriteria);
    }

    public void delete(IExam exam) throws ExcepcaoPersistencia {
        List examsExecutionCourses = readByExam(exam);
        for (int i = 0; i < examsExecutionCourses.size(); i++) {
            IExamExecutionCourse examExecutionCourse = (IExamExecutionCourse) examsExecutionCourses
                    .get(i);
            super.delete(examExecutionCourse);
        }
    }

    public void delete(IExamExecutionCourse examExecutionCourse) throws ExcepcaoPersistencia {
        super.delete(examExecutionCourse);
    }

}