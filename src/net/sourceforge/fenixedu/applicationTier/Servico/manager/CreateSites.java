package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class CreateSites extends FenixService {

    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Service
    public static Integer run(final Integer executionPeriodID) {
	final ExecutionSemester executionSemester = rootDomainObject.readExecutionSemesterByOID(executionPeriodID);

	int numberCreatedSites = 0;
	for (final ExecutionCourse executionCourse : executionSemester.getAssociatedExecutionCourses()) {
	    if (executionCourse.getSite() == null) {
		executionCourse.createSite();
		numberCreatedSites++;
	    }
	}

	return Integer.valueOf(numberCreatedSites);
    }

}