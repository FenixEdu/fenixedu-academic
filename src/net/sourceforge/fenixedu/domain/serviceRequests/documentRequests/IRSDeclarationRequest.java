package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.joda.time.YearMonthDay;

public class IRSDeclarationRequest extends IRSDeclarationRequest_Base {
    
    static final private int FIRST_VALID_YEAR = 2006;
    
    private IRSDeclarationRequest() {
        super();
    }

    public IRSDeclarationRequest(Registration registration,
	    DocumentPurposeType documentPurposeType, String otherDocumentPurposeTypeDescription,
	    Integer year, Boolean freeProcessed) {

	this();
	this.init(registration, documentPurposeType, otherDocumentPurposeTypeDescription, year, freeProcessed);
    }

    protected void init(Registration registration, DocumentPurposeType documentPurposeType,
	    String otherDocumentPurposeTypeDescription, Integer year, Boolean freeProcessed) {

	super.init(registration, documentPurposeType, otherDocumentPurposeTypeDescription, freeProcessed);

	if (!registration.isActive()) {
	    throw new DomainException("IRSDeclarationRequest.registration.is.not.active");
	}
	
	if (!registration.isBolonha()) {
	    throw new DomainException("IRSDeclarationRequest.only.available.for.bolonha.registrations");
	}
	
	checkParameters(year);
	super.setYear(year);
    }

    private void checkParameters(Integer year) {
	if (new YearMonthDay(year, 1, 1).isBefore(new YearMonthDay(FIRST_VALID_YEAR, 1, 1))) {
	    throw new DomainException("IRSDeclarationRequest.only.available.after.first.valid.year");
	}
	
	if (year == null) {
	    throw new DomainException(
		    "error.serviceRequests.documentRequests.SchoolRegistrationDeclarationRequest.year.cannot.be.null");
	}
    }

    @Override
    final public DocumentRequestType getDocumentRequestType() {
	return DocumentRequestType.IRS_DECLARATION;
    }

    @Override
    final public String getDocumentTemplateKey() {
	return getClass().getName();
    }
    
    @Override
    final public void setYear(Integer year) {
	throw new DomainException(
		"error.serviceRequests.documentRequests.IRSDeclarationRequest.cannot.modify.year");
    }

    @Override
    final public ExecutionYear getExecutionYear() {
	return null;
    }
    
    @Override
    final public EventType getEventType() {
	return null;
    }

}
