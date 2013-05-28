package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class AssociateExecutionCourseToCurricularCourse {

    @Checked("RolePredicates.MANAGER_OR_OPERATOR_PREDICATE")
    @Service
    public static void run(Integer executionCourseId, Integer curricularCourseId, Integer executionPeriodId)
            throws FenixServiceException {

        final CurricularCourse curricularCourse = (CurricularCourse) AbstractDomainObject.fromExternalId(curricularCourseId);
        if (curricularCourse == null) {
            throw new NonExistingServiceException("message.nonExistingCurricularCourse", null);
        }

        final ExecutionSemester executionSemester = AbstractDomainObject.fromExternalId(executionPeriodId);
        if (executionSemester == null) {
            throw new NonExistingServiceException("message.nonExistingExecutionPeriod", null);
        }

        List<ExecutionCourse> executionCourses = curricularCourse.getAssociatedExecutionCourses();
        for (ExecutionCourse executionCourse : executionCourses) {
            if (executionCourse.getExecutionPeriod() == executionSemester) {
                throw new ExistingServiceException("message.unavailable.execution.period", null);
            }
        }

        final ExecutionCourse executionCourse = AbstractDomainObject.fromExternalId(executionCourseId);
        if (executionCourse == null) {
            throw new NonExistingServiceException("message.nonExisting.executionCourse", null);
        }

        if (!curricularCourse.hasAssociatedExecutionCourses(executionCourse)) {
            curricularCourse.addAssociatedExecutionCourses(executionCourse);
        }
    }

}