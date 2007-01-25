package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.joda.time.YearMonthDay;

public class IRSDeclarationRequest extends IRSDeclarationRequest_Base {
    
    static final private int FIRST_VALID_YEAR = 2006;
    
    public  IRSDeclarationRequest() {
        super();
    }

    public IRSDeclarationRequest(Registration registration,
	    DocumentPurposeType documentPurposeType, String otherDocumentPurposeTypeDescription,
	    Integer year) {

	this();

	init(registration, documentPurposeType, otherDocumentPurposeTypeDescription, year);
    }

    protected void init(Registration registration, DocumentPurposeType documentPurposeType,
	    String otherDocumentPurposeTypeDescription, Integer year) {

	super.init(registration, documentPurposeType, otherDocumentPurposeTypeDescription);

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
    public Set<AdministrativeOfficeType> getPossibleAdministrativeOffices() {
	final Set<AdministrativeOfficeType> result = new HashSet<AdministrativeOfficeType>();
	
	result.add(AdministrativeOfficeType.DEGREE);
	
	return result;
    }

    @Override
    public DocumentRequestType getDocumentRequestType() {
	return DocumentRequestType.IRS_DECLARATION;
    }

    @Override
    public String getDocumentTemplateKey() {
	return getClass().getName();
    }
    
    @Override
    public void setYear(Integer year) {
	throw new DomainException(
		"error.serviceRequests.documentRequests.IRSDeclarationRequest.cannot.modify.year");
    }

    @Override
    public ExecutionYear getExecutionYear() {
	return null;
    }
    
    public EventType getEventType() {
	return null;
    }

}
