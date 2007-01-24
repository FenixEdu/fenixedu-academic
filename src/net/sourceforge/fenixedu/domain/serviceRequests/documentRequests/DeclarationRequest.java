package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import java.util.Set;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.DeclarationRequestEvent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;
import net.sourceforge.fenixedu.domain.student.Registration;

public abstract class DeclarationRequest extends DeclarationRequest_Base {

    private static final int MAX_FREE_DECLARATIONS_PER_EXECUTION_YEAR = 4;
    
    protected DeclarationRequest() {
	super();
	super.setNumberOfPages(0);
    }

    protected void init(Registration registration, DocumentPurposeType documentPurposeType,
	    String otherDocumentPurposeTypeDescription) {

	init(registration);
	checkParameters(documentPurposeType, otherDocumentPurposeTypeDescription);

	super.setDocumentPurposeType(documentPurposeType);
	super.setOtherDocumentPurposeTypeDescription(otherDocumentPurposeTypeDescription);

    }

    private void checkParameters(DocumentPurposeType documentPurposeType,
	    String otherDocumentPurposeTypeDescription) {

	if (documentPurposeType == DocumentPurposeType.OTHER
		&& otherDocumentPurposeTypeDescription == null) {
	    throw new DomainException(
		    "error.serviceRequests.documentRequests.DeclarationRequest.otherDocumentPurposeTypeDescription.cannot.be.null.for.other.purpose.type");
	}

    }

    protected static DeclarationRequest create(Registration registration,
	    DocumentRequestType chosenDocumentRequestType,
	    DocumentPurposeType chosenDocumentPurposeType, String otherPurpose, String notes,
	    Boolean average, Boolean detailed, Integer year) {

	switch (chosenDocumentRequestType) {
	case SCHOOL_REGISTRATION_DECLARATION:
	    return new SchoolRegistrationDeclarationRequest(registration, chosenDocumentPurposeType,
		    otherPurpose);
	case ENROLMENT_DECLARATION:
	    return new EnrolmentDeclarationRequest(registration, chosenDocumentPurposeType,
		    otherPurpose);
	case IRS_DECLARATION:
	    return new IRSDeclarationRequest(registration, chosenDocumentPurposeType, otherPurpose, year);
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
	
	if (isPayable() && isPayed() && !getNumberOfPages().equals(numberOfPages)) {
	    throw new DomainException(
		    "error.serviceRequests.documentRequests.cannot.change.numberOfPages.on.payed.documents");
	}

	super.edit(academicServiceRequestSituationType, employee, justification);
	super.setNumberOfPages(numberOfPages);
    }

    @Override
    protected void internalChangeState(AcademicServiceRequestSituationType academicServiceRequestSituationType, Employee employee) {
	super.internalChangeState(academicServiceRequestSituationType, employee);

	if (academicServiceRequestSituationType == AcademicServiceRequestSituationType.CONCLUDED) {
	    if (getNumberOfPages() == null || getNumberOfPages().intValue() == 0) {
		throw new DomainException("error.serviceRequests.documentRequests.numberOfPages.must.be.set");
	    }

	    if (!isFree()) {
		new DeclarationRequestEvent(getAdministrativeOffice(), 
			getEventType(), getRegistration().getPerson(), this);
	    }
	}
    }

    @Override
    public Boolean getUrgentRequest() {
	return Boolean.FALSE;
    }
    
    protected boolean isFree() {
	if (!isPayable()) {
	    return true;
	}
	
	if (getDocumentPurposeType() == DocumentPurposeType.PPRE) {
	    return false;
	}
	
	final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();

	final Set<DocumentRequest> schoolRegistrationDeclarations = getRegistration()
		.getSucessfullyFinishedDocumentRequestsBy(currentExecutionYear,
			DocumentRequestType.SCHOOL_REGISTRATION_DECLARATION);
	
	final Set<DocumentRequest> enrolmentDeclarations = getRegistration()
		.getSucessfullyFinishedDocumentRequestsBy(currentExecutionYear,
			DocumentRequestType.ENROLMENT_DECLARATION);

	return (schoolRegistrationDeclarations.size() + enrolmentDeclarations.size()) < MAX_FREE_DECLARATIONS_PER_EXECUTION_YEAR;
    }

}
