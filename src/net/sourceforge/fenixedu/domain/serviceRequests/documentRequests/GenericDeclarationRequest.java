package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestCreateBean;
import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.RegistrationAcademicServiceRequestCreateBean;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;

public class GenericDeclarationRequest extends GenericDeclarationRequest_Base {

	public GenericDeclarationRequest() {
		super();
	}

	public GenericDeclarationRequest(final DocumentRequestCreateBean bean) {
		this();
		super.init(bean);
	}

	@Override
	public DocumentRequestType getDocumentRequestType() {
		return DocumentRequestType.GENERIC_DECLARATION;
	}

	@Override
	protected boolean hasFreeDeclarationRequests() {
		return false;
	}

	@Override
	public String getDocumentTemplateKey() {
		return getClass().getName();
	}

	@Override
	public EventType getEventType() {
		return EventType.GENERIC_DECLARATION_REQUEST;
	}

	public EntryType getEntryType() {
		return EntryType.GENERIC_DECLARATION_REQUEST_FEE;
	}

	@Override
	public boolean isToPrint() {
		return false;
	}

	@Override
	protected void checkRegistrationStartDate(RegistrationAcademicServiceRequestCreateBean bean) {

	}

}
