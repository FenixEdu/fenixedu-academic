package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.CourseLoad;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteCourseLoad extends FenixService {

	@Checked("RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE")
	@Service
	public static void run(CourseLoad courseLoad) {
		if (courseLoad != null) {
			courseLoad.delete();
		}
	}
}