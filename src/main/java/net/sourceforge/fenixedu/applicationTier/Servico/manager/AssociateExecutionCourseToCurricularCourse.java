package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class AssociateExecutionCourseToCurricularCourse {

    @Atomic
    public static void run(String executionCourseId, String curricularCourseId, String executionPeriodId)
            throws FenixServiceException {
        check(RolePredicates.MANAGER_OR_OPERATOR_PREDICATE);

        final CurricularCourse curricularCourse = (CurricularCourse) FenixFramework.getDomainObject(curricularCourseId);
        if (curricularCourse == null) {
            throw new NonExistingServiceException("message.nonExistingCurricularCourse", null);
        }

        final ExecutionSemester executionSemester = FenixFramework.getDomainObject(executionPeriodId);
        if (executionSemester == null) {
            throw new NonExistingServiceException("message.nonExistingExecutionPeriod", null);
        }

        Collection<ExecutionCourse> executionCourses = curricularCourse.getAssociatedExecutionCourses();
        for (ExecutionCourse executionCourse : executionCourses) {
            if (executionCourse.getExecutionPeriod() == executionSemester) {
                throw new ExistingServiceException("message.unavailable.execution.period", null);
            }
        }

        final ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseId);
        if (executionCourse == null) {
            throw new NonExistingServiceException("message.nonExisting.executionCourse", null);
        }

        if (!curricularCourse.getAssociatedExecutionCoursesSet().contains(executionCourse)) {
            curricularCourse.addAssociatedExecutionCourses(executionCourse);
        }
    }

}