package net.sourceforge.fenixedu.applicationTier.Servico.teacher;


import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Shift;
import pt.ist.fenixWebFramework.services.Service;

public class ImportLessonPlannings {

    protected void run(Integer executionCourseID, ExecutionCourse executionCourseTo, ExecutionCourse executionCourseFrom,
            Shift shift) {
        if (executionCourseTo != null && executionCourseFrom != null) {
            if (shift == null) {
                executionCourseTo.copyLessonPlanningsFrom(executionCourseFrom);
            } else {
                executionCourseTo.createLessonPlanningsUsingSummariesFrom(shift);
            }
        }
    }

    // Service Invokers migrated from Berserk

    private static final ImportLessonPlannings serviceInstance = new ImportLessonPlannings();

    @Service
    public static void runImportLessonPlannings(Integer executionCourseID, ExecutionCourse executionCourseTo,
            ExecutionCourse executionCourseFrom, Shift shift) throws NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseID);
        serviceInstance.run(executionCourseID, executionCourseTo, executionCourseFrom, shift);
    }

}