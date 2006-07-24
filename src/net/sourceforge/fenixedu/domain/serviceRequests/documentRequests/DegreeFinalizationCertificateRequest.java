package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class DegreeFinalizationCertificateRequest extends DegreeFinalizationCertificateRequest_Base {

	protected DegreeFinalizationCertificateRequest() {
		super();
	}

	public DegreeFinalizationCertificateRequest(StudentCurricularPlan studentCurricularPlan,
			AdministrativeOffice administrativeOffice, DocumentPurposeType documentPurposeType,
			String otherDocumentPurposeTypeDescription, Boolean urgentRequest,
			Boolean average, Boolean detailed) {
		this();
		init(studentCurricularPlan, administrativeOffice, documentPurposeType,
				otherDocumentPurposeTypeDescription, urgentRequest, average, detailed);
	}

	protected void init(StudentCurricularPlan studentCurricularPlan,
			AdministrativeOffice administrativeOffice, DocumentPurposeType documentPurposeType,
			String otherDocumentPurposeTypeDescription, Boolean urgentRequest,
			Boolean average, Boolean detailed) {
		init(studentCurricularPlan, administrativeOffice,
				DocumentRequestType.DEGREE_FINALIZATION_CERTIFICATE, documentPurposeType,
				otherDocumentPurposeTypeDescription, urgentRequest);

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

}
