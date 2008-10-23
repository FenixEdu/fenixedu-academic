package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.serviceRequest.documentRequest.DocumentRequestCreateBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class EnrolmentDeclarationRequest extends EnrolmentDeclarationRequest_Base {

    private EnrolmentDeclarationRequest() {
	super();
    }

    public EnrolmentDeclarationRequest(final DocumentRequestCreateBean bean) {
	this();
	super.init(bean);

	checkRulesToCreate(bean);
    }

    private void checkRulesToCreate(final DocumentRequestCreateBean bean) {
	final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
	if (!bean.getRegistration().hasAnyEnrolmentsIn(currentExecutionYear)) {
	    throw new DomainException("EnrolmentDeclarationRequest.no.enrolments.for.registration.in.current.executionYear");
	}
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

    @Override
    public boolean hasPersonalInfo() {
	return true;
    }

}
