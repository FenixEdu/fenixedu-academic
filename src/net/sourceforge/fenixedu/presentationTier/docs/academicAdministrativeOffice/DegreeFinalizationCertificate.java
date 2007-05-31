package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DegreeFinalizationCertificateRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.student.MobilityProgram;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.LanguageUtils;
import net.sourceforge.fenixedu.util.StringUtils;

public class DegreeFinalizationCertificate extends AdministrativeOfficeDocument {

    protected DegreeFinalizationCertificate(final DocumentRequest documentRequest) {
	super(documentRequest);
    }

    @Override
    protected void fillReport() {
	super.fillReport();

	final DegreeFinalizationCertificateRequest degreeFinalizationCertificateRequest = (DegreeFinalizationCertificateRequest) getDocumentRequest();
	final Registration registration = degreeFinalizationCertificateRequest.getRegistration();

	parameters.put("degreeFinalizationDate", registration.getConclusionDate().toString("dd 'de' MMMM 'de' yyyy", LanguageUtils.getLocale()));
	parameters.put("degreeFinalizationGrade", getDegreeFinalizationGrade(degreeFinalizationCertificateRequest));
	parameters.put("degreeFinalizationInfo", getDegreeFinalizationInfo(degreeFinalizationCertificateRequest, registration));
    }

    final private String getDegreeFinalizationGrade(final DegreeFinalizationCertificateRequest degreeFinalizationCertificateRequest) {
	final StringBuilder result = new StringBuilder();
	
	if (degreeFinalizationCertificateRequest.getAverage()) {
	    final Integer finalAverage = degreeFinalizationCertificateRequest.getRegistration().getFinalAverage();
	    result.append(" ").append(resourceBundle.getString("documents.registration.final.arithmetic.mean"));
	    result.append(" ").append(finalAverage);
	    result.append(" (").append(enumerationBundle.getString(finalAverage.toString()));
	    result.append(") ").append(resourceBundle.getString("values"));
	}
	
	return result.toString();
    }

    final private String getDegreeFinalizationInfo(final DegreeFinalizationCertificateRequest degreeFinalizationCertificateRequest, final Registration registration) {
	final StringBuilder result = new StringBuilder();
	
	if (degreeFinalizationCertificateRequest.getDetailed()) {
	    result.append(resourceBundle.getString("documents.registration.approved.enrolments.info"));
	    result.append(":\n");
	    
	    final List<IEnrolment> approvedIEnrolments = new ArrayList<IEnrolment>(registration.getApprovedIEnrolments());
	    Collections.sort(approvedIEnrolments, IEnrolment.COMPARATOR_BY_EXECUTION_PERIOD_AND_NAME);

	    final Map<Unit,String> academicUnitIdentifiers = new HashMap<Unit,String>();
	    for (final IEnrolment approvedIEnrolment : approvedIEnrolments) {
		result.append(
			StringUtils.multipleLineRightPadWithSuffix(
				getEnrolmentName(academicUnitIdentifiers, approvedIEnrolment), 
				LINE_LENGTH,
				'-', 
				getGradeInfo(approvedIEnrolment)));
		
		result.append("\n");
	    }
	    
	    result.append(generateEndLine());

	    if (!academicUnitIdentifiers.isEmpty()) {
		result.append("\n").append(getAcademicUnitInfo(academicUnitIdentifiers, degreeFinalizationCertificateRequest.getMobilityProgram()));
	    }
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

    final private String getGradeInfo(final IEnrolment approvedIEnrolment) {
	final StringBuilder result = new StringBuilder();

	result.append(" ").append(resourceBundle.getString("label.with"));
	result.append(" ").append(approvedIEnrolment.getGrade());
	result.append(
		StringUtils.rightPad(
			"("
			+ enumerationBundle.getString(approvedIEnrolment.getGrade()) 
			+ ")", 
			SUFFIX_LENGTH, 
			' '));
	result.append(resourceBundle.getString("values"));
	
	return result.toString();
    }

    final private String getAcademicUnitInfo(final Map<Unit, String> academicUnitIdentifiers, final MobilityProgram mobilityProgram) {
	final StringBuilder result = new StringBuilder();
	
	for (final Entry<Unit,String> academicUnitIdentifier : academicUnitIdentifiers.entrySet()) {
	    final StringBuilder academicUnit = new StringBuilder();
	    
	    academicUnit.append(academicUnitIdentifier.getValue());
	    academicUnit.append(" ").append(resourceBundle.getString("documents.external.curricular.courses.program"));
	    academicUnit.append(" ").append(enumerationBundle.getString(mobilityProgram.getQualifiedName()).toUpperCase());
	    academicUnit.append(" ").append(resourceBundle.getString("in.feminine"));
	    academicUnit.append(" ").append(academicUnitIdentifier.getKey().getName().toUpperCase());
	    
	    result.append(StringUtils.multipleLineRightPad(academicUnit.toString(), LINE_LENGTH, '-') + "\n");
	}
	
	return result.toString();
    }
    
}
