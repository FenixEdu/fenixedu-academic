package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;

public class EnrolmentDeclarationRequest extends EnrolmentDeclarationRequest_Base {
    
    private EnrolmentDeclarationRequest() {
        super();
    }

    public EnrolmentDeclarationRequest(Registration registration,
	    DocumentPurposeType documentPurposeType, String otherDocumentPurposeTypeDescription, Boolean freeProcessed) {

	this();
	init(registration, documentPurposeType, otherDocumentPurposeTypeDescription, freeProcessed);
    }

    protected void init(Registration registration, DocumentPurposeType documentPurposeType,
	    String otherDocumentPurposeTypeDescription, Boolean freeProcessed) {

	final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
	if (!registration.hasAnyEnrolmentsIn(currentExecutionYear)) {
	    throw new DomainException("EnrolmentDeclarationRequest.no.enrolments.for.registration.in.current.executionYear");
	}
	super.init(registration, currentExecutionYear, documentPurposeType, otherDocumentPurposeTypeDescription, freeProcessed);
    }

    @Override
    final public DocumentRequestType getDocumentRequestType() {
	return DocumentRequestType.ENROLMENT_DECLARATION;
    }

    @Override
    final public String getDocumentTemplateKey() {
	return getClass().getName();
    }

    @Override
    final public EventType getEventType() {
	return EventType.ENROLMENT_DECLARATION_REQUEST;
    }
}
