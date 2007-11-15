package net.sourceforge.fenixedu.domain.serviceRequests;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.EquivalencePlanRequestEvent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.AcademicServiceRequestType;
import net.sourceforge.fenixedu.domain.student.Registration;

public class EquivalencePlanRequest extends EquivalencePlanRequest_Base {
    
    protected EquivalencePlanRequest() {
        super();
    }
    
    public EquivalencePlanRequest(final Registration registration, final ExecutionYear executionYear) {
	this(registration, executionYear, false, false);
    }
    
    public EquivalencePlanRequest(final Registration registration, final ExecutionYear executionYear,
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
	return getDescription(AcademicServiceRequestType.EQUIVALENCE_PLAN);
    }

    @Override
    public EventType getEventType() {
	return EventType.EQUIVALENCE_PLAN_REQUEST;
    }
    
    @Override
    protected void internalChangeState(final AcademicServiceRequestBean academicServiceRequestBean) {

	if (academicServiceRequestBean.isToCancelOrReject() && hasEvent()) {
	    getEvent().cancel(academicServiceRequestBean.getEmployee());
	}
	
	if (academicServiceRequestBean.isToProcess()) {
	    if (hasEvent()) {
		throw new DomainException("AcademicServiceRequest.already.has.event");
	    }
	    new EquivalencePlanRequestEvent(getAdministrativeOffice(), getPerson(), this);
	}
    }
    
}
