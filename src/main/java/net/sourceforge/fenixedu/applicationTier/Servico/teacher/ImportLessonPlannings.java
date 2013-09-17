package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Shift;
import pt.ist.fenixframework.Atomic;

public class ImportLessonPlannings {

    protected void run(String executionCourseID, ExecutionCourse executionCourseTo, ExecutionCourse executionCourseFrom,
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

    @Atomic
    public static void runImportLessonPlannings(String executionCourseID, ExecutionCourse executionCourseTo,
            ExecutionCourse executionCourseFrom, Shift shift) throws NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseID);
        serviceInstance.run(executionCourseID, executionCourseTo, executionCourseFrom, shift);
    }

}