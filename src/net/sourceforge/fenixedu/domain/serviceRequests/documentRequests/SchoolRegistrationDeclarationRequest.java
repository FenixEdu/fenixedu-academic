package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class SchoolRegistrationDeclarationRequest extends SchoolRegistrationDeclarationRequest_Base {
    
    public  SchoolRegistrationDeclarationRequest() {
        super();
    }
    
    public  SchoolRegistrationDeclarationRequest(StudentCurricularPlan studentCurricularPlan,
        AdministrativeOffice administrativeOffice, DocumentRequestType documentRequestType,DocumentPurposeType documentPurposeType,
        String otherDocumentPurposeTypeDescription, 
        Boolean urgentRequest,ExecutionYear executionYear) {

    this();
  
    init(studentCurricularPlan, administrativeOffice, documentRequestType,documentPurposeType,
        otherDocumentPurposeTypeDescription, urgentRequest,executionYear);
    }
    
    protected void init(StudentCurricularPlan studentCurricularPlan,
        AdministrativeOffice administrativeOffice, DocumentRequestType documentRequestType,DocumentPurposeType documentPurposeType,
        String otherDocumentPurposeTypeDescription, 
        Boolean urgentRequest,ExecutionYear executionYear) {

    init(studentCurricularPlan, administrativeOffice, documentRequestType,documentPurposeType,
        otherDocumentPurposeTypeDescription,urgentRequest);

    checkParameters(executionYear);
    super.setExecutionYear(executionYear);
    
    
    }
    
    private void checkParameters(ExecutionYear executionYear) {
    if (executionYear == null) {
        throw new DomainException(
            "error.serviceRequests.documentRequests.SchoolRegistrationDeclarationRequest.executionYear.cannot.be.null");
    } else if (!getStudentCurricularPlan().hasSchoolRegistration(executionYear)) {
        throw new DomainException(
            "error.serviceRequests.documentRequests.SchoolRegistrationDecalrationRequest.executionYear.before.studentCurricularPlan.start");
    }
    }
    
    @Override
    public Set<AdministrativeOfficeType> getPossibleAdministrativeOffices() {
	final Set<AdministrativeOfficeType> result = new HashSet<AdministrativeOfficeType>();
	
	result.add(AdministrativeOfficeType.DEGREE);
	
	return result;
    }

    @Override
    protected void assertProcessingStatePreConditions() throws DomainException {
	// TODO Auto-generated method stub
	
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
