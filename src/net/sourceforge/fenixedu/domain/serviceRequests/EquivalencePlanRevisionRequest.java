package net.sourceforge.fenixedu.domain.serviceRequests;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.student.Registration;

public class EquivalencePlanRevisionRequest extends EquivalencePlanRevisionRequest_Base {
    
    protected EquivalencePlanRevisionRequest() {
        super();
    }
    
    public EquivalencePlanRevisionRequest(final Registration registration, final ExecutionYear executionYear,
	    final Boolean urgentRequest, final Boolean freeProcessed) {
	this();
	checkParameters(executionYear);
	super.init(registration, executionYear, urgentRequest, freeProcessed);
    }

    private void checkParameters(final ExecutionYear executionYear) {
	if (executionYear == null) {
	    throw new RuntimeException("error.StudentReingressionRequest.executionYear.cannot.be.null");
	}
    }

    @Override
    public String getDescription() {
	return getDescription("AcademicServiceRequestType.REVISION_EQUIVALENCE_PLAN");
    }

    @Override
    public EventType getEventType() {
	return EventType.REVISION_EQUIVALENCE_PLAN_REQUEST;
    }
}
