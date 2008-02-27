package net.sourceforge.fenixedu.domain.serviceRequests;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.AcademicServiceRequestType;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

public class FreeSolicitationAcademicRequest extends FreeSolicitationAcademicRequest_Base {

    protected FreeSolicitationAcademicRequest() {
	super();
    }

    public FreeSolicitationAcademicRequest(final Registration registration, final ExecutionYear executionYear,
	    final DateTime requestDate, final String subject, final String purpose) {
	this(registration, executionYear, subject, purpose, requestDate, false, false);
    }

    public FreeSolicitationAcademicRequest(final Registration registration, final ExecutionYear executionYear,
	    final String subject, final String purpose, final DateTime requestDate, final Boolean urgentRequest,
	    final Boolean freeProcessed) {
	this();
	super.init(registration, executionYear, requestDate, urgentRequest, freeProcessed);
	checkParameters(subject);
	super.setSubject(subject);
	super.setPurpose(purpose);
    }

    private void checkParameters(final String subject) {
	if (StringUtils.isEmpty(subject)) {
	    throw new DomainException("error.FreeSolicitationAcademicRequest.invalid.subject");
	}
    }

    @Override
    public void setSubject(String subject) {
	throw new DomainException("error.FreeSolicitationAcademicRequest.cannot.modify.subject");
    }

    @Override
    public void setPurpose(String purpose) {
	throw new DomainException("error.FreeSolicitationAcademicRequest.cannot.modify.purpose");
    }

    @Override
    public AcademicServiceRequestType getAcademicServiceRequestType() {
	return AcademicServiceRequestType.FREE_SOLICITATION_ACADEMIC_REQUEST;
    }

    @Override
    public EventType getEventType() {
	return null;
    }

    @Override
    public boolean isPossibleToSendToOtherEntity() {
	return true;
    }
}
