/*
 * ExamExecutionCourseVO.java
 * 
 * Created on 2005/05/16
 */

package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

/**
 * @author Pedro Santos & Rita Carvalho
 */
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExam;
import net.sourceforge.fenixedu.domain.IExamExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExamExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

public class ExamExecutionCourseVO extends VersionedObjectsBase implements
        IPersistentExamExecutionCourse {

    public List readByExecutionCourse(String executionCourseAcronym, String executionPeriodName,
            String year) throws ExcepcaoPersistencia {

        List<IExamExecutionCourse> result = new ArrayList();
        List<IExecutionCourse> executionCourses = (List<IExecutionCourse>) readAll(ExecutionCourse.class);
        for (IExecutionCourse executionCourse : executionCourses) {
            if (executionCourse.getSigla().equals(executionCourseAcronym)
                    && executionCourse.getExecutionPeriod().getName().equals(executionPeriodName)
                    && executionCourse.getExecutionPeriod().getExecutionYear().getYear().equals(year)) {
                List<IExam> exams = executionCourse.getAssociatedExams();
                for (IExam exam : exams) {
                    List<IExamExecutionCourse> examExecutionCourses = exam.getExamExecutionCourses();
                    for (IExamExecutionCourse examExecutionCourse : examExecutionCourses) {
                        result.add(examExecutionCourse);
                    }
                }
            }
        }
        return result;
        /*
         * Criteria crit = new Criteria();
         * crit.addEqualTo("executionCourse.sigla", executionCourse.getSigla());
         * crit.addEqualTo("executionCourse.executionPeriod.name",
         * executionCourse.getExecutionPeriod() .getName());
         * crit.addEqualTo("executionCourse.executionPeriod.executionYear.year",
         * executionCourse .getExecutionPeriod().getExecutionYear().getYear());
         * return queryList(ExamExecutionCourse.class, crit);
         */

    }
}