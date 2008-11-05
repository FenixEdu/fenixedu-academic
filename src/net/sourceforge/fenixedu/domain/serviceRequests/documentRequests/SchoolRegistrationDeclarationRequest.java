package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestCreateBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class SchoolRegistrationDeclarationRequest extends SchoolRegistrationDeclarationRequest_Base {

    protected SchoolRegistrationDeclarationRequest() {
	super();
    }

    public SchoolRegistrationDeclarationRequest(final DocumentRequestCreateBean bean) {
	this();
	super.init(bean);

	checkRulesToCreate(bean);
    }

    private void checkRulesToCreate(final DocumentRequestCreateBean bean) {
	final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
	if (!bean.getRegistration().isRegistered(currentExecutionYear)) {
	    throw new DomainException(
		    "SchoolRegistrationDeclarationRequest.registration.not.in.registered.state.in.current.executionYear");
	}
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

    @Override
    public boolean hasPersonalInfo() {
	return true;
    }

}
