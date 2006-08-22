package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.LessonPlanning;

public class DeleteLessonPlanning extends Service {

    public void run(Integer executionCourseID, LessonPlanning lessonPlanning) {
        lessonPlanning.delete();
    }
    
}
