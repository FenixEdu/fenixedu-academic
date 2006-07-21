package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;

public class SchoolRegistrationCertificateRequest extends SchoolRegistrationCertificateRequest_Base {

	private SchoolRegistrationCertificateRequest() {
		super();
	}

	public SchoolRegistrationCertificateRequest(StudentCurricularPlan studentCurricularPlan,
			AdministrativeOffice administrativeOffice, DocumentPurposeType documentPurposeType,
			String otherDocumentPurposeTypeDescription, Integer numberOfPages, Boolean urgentRequest) {

		this();

		init(studentCurricularPlan, administrativeOffice,
				DocumentRequestType.SCHOOL_REGISTRATION_CERTIFICATE, documentPurposeType,
				otherDocumentPurposeTypeDescription, numberOfPages, urgentRequest);
	}
}
