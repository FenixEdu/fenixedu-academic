package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.CertificateRequestEvent;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;

public class ApprovementCertificateRequest extends ApprovementCertificateRequest_Base {

    private ApprovementCertificateRequest() {
	super();
    }

    public ApprovementCertificateRequest(StudentCurricularPlan studentCurricularPlan,
	    DocumentPurposeType documentPurposeType, String otherDocumentPurposeTypeDescription,
	    Boolean urgentRequest) {

	this();

	init(studentCurricularPlan, DocumentRequestType.APPROVEMENT_CERTIFICATE, documentPurposeType,
		otherDocumentPurposeTypeDescription, urgentRequest);
    }

    @Override
    protected void internalChangeState(
	    AcademicServiceRequestSituationType academicServiceRequestSituationType, Employee employee) {

	super.internalChangeState(academicServiceRequestSituationType, employee);

	if (academicServiceRequestSituationType == AcademicServiceRequestSituationType.CONCLUDED) {

	    new CertificateRequestEvent(getAdministrativeOffice(),
		    EventType.APPROVEMENT_CERTIFICATE_REQUEST, getStudent().getPerson(), this);
	}
    }

    @Override
    public String getDocumentTemplateKey() {
	return null;
    }

}
