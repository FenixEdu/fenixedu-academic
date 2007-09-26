package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
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
	parameters.put("approvementsInfo", getApprovementsInfo());
    }

    final private String getApprovementsInfo() {
	final StringBuilder result = new StringBuilder();

	final List<IEnrolment> approvedIEnrolments = new ArrayList<IEnrolment>(getDocumentRequest().getRegistration().getApprovedIEnrolments());
	Collections.sort(approvedIEnrolments, IEnrolment.COMPARATOR_BY_EXECUTION_PERIOD_AND_NAME);

	final List<Enrolment> extraCurricularEnrolments = new ArrayList<Enrolment>();
	final List<Enrolment> propaedeuticEnrolments = new ArrayList<Enrolment>();
	final Map<Unit,String> academicUnitIdentifiers = new HashMap<Unit,String>();

	reportIEnrolments(result, approvedIEnrolments, extraCurricularEnrolments, propaedeuticEnrolments, academicUnitIdentifiers);
	
	if (!extraCurricularEnrolments.isEmpty()) {
	    reportRemainingEnrolments(result, extraCurricularEnrolments, "Extra-Curriculares");
	}
	    
	if (!propaedeuticEnrolments.isEmpty()) {
	    reportRemainingEnrolments(result, propaedeuticEnrolments, "Propedêuticas");
	}
	
    	if (getDocumentRequest().isToShowCredits()) {
    	    result.append(getDismissalsEctsCreditsInfo());
    	}
	
    	result.append(generateEndLine());
	
    	if (!academicUnitIdentifiers.isEmpty()) {
	    result.append("\n").append(getAcademicUnitInfo(academicUnitIdentifiers, ((ApprovementCertificateRequest) getDocumentRequest()).getMobilityProgram()));
	}
	
	return result.toString();
    }

    final private void reportIEnrolments(final StringBuilder result, final List<IEnrolment> approvedIEnrolments, final List<Enrolment> extraCurricularEnrolments, final List<Enrolment> propaedeuticEnrolments, final Map<Unit, String> academicUnitIdentifiers) {
	ExecutionYear lastReportedExecutionYear = approvedIEnrolments.get(0).getExecutionPeriod().getExecutionYear(); 
	for (final IEnrolment approvedIEnrolment : approvedIEnrolments) {
	    if (approvedIEnrolment.isEnrolment()) {
		final Enrolment enrolment = (Enrolment) approvedIEnrolment;

		if (enrolment.isExtraCurricular()) {
		    extraCurricularEnrolments.add(enrolment);
		    continue;
		} else if (enrolment.isPropaedeutic()) {
		    propaedeuticEnrolments.add(enrolment);
		    continue;
		}
	    }
	
	    final ExecutionYear executionYear = approvedIEnrolment.getExecutionYear();
	    if (executionYear != lastReportedExecutionYear) {
		lastReportedExecutionYear = executionYear;
		result.append("\n");
	    }
	    
	    reportIEnrolment(result, approvedIEnrolment, academicUnitIdentifiers, executionYear);
	}
    }

    final private void reportRemainingEnrolments(final StringBuilder result, final List<Enrolment> enrolments, final String title) {
	result.append(generateEndLine()).append("\n").append(title).append(":\n");
	
	for (final Enrolment enrolment : enrolments) {
	    reportIEnrolment(result, enrolment, null, enrolment.getExecutionYear());
	}
    }

    final private void reportIEnrolment(final StringBuilder result, final IEnrolment approvedIEnrolment, final Map<Unit, String> academicUnitIdentifiers, final ExecutionYear executionYear) {
	result.append(
	    StringUtils.multipleLineRightPadWithSuffix(
		    getEnrolmentName(academicUnitIdentifiers, approvedIEnrolment), 
		    LINE_LENGTH, 
		    '-', 
		    getCreditsAndGradeInfo(approvedIEnrolment,executionYear))).append("\n");
    }

    final private String getCreditsAndGradeInfo(final IEnrolment approvedIEnrolment, final ExecutionYear executionYear) {
	final StringBuilder result = new StringBuilder();
	
	if (getDocumentRequest().isToShowCredits()) {
	    getCreditsInfo(result, approvedIEnrolment);
	}
	result.append(approvedIEnrolment.getGradeValue());
	result.append(
	    StringUtils.rightPad(
		    "(" 
		    + enumerationBundle.getString(approvedIEnrolment.getGradeValue()) 
		    + ")",
		    SUFFIX_LENGTH,
		    ' '));
	result.append(" ").append(resourceBundle.getString("label.in"));
	result.append(" ").append(executionYear.getYear());
	
	return result.toString();
    }

}
