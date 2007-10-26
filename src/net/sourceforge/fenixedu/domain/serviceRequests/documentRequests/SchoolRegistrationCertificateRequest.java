package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;

public class SchoolRegistrationCertificateRequest extends SchoolRegistrationCertificateRequest_Base {

    private SchoolRegistrationCertificateRequest() {
	super();
    }

    public SchoolRegistrationCertificateRequest(Registration registration,
	    DocumentPurposeType documentPurposeType, String otherDocumentPurposeTypeDescription,
	    Boolean urgentRequest, ExecutionYear executionYear) {

	this();

	init(registration, documentPurposeType, otherDocumentPurposeTypeDescription, urgentRequest,
		executionYear);
    }

    protected void init(Registration registration, DocumentPurposeType documentPurposeType,
	    String otherDocumentPurposeTypeDescription, Boolean urgentRequest,
	    ExecutionYear executionYear) {

	super
		.init(registration, documentPurposeType, otherDocumentPurposeTypeDescription,
			urgentRequest);

	checkParameters(executionYear);
	super.setExecutionYear(executionYear);
    }

    private void checkParameters(ExecutionYear executionYear) {
	if (executionYear == null) {
	    throw new DomainException(
		    "error.serviceRequests.documentRequests.SchoolRegistrationCertificateRequest.executionYear.cannot.be.null");
	} else if (!getRegistration().isInRegisteredState(executionYear)) {
	    throw new DomainException(
		    "SchoolRegistrationCertificateRequest.registration.not.in.registered.state.in.given.executionYear");
	}
    }

    @Override
    public void delete() {
	super.setExecutionYear(null);
	super.delete();
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
    final public void setExecutionYear(ExecutionYear executionYear) {
	throw new DomainException(
		"error.serviceRequests.documentRequests.SchoolRegistrationCertificateRequest.cannot.modify.executionYear");
    }

    @Override
    final public EventType getEventType() {
	return EventType.SCHOOL_REGISTRATION_CERTIFICATE_REQUEST;
    }

    @Override
    final public Integer getNumberOfUnits() {
	return 0;
    }

}
