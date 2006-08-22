package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.LessonPlanning;
import net.sourceforge.fenixedu.domain.ShiftType;

public class CreateLessonPlanning extends Service {

    public void run(Integer executionCourseId, String title, String planning, ShiftType lessonType, ExecutionCourse executionCourse) {
        new LessonPlanning(title, planning, lessonType, executionCourse);
    }
}
