/*
 *
 * Created on 2003/08/15
 */

package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;

public class SwitchPublishedExamsFlag {

    @Checked("RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE")
    @Atomic
    public static void run(AcademicInterval academicInterval) {
        final List<ExecutionDegree> executionDegrees = ExecutionDegree.filterByAcademicInterval(academicInterval);
        ExecutionSemester executionSemester = (ExecutionSemester) ExecutionInterval.getExecutionInterval(academicInterval);

        final Boolean isToRemove = new Boolean(executionDegrees.iterator().next().getPublishedExamMapsSet().contains(executionSemester));

        for (final ExecutionDegree executionDegree : executionDegrees) {
            if (isToRemove) {
                executionDegree.getPublishedExamMapsSet().remove(executionSemester);
            } else {
                executionDegree.getPublishedExamMapsSet().add(executionSemester);
            }
        }
    }
}
