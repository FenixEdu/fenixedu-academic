package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Shift;

public class ImportLessonPlanning extends Service {

    public void run(Integer executionCourseID, ExecutionCourse executionCourseTo, ExecutionCourse executionCourseFrom, Shift shift) {
        if(executionCourseTo != null && executionCourseFrom != null) {
            if(shift == null) {
                executionCourseTo.copyLessonPlanningsFrom(executionCourseFrom);
            } else {
                executionCourseTo.createLessonPlanningsUsingSummariesFrom(shift);
            }
        }
    }
}
