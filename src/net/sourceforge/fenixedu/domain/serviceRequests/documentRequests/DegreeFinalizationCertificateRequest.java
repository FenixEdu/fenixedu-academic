package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.CertificateRequestEvent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;

public class DegreeFinalizationCertificateRequest extends DegreeFinalizationCertificateRequest_Base {

    protected DegreeFinalizationCertificateRequest() {
	super();
    }

    public DegreeFinalizationCertificateRequest(StudentCurricularPlan studentCurricularPlan,
	    DocumentPurposeType documentPurposeType, String otherDocumentPurposeTypeDescription,
	    Boolean urgentRequest, Boolean average, Boolean detailed) {
	this();
	
	if (!studentCurricularPlan.hasDegreeDiplomaRequest()) {
	    throw new DomainException("error.serviceRequests.documentRequests.DegreeFinalizationCertificateRequest.has.no.degree.diploma.request");
	}
	
	init(studentCurricularPlan, documentPurposeType, otherDocumentPurposeTypeDescription,
		urgentRequest, average, detailed);
    }

    protected void init(StudentCurricularPlan studentCurricularPlan,
	    DocumentPurposeType documentPurposeType, String otherDocumentPurposeTypeDescription,
	    Boolean urgentRequest, Boolean average, Boolean detailed) {
	init(studentCurricularPlan, DocumentRequestType.DEGREE_FINALIZATION_CERTIFICATE,
		documentPurposeType, otherDocumentPurposeTypeDescription, urgentRequest);

	checkParameters(average, detailed);
	super.setAverage(average);
	super.setDetailed(detailed);
    }

    private void checkParameters(Boolean average, Boolean detailed) {
	if (average == null) {
	    throw new DomainException(
		    "error.serviceRequests.documentRequests.DegreeFinalizationCertificateRequest.average.cannot.be.null");
	}
	if (detailed == null) {
	    throw new DomainException(
		    "error.serviceRequests.documentRequests.DegreeFinalizationCertificateRequest.detailed.cannot.be.null");
	}
    }

    @Override
    public void setAverage(Boolean average) {
	throw new DomainException(
		"error.serviceRequests.documentRequests.DegreeFinalizationCertificateRequest.cannot.modify.average");
    }

    @Override
    public void setDetailed(Boolean detailed) {
	throw new DomainException(
		"error.serviceRequests.documentRequests.DegreeFinalizationCertificateRequest.cannot.modify.detailed");
    }

    @Override
    protected void internalChangeState(
	    AcademicServiceRequestSituationType academicServiceRequestSituationType, Employee employee) {

	super.internalChangeState(academicServiceRequestSituationType, employee);

	if (academicServiceRequestSituationType == AcademicServiceRequestSituationType.PROCESSING) {
	    new CertificateRequestEvent(getAdministrativeOffice(),
		    EventType.DEGREE_FINALIZATION_CERTIFICATE_REQUEST, getStudent().getPerson(), this);
	}

    }

    @Override
    public String getDocumentTemplateKey() {
	return null;
    }

}
