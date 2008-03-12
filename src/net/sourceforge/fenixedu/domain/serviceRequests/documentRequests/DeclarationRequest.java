package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.DeclarationRequestEvent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.joda.time.DateTime;

public abstract class DeclarationRequest extends DeclarationRequest_Base {

    private static final int MAX_FREE_DECLARATIONS_PER_EXECUTION_YEAR = 4;

    protected DeclarationRequest() {
	super();
	super.setNumberOfPages(0);
    }

    protected void init(Registration registration, DocumentPurposeType documentPurposeType,
	    String otherDocumentPurposeTypeDescription, Boolean freeProcessed) {
	init(registration, null, documentPurposeType, otherDocumentPurposeTypeDescription, freeProcessed);
    }

    protected void init(Registration registration, ExecutionYear executionYear, DocumentPurposeType documentPurposeType,
	    String otherDocumentPurposeTypeDescription, Boolean freeProcessed) {

	super.init(registration, executionYear, new DateTime(), Boolean.FALSE, freeProcessed);

	super.checkParameters(documentPurposeType, otherDocumentPurposeTypeDescription);
	super.setDocumentPurposeType(documentPurposeType);
	super.setOtherDocumentPurposeTypeDescription(otherDocumentPurposeTypeDescription);
    }

    static final protected DeclarationRequest create(Registration registration, DocumentRequestType chosenDocumentRequestType,
	    DocumentPurposeType chosenDocumentPurposeType, String otherPurpose, Boolean average, Boolean detailed, Integer year,
	    Boolean freeProcessed) {

	switch (chosenDocumentRequestType) {
	case SCHOOL_REGISTRATION_DECLARATION:
	    return new SchoolRegistrationDeclarationRequest(registration, chosenDocumentPurposeType, otherPurpose, freeProcessed);
	case ENROLMENT_DECLARATION:
	    return new EnrolmentDeclarationRequest(registration, chosenDocumentPurposeType, otherPurpose, freeProcessed);
	case IRS_DECLARATION:
	    return new IRSDeclarationRequest(registration, chosenDocumentPurposeType, otherPurpose, year, freeProcessed);
	}

	return null;
    }

    @Override
    final public void setDocumentPurposeType(DocumentPurposeType documentPurposeType) {
	throw new DomainException("error.serviceRequests.documentRequests.DeclarationRequest.cannot.modify.documentPurposeType");
    }

    @Override
    final public void setOtherDocumentPurposeTypeDescription(String otherDocumentTypeDescription) {
	throw new DomainException(
		"error.serviceRequests.documentRequests.DeclarationRequest.cannot.modify.otherDocumentTypeDescription");
    }

    @Override
    final public Boolean getUrgentRequest() {
	return Boolean.FALSE;
    }

    final public void edit(final DocumentRequestBean documentRequestBean) {

	if (isPayable() && isPayed() && !getNumberOfPages().equals(documentRequestBean.getNumberOfPages())) {
	    throw new DomainException("error.serviceRequests.documentRequests.cannot.change.numberOfPages.on.payed.documents");
	}

	super.edit(documentRequestBean);
	super.setNumberOfPages(documentRequestBean.getNumberOfPages());
    }

    @Override
    protected void internalChangeState(AcademicServiceRequestBean academicServiceRequestBean) {
	super.internalChangeState(academicServiceRequestBean);

	if (academicServiceRequestBean.isToConclude()) {
	    if (getNumberOfPages() == null || getNumberOfPages().intValue() == 0) {
		throw new DomainException("error.serviceRequests.documentRequests.numberOfPages.must.be.set");
	    }

	    if (!isFree()) {
		new DeclarationRequestEvent(getAdministrativeOffice(), getEventType(), getRegistration().getPerson(), this);
	    }
	}
    }

    /**
     * Important: Notice that this method's return value may not be the same
     * before and after conclusion of the academic service request.
     */
    @Override
    final public boolean isFree() {
	if (getDocumentPurposeType() == DocumentPurposeType.PPRE) {
	    return false;
	}

	final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
	final Set<DocumentRequest> schoolRegistrationDeclarations = getRegistration().getSucessfullyFinishedDocumentRequestsBy(
		currentExecutionYear, DocumentRequestType.SCHOOL_REGISTRATION_DECLARATION, false);

	final Set<DocumentRequest> enrolmentDeclarations = getRegistration().getSucessfullyFinishedDocumentRequestsBy(
		currentExecutionYear, DocumentRequestType.ENROLMENT_DECLARATION, false);

	return super.isFree()
		|| ((schoolRegistrationDeclarations.size() + enrolmentDeclarations.size()) < MAX_FREE_DECLARATIONS_PER_EXECUTION_YEAR);
    }

    @Override
    public boolean isPayedUponCreation() {
	return false;
    }

    @Override
    public boolean isPagedDocument() {
	return true;
    }

    @Override
    public boolean isToPrint() {
	return true;
    }

    @Override
    public boolean isPossibleToSendToOtherEntity() {
	return false;
    }

    @Override
    public boolean isAvailableForTransitedRegistrations() {
	return false;
    }

    @Override
    public boolean hasPersonalInfo() {
	return false;
    }

}
