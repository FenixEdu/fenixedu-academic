package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

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

}
