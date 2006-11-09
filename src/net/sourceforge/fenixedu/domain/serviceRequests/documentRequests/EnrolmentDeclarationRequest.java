package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class EnrolmentDeclarationRequest extends EnrolmentDeclarationRequest_Base {
    
    public  EnrolmentDeclarationRequest() {
        super();
    }

    public EnrolmentDeclarationRequest(StudentCurricularPlan studentCurricularPlan,
            AdministrativeOffice administrativeOffice, DocumentRequestType chosenDocumentRequestType,DocumentPurposeType documentPurposeType,
            String otherDocumentPurposeTypeDescription, Boolean urgentRequest, 
            ExecutionYear executionYear) {

        this();

        init(studentCurricularPlan, administrativeOffice, chosenDocumentRequestType,documentPurposeType,
            otherDocumentPurposeTypeDescription, urgentRequest,  executionYear);
        }

        protected void init(StudentCurricularPlan studentCurricularPlan,
            AdministrativeOffice administrativeOffice, DocumentRequestType chosenDocumentRequestType,DocumentPurposeType documentPurposeType,
            String otherDocumentPurposeTypeDescription, Boolean urgentRequest, 
            ExecutionYear executionYear) {

        init(studentCurricularPlan, administrativeOffice, chosenDocumentRequestType,documentPurposeType,
            otherDocumentPurposeTypeDescription, urgentRequest);

        checkParameters( executionYear);
        
        super.setExecutionYear(executionYear);
        }

        private void checkParameters(ExecutionYear executionYear) {
        
        if (executionYear == null) {
            throw new DomainException(
                "error.serviceRequests.documentRequests.EnrolmentCertificateRequest.executionYear.cannot.be.null");
        } else if (!getStudentCurricularPlan().hasAnyEnrolmentForExecutionYear(executionYear)) {
            throw new DomainException(
                "error.serviceRequests.documentRequests.EnrolmentCertificateRequest.executionYear.before.studentCurricularPlan.start");
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
	return DocumentRequestType.ENROLMENT_DECLARATION;
    }

    @Override
    public String getDocumentTemplateKey() {
	return getClass().getName();
    }

}
