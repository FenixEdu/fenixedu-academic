package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;

public abstract class DocumentRequest extends DocumentRequest_Base {

    protected DocumentRequest() {
        super();
    }

    public DocumentRequest(StudentCurricularPlan studentCurricularPlan,
            AdministrativeOffice administrativeOffice, DocumentRequestType documentRequestType,
            DocumentPurposeType documentPurposeType, String otherDocumentPurposeTypeDescription,
            Boolean urgentRequest) {
        this();
        init(studentCurricularPlan, administrativeOffice, documentRequestType, documentPurposeType,
                otherDocumentPurposeTypeDescription, urgentRequest);
    }

    private void checkParameters(DocumentRequestType documentRequestType,
            DocumentPurposeType documentPurposeType, String otherDocumentPurposeTypeDescription,
            Boolean urgentRequest) {

        if (documentRequestType == null) {
            throw new DomainException(
                    "error.serviceRequests.documentRequests.DocumentRequest.documentRequestType.cannot.be.null");
        }

        if (documentPurposeType == null) {
            throw new DomainException(
                    "error.serviceRequests.documentRequests.DocumentRequest.documentPurposeType.cannot.be.null");
        }

        if (documentPurposeType == DocumentPurposeType.OTHER
                && otherDocumentPurposeTypeDescription == null) {
            throw new DomainException(
                    "error.serviceRequests.documentRequests.DocumentRequest.otherDocumentPurposeTypeDescription.cannot.be.null.for.other.purpose.type");
        }

        if (urgentRequest == null) {
            throw new DomainException(
                    "error.serviceRequests.documentRequests.DocumentRequest.urgentRequest.cannot.be.null");
        }

    }

    protected void init(StudentCurricularPlan studentCurricularPlan,
            AdministrativeOffice administrativeOffice, DocumentRequestType documentRequestType,
            DocumentPurposeType documentPurposeType, String otherDocumentPurposeTypeDescription,
            Boolean urgentRequest) {

        init(studentCurricularPlan, administrativeOffice);

        checkParameters(documentRequestType, documentPurposeType, otherDocumentPurposeTypeDescription,
                urgentRequest);

        super.setDocumentRequestType(documentRequestType);
        super.setDocumentPurposeType(documentPurposeType);
        super.setOtherDocumentPurposeTypeDescription(otherDocumentPurposeTypeDescription);
        super.setUrgentRequest(urgentRequest);
    }

    public boolean isUrgentRequest() {
        return getUrgentRequest().booleanValue();
    }

    public static DocumentRequest create(StudentCurricularPlan studentCurricularPlan,
            DocumentRequestType chosenDocumentRequestType,
            DocumentPurposeType chosenDocumentPurposeType, String otherPurpose, String notes,
            Boolean urgentRequest, Boolean average, Boolean detailed, ExecutionYear executionYear) {

        final DegreeType degreeType = studentCurricularPlan.getDegreeType();
        final AdministrativeOffice administrativeOffice = AdministrativeOffice
                .getResponsibleAdministrativeOffice(degreeType);

        switch (chosenDocumentRequestType) {
        case SCHOOL_REGISTRATION_CERTIFICATE:
            return new SchoolRegistrationCertificateRequest(studentCurricularPlan, administrativeOffice,
                    chosenDocumentPurposeType, otherPurpose, urgentRequest, executionYear);
        case ENROLMENT_CERTIFICATE:
            return new EnrolmentCertificateRequest(studentCurricularPlan, administrativeOffice,
                    chosenDocumentPurposeType, otherPurpose, urgentRequest, detailed, executionYear);
        case APPROVEMENT_CERTIFICATE:
            return new ApprovementCertificateRequest(studentCurricularPlan, administrativeOffice,
                    chosenDocumentPurposeType, otherPurpose, urgentRequest);
        case DEGREE_FINALIZATION_CERTIFICATE:
            return new DegreeFinalizationCertificateRequest(studentCurricularPlan, administrativeOffice,
                    chosenDocumentPurposeType, otherPurpose, urgentRequest, average, detailed);
        }

        return null;
    }

    @Override
    public void setDocumentRequestType(DocumentRequestType documentRequestType) {
        throw new DomainException(
                "error.serviceRequests.documentRequests.DocumentRequest.cannot.modify.documentRequestType");
    }

    @Override
    public void setDocumentPurposeType(DocumentPurposeType documentPurposeType) {
        throw new DomainException(
                "error.serviceRequests.documentRequests.DocumentRequest.cannot.modify.documentPurposeType");
    }

    @Override
    public void setOtherDocumentPurposeTypeDescription(String otherDocumentTypeDescription) {
        throw new DomainException(
                "error.serviceRequests.documentRequests.DocumentRequest.cannot.modify.otherDocumentTypeDescription");
    }

    @Override
    public void setNumberOfPages(Integer numberOfPages) {
        throw new DomainException(
                "error.serviceRequests.documentRequests.enclosing_type.cannot.modify.numberOfPages");
    }

    @Override
    public void setUrgentRequest(Boolean urgentRequest) {
        throw new DomainException(
                "error.serviceRequests.documentRequests.CertificateRequest.cannot.modify.urgentRequest");
    }

    public void edit(AcademicServiceRequestSituationType academicServiceRequestSituationType,
            Employee employee, String justification, Integer numberOfPages) {
        super.edit(academicServiceRequestSituationType, employee, justification);
        super.setNumberOfPages(numberOfPages);
    }

}
