package net.sourceforge.fenixedu.domain.serviceRequests;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.EquivalencePlanRequestEvent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.AcademicServiceRequestType;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.joda.time.DateTime;

public class EquivalencePlanRequest extends EquivalencePlanRequest_Base {

    protected EquivalencePlanRequest() {
	super();
    }

    public EquivalencePlanRequest(final Registration registration, final ExecutionYear executionYear, final DateTime requestDate) {
	this(registration, executionYear, requestDate, false, false);
    }

    public EquivalencePlanRequest(final Registration registration, final ExecutionYear executionYear, final DateTime requestDate,
	    final Boolean urgentRequest, final Boolean freeProcessed) {
	this();
	checkParameters(executionYear);
	super.init(registration, executionYear, requestDate, urgentRequest, freeProcessed);
	new EquivalencePlanRequestEvent(getAdministrativeOffice(), getPerson(), this);
    }

    private void checkParameters(final ExecutionYear executionYear) {
	if (executionYear == null) {
	    throw new DomainException("error.EquivalencePlanRequest.executionYear.cannot.be.null");
	}
    }

    @Override
    public AcademicServiceRequestType getAcademicServiceRequestType() {
	return AcademicServiceRequestType.EQUIVALENCE_PLAN;
    }

    @Override
    public EventType getEventType() {
	return EventType.EQUIVALENCE_PLAN_REQUEST;
    }

    @Override
    protected void internalChangeState(final AcademicServiceRequestBean academicServiceRequestBean) {

	if (academicServiceRequestBean.isToCancelOrReject() && hasEvent()) {
	    getEvent().cancel(academicServiceRequestBean.getEmployee());

	} else if (academicServiceRequestBean.isToProcess()) {
	    if (isPayable() && !isPayed()) {
		throw new DomainException("AcademicServiceRequest.hasnt.been.payed");
	    }
	    academicServiceRequestBean.setSituationDate(getActiveSituation().getSituationDate().toYearMonthDay());
	}
    }

    @Override
    protected void checkRulesToDelete() {
	if (hasAnyEquivalencePlanRevisionRequests()) {
	    throw new DomainException("error.AcademicServiceRequest.cannot.be.deleted");
	}
    }

    @Override
    public boolean isToPrint() {
	return false;
    }

    @Override
    public boolean isPossibleToSendToOtherEntity() {
	return true;
    }

    @Override
    public boolean isAvailableForTransitedRegistrations() {
	return false;
    }

    @Override
    public boolean isPayedUponCreation() {
	return true;
    }

    @Override
    public boolean hasPersonalInfo() {
	return false;
    }

}
