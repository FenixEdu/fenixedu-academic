/*
 *
 * Created on 2003/08/15
 */

package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import pt.ist.fenixWebFramework.services.Service;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

/**
 * 
 * @author Luis Cruz
 */
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;

public class SwitchPublishedExamsFlag extends FenixService {

    @Checked("RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE")
    @Service
    public static void run(final Integer executionPeriodOID) {
	final ExecutionSemester executionSemester = rootDomainObject.readExecutionSemesterByOID(executionPeriodOID);
	final ExecutionYear executionYear = executionSemester.getExecutionYear();
	final List<ExecutionDegree> executionDegrees = ExecutionDegree.getAllByExecutionYear(executionYear.getYear());

	if (!executionDegrees.isEmpty()) {
	    final Boolean examsPublicationState = new Boolean(!executionDegrees.get(0).getTemporaryExamMap().booleanValue());

	    for (final ExecutionDegree executionDegree : executionDegrees) {
		executionDegree.setTemporaryExamMap(examsPublicationState);
	    }
	}
    }
}