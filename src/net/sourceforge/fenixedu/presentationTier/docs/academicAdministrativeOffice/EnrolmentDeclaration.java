package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentPurposeType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.EnrolmentDeclarationRequest;
import net.sourceforge.fenixedu.domain.student.Registration;

public class EnrolmentDeclaration extends AdministrativeOfficeDocument {

    protected EnrolmentDeclaration(final DocumentRequest documentRequest) {
	super(documentRequest);
    }

    @Override
    protected void fillReport() {
	super.fillReport();
	
	final EnrolmentDeclarationRequest enrolmentDeclarationRequest = (EnrolmentDeclarationRequest) getDocumentRequest();
	final Registration registration = enrolmentDeclarationRequest.getRegistration();
	final ExecutionYear executionYear = enrolmentDeclarationRequest.getExecutionYear();
	
	final Integer curricularYear = Integer.valueOf(registration.getCurricularYear(executionYear));
	parameters.put("curricularYear", curricularYear);

	final List<Enrolment> enrolments = registration.getStudentCurricularPlan(executionYear).getEnrolmentsByExecutionYear(executionYear);
	parameters.put("numberEnrolments", Integer.valueOf(enrolments.size()));
	
	if (enrolmentDeclarationRequest.getDocumentPurposeType() != null) {
	    StringBuilder documentPurpose = new StringBuilder();
    
	    documentPurpose.append(resourceBundle.getString("documents.declaration.valid.purpose")).append(" ");
	    if (enrolmentDeclarationRequest.getDocumentPurposeType() == DocumentPurposeType.OTHER
		    && !StringUtils.isEmpty(enrolmentDeclarationRequest.getOtherDocumentPurposeTypeDescription())) {
		documentPurpose.append(enrolmentDeclarationRequest.getOtherDocumentPurposeTypeDescription().toUpperCase());		
	    } else {
		documentPurpose.append(enumerationBundle.getString(enrolmentDeclarationRequest.getDocumentPurposeType().name()).toUpperCase());
	    }
	    documentPurpose.append(".");
	    
	    parameters.put("documentPurpose", documentPurpose.toString());
	} else {
	    parameters.put("documentPurpose", "");
	}
    }

}
