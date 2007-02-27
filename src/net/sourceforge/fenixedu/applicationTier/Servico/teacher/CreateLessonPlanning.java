package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.LessonPlanning;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.util.MultiLanguageString;

public class CreateLessonPlanning extends Service {

    public void run(Integer executionCourseId, MultiLanguageString title, MultiLanguageString planning, 
	    ShiftType lessonType, ExecutionCourse executionCourse) {
	
        new LessonPlanning(title, planning, lessonType, executionCourse);
    }
}
