package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.SortedSet;
import java.util.TreeSet;

import org.joda.time.YearMonthDay;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.Grade;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DegreeFinalizationCertificateRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.curriculum.ICurriculum;
import net.sourceforge.fenixedu.domain.student.curriculum.ICurriculumEntry;
import net.sourceforge.fenixedu.domain.studentCurriculum.CreditsDismissal;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.Equivalence;
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
	final Registration registration = getRegistration();

	final CycleType requestedCycle = degreeFinalizationCertificateRequest.getRequestedCycle();
	parameters.put("degreeFinalizationDate", getConclusionDate()
		.toString("dd 'de' MMMM 'de' yyyy", LanguageUtils.getLocale()));
	parameters.put("degreeFinalizationGrade",
		degreeFinalizationCertificateRequest.getAverage() ? getDegreeFinalizationGrade(getFinalAverage()) : "");
	parameters.put("degreeFinalizationEcts", getDegreeFinalizationEcts());
	parameters.put("creditsDescription", getCreditsDescription());
	parameters.put("graduateTitle", getGraduateTitle(registration, requestedCycle));
	parameters.put("diplomaDescription", getDiplomaDescription());
	parameters.put("degreeFinalizationInfo", getDegreeFinalizationInfo(degreeFinalizationCertificateRequest, registration));
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

    final private String getDegreeFinalizationEcts() {
	final StringBuilder result = new StringBuilder();

	if (getDocumentRequest().isToShowCredits()) {
	    result.append(", tendo obtido o total de ");
	    result.append(String.valueOf(getEctsCredits())).append(getCreditsDescription());
	    result.append(",");
	}

	return result.toString();
    }

    public Integer getFinalAverage() {
	return hasCycleCurriculumGroup() ? getCycleCurriculumGroup().calculateRoundedAverage() : getRegistration()
		.calculateFinalAverage();
    }

    public BigDecimal getAverage() {
	return hasCycleCurriculumGroup() ? getCycleCurriculumGroup().calculateAverage() : getRegistration().getAverage();
    }

    public YearMonthDay getConclusionDate() {
	return hasCycleCurriculumGroup() ? getCycleCurriculumGroup().calculateConclusionDate() : getRegistration()
		.calculateConclusionDate();
    }

    public double getEctsCredits() {
	return hasCycleCurriculumGroup() ? getCycleCurriculumGroup().getCreditsConcluded() : getRegistration().getEctsCredits();
    }

    public ICurriculum getCurriculum() {
	return hasCycleCurriculumGroup() ? getCycleCurriculumGroup().getCurriculum() : getRegistration().getCurriculum();
    }

    private boolean hasCycleCurriculumGroup() {
	return getCycleCurriculumGroup() != null;
    }

    private CycleCurriculumGroup getCycleCurriculumGroup() {
	final DegreeFinalizationCertificateRequest degreeFinalizationCertificateRequest = (DegreeFinalizationCertificateRequest) getDocumentRequest();
	final CycleType requestedCycle = degreeFinalizationCertificateRequest.getRequestedCycle();
	final Registration registration = getRegistration();

	if (requestedCycle == null) {
	    if (registration.getDegreeType().hasExactlyOneCycleType()) {
		return registration.getLastStudentCurricularPlan().getLastCycleCurriculumGroup();
	    } else {
		return null;
	    }
	} else {
	    return registration.getLastStudentCurricularPlan().getCycle(requestedCycle);
	}
    }

    private Registration getRegistration() {
	return getDocumentRequest().getRegistration();
    }

    final private String getGraduateTitle(final Registration registration, final CycleType requestedCycle) {
	final StringBuilder result = new StringBuilder();

	final DegreeType degreeType = getDocumentRequest().getDegreeType();
	if (degreeType.getQualifiesForGraduateTitle()) {
	    result.append(" pelo que tem direito ao grau académico de ");
	    result.append(registration.getGraduateTitle(requestedCycle));
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

    final private String getDegreeFinalizationInfo(
	    final DegreeFinalizationCertificateRequest degreeFinalizationCertificateRequest, final Registration registration) {
	final StringBuilder result = new StringBuilder();

	if (degreeFinalizationCertificateRequest.getDetailed()) {
	    final ICurriculum curriculum = getCurriculum();

	    final SortedSet<ICurriculumEntry> entries = new TreeSet<ICurriculumEntry>(
		    ICurriculumEntry.COMPARATOR_BY_EXECUTION_PERIOD_AND_NAME_AND_ID);
	    entries.addAll(curriculum.getCurriculumEntries());

	    final Map<Unit, String> academicUnitIdentifiers = new HashMap<Unit, String>();
	    reportEntries(result, entries, academicUnitIdentifiers);

	    final SortedSet<Enrolment> remainingEntries = new TreeSet<Enrolment>(
		    Enrolment.COMPARATOR_BY_EXECUTION_PERIOD_AND_NAME_AND_ID);
	    remainingEntries.addAll(registration.getExtraCurricularEnrolments());
	    if (!remainingEntries.isEmpty()) {
		reportRemainingEnrolments(result, remainingEntries, "Extra-Curriculares");
	    }

	    remainingEntries.clear();
	    remainingEntries.addAll(registration.getPropaedeuticEnrolments());
	    if (!remainingEntries.isEmpty()) {
		reportRemainingEnrolments(result, remainingEntries, "Propedêuticas");
	    }

	    if (getDocumentRequest().isToShowCredits()) {
		result.append(getRemainingCreditsInfo(curriculum));
	    }

	    result.append(generateEndLine());

	    if (!academicUnitIdentifiers.isEmpty()) {
		result.append("\n").append(
			getAcademicUnitInfo(academicUnitIdentifiers, degreeFinalizationCertificateRequest.getMobilityProgram()));
	    }
	}

	return result.toString();
    }

    final private void reportEntries(final StringBuilder result, final SortedSet<ICurriculumEntry> entries,
	    final Map<Unit, String> academicUnitIdentifiers) {
	for (final ICurriculumEntry entry : entries) {
	    if (!(entry instanceof Equivalence || entry instanceof CreditsDismissal)) {
		reportEntry(result, entry, academicUnitIdentifiers);
	    }
	}
    }

    final private void reportRemainingEnrolments(final StringBuilder result, final Collection<Enrolment> enrolments,
	    final String title) {
	result.append(generateEndLine()).append("\n").append(title).append(":\n");

	for (final Enrolment enrolment : enrolments) {
	    reportEntry(result, enrolment, null);
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
	return getDocumentRequest().getRegistration().getDegreeDescription();
    }

}
