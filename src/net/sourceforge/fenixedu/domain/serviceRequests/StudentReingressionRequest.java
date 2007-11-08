package net.sourceforge.fenixedu.domain.serviceRequests;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.StudentReingressionRequestEvent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;

public class StudentReingressionRequest extends StudentReingressionRequest_Base {
    
    protected StudentReingressionRequest() {
        super();
    }
    
    public StudentReingressionRequest(final Registration registration, final ExecutionYear executionYear, final Boolean urgentRequest, final Boolean freeProcessed) {
	this();
	checkParameters(executionYear);
	checkRulesToCreate(registration);
	super.init(registration, executionYear, urgentRequest, freeProcessed);
    }

    private void checkParameters(final ExecutionYear executionYear) {
	if (executionYear == null) {
	    throw new RuntimeException("error.StudentReingressionRequest.executionYear.cannot.be.null");
	}
    }

    private void checkRulesToCreate(final Registration registration) {
	
	if (!hasValidState(registration)) {
	    throw new DomainException("error.StudentReingressionRequest.registration.with.invalid.state");
	}
	
	if (wasFlunkedAtLeastThreeTimes(registration)) {
	    throw new DomainException("error.StudentReingressionRequest.registration.was.flunked.at.least.three.times");
	}
	
	if (!isEnrolmentPeriodOpen(registration)) {
	    throw new DomainException("error.StudentReingressionRequest.out.of.enrolment.period");
	}
    }

    private boolean hasValidState(final Registration registration) {
	return registration.isInterrupted() || registration.isFlunked();
    }
    
    private boolean wasFlunkedAtLeastThreeTimes(final Registration registration) {
	return registration.getRegistrationStates(RegistrationStateType.FLUNKED).size() >= 3;
    }

    private boolean isEnrolmentPeriodOpen(final Registration registration) {
	return registration.getLastDegreeCurricularPlan().hasActualEnrolmentPeriodInCurricularCourses();
    }

    @Override
    public String getDescription() {
	return getDescription("AcademicServiceRequestType.REINGRESSION");
    }

    @Override
    public EventType getEventType() {
	return EventType.STUDENT_REINGRESSION_REQUEST;
    }

    @Override
    protected void internalChangeState(AcademicServiceRequestBean academicServiceRequestBean) {
	
	if (academicServiceRequestBean.isToCancelOrReject() && hasEvent()) {
	    getEvent().cancel(academicServiceRequestBean.getEmployee());
	    
	} else if (academicServiceRequestBean.isToProcess()) {
	    if (hasEvent()) {
		throw new DomainException("error.ExternalDegreeChangeRequest.already.has.event");
	    }
	    new StudentReingressionRequestEvent(getAdministrativeOffice(), getPerson(), this);
	    
	} else if (isProcessing()) {
	    if (isPayable() && !isPayed()) {
		throw new DomainException("AcademicServiceRequest.hasnt.been.payed");
	    }
	}
    }
}
