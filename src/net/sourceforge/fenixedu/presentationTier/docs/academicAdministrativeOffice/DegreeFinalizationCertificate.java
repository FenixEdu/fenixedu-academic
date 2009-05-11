package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.util.HashMap;
import java.util.Locale;
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
import net.sourceforge.fenixedu.util.StringUtils;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class DegreeFinalizationCertificate extends AdministrativeOfficeDocument {

    protected DegreeFinalizationCertificate(final DocumentRequest documentRequest) {
	super(documentRequest);
    }

    @Override
    protected DegreeFinalizationCertificateRequest getDocumentRequest() {
	return (DegreeFinalizationCertificateRequest) super.getDocumentRequest();
    }

    @Override
    protected void fillReport() {
	super.fillReport();

	final DegreeFinalizationCertificateRequest req = getDocumentRequest();

	addParameter("degreeFinalizationDate", getDegreeFinalizationDate(req));
	addParameter("exceptionalConclusionInfo", getExceptionalConclusionInfo(req));
	addParameter("degreeFinalizationGrade", req.getAverage() ? getDegreeFinalizationGrade(req.getFinalAverage(), getLocale())
		: EMPTY_STR);
	addParameter("degreeFinalizationEcts", getDegreeFinalizationEcts(req));
	addParameter("creditsDescription", getCreditsDescription());
	addParameter("graduateTitle", getGraduateTitle(getRegistration(), req.getRequestedCycle()));
	addParameter("diplomaDescription", getDiplomaDescription());
	addParameter("detailedInfoIntro", getDetailedInfoIntro(req));
	addParameter("degreeFinalizationInfo", getDegreeFinalizationInfo(req));

	setBranchField();
    }

    private void setBranchField() {
	String branch = getDocumentRequest().getBranch();
	if ((branch == null) || (branch.isEmpty())) {
	    addParameter("branch", "");
	    return;
	}
	addParameter("branch", SINGLE_SPACE + getDocumentRequest().getBranch());
    }

    @Override
    protected String getDegreeDescription() {
	final DegreeFinalizationCertificateRequest request = getDocumentRequest();
	return getRegistration().getDegreeDescription(request.getWhatShouldBeRequestedCycle(), getLocale());
    }

    private String getDegreeFinalizationDate(final DegreeFinalizationCertificateRequest request) {
	final StringBuilder result = new StringBuilder();

	if (!request.mustHideConclusionDate()) {
	    result.append(SINGLE_SPACE).append(getResourceBundle().getString("label.in"));
	    result.append(SINGLE_SPACE).append(request.getConclusionDate().toString(DD_MM_YYYY, getLocale()));
	}

	return result.toString();
    }

    private String getExceptionalConclusionInfo(final DegreeFinalizationCertificateRequest request) {
	final StringBuilder result = new StringBuilder();

	if (request.hasExceptionalConclusionInfo()) {
	    if (request.getTechnicalEngineer()) {
		result.append(SINGLE_SPACE);
		result.append(getResourceBundle().getString(
			"documents.DegreeFinalizationCertificate.exceptionalConclusionInfo.technicalEngineer"));
	    } else {
		final String date = request.getExceptionalConclusionDate().toString(DD_MM_YYYY, getLocale());
		if (request.getInternshipAbolished()) {
		    result.append(SINGLE_SPACE).append(getResourceBundle().getString("label.in"));
		    result.append(SINGLE_SPACE).append(date);
		    result.append(", ");
		    result.append(getResourceBundle().getString(
			    "documents.DegreeFinalizationCertificate.exceptionalConclusionInfo.internshipAbolished"));
		} else if (request.getInternshipApproved()) {
		    result.append(SINGLE_SPACE).append(getResourceBundle().getString("label.in"));
		    result.append(SINGLE_SPACE).append(date);
		    result.append(", ");
		    result.append(getResourceBundle().getString(
			    "documents.DegreeFinalizationCertificate.exceptionalConclusionInfo.internshipApproved"));
		} else if (request.getStudyPlan()) {
		    result.append(SINGLE_SPACE);
		    result.append(getResourceBundle().getString(
			    "documents.DegreeFinalizationCertificate.exceptionalConclusionInfo.studyPlan.one"));
		    result.append(SINGLE_SPACE).append(date);
		    result.append(SINGLE_SPACE);
		    result.append(getResourceBundle().getString(
			    "documents.DegreeFinalizationCertificate.exceptionalConclusionInfo.studyPlan.two"));
		}
	    }
	}

	return result.toString();
    }

    static final public String getDegreeFinalizationGrade(final Integer finalAverage) {
	return getDegreeFinalizationGrade(finalAverage, Language.getLocale());
    }

    static final public String getDegreeFinalizationGrade(final Integer finalAverage, final Locale locale) {
	final StringBuilder result = new StringBuilder();

	final ResourceBundle resourceBundle = ResourceBundle.getBundle("resources.AcademicAdminOffice", locale);
	final ResourceBundle enumerationBundle = ResourceBundle.getBundle("resources.EnumerationResources", locale);

	result.append(", ").append(resourceBundle.getString("documents.registration.final.arithmetic.mean"));
	result.append(SINGLE_SPACE).append(resourceBundle.getString("label.of.both"));
	result.append(SINGLE_SPACE).append(finalAverage);
	result.append(" (").append(enumerationBundle.getString(finalAverage.toString()));
	result.append(") ").append(resourceBundle.getString("values"));

	return result.toString();
    }

    final private String getDegreeFinalizationEcts(DegreeFinalizationCertificateRequest request) {
	final StringBuilder res = new StringBuilder();

	if (getDocumentRequest().isToShowCredits()) {
	    res.append(SINGLE_SPACE).append(getResourceBundle().getString("documents.DegreeFinalizationCertificate.creditsInfo"));
	    res.append(SINGLE_SPACE).append(String.valueOf(request.getEctsCredits())).append(getCreditsDescription());
	}

	return res.toString();
    }

    final private String getGraduateTitle(final Registration registration, final CycleType requestedCycle) {
	final StringBuilder res = new StringBuilder();

	final DegreeType degreeType = getDocumentRequest().getDegreeType();
	if (degreeType.getQualifiesForGraduateTitle()) {
	    res.append(", ").append(getResourceBundle().getString("documents.DegreeFinalizationCertificate.graduateTitleInfo"));
	    res.append(SINGLE_SPACE).append(registration.getGraduateTitle(requestedCycle, getLocale()));
	}

	return res.toString();
    }

    final private String getDiplomaDescription() {
	final StringBuilder res = new StringBuilder();

	final Degree degree = getDocumentRequest().getDegree();
	final DegreeType degreeType = degree.getDegreeType();
	if (degreeType.getQualifiesForGraduateTitle()) {
	    res.append(", ");
	    res.append(getResourceBundle().getString("documents.DegreeFinalizationCertificate.diplomaDescription.one"));

	    switch (degreeType) {
	    case BOLONHA_ADVANCED_FORMATION_DIPLOMA:
		break;
	    case BOLONHA_SPECIALIZATION_DEGREE:
		res.append(SINGLE_SPACE);
		res.append(getResourceBundle().getString("documents.DegreeFinalizationCertificate.diplomaDescription.diploma"));
		break;
	    default:
		res.append(SINGLE_SPACE);
		res.append(getResourceBundle().getString("documents.DegreeFinalizationCertificate.diplomaDescription.letter"));
		break;
	    }
	}

	return res.toString();
    }

    final private String getDetailedInfoIntro(final DegreeFinalizationCertificateRequest request) {
	final StringBuilder res = new StringBuilder();

	if (request.getDetailed()) {
	    res.append(SINGLE_SPACE).append(
		    getResourceBundle().getString("documents.DegreeFinalizationCertificate.detailedInfoIntro")).append(":");
	} else {
	    res.append(".");
	}

	return res.toString();
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
		result.append(LINE_BREAK).append(getAcademicUnitInfo(academicUnitIdentifiers, request.getMobilityProgram()));
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

    final private void reportEntry(final StringBuilder result, final ICurriculumEntry entry, final Map<Unit, String> unitIDs) {
	result.append(
		StringUtils.multipleLineRightPadWithSuffix(getCurriculumEntryName(unitIDs, entry), LINE_LENGTH, END_CHAR,
			getCreditsAndGradeInfo(entry))).append(LINE_BREAK);
    }

    final private String getCreditsAndGradeInfo(final ICurriculumEntry entry) {
	final StringBuilder result = new StringBuilder();

	if (getDocumentRequest().isToShowCredits()) {
	    getCreditsInfo(result, entry);
	}
	result.append(getResourceBundle().getString("label.with"));

	final Grade grade = entry.getGrade();
	result.append(SINGLE_SPACE).append(grade.getValue());
	result.append(StringUtils.rightPad("(" + getEnumerationBundle().getString(grade.getValue()) + ")", SUFFIX_LENGTH, ' '));
	String values = getResourceBundle().getString("values");
	result.append(grade.isNumeric() ? values : StringUtils.rightPad(EMPTY_STR, values.length(), ' '));

	return result.toString();
    }

}
