package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;

public class SchoolRegistrationDeclarationRequest extends SchoolRegistrationDeclarationRequest_Base {

    private SchoolRegistrationDeclarationRequest() {
	super();
    }

    public SchoolRegistrationDeclarationRequest(Registration registration,
	    DocumentPurposeType documentPurposeType, String otherDocumentPurposeTypeDescription, Boolean freeProcessed) {

	this();
	this.init(registration, documentPurposeType, otherDocumentPurposeTypeDescription, freeProcessed);
    }

    @Override
    final protected void init(Registration registration, DocumentPurposeType documentPurposeType,
	    String otherDocumentPurposeTypeDescription, Boolean freeProcessed) {

	super.init(registration, documentPurposeType, otherDocumentPurposeTypeDescription, freeProcessed);

        final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
        if (!getRegistration().isInRegisteredState(currentExecutionYear)) {
            throw new DomainException("SchoolRegistrationDeclarationRequest.registration.not.in.registered.state.in.current.executionYear");
        }
        super.setExecutionYear(currentExecutionYear);
    }

    @Override
    public void delete() {
	super.setExecutionYear(null);
	super.delete();
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
    final public void setExecutionYear(ExecutionYear executionYear) {
	throw new DomainException(
		"error.serviceRequests.documentRequests.SchoolRegistrationDeclarationRequest.cannot.modify.executionYear");
    }
    
    @Override
    final public EventType getEventType() {
	return EventType.SCHOOL_REGISTRATION_DECLARATION_REQUEST;
    }

}
