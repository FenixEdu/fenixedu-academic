package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.ApprovementCertificateRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.util.StringUtils;

public class ApprovementCertificate extends AdministrativeOfficeDocument {
    
    protected ApprovementCertificate(final DocumentRequest documentRequest) {
	super(documentRequest);
    }

    @Override
    protected void fillReport() {
	super.fillReport();

	final ApprovementCertificateRequest approvementCertificateRequest = (ApprovementCertificateRequest) getDocumentRequest();

	final List<Enrolment> approvedEnrolments = new ArrayList<Enrolment>(approvementCertificateRequest.getRegistration().getApprovedEnrolments());
	parameters.put("numberApprovements", Integer.valueOf(approvedEnrolments.size()));

	Collections.sort(approvedEnrolments, Enrolment.COMPARATOR_BY_EXECUTION_PERIOD_AND_NAME_AND_ID);

	final StringBuilder approvementsInfo = new StringBuilder();
	ExecutionYear lastReportedExecutionYear = approvedEnrolments.get(0).getExecutionPeriod().getExecutionYear(); 
	for (final Enrolment approvedEnrolment : approvedEnrolments) {
	    final ExecutionYear executionYear = approvedEnrolment.getExecutionPeriod().getExecutionYear();
	    final EnrolmentEvaluation latestEnrolmentEvaluation = approvedEnrolment.getLatestEnrolmentEvaluation();
	    
	    if (executionYear != lastReportedExecutionYear) {
		lastReportedExecutionYear = executionYear;
		approvementsInfo.append("\n");
	    }
	    
	    final StringBuilder gradeInfo = new StringBuilder();
	    gradeInfo.append(" ").append(latestEnrolmentEvaluation.getGrade());
	    gradeInfo.append(
		    StringUtils.rightPad(
			    "(" 
			    + enumerationBundle.getString(latestEnrolmentEvaluation.getGrade()) 
			    + ")",
			    11,
			    ' '));
	    gradeInfo.append(" ").append(resourceBundle.getString("label.in"));
	    
	    gradeInfo.append(" ").append(executionYear.getYear());
	    
	    approvementsInfo.append(
		    StringUtils.multipleLineRightPadWithSuffix(
			    approvedEnrolment.getName().getContent().toUpperCase(),
			    LINE_LENGTH, 
			    '-',
			    gradeInfo.toString())).append("\n");
	}

	approvementsInfo.append(StringUtils.multipleLineRightPad(
		org.apache.commons.lang.StringUtils.EMPTY,
		LINE_LENGTH, 
		'-')).append("\n");

	
	parameters.put("approvementsInfo", approvementsInfo.toString());
    }

}
