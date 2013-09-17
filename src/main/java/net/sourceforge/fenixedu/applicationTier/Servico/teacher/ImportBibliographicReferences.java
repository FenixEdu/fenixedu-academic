package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Shift;
import pt.ist.fenixframework.Atomic;

public class ImportBibliographicReferences {

    protected void run(String executionCourseToId, ExecutionCourse executionCourseTo, ExecutionCourse executionCourseFrom,
            Shift shift) {
        if (executionCourseTo != null && executionCourseFrom != null) {
            executionCourseTo.copyBibliographicReferencesFrom(executionCourseFrom);
        }
    }

    // Service Invokers migrated from Berserk

    private static final ImportBibliographicReferences serviceInstance = new ImportBibliographicReferences();

    @Atomic
    public static void runImportBibliographicReferences(String executionCourseToId, ExecutionCourse executionCourseTo,
            ExecutionCourse executionCourseFrom, Shift shift) throws NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseToId);
        serviceInstance.run(executionCourseToId, executionCourseTo, executionCourseFrom, shift);
    }

}