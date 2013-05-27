package net.sourceforge.fenixedu.applicationTier.Servico.teacher;


import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseCoordinatorAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.AdHocEvaluation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.services.Service;

public class CreateAdHocEvaluation {

    protected void run(Integer executionCourseID, String name, String description, GradeScale gradeScale)
            throws FenixServiceException {

        ExecutionCourse executionCourse = RootDomainObject.getInstance().readExecutionCourseByOID(executionCourseID);

        if (executionCourse == null) {
            throw new FenixServiceException("error.noExecutionCourse");
        }

        new AdHocEvaluation(executionCourse, name, description, gradeScale);
    }

    // Service Invokers migrated from Berserk

    private static final CreateAdHocEvaluation serviceInstance = new CreateAdHocEvaluation();

    @Service
    public static void runCreateAdHocEvaluation(Integer executionCourseID, String name, String description, GradeScale gradeScale)
            throws FenixServiceException, NotAuthorizedException {
        try {
            ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseID);
            serviceInstance.run(executionCourseID, name, description, gradeScale);
        } catch (NotAuthorizedException ex1) {
            try {
                ExecutionCourseCoordinatorAuthorizationFilter.instance.execute(executionCourseID);
                serviceInstance.run(executionCourseID, name, description, gradeScale);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

}