package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;

public class SchoolRegistrationDeclarationRequest extends SchoolRegistrationDeclarationRequest_Base {

    private SchoolRegistrationDeclarationRequest() {
	super();
    }

    public SchoolRegistrationDeclarationRequest(Registration registration, DocumentPurposeType documentPurposeType,
	    String otherDocumentPurposeTypeDescription, Boolean freeProcessed) {
	this();
	init(registration, documentPurposeType, otherDocumentPurposeTypeDescription, freeProcessed);
    }

    protected void init(Registration registration, DocumentPurposeType documentPurposeType,
	    String otherDocumentPurposeTypeDescription, Boolean freeProcessed) {

	final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
	if (!registration.isInRegisteredState(currentExecutionYear)) {
	    throw new DomainException(
		    "SchoolRegistrationDeclarationRequest.registration.not.in.registered.state.in.current.executionYear");
	}
	super.init(registration, currentExecutionYear, documentPurposeType, otherDocumentPurposeTypeDescription, freeProcessed);
    }

    @Override
    final public DocumentRequestType getDocumentRequestType() {
	return DocumentRequestType.SCHOOL_REGISTRATION_DECLARATION;
    }

    @Override
    final public String getDocumentTemplateKey() {
	return getClass().getName();
    }

    @Override
    final public EventType getEventType() {
	return EventType.SCHOOL_REGISTRATION_DECLARATION_REQUEST;
    }

}
