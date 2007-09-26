package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.Grade;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DegreeFinalizationCertificateRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
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
	parameters.put("degreeFinalizationEcts", String.valueOf(registration.getEctsCredits()));
	parameters.put("creditsDescription", getCreditsDescription());
	parameters.put("graduateTitle", getGraduateTitle());
	parameters.put("diplomaDescription", getDiplomaDescription());
	parameters.put("degreeFinalizationInfo", getDegreeFinalizationInfo(degreeFinalizationCertificateRequest, registration));
    }

    final private String getDegreeFinalizationGrade(final DegreeFinalizationCertificateRequest degreeFinalizationCertificateRequest) {
	final StringBuilder result = new StringBuilder();
	
	if (degreeFinalizationCertificateRequest.getAverage()) {
	    final Integer finalAverage = degreeFinalizationCertificateRequest.getRegistration().getFinalAverage();
	    result.append(", ").append(resourceBundle.getString("documents.registration.final.arithmetic.mean"));
	    result.append(" de ").append(finalAverage);
	    result.append(" (").append(enumerationBundle.getString(finalAverage.toString()));
	    result.append(") ").append(resourceBundle.getString("values"));
	}
	
	return result.toString();
    }

    final private String getGraduateTitle() {
	final StringBuilder result = new StringBuilder();
	
	final DegreeType degreeType = getDocumentRequest().getDegreeType();
	if (degreeType.getQualifiesForGraduateTitle()) {
	    result.append("pelo que tem direito ao grau académico de ");
	    result.append(degreeType.getGraduateTitle());
	    result.append(", ");
	}
	
	return result.toString();
    }

    final private String getDiplomaDescription() {
	final StringBuilder result = new StringBuilder();
	
	final Degree degree = getDocumentRequest().getDegree();
	final DegreeType degreeType = degree.getDegreeType();
	switch (degreeType) {
	case BOLONHA_ADVANCED_FORMATION_DIPLOMA:
	case BOLONHA_SPECIALIZATION_DEGREE:
		result.append("o respectivo diploma");
	    break;
	default:
	    result.append("a respectiva carta");
	    break;
	}
	
	return result.toString();
    }

    final private String getDegreeFinalizationInfo(final DegreeFinalizationCertificateRequest degreeFinalizationCertificateRequest, final Registration registration) {
	final StringBuilder result = new StringBuilder();
	
	if (degreeFinalizationCertificateRequest.getDetailed()) {
	    final List<IEnrolment> approvedIEnrolments = new ArrayList<IEnrolment>(registration.getApprovedIEnrolments());
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
		result.append("\n").append(getAcademicUnitInfo(academicUnitIdentifiers, degreeFinalizationCertificateRequest.getMobilityProgram()));
	    }
	}
	
	return result.toString();
    }

    final private void reportIEnrolments(final StringBuilder result, final List<IEnrolment> approvedIEnrolments, final List<Enrolment> extraCurricularEnrolments, final List<Enrolment> propaedeuticEnrolments, final Map<Unit, String> academicUnitIdentifiers) {
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
	
	    reportIEnrolment(result, approvedIEnrolment, academicUnitIdentifiers);
	}
    }

    final private void reportRemainingEnrolments(final StringBuilder result, final List<Enrolment> enrolments, final String title) {
	result.append(generateEndLine()).append("\n").append(title).append(":\n");
	
	for (final Enrolment enrolment : enrolments) {
	    reportIEnrolment(result, enrolment, null);
	}
    }

    final private void reportIEnrolment(final StringBuilder result, final IEnrolment approvedIEnrolment, final Map<Unit, String> academicUnitIdentifiers) {
	result.append(
		StringUtils.multipleLineRightPadWithSuffix(
			getEnrolmentName(academicUnitIdentifiers, approvedIEnrolment), 
			LINE_LENGTH,
			'-', 
			getCreditsAndGradeInfo(approvedIEnrolment))).append("\n");
    }

    final private String getCreditsAndGradeInfo(final IEnrolment approvedIEnrolment) {
	final StringBuilder result = new StringBuilder();

	if (getDocumentRequest().isToShowCredits()) {
	    getCreditsInfo(result, approvedIEnrolment);
	}
	result.append(resourceBundle.getString("label.with"));
	
	final Grade grade = approvedIEnrolment.getGrade();
	result.append(" ").append(grade.getValue());
	result.append(
		StringUtils.rightPad(
			"("
			+ enumerationBundle.getString(grade.getValue()) 
			+ ")", 
			SUFFIX_LENGTH, 
			' '));
	String values = resourceBundle.getString("values");
	result.append(grade.isNumeric() ? values : StringUtils.rightPad("", values.length(), ' '));
	
	return result.toString();
    }

}
