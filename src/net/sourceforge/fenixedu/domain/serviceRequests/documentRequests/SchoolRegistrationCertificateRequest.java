package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.joda.time.DateTime;

public class SchoolRegistrationCertificateRequest extends SchoolRegistrationCertificateRequest_Base {

    private SchoolRegistrationCertificateRequest() {
	super();
    }

    public SchoolRegistrationCertificateRequest(Registration registration, DateTime requestDate,
	    DocumentPurposeType documentPurposeType, String otherDocumentPurposeTypeDescription, Boolean urgentRequest,
	    ExecutionYear executionYear) {

	this();
	super.init(registration, requestDate, executionYear, Boolean.FALSE, documentPurposeType,
		otherDocumentPurposeTypeDescription, urgentRequest);
	checkRulesToCreate(registration, executionYear);
    }

    private void checkRulesToCreate(final Registration registration, final ExecutionYear executionYear) {
	if (executionYear == null) {
	    throw new DomainException(
		    "error.serviceRequests.documentRequests.SchoolRegistrationCertificateRequest.executionYear.cannot.be.null");

	} else if (!registration.isInRegisteredState(executionYear)) {
	    throw new DomainException(
		    "SchoolRegistrationCertificateRequest.registration.not.in.registered.state.in.given.executionYear");
	}
    }

    @Override
    final public DocumentRequestType getDocumentRequestType() {
	return DocumentRequestType.SCHOOL_REGISTRATION_CERTIFICATE;
    }

    @Override
    final public String getDocumentTemplateKey() {
	return getClass().getName();
    }

    @Override
    final public EventType getEventType() {
	return EventType.SCHOOL_REGISTRATION_CERTIFICATE_REQUEST;
    }

    @Override
    final public Integer getNumberOfUnits() {
	return 0;
    }

    @Override
    public boolean isAvailableForTransitedRegistrations() {
	return false;
    }

}
