package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestCreateBean;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class StandaloneEnrolmentCertificateRequest extends StandaloneEnrolmentCertificateRequest_Base {

	public StandaloneEnrolmentCertificateRequest() {
		super();
	}

	public StandaloneEnrolmentCertificateRequest(DocumentRequestCreateBean bean) {
		this();
		super.init(bean);
		checkParameters(bean);
		super.getEnrolments().addAll(bean.getEnrolments());
	}

	@Override
	protected void checkParameters(final DocumentRequestCreateBean bean) {
		if (bean.getEnrolments() == null || bean.getEnrolments().isEmpty()) {
			throw new DomainException("error.StandaloneEnrolmentCertificateRequest.no.enrolments");
		}
	}

	@Override
	public Integer getNumberOfUnits() {
		return super.getEnrolmentsCount();
	}

	@Override
	public DocumentRequestType getDocumentRequestType() {
		return DocumentRequestType.STANDALONE_ENROLMENT_CERTIFICATE;
	}

	@Override
	public String getDocumentTemplateKey() {
		return getClass().getName();
	}

	@Override
	public EventType getEventType() {
		return EventType.STANDALONE_ENROLMENT_APPROVEMENT_CERTIFICATE_REQUEST;
	}

	@Override
	public boolean isAvailableForTransitedRegistrations() {
		return true;
	}

	@Override
	public boolean hasPersonalInfo() {
		return true;
	}

	@Override
	protected void internalChangeState(AcademicServiceRequestBean academicServiceRequestBean) {
		super.internalChangeState(academicServiceRequestBean);

		if (academicServiceRequestBean.isToCancelOrReject()) {
			for (; hasAnyEnrolments();) {
				removeEnrolments(getEnrolments().get(0));
			}
		}
	}

}
