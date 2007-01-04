package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.DeclarationRequestEvent;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;
import net.sourceforge.fenixedu.domain.student.Registration;

public class EnrolmentDeclarationRequest extends EnrolmentDeclarationRequest_Base {
    
    public  EnrolmentDeclarationRequest() {
        super();
    }

    public EnrolmentDeclarationRequest(Registration registration,
	    DocumentPurposeType documentPurposeType, String otherDocumentPurposeTypeDescription) {

	this();
	this.init(registration, documentPurposeType, otherDocumentPurposeTypeDescription);
    }

    protected void init(Registration registration,DocumentPurposeType documentPurposeType,
            String otherDocumentPurposeTypeDescription) {

        super.init(registration, documentPurposeType, otherDocumentPurposeTypeDescription);

        final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
        if (!getRegistration().hasAnyEnrolmentsIn(currentExecutionYear)) {
            throw new DomainException("EnrolmentDeclarationRequest.no.enrolments.for.registration.in.given.executionYear");
        }
        super.setExecutionYear(currentExecutionYear);
    }

    @Override
    public Set<AdministrativeOfficeType> getPossibleAdministrativeOffices() {
	final Set<AdministrativeOfficeType> result = new HashSet<AdministrativeOfficeType>();
	
	result.add(AdministrativeOfficeType.DEGREE);
	
	return result;
    }

    @Override
    public DocumentRequestType getDocumentRequestType() {
	return DocumentRequestType.ENROLMENT_DECLARATION;
    }

    @Override
    public String getDocumentTemplateKey() {
	return getClass().getName();
    }

    @Override
    public void setExecutionYear(ExecutionYear executionYear) {
	throw new DomainException(
		"error.serviceRequests.documentRequests.EnrolmentCertificateRequest.cannot.modify.executionYear");
    }
    
    @Override
    protected void internalChangeState(
	    AcademicServiceRequestSituationType academicServiceRequestSituationType, Employee employee) {

	super.internalChangeState(academicServiceRequestSituationType, employee);

	if (academicServiceRequestSituationType == AcademicServiceRequestSituationType.CONCLUDED
		&& !isFree()) {
	    new DeclarationRequestEvent(getAdministrativeOffice(), getEventType(), getRegistration()
		    .getPerson(), this);
	}
    }

    @Override
    public EventType getEventType() {
	return EventType.ENROLMENT_DECLARATION_REQUEST;
    }

}
