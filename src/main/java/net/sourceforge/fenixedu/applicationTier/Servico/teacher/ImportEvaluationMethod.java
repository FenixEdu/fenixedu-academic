package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Shift;
import pt.ist.fenixframework.Atomic;

public class ImportEvaluationMethod {

    protected void run(String executionCourseToId, ExecutionCourse executionCourseTo, ExecutionCourse executionCourseFrom,
            Shift shift) {
        if (executionCourseTo != null && executionCourseFrom != null) {
            executionCourseTo.copyEvaluationMethodFrom(executionCourseFrom);
        }
    }

    // Service Invokers migrated from Berserk

    private static final ImportEvaluationMethod serviceInstance = new ImportEvaluationMethod();

    @Atomic
    public static void runImportEvaluationMethod(String executionCourseToId, ExecutionCourse executionCourseTo,
            ExecutionCourse executionCourseFrom, Shift shift) throws NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseToId);
        serviceInstance.run(executionCourseToId, executionCourseTo, executionCourseFrom, shift);
    }

}