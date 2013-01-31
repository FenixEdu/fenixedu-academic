/*
 *
 * Created on 2003/08/15
 */

package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class SwitchPublishedExamsFlag extends FenixService {

	@Checked("RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE")
	@Service
	public static void run(AcademicInterval academicInterval) {
		final List<ExecutionDegree> executionDegrees = ExecutionDegree.filterByAcademicInterval(academicInterval);

		if (!executionDegrees.isEmpty()) {
			final Boolean examsPublicationState = new Boolean(!executionDegrees.get(0).getTemporaryExamMap().booleanValue());

			for (final ExecutionDegree executionDegree : executionDegrees) {
				executionDegree.setTemporaryExamMap(examsPublicationState);
			}
		}
	}
}