package net.sourceforge.fenixedu.domain.serviceRequests;

import java.util.Arrays;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.EnrolmentPeriod;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.StudentReingressionRequestEvent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.AcademicServiceRequestType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;

import org.joda.time.DateTime;

public class StudentReingressionRequest extends StudentReingressionRequest_Base {

    static final public List<RegistrationStateType> ALLOWED_TYPES = Arrays.asList(RegistrationStateType.FLUNKED,
	    RegistrationStateType.INTERRUPTED);

    private StudentReingressionRequest() {
	super();
    }

    public StudentReingressionRequest(final Registration registration, final ExecutionYear executionYear,
	    final DateTime requestDate) {
	this(registration, executionYear, requestDate, false, false);
    }

    public StudentReingressionRequest(final Registration registration, final ExecutionYear executionYear,
	    final DateTime requestDate, final Boolean urgentRequest, final Boolean freeProcessed) {
	this();
	checkParameters(executionYear);
	checkRulesToCreate(registration, executionYear, requestDate);
	super.init(registration, executionYear, requestDate, urgentRequest, freeProcessed);
	new StudentReingressionRequestEvent(getAdministrativeOffice(), getPerson(), this);
    }

    private void checkParameters(final ExecutionYear executionYear) {
	if (executionYear == null) {
	    throw new DomainException("error.StudentReingressionRequest.executionYear.cannot.be.null");
	}
    }

    private void checkRulesToCreate(final Registration registration, final ExecutionYear executionYear, final DateTime requestDate) {

	if (!hasValidState(registration)) {
	    throw new DomainException("error.StudentReingressionRequest.registration.with.invalid.state");
	}

	if (hasInterruptedAtLeastThreeTimes(registration)) {
	    throw new DomainException("error.StudentReingressionRequest.registration.was.flunked.at.least.three.times");
	}

	if (!isEnrolmentPeriodOpen(registration, executionYear, requestDate)) {
	    throw new DomainException("error.StudentReingressionRequest.out.of.enrolment.period");
	}
    }

    private boolean hasValidState(final Registration registration) {
	return registration.hasAnyState(ALLOWED_TYPES);
    }

    private boolean hasInterruptedAtLeastThreeTimes(final Registration registration) {
	return registration.getRegistrationStates(ALLOWED_TYPES).size() >= 3;
    }

    private boolean isEnrolmentPeriodOpen(final Registration registration, final ExecutionYear executionYear,
	    final DateTime requestDate) {
	final DegreeCurricularPlan degreeCurricularPlan = registration.getLastDegreeCurricularPlan();
	for (final ExecutionPeriod executionPeriod : executionYear.getExecutionPeriodsSet()) {
	    final EnrolmentPeriod enrolmentPeriod = degreeCurricularPlan.getEnrolmentPeriodInCurricularCoursesBy(executionPeriod);
	    if (enrolmentPeriod != null && enrolmentPeriod.containsDate(requestDate)) {
		return true;
	    }
	}
	return false;
    }

    @Override
    public AcademicServiceRequestType getAcademicServiceRequestType() {
	return AcademicServiceRequestType.REINGRESSION;
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
	    if (isPayable() && !isPayed()) {
		throw new DomainException("AcademicServiceRequest.hasnt.been.payed");
	    }
	    academicServiceRequestBean.setSituationDate(getActiveSituation().getSituationDate().toYearMonthDay());

	} else if (academicServiceRequestBean.isToConclude()) {
	    RegistrationState.createState(getRegistration(), academicServiceRequestBean.getEmployee().getPerson(),
		    new DateTime(), RegistrationStateType.REGISTERED);
	}
    }

    @Override
    protected void createAcademicServiceRequestSituations(AcademicServiceRequestBean academicServiceRequestBean) {
	super.createAcademicServiceRequestSituations(academicServiceRequestBean);

	if (academicServiceRequestBean.isToConclude()) {
	    AcademicServiceRequestSituation.create(this, new AcademicServiceRequestBean(
		    AcademicServiceRequestSituationType.DELIVERED, academicServiceRequestBean.getEmployee()));
	}
    }

    @Override
    public boolean isPossibleToSendToOtherEntity() {
	return true;
    }

    @Override
    public boolean isAvailableForTransitedRegistrations() {
	return false;
    }

}
