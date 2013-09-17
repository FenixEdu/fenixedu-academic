package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionCourseSite;
import pt.ist.fenixframework.Atomic;

public class ImportCustomizationOptions {

    protected void run(String executionCourseID, ExecutionCourseSite siteTo, ExecutionCourseSite siteFrom) {
        if (siteTo != null && siteFrom != null) {
            siteTo.copyCustomizationOptionsFrom(siteFrom);
        }
    }

    // Service Invokers migrated from Berserk

    private static final ImportCustomizationOptions serviceInstance = new ImportCustomizationOptions();

    @Atomic
    public static void runImportCustomizationOptions(String executionCourseID, ExecutionCourseSite siteTo,
            ExecutionCourseSite siteFrom) throws NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseID);
        serviceInstance.run(executionCourseID, siteTo, siteFrom);
    }

}