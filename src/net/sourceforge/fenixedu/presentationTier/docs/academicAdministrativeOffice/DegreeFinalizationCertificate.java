package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Grade;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DegreeFinalizationCertificateRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.curriculum.ICurriculumEntry;
import net.sourceforge.fenixedu.util.LanguageUtils;
import net.sourceforge.fenixedu.util.StringUtils;

public class DegreeFinalizationCertificate extends AdministrativeOfficeDocument {

    private static final String DATE_FORMAT = "dd 'de' MMMM 'de' yyyy";

    protected DegreeFinalizationCertificate(final DocumentRequest documentRequest) {
	super(documentRequest);
    }

    @Override
    protected void fillReport() {
	super.fillReport();

	final DegreeFinalizationCertificateRequest request = (DegreeFinalizationCertificateRequest) getDocumentRequest();

	addParameter("degreeFinalizationDate", getDegreeFinalizationDate(request));
	addParameter("exceptionalConclusionInfo", getExceptionalConclusionInfo(request));
	addParameter("degreeFinalizationGrade", request.getAverage() ? getDegreeFinalizationGrade(request.getFinalAverage()) : "");
	addParameter("degreeFinalizationEcts", getDegreeFinalizationEcts(request));
	addParameter("creditsDescription", getCreditsDescription());
	addParameter("graduateTitle", getGraduateTitle(request.getRegistration(), request.getRequestedCycle()));
	addParameter("diplomaDescription", getDiplomaDescription());
	addParameter("degreeFinalizationInfo", getDegreeFinalizationInfo(request));
    }

    private String getDegreeFinalizationDate(final DegreeFinalizationCertificateRequest request) {
	return request.mustHideConclusionDate() ? StringUtils.EMPTY : " em "
		+ request.getConclusionDate().toString(DATE_FORMAT, LanguageUtils.getLocale());
    }

    private String getExceptionalConclusionInfo(final DegreeFinalizationCertificateRequest request) {
	if (!request.hasExceptionalConclusionDate()) {
	    return StringUtils.EMPTY;
	}

	final String date = request.getExceptionalConclusionDate().toString(DATE_FORMAT, LanguageUtils.getLocale());
	if (request.getInternshipAbolished()) {
	    return " em " + date + ", data em que a Comissão Directiva decidiu abolir o sistema de estágios";
	} else if (request.getInternshipApproved()) {
	    return " em " + date + ", data em que foi aprovado o estágio regulamentar";
	} else if (request.getStudyPlan()) {
	    return " com Plano de Estudos homologado em " + date + " pelo Conselho Científico";
	} else {
	    return StringUtils.EMPTY;
	}
    }

    static final public String getDegreeFinalizationGrade(final Integer finalAverage) {
	final StringBuilder result = new StringBuilder();

	ResourceBundle resourceBundle = ResourceBundle.getBundle("resources.AcademicAdminOffice", LanguageUtils.getLocale());

	result.append(", ").append(resourceBundle.getString("documents.registration.final.arithmetic.mean"));
	result.append(" de ").append(finalAverage);
	result.append(" (").append(enumerationBundle.getString(finalAverage.toString()));
	result.append(") ").append(resourceBundle.getString("values"));

	return result.toString();
    }

    final private String getDegreeFinalizationEcts(DegreeFinalizationCertificateRequest request) {
	final StringBuilder result = new StringBuilder();

	if (getDocumentRequest().isToShowCredits()) {
	    result.append(" tendo obtido o total de ");
	    result.append(String.valueOf(request.getEctsCredits())).append(getCreditsDescription());
	}

	return result.toString();
    }

    final private String getGraduateTitle(final Registration registration, final CycleType requestedCycle) {
	final StringBuilder result = new StringBuilder();

	final DegreeType degreeType = getDocumentRequest().getDegreeType();
	if (degreeType.getQualifiesForGraduateTitle()) {
	    result.append(", pelo que tem direito ao grau académico de ");
	    result.append(registration.getGraduateTitle(requestedCycle));
	}

	return result.toString();
    }

    final private String getDiplomaDescription() {
	final Degree degree = getDocumentRequest().getDegree();
	final DegreeType degreeType = degree.getDegreeType();
	if (!degreeType.getQualifiesForGraduateTitle()) {
	    return StringUtils.EMPTY;
	}

	final StringBuilder result = new StringBuilder();
	result.append(", tendo requerido ");

	switch (degreeType) {
	case BOLONHA_ADVANCED_FORMATION_DIPLOMA:
	    break;
	case BOLONHA_SPECIALIZATION_DEGREE:
	    result.append("o respectivo diploma");
	    break;
	default:
	    result.append("a respectiva carta");
	    break;
	}

	return result.toString();
    }

    final private String getDegreeFinalizationInfo(final DegreeFinalizationCertificateRequest request) {
	final StringBuilder result = new StringBuilder();

	if (request.getDetailed()) {
	    final SortedSet<ICurriculumEntry> entries = new TreeSet<ICurriculumEntry>(
		    ICurriculumEntry.COMPARATOR_BY_EXECUTION_PERIOD_AND_NAME_AND_ID);
	    entries.addAll(request.getEntriesToReport());

	    final Map<Unit, String> academicUnitIdentifiers = new HashMap<Unit, String>();
	    reportEntries(result, entries, academicUnitIdentifiers);

	    if (getDocumentRequest().isToShowCredits()) {
		result.append(getRemainingCreditsInfo(request.getCurriculum()));
	    }

	    result.append(generateEndLine());

	    if (!academicUnitIdentifiers.isEmpty()) {
		result.append("\n").append(getAcademicUnitInfo(academicUnitIdentifiers, request.getMobilityProgram()));
	    }
	}

	return result.toString();
    }

    final private void reportEntries(final StringBuilder result, final SortedSet<ICurriculumEntry> entries,
	    final Map<Unit, String> academicUnitIdentifiers) {
	for (final ICurriculumEntry entry : entries) {
	    reportEntry(result, entry, academicUnitIdentifiers);
	}
    }

    final private void reportEntry(final StringBuilder result, final ICurriculumEntry entry,
	    final Map<Unit, String> academicUnitIdentifiers) {
	result.append(
		StringUtils.multipleLineRightPadWithSuffix(getCurriculumEntryName(academicUnitIdentifiers, entry), LINE_LENGTH,
			'-', getCreditsAndGradeInfo(entry))).append("\n");
    }

    final private String getCreditsAndGradeInfo(final ICurriculumEntry entry) {
	final StringBuilder result = new StringBuilder();

	if (getDocumentRequest().isToShowCredits()) {
	    getCreditsInfo(result, entry);
	}
	result.append(resourceBundle.getString("label.with"));

	final Grade grade = entry.getGrade();
	result.append(" ").append(grade.getValue());
	result.append(StringUtils.rightPad("(" + enumerationBundle.getString(grade.getValue()) + ")", SUFFIX_LENGTH, ' '));
	String values = resourceBundle.getString("values");
	result.append(grade.isNumeric() ? values : StringUtils.rightPad("", values.length(), ' '));

	return result.toString();
    }

    @Override
    protected String getDegreeDescription() {
	final DegreeFinalizationCertificateRequest request = (DegreeFinalizationCertificateRequest) getDocumentRequest();
	return getDocumentRequest().getRegistration().getDegreeDescription(request.getWhatShouldBeRequestedCycle());
    }

}
