package net.sourceforge.fenixedu.applicationTier.Servico.teacher;


import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.LessonPlanning;
import net.sourceforge.fenixedu.domain.ShiftType;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteLessonPlanning {

    protected void run(Integer executionCourseID, LessonPlanning lessonPlanning, ExecutionCourse executionCourse,
            ShiftType shiftType) {
        if (lessonPlanning != null) {
            lessonPlanning.delete();
        } else if (executionCourse != null && shiftType != null) {
            executionCourse.deleteLessonPlanningsByLessonType(shiftType);
        }
    }

    // Service Invokers migrated from Berserk

    private static final DeleteLessonPlanning serviceInstance = new DeleteLessonPlanning();

    @Service
    public static void runDeleteLessonPlanning(Integer executionCourseID, LessonPlanning lessonPlanning,
            ExecutionCourse executionCourse, ShiftType shiftType) throws NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseID);
        serviceInstance.run(executionCourseID, lessonPlanning, executionCourse, shiftType);
    }

}