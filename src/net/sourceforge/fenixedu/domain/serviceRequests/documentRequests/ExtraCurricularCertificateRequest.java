package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestCreateBean;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class ExtraCurricularCertificateRequest extends ExtraCurricularCertificateRequest_Base {
    
    public  ExtraCurricularCertificateRequest() {
        super();
    }
    
    public ExtraCurricularCertificateRequest(final DocumentRequestCreateBean bean) {
	this();
	super.init(bean);
	checkParameters(bean);
	super.getEnrolments().addAll(bean.getEnrolments());
    }
    
    @Override
    protected void checkParameters(final DocumentRequestCreateBean bean) {
	if (bean.getEnrolments()== null || bean.getEnrolments().isEmpty()) {
	    throw new DomainException("error.ExtraCurricularCertificateRequest.no.enrolments");
	}
    }

    @Override
    public Integer getNumberOfUnits() {
	return super.getEnrolmentsCount();
    }

    @Override
    public DocumentRequestType getDocumentRequestType() {
	return DocumentRequestType.EXTRA_CURRICULAR_CERTIFICATE;
    }

    @Override
    public String getDocumentTemplateKey() {
	return getClass().getName();
    }

    @Override
    public EventType getEventType() {
	return EventType.EXTRA_CURRICULAR_APPROVEMENT_CERTIFICATE_REQUEST;
    }
    
    @Override
    public boolean isAvailableForTransitedRegistrations() {
	return true;
    }
    
}
