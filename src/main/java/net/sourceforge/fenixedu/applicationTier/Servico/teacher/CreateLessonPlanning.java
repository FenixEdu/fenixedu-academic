package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.LessonPlanning;
import net.sourceforge.fenixedu.domain.ShiftType;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class CreateLessonPlanning {

    protected void run(String executionCourseId, MultiLanguageString title, MultiLanguageString planning, ShiftType lessonType,
            ExecutionCourse executionCourse) {

        new LessonPlanning(title, planning, lessonType, executionCourse);
    }

    // Service Invokers migrated from Berserk

    private static final CreateLessonPlanning serviceInstance = new CreateLessonPlanning();

    @Service
    public static void runCreateLessonPlanning(String executionCourseId, MultiLanguageString title, MultiLanguageString planning,
            ShiftType lessonType, ExecutionCourse executionCourse) throws NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseId);
        serviceInstance.run(executionCourseId, title, planning, lessonType, executionCourse);
    }

}