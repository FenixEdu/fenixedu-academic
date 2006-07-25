package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class EnrolmentCertificateRequest extends EnrolmentCertificateRequest_Base {

    private EnrolmentCertificateRequest() {
        super();
    }

    public EnrolmentCertificateRequest(StudentCurricularPlan studentCurricularPlan,
            AdministrativeOffice administrativeOffice, DocumentPurposeType documentPurposeType,
            String otherDocumentPurposeTypeDescription, Boolean urgentRequest, Boolean detailed,
            ExecutionYear executionYear) {

        this();

        init(studentCurricularPlan, administrativeOffice, documentPurposeType,
                otherDocumentPurposeTypeDescription, urgentRequest, detailed, executionYear);
    }

    protected void init(StudentCurricularPlan studentCurricularPlan,
            AdministrativeOffice administrativeOffice, DocumentPurposeType documentPurposeType,
            String otherDocumentPurposeTypeDescription, Boolean urgentRequest, Boolean detailed,
            ExecutionYear executionYear) {

        init(studentCurricularPlan, administrativeOffice, DocumentRequestType.ENROLMENT_CERTIFICATE,
                documentPurposeType, otherDocumentPurposeTypeDescription, urgentRequest);

        checkParameters(detailed, executionYear);
        super.setDetailed(detailed);
        super.setExecutionYear(executionYear);
    }

    private void checkParameters(Boolean detailed, ExecutionYear executionYear) {
        if (detailed == null) {
            throw new DomainException(
                    "error.serviceRequests.documentRequests.EnrolmentCertificateRequest.detailed.cannot.be.null");
        }
        if (executionYear == null) {
            throw new DomainException(
                    "error.serviceRequests.documentRequests.EnrolmentCertificateRequest.executionYear.cannot.be.null");
        } else if (!getStudentCurricularPlan().hasAnyEnrolmentForExecutionYear(executionYear)) {
            throw new DomainException(
                    "error.serviceRequests.documentRequests.EnrolmentCertificateRequest.executionYear.before.studentCurricularPlan.start");
        }
    }

}
