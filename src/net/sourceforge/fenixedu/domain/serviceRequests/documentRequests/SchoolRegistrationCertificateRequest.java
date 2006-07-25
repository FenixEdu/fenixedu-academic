package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.CertificateRequestEvent;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;

public class SchoolRegistrationCertificateRequest extends SchoolRegistrationCertificateRequest_Base {

	private SchoolRegistrationCertificateRequest() {
		super();
	}

	public SchoolRegistrationCertificateRequest(StudentCurricularPlan studentCurricularPlan,
			AdministrativeOffice administrativeOffice, DocumentPurposeType documentPurposeType,
			String otherDocumentPurposeTypeDescription, Boolean urgentRequest,
			ExecutionYear executionYear) {

		this();

		init(studentCurricularPlan, administrativeOffice, documentPurposeType,
				otherDocumentPurposeTypeDescription, urgentRequest, executionYear);
	}

	protected void init(StudentCurricularPlan studentCurricularPlan,
			AdministrativeOffice administrativeOffice, DocumentPurposeType documentPurposeType,
			String otherDocumentPurposeTypeDescription, Boolean urgentRequest,
			ExecutionYear executionYear) {

		init(studentCurricularPlan, administrativeOffice, DocumentRequestType.ENROLMENT_CERTIFICATE,
				documentPurposeType, otherDocumentPurposeTypeDescription, urgentRequest);

		checkParameters(executionYear);
		super.setExecutionYear(executionYear);
	}

	private void checkParameters(ExecutionYear executionYear) {
        if (executionYear == null) {
            throw new DomainException(
                    "error.serviceRequests.documentRequests.SchoolRegistrationCertificateRequest.executionYear.cannot.be.null");
        } else if (!getStudentCurricularPlan().hasSchoolRegistration(executionYear)) {
            throw new DomainException(
                    "error.serviceRequests.documentRequests.SchoolRegistrationCertificateRequest.executionYear.before.studentCurricularPlan.start");
        }
    }

	@Override
	public void setExecutionYear(ExecutionYear executionYear) {
		throw new DomainException(
				"error.serviceRequests.documentRequests.SchoolRegistrationCertificateRequest.cannot.modify.executionYear");
	}

	@Override
	protected void internalChangeState(
			AcademicServiceRequestSituationType academicServiceRequestSituationType) {

		super.internalChangeState(academicServiceRequestSituationType);

		if (academicServiceRequestSituationType == AcademicServiceRequestSituationType.CONCLUDED) {

			new CertificateRequestEvent(getAdministrativeOffice(),
					EventType.SCHOOL_REGISTRATION_CERTIFICATE_REQUEST, getStudent().getPerson(), this);
		}
	}

}
