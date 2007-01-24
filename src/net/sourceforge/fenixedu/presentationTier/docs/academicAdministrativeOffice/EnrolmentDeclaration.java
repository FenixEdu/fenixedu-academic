package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.util.List;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentPurposeType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.EnrolmentDeclarationRequest;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.apache.commons.lang.StringUtils;

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

	StringBuilder approvementInfo = new StringBuilder();
	if (enrolmentDeclarationRequest.getDocumentPurposeType() == DocumentPurposeType.PPRE) {
	    if (registration.isFirstTime(executionYear)) {
		approvementInfo.append(", pela 1ª vez");
	    } else if (registration.hasApprovement(executionYear.getPreviousExecutionYear())) {
		approvementInfo.append(" e teve aproveitamento no ano lectivo " + executionYear.getPreviousExecutionYear().getYear());
	    } else {
		approvementInfo.append(" e não teve aproveitamento no ano lectivo " + executionYear.getPreviousExecutionYear().getYear());
	    }
	}
	parameters.put("approvementInfo", approvementInfo.toString());
	
	StringBuilder documentPurpose = new StringBuilder();
	if (enrolmentDeclarationRequest.getDocumentPurposeType() != null) {
	    documentPurpose.append(resourceBundle.getString("documents.declaration.valid.purpose")).append(" ");
	    if (enrolmentDeclarationRequest.getDocumentPurposeType() == DocumentPurposeType.OTHER
		    && !StringUtils.isEmpty(enrolmentDeclarationRequest.getOtherDocumentPurposeTypeDescription())) {
		documentPurpose.append(enrolmentDeclarationRequest.getOtherDocumentPurposeTypeDescription().toUpperCase());		
	    } else {
		documentPurpose.append(enumerationBundle.getString(enrolmentDeclarationRequest.getDocumentPurposeType().name()).toUpperCase());
	    }
	    documentPurpose.append(".");
	} 
	parameters.put("documentPurpose", documentPurpose.toString());
    }

}
