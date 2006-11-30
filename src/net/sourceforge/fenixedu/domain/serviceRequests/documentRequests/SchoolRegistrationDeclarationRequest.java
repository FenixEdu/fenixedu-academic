package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;

public class SchoolRegistrationDeclarationRequest extends SchoolRegistrationDeclarationRequest_Base {

    public SchoolRegistrationDeclarationRequest() {
	super();
    }

    public SchoolRegistrationDeclarationRequest(Registration registration,
	    DocumentPurposeType documentPurposeType, String otherDocumentPurposeTypeDescription,
	    ExecutionYear executionYear) {

	this();

	init(registration, documentPurposeType, otherDocumentPurposeTypeDescription, executionYear);
    }

    protected void init(Registration registration, DocumentPurposeType documentPurposeType,
	    String otherDocumentPurposeTypeDescription, ExecutionYear executionYear) {

	super.init(registration, documentPurposeType, otherDocumentPurposeTypeDescription);

	checkParameters(executionYear);
	super.setExecutionYear(executionYear);

    }

    private void checkParameters(ExecutionYear executionYear) {
	if (executionYear == null) {
	    throw new DomainException(
		    "error.serviceRequests.documentRequests.SchoolRegistrationDeclarationRequest.executionYear.cannot.be.null");
	} else if (!getRegistration().hasSchoolRegistration(executionYear)) {
	    throw new DomainException(
		    "error.serviceRequests.documentRequests.no.school.registration.for.given.executionYear");
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
	return DocumentRequestType.SCHOOL_REGISTRATION_DECLARATION;
    }

    @Override
    public String getDocumentTemplateKey() {
	return getClass().getName();
    }

    @Override
    public void setExecutionYear(ExecutionYear executionYear) {
	throw new DomainException(
		"error.serviceRequests.documentRequests.SchoolRegistrationDeclarationRequest.cannot.modify.executionYear");
    }

}
