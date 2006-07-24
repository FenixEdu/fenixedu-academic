package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;

public class ApprovementCertificateRequest extends ApprovementCertificateRequest_Base {

	private ApprovementCertificateRequest() {
		super();
	}

	public ApprovementCertificateRequest(StudentCurricularPlan studentCurricularPlan,
			AdministrativeOffice administrativeOffice, DocumentPurposeType documentPurposeType,
			String otherDocumentPurposeTypeDescription, Boolean urgentRequest) {

		this();

		init(studentCurricularPlan, administrativeOffice, DocumentRequestType.APPROVEMENT_CERTIFICATE,
				documentPurposeType, otherDocumentPurposeTypeDescription, urgentRequest);
	}

}
