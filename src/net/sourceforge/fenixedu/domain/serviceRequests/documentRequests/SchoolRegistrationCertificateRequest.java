package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestCreateBean;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class SchoolRegistrationCertificateRequest extends SchoolRegistrationCertificateRequest_Base {

	protected SchoolRegistrationCertificateRequest() {
		super();
	}

	public SchoolRegistrationCertificateRequest(final DocumentRequestCreateBean bean) {
		this();
		super.init(bean);

		checkRulesToCreate(bean);
	}

	private void checkRulesToCreate(final DocumentRequestCreateBean bean) {
		if (bean.getExecutionYear() == null) {
			throw new DomainException(
					"error.serviceRequests.documentRequests.SchoolRegistrationCertificateRequest.executionYear.cannot.be.null");

		} else if (!bean.getRegistration().isRegistered(bean.getExecutionYear())) {
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
		return true;
	}

	@Override
	public boolean hasPersonalInfo() {
		return true;
	}

}
