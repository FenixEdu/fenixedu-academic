package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class CreateSites extends Service {

    public Integer run(final Integer executionPeriodID) {
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
