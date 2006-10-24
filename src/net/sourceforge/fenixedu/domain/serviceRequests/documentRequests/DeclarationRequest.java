package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;

public abstract class DeclarationRequest extends DeclarationRequest_Base {

    protected DeclarationRequest() {
	super();
	super.setNumberOfPages(0);
    }

    public DeclarationRequest(StudentCurricularPlan studentCurricularPlan, AdministrativeOffice administrativeOffice,
	    DocumentRequestType documentRequestType, DocumentPurposeType documentPurposeType,
	    String otherDocumentPurposeTypeDescription, Boolean urgentRequest) {

	this();
	init(studentCurricularPlan, administrativeOffice, documentRequestType, documentPurposeType,
		otherDocumentPurposeTypeDescription, urgentRequest);
    }

    protected void init(StudentCurricularPlan studentCurricularPlan, AdministrativeOffice administrativeOffice,
            DocumentRequestType documentRequestType,
            DocumentPurposeType documentPurposeType, String otherDocumentPurposeTypeDescription,
            Boolean urgentRequest) {

        init(studentCurricularPlan, administrativeOffice);
        checkParameters(documentPurposeType, otherDocumentPurposeTypeDescription, urgentRequest);

        super.setDocumentPurposeType(documentPurposeType);
        super.setOtherDocumentPurposeTypeDescription(otherDocumentPurposeTypeDescription);
    }

    private void checkParameters(DocumentPurposeType documentPurposeType, String otherDocumentPurposeTypeDescription,
            Boolean urgentRequest) {

        if (documentPurposeType == DocumentPurposeType.OTHER
                && otherDocumentPurposeTypeDescription == null) {
            throw new DomainException(
                    "error.serviceRequests.documentRequests.DeclarationRequest.otherDocumentPurposeTypeDescription.cannot.be.null.for.other.purpose.type");
        }

        if (urgentRequest == null) {
            throw new DomainException(
                    "error.serviceRequests.documentRequests.DeclarationRequest.urgentRequest.cannot.be.null");
        }
    }

    public static DeclarationRequest create(StudentCurricularPlan studentCurricularPlan,
	    DocumentRequestType chosenDocumentRequestType,
	    DocumentPurposeType chosenDocumentPurposeType, String otherPurpose, String notes,
	    Boolean urgentRequest, Boolean average, Boolean detailed, ExecutionYear executionYear) {

	final AdministrativeOffice administrativeOffice = AdministrativeOffice.readByEmployee(AccessControl.getUserView().getPerson().getEmployee());

	switch (chosenDocumentRequestType) {
	case SCHOOL_REGISTRATION_DECLARATION:
	    return new SchoolRegistrationDeclarationRequest();
	case ENROLMENT_DECLARATION:
	    return new EnrolmentDeclarationRequest();
	case IRS_DECLARATION:
	    return new IRSDeclarationRequest();
	}

	return null;
    }
    

    @Override
    public void setDocumentPurposeType(DocumentPurposeType documentPurposeType) {
        throw new DomainException(
                "error.serviceRequests.documentRequests.DeclarationRequest.cannot.modify.documentPurposeType");
    }

    @Override
    public void setOtherDocumentPurposeTypeDescription(String otherDocumentTypeDescription) {
        throw new DomainException(
                "error.serviceRequests.documentRequests.DeclarationRequest.cannot.modify.otherDocumentTypeDescription");
    }

    @Override
    public void setNumberOfPages(Integer numberOfPages) {
        throw new DomainException(
                "error.serviceRequests.documentRequests.enclosing_type.cannot.modify.numberOfPages");
    }

    public void edit(AcademicServiceRequestSituationType academicServiceRequestSituationType,
            Employee employee, String justification, Integer numberOfPages) {
        super.edit(academicServiceRequestSituationType, employee, justification);
        super.setNumberOfPages(numberOfPages);
    }

}
