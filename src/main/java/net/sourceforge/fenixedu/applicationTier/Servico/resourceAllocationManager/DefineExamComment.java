package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class DefineExamComment {

    @Checked("RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE")
    @Service
    public static void run(String executionCourseInitials, Integer executionPeriodId, String comment)
            throws FenixServiceException {
        final ExecutionSemester executionSemester = RootDomainObject.getInstance().readExecutionSemesterByOID(executionPeriodId);
        final ExecutionCourse executionCourse = executionSemester.getExecutionCourseByInitials(executionCourseInitials);

        if (executionCourse == null) {
            throw new FenixServiceException("error.noExecutionCourse");
        }
        executionCourse.setComment(comment);
    }

}