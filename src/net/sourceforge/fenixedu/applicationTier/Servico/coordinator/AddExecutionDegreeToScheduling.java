package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Scheduleing;

public class AddExecutionDegreeToScheduling extends Service {

    private class SchedulingContainsProposalsException extends FenixServiceException {
    }

    public void run(final Scheduleing scheduleing, final ExecutionDegree executionDegree)
	    throws SchedulingContainsProposalsException {
	if (!scheduleing.getProposalsSet().isEmpty()
		|| (executionDegree.getScheduling() != null && executionDegree.getScheduling().getProposalsSet().isEmpty())) {
	    throw new SchedulingContainsProposalsException();
	}
	if (!scheduleing.getExecutionDegrees().contains(executionDegree)) {
	    scheduleing.getExecutionDegrees().add(executionDegree);
	}
    }

}