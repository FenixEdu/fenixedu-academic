package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public abstract class DeclarationRequest extends DeclarationRequest_Base {

    protected DeclarationRequest() {
	super();
	super.setNumberOfPages(0);
    }

    public DeclarationRequest(StudentCurricularPlan studentCurricularPlan, AdministrativeOffice administrativeOffice,
	    DocumentRequestType documentRequestType, DocumentPurposeType documentPurposeType,
	    String otherDocumentPurposeTypeDescription,Boolean urgentRequest) {

	this();
    init(studentCurricularPlan, administrativeOffice);
   
	init(studentCurricularPlan, administrativeOffice, documentRequestType, documentPurposeType,
		otherDocumentPurposeTypeDescription,urgentRequest);
    }

    protected void init(StudentCurricularPlan studentCurricularPlan, AdministrativeOffice administrativeOffice,
            DocumentRequestType documentRequestType,
            DocumentPurposeType documentPurposeType, String otherDocumentPurposeTypeDescription,Boolean urgentRequest) {

        init(studentCurricularPlan, administrativeOffice);
        checkParameters(documentPurposeType, otherDocumentPurposeTypeDescription, urgentRequest);

        super.setDocumentPurposeType(documentPurposeType);
        super.setOtherDocumentPurposeTypeDescription(otherDocumentPurposeTypeDescription);
        super.process();
    }

    private void checkParameters(DocumentPurposeType documentPurposeType, String otherDocumentPurposeTypeDescription,Boolean urgentRequest) {

        if (documentPurposeType == DocumentPurposeType.OTHER
                && otherDocumentPurposeTypeDescription == null) {
            throw new DomainException(
                    "error.serviceRequests.documentRequests.DeclarationRequest.otherDocumentPurposeTypeDescription.cannot.be.null.for.other.purpose.type");
        }
        if (urgentRequest == null) {
            throw new DomainException(
                "error.serviceRequests.documentRequests.CertificateRequest.urgentRequest.cannot.be.null");
        }
      
    }

    @Override
    public EventType getEventType() {
	return null;
    }
    
    public static DeclarationRequest create(StudentCurricularPlan studentCurricularPlan,
            DocumentRequestType chosenDocumentRequestType,
            DocumentPurposeType chosenDocumentPurposeType, String otherPurpose, String notes,Boolean urgentRequest,
            Boolean average, Boolean detailed,  ExecutionYear executionYear) {

           
            
        AdministrativeOffice administrativeOffice = AdministrativeOffice.readByEmployee(AccessControl.getUserView().getPerson().getEmployee());
        if (administrativeOffice == null) {
            administrativeOffice = AdministrativeOffice
                .getResponsibleAdministrativeOffice(studentCurricularPlan.getDegree());
        }
        
        switch (chosenDocumentRequestType) {
        case SCHOOL_REGISTRATION_DECLARATION:
            return new SchoolRegistrationDeclarationRequest(studentCurricularPlan, administrativeOffice,
                    chosenDocumentRequestType, chosenDocumentPurposeType, otherPurpose,  urgentRequest,executionYear);
        case ENROLMENT_DECLARATION:
            return new EnrolmentDeclarationRequest(studentCurricularPlan, administrativeOffice,
                    chosenDocumentRequestType, chosenDocumentPurposeType, otherPurpose, urgentRequest, executionYear);
       
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

    public void edit(AcademicServiceRequestSituationType academicServiceRequestSituationType,
            Employee employee, String justification, Integer numberOfPages) {
        super.edit(academicServiceRequestSituationType, employee, justification);
        super.setNumberOfPages(numberOfPages);
    }

    @Override
    public void assertConcludedStatePreConditions() throws DomainException {
	if (getNumberOfPages() == null || getNumberOfPages().intValue() == 0) {
	    throw new DomainException(
            	"error.serviceRequests.documentRequests.numberOfPages.must.be.set");
	}
    }

}
