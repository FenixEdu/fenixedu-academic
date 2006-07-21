package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;

public class EnrolmentCertificateRequest extends EnrolmentCertificateRequest_Base {

	private EnrolmentCertificateRequest() {
		super();
	}

	public EnrolmentCertificateRequest(StudentCurricularPlan studentCurricularPlan,
			AdministrativeOffice administrativeOffice, DocumentPurposeType documentPurposeType,
			String otherDocumentPurposeTypeDescription, Integer numberOfPages, Boolean urgentRequest) {

		this();

		init(studentCurricularPlan, administrativeOffice, DocumentRequestType.ENROLMENT_CERTIFICATE,
				documentPurposeType, otherDocumentPurposeTypeDescription, numberOfPages, urgentRequest);
	}
}
