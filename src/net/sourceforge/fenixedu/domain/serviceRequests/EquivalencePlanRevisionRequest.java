package net.sourceforge.fenixedu.domain.serviceRequests;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.AcademicServiceRequestType;

import org.joda.time.DateTime;

public class EquivalencePlanRevisionRequest extends EquivalencePlanRevisionRequest_Base {
    
    protected EquivalencePlanRevisionRequest() {
        super();
    }
    
    public EquivalencePlanRevisionRequest(final EquivalencePlanRequest equivalencePlanRequest, final ExecutionYear executionYear, final DateTime requestDate) {
	this(equivalencePlanRequest, executionYear, requestDate, false, false);
    }
    
    public EquivalencePlanRevisionRequest(final EquivalencePlanRequest equivalencePlanRequest, final ExecutionYear executionYear, final DateTime requestDate,
	    final Boolean urgentRequest, final Boolean freeProcessed) {
	this();
	checkParameters(equivalencePlanRequest, executionYear);
	super.init(equivalencePlanRequest.getRegistration(), executionYear, requestDate, urgentRequest, freeProcessed);
	super.setEquivalencePlanRequest(equivalencePlanRequest);
    }
    
    @Override
    public void setEquivalencePlanRequest(EquivalencePlanRequest equivalencePlanRequest) {
	throw new DomainException("error.EquivalencePlanRevisionRequest.cannot.modify.equivalencePlanRequest");
    }

    private void checkParameters(final EquivalencePlanRequest equivalencePlanRequest, final ExecutionYear executionYear) {
	if (equivalencePlanRequest == null) {
	    throw new DomainException("error.EquivalencePlanRevisionRequest.equivalencePlanRequest.cannot.be.null");
	}
	if (executionYear == null) {
	    throw new DomainException("error.EquivalencePlanRevisionRequest.executionYear.cannot.be.null");
	}
    }

    @Override
    public String getDescription() {
	return getDescription(AcademicServiceRequestType.REVISION_EQUIVALENCE_PLAN);
    }

    @Override
    public EventType getEventType() {
	return null;
    }
    
    @Override
    public void delete() {
        super.setEquivalencePlanRequest(null);
        super.delete();
    }
    
    @Override
    public boolean isPossibleToSendToOtherEntity() {
        return true;
    }
}
