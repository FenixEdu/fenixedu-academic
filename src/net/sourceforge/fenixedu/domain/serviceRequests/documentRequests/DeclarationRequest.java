package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;
import net.sourceforge.fenixedu.domain.student.Registration;

public abstract class DeclarationRequest extends DeclarationRequest_Base {

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

    @Override
    public EventType getEventType() {
	return null;
    }

    public static DeclarationRequest create(Registration registration,
	    DocumentRequestType chosenDocumentRequestType,
	    DocumentPurposeType chosenDocumentPurposeType, String otherPurpose, String notes,
	    Boolean average, Boolean detailed, ExecutionYear executionYear) {

	switch (chosenDocumentRequestType) {
	case SCHOOL_REGISTRATION_DECLARATION:
	    return new SchoolRegistrationDeclarationRequest(registration, chosenDocumentPurposeType,
		    otherPurpose, executionYear);
	case ENROLMENT_DECLARATION:
	    return new EnrolmentDeclarationRequest(registration, chosenDocumentPurposeType,
		    otherPurpose, executionYear);

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
	    throw new DomainException("error.serviceRequests.documentRequests.numberOfPages.must.be.set");
	}
    }

}
