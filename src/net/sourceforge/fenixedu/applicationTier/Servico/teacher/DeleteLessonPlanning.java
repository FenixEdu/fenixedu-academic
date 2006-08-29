package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.LessonPlanning;
import net.sourceforge.fenixedu.domain.ShiftType;

public class DeleteLessonPlanning extends Service {

    public void run(Integer executionCourseID, LessonPlanning lessonPlanning, ExecutionCourse executionCourse, ShiftType shiftType) {
        if(lessonPlanning != null) {
            lessonPlanning.delete();
        } else if(executionCourse != null && shiftType != null){
            executionCourse.deleteLessonPlanningsByLessonType(shiftType);
        }
    }    
}
