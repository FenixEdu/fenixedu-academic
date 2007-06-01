package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.ApprovementCertificateRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.student.MobilityProgram;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.StringUtils;

public class ApprovementCertificate extends AdministrativeOfficeDocument {
    
    protected ApprovementCertificate(final DocumentRequest documentRequest) {
	super(documentRequest);
    }

    @Override
    protected void fillReport() {
	super.fillReport();

	final ApprovementCertificateRequest approvementCertificateRequest = (ApprovementCertificateRequest) getDocumentRequest();
	final Registration registration = approvementCertificateRequest.getRegistration();

	final List<IEnrolment> approvedIEnrolments = new ArrayList<IEnrolment>(registration.getApprovedIEnrolments());
	Collections.sort(approvedIEnrolments, IEnrolment.COMPARATOR_BY_EXECUTION_PERIOD_AND_NAME);

	parameters.put("numberApprovements", Integer.valueOf(approvedIEnrolments.size()));
	parameters.put("approvementsInfo", getApprovementsInfo(approvementCertificateRequest, approvedIEnrolments));
    }

    final private String getApprovementsInfo(final ApprovementCertificateRequest approvementCertificateRequest, final List<IEnrolment> approvedIEnrolments) {
	final StringBuilder result = new StringBuilder();

	final Map<Unit,String> academicUnitIdentifiers = new HashMap<Unit,String>();
	ExecutionYear lastReportedExecutionYear = approvedIEnrolments.get(0).getExecutionPeriod().getExecutionYear(); 
	for (final IEnrolment approvedIEnrolment : approvedIEnrolments) {
	    final ExecutionYear executionYear = approvedIEnrolment.getExecutionYear();
	    if (executionYear != lastReportedExecutionYear) {
		lastReportedExecutionYear = executionYear;
		result.append("\n");
	    }
	    
	    result.append(
		    StringUtils.multipleLineRightPadWithSuffix(
			    getEnrolmentName(academicUnitIdentifiers, approvedIEnrolment), 
			    LINE_LENGTH, 
			    '-', 
			    getGradeInfo(approvedIEnrolment,executionYear)));

	    result.append("\n");
	}
	result.append(generateEndLine());
	
	if (!academicUnitIdentifiers.isEmpty()) {
	    result.append("\n").append(getAcademicUnitInfo(academicUnitIdentifiers, approvementCertificateRequest.getMobilityProgram()));
	}
	
	return result.toString();
    }

    final private String getEnrolmentName(final Map<Unit, String> academicUnitIdentifiers, final IEnrolment approvedIEnrolment) {
	StringBuilder result = new StringBuilder();
	
	if (approvedIEnrolment.isExternalEnrolment()) {
	    result.append(getAcademicUnitIdentifier(academicUnitIdentifiers, approvedIEnrolment.getAcademicUnit()));
	}
	
	result.append(approvedIEnrolment.getName().toUpperCase());
	
	return result.toString();
    }

    @SuppressWarnings("static-access")
    final private String getAcademicUnitIdentifier(final Map<Unit, String> academicUnitIdentifiers, final Unit academicUnit) {
	if (!academicUnitIdentifiers.containsKey(academicUnit)) {
	    academicUnitIdentifiers.put(academicUnit, this.identifiers[academicUnitIdentifiers.size()]);
	} 
	
	return academicUnitIdentifiers.get(academicUnit);
    }

    final private String getGradeInfo(final IEnrolment approvedIEnrolment, final ExecutionYear executionYear) {
	final StringBuilder result = new StringBuilder();
	
	result.append(" ").append(approvedIEnrolment.getGradeValue());
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

    final private String getAcademicUnitInfo(final Map<Unit, String> academicUnitIdentifiers, final MobilityProgram mobilityProgram) {
	final StringBuilder result = new StringBuilder();
	
	for (final Entry<Unit,String> academicUnitIdentifier : academicUnitIdentifiers.entrySet()) {
	    final StringBuilder academicUnit = new StringBuilder();
	    
	    academicUnit.append(academicUnitIdentifier.getValue());
	    academicUnit.append(" ").append(resourceBundle.getString("documents.external.curricular.courses.program"));
	    academicUnit.append(" ").append(enumerationBundle.getString(mobilityProgram.getQualifiedName()));
	    academicUnit.append(" ").append(resourceBundle.getString("in.feminine"));
	    academicUnit.append(" ").append(academicUnitIdentifier.getKey().getName().toUpperCase());
	    
	    result.append(StringUtils.multipleLineRightPad(academicUnit.toString(), LINE_LENGTH, '-') + "\n");
	}
	
	return result.toString();
    }
    
}
