package net.sourceforge.fenixedu.applicationTier.Servico.teacher;


import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionCourseSite;
import pt.ist.fenixWebFramework.services.Service;

public class ImportCustomizationOptions {

    protected void run(Integer executionCourseID, ExecutionCourseSite siteTo, ExecutionCourseSite siteFrom) {
        if (siteTo != null && siteFrom != null) {
            siteTo.copyCustomizationOptionsFrom(siteFrom);
        }
    }

    // Service Invokers migrated from Berserk

    private static final ImportCustomizationOptions serviceInstance = new ImportCustomizationOptions();

    @Service
    public static void runImportCustomizationOptions(Integer executionCourseID, ExecutionCourseSite siteTo,
            ExecutionCourseSite siteFrom) throws NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseID);
        serviceInstance.run(executionCourseID, siteTo, siteFrom);
    }

}