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
	
	parameters.put("curricularYear", getCurricularYear());
	
	final List<Enrolment> enrolments = (List<Enrolment>) getDocumentRequest().getRegistration().getEnrolments(getDocumentRequest().getExecutionYear());
	parameters.put("numberEnrolments", Integer.valueOf(enrolments.size()));

	parameters.put("approvementInfo", getApprovementInfo());
	parameters.put("documentPurpose", getDocumentPurpose());
    }

    final private String getCurricularYear() {
	final StringBuilder result = new StringBuilder();
	
	if (!getDocumentRequest().getDegreeType().hasExactlyOneCurricularYear()) {
	    final Integer curricularYear = Integer.valueOf(getDocumentRequest().getRegistration().getCurricularYear(getDocumentRequest().getExecutionYear()));
	    
	    result.append(enumerationBundle.getString(curricularYear.toString() + ".ordinal").toUpperCase());
	    result.append(" ano curricular, do ");
	}

	return result.toString();
    }

    final private String getApprovementInfo() {
	final StringBuilder result = new StringBuilder();
	
	final EnrolmentDeclarationRequest enrolmentDeclarationRequest = (EnrolmentDeclarationRequest) getDocumentRequest();
	final Registration registration = enrolmentDeclarationRequest.getRegistration();
	final ExecutionYear executionYear = enrolmentDeclarationRequest.getExecutionYear();
	
	if (enrolmentDeclarationRequest.getDocumentPurposeType() == DocumentPurposeType.PPRE) {
	    if (registration.isFirstTime(executionYear)) {
		result.append(", pela 1ª vez");
	    } else if (registration.hasApprovement(executionYear.getPreviousExecutionYear())) {
		result.append(" e teve aproveitamento no ano lectivo " + executionYear.getPreviousExecutionYear().getYear());
	    } else {
		result.append(" e não teve aproveitamento no ano lectivo " + executionYear.getPreviousExecutionYear().getYear());
	    }
	}
	
	return result.toString();
    }

    final private String getDocumentPurpose() {
	final StringBuilder result = new StringBuilder();
	
	final EnrolmentDeclarationRequest enrolmentDeclarationRequest = (EnrolmentDeclarationRequest) getDocumentRequest();
	
	if (enrolmentDeclarationRequest.getDocumentPurposeType() != null) {
	    result.append(resourceBundle.getString("documents.declaration.valid.purpose")).append(" ");
	    if (enrolmentDeclarationRequest.getDocumentPurposeType() == DocumentPurposeType.OTHER
		    && !StringUtils.isEmpty(enrolmentDeclarationRequest.getOtherDocumentPurposeTypeDescription())) {
		result.append(enrolmentDeclarationRequest.getOtherDocumentPurposeTypeDescription().toUpperCase());		
	    } else {
		result.append(enumerationBundle.getString(enrolmentDeclarationRequest.getDocumentPurposeType().name()).toUpperCase());
	    }
	    result.append(".");
	}
	
	return result.toString();
    }

}
