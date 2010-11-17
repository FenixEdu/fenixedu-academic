package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.math.BigDecimal;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeOfficialPublication;
import net.sourceforge.fenixedu.domain.DegreeSpecializationArea;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.degreeStructure.EctsGraduationGradeConversionTable;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.AcademicalInstitutionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DiplomaSupplementRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.curriculum.CycleConclusionProcess;
import net.sourceforge.fenixedu.domain.student.curriculum.ExtraCurricularActivity;
import net.sourceforge.fenixedu.domain.student.curriculum.ExtraCurricularActivityType;
import net.sourceforge.fenixedu.domain.student.curriculum.ICurriculumEntry;
import net.sourceforge.fenixedu.domain.studentCurriculum.Dismissal;
import net.sourceforge.fenixedu.domain.studentCurriculum.ExternalEnrolment;
import net.sourceforge.fenixedu.util.StringFormatter;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public class DiplomaSupplement extends AdministrativeOfficeDocument {

    private static final String GRADUATE_LEVEL_SUFFIX = ".graduate.level";

    protected DiplomaSupplement(final DocumentRequest documentRequest, final Locale locale) {
	super(documentRequest, locale);
    }

    @Override
    public String getReportTemplateKey() {
	return getClass().getName();
    }

    @Override
    protected void fillReport() {
	addParameter("bundle", getResourceBundle());
	Registration registration = getRegistration();
	Person person = registration.getPerson();
	Degree degree = registration.getDegree();
	DegreeType degreeType = degree.getDegreeType();
	ExecutionYear conclusion = registration.getLastStudentCurricularPlan().getCycle(getRequestedCycle())
		.getConclusionProcess().getConclusionYear();
	String degreeName = degree.getFilteredName(conclusion, getLocale());
	final UniversityUnit institutionsUniversityUnit = UniversityUnit.getInstitutionsUniversityUnit();

	addParameter("name", StringFormatter.prettyPrint(person.getName().trim()));

	// Group 1
	addParameter("familyName", registration.getPerson().getFamilyNames());
	addParameter("givenName", registration.getPerson().getGivenNames());
	addParameter("birthDay", person.getDateOfBirthYearMonthDay().toString(DD_SLASH_MM_SLASH_YYYY, getLocale()));
	addParameter("nationality",
		StringFormatter.prettyPrint(person.getCountry().getCountryNationality().getContent(getLanguage())));
	addParameter(
		"documentIdType",
		applyMessageArguments(getResourceBundle().getString("diploma.supplement.one.five.one"), getEnumerationBundle()
			.getString(person.getIdDocumentType().getName())));
	addParameter("documentIdNumber", person.getDocumentIdNumber());
	if (person.getExpirationDateOfDocumentIdYearMonthDay() != null) {
	    addParameter("documentIdExpiration",
		    " / " + person.getExpirationDateOfDocumentIdYearMonthDay().toString(DD_SLASH_MM_SLASH_YYYY, getLocale()));
	} else {
	    addParameter("documentIdExpiration", EMPTY_STR);
	}
	addParameter("registrationNumber", registration.getNumber());

	// Group 2
	String degreeDesignation = getDegreeDesignation(getRequestedCycle(), getLocale());

	String graduateTitleNative = degreeType.getGraduateTitle(getRequestedCycle(), Language.getLocale());

	addParameter("graduateTitle", getDegreeDesignation(getRequestedCycle(), Language.getLocale()) + ", "
		+ graduateTitleNative);
	addParameter("prevailingScientificArea", degreeName);
	addParameter("universityName", institutionsUniversityUnit.getName());
	addParameter(
		"universityStatus",
		getEnumerationBundle().getString(
			AcademicalInstitutionType.class.getSimpleName() + "."
				+ institutionsUniversityUnit.getInstitutionType().getName()));
	addParameter("institutionName", RootDomainObject.getInstance().getInstitutionUnit().getName());
	addParameter("institutionStatus",
		getEnumerationBundle().getString(RootDomainObject.getInstance().getInstitutionUnit().getType().getName())
			+ SINGLE_SPACE + getResourceBundle().getString("diploma.supplement.of") + SINGLE_SPACE
			+ institutionsUniversityUnit.getName());
	if (getRequestedCycle().equals(CycleType.FIRST_CYCLE)) {
	    addParameter("languages", getEnumerationBundle().getString("pt"));
	} else {
	    addParameter("languages",
		    getEnumerationBundle().getString("pt") + SINGLE_SPACE
			    + getResourceBundle().getString("label.and").toLowerCase() + SINGLE_SPACE
			    + getEnumerationBundle().getString("en"));
	}

	// Group 3
	addParameter("qualificationLevel",
		getResourceBundle().getString("diploma.supplement.qualification." + getRequestedCycle()));
	addParameter("years", degreeType.getYears(getRequestedCycle()));
	addParameter("semesters", degreeType.getSemesters(getRequestedCycle()));
	addParameter("weeksOfStudyPerYear",
		getResourceBundle().getString("diploma.supplement.weeksOfStudyPerYear." + getRequestedCycle()));
	addParameter("ectsCredits",
		Math.round(registration.getLastStudentCurricularPlan().getCycle(getRequestedCycle()).getDefaultEcts(conclusion)));

	// Group 4
	addProgrammeRequirements(registration, degreeDesignation);
	addEntriesParameters(registration);
	addParameter(
		"classificationSystem",
		applyMessageArguments(getResourceBundle().getString("diploma.supplement.four.four.one"),
			degreeType.getFilteredName()));
	final Integer finalAverage = registration.getFinalAverage(getRequestedCycle());
	addParameter("finalAverage", finalAverage);
	String qualifiedAverageGrade;
	if (finalAverage <= 13) {
	    qualifiedAverageGrade = "sufficient";
	} else if (finalAverage <= 15) {
	    qualifiedAverageGrade = "good";
	} else if (finalAverage <= 17) {
	    qualifiedAverageGrade = "verygood";
	} else {
	    qualifiedAverageGrade = "excelent";
	}
	addParameter("finalAverageQualified",
		getResourceBundle().getString("diploma.supplement.qualifiedgrade." + qualifiedAverageGrade));
	EctsGraduationGradeConversionTable table = degree.getGraduationConversionTable(conclusion.getAcademicInterval(),
		getRequestedCycle());
	addParameter("ectsGradeConversionTable", table.getEctsTable());
	addParameter("ectsGradePercentagesTable", table.getPercentages());

	// Group 5
	final StringBuilder access = new StringBuilder();
	if (getRequestedCycle() == CycleType.THIRD_CYCLE) {
	    access.append(getResourceBundle().getString("diploma.supplement.five.one.three"));
	} else {
	    access.append(getResourceBundle().getString("diploma.supplement.five.one.one")).append(SINGLE_SPACE);
	    access.append(degreeDesignation).append(SINGLE_SPACE);
	    access.append(getResourceBundle().getString("diploma.supplement.five.one.two"));
	}
	addParameter("accessToHigherLevelOfEducation", access.toString());
	addProfessionalStatus(degreeName, degree.getSigla());

	// Group 6
	addExtraCurricularActivities(registration.getStudent());

	// Group 7
	addParameter("day", new YearMonthDay().toString(DD_SLASH_MM_SLASH_YYYY, getLocale()));
	addParameter("universityPrincipalName", institutionsUniversityUnit.getInstitutionsUniversityPrincipal()
		.getValidatedName());

	// Group 8
	addParameter("langSuffix", getLanguage().name());
    }

    protected CycleType getRequestedCycle() {
	return ((DiplomaSupplementRequest) getDocumentRequest()).getRequestedCycle();
    }

    private String getDegreeDesignation(CycleType cycle, Locale locale) {
	String title = getRegistration().getGraduateTitle(cycle, locale);
	title = title.replace("Licenciado", "Licenciatura");
	title = title.replace("Graduated", "Graduation");
	return title;
    }

    private void addProgrammeRequirements(Registration registration, String graduateDegree) {
	String labelThe = getRequestedCycle().equals(CycleType.FIRST_CYCLE) ? getResourceBundle().getString("label.the.female")
		: getResourceBundle().getString("label.the.male");
	CycleConclusionProcess conclusionProcess = registration.getLastStudentCurricularPlan().getCycle(getRequestedCycle())
		.getConclusionProcess();
	long ectsCredits = Math.round(registration.getLastStudentCurricularPlan().getCycle(getRequestedCycle())
		.getDefaultEcts(conclusionProcess.getConclusionYear()));
	DegreeOfficialPublication dr = registration.getDegree().getOfficialPublication(
		conclusionProcess.getConclusionDate().toDateTimeAtStartOfDay());
	if (dr == null) {
	    throw new DomainException("error.DiplomaSupplement.degreeOfficialPublicationNotFound");
	}
	String officialPublication = dr.getOfficialReference();
	String programmeRequirements;
	if (getRequestedCycle().equals(CycleType.FIRST_CYCLE) || dr.getSpecializationAreaCount() == 0) {
	    programmeRequirements = applyMessageArguments(
		    getResourceBundle().getString("diploma.supplement.four.two.programmerequirements.template.noareas"),
		    labelThe, graduateDegree, Long.toString(ectsCredits));
	} else {
	    List<String> areas = new ArrayList<String>();
	    for (DegreeSpecializationArea area : dr.getSpecializationAreaSet()) {
		areas.add(area.getName().getContent(getLanguage()));
	    }
	    programmeRequirements = applyMessageArguments(
		    getResourceBundle().getString("diploma.supplement.four.two.programmerequirements.template.withareas"),
		    labelThe, graduateDegree, Long.toString(ectsCredits), Integer.toString(areas.size()),
		    StringUtils.join(areas, "; "), officialPublication);
	}
	programmeRequirements = programmeRequirements.substring(0, 1).toUpperCase() + programmeRequirements.substring(1);
	addParameter("programmeRequirements", programmeRequirements);
    }

    private void addProfessionalStatus(String degreeName, String degreeSigla) {
	String professionalStatus;
	if (!getRequestedCycle().equals(CycleType.SECOND_CYCLE)) {
	    professionalStatus = getResourceBundle().getString("diploma.supplement.professionalstatus.notapplicable");
	} else {
	    if (degreeSigla.equals("MA")) {
		professionalStatus = getResourceBundle().getString(
			"diploma.supplement.professionalstatus.credited.arquitect.withintership");
	    } else if (degreeSigla.equals("MMA") || degreeSigla.equals("MQ") || degreeSigla.equals("MUOT")
		    || degreeSigla.equals("MPOT") || degreeSigla.equals("MFarm")) {
		professionalStatus = getResourceBundle().getString("diploma.supplement.professionalstatus.notapplicable");
	    } else {
		professionalStatus = getResourceBundle().getString("diploma.supplement.professionalstatus.credited.engineer");
	    }
	}
	addParameter("professionalStatus", professionalStatus);
    }

    private void addExtraCurricularActivities(final Student student) {
	if (student.hasAnyExtraCurricularActivity()) {
	    List<String> activities = new ArrayList<String>();
	    Map<ExtraCurricularActivityType, List<ExtraCurricularActivity>> activityMap = new HashMap<ExtraCurricularActivityType, List<ExtraCurricularActivity>>();
	    for (ExtraCurricularActivity activity : student.getExtraCurricularActivitySet()) {
		if (!activityMap.containsKey(activity.getType())) {
		    activityMap.put(activity.getType(), new ArrayList<ExtraCurricularActivity>());
		}
		activityMap.get(activity.getType()).add(activity);
	    }
	    for (Entry<ExtraCurricularActivityType, List<ExtraCurricularActivity>> entry : activityMap.entrySet()) {
		StringBuilder activityText = new StringBuilder();
		activityText.append(getResourceBundle().getString("diploma.supplement.six.one.extracurricularactivity.heading"));
		activityText.append(SINGLE_SPACE);
		activityText.append(entry.getKey().getName().getContent(getLanguage()));
		activityText.append(SINGLE_SPACE);
		List<String> activityTimings = new ArrayList<String>();
		for (ExtraCurricularActivity activity : entry.getValue()) {
		    activityTimings.add(getResourceBundle().getString(
			    "diploma.supplement.six.one.extracurricularactivity.time.heading")
			    + SINGLE_SPACE
			    + activity.getStart().toString("MM-yyyy")
			    + SINGLE_SPACE
			    + getResourceBundle().getString("diploma.supplement.six.one.extracurricularactivity.time.separator")
			    + SINGLE_SPACE + activity.getEnd().toString("MM-yyyy"));
		}
		activityText.append(StringUtils.join(activityTimings, ", "));
		activities.add(activityText.toString());
	    }
	    addParameter("extraCurricularActivities", StringUtils.join(activities, '\n') + ".");
	} else {
	    addParameter("extraCurricularActivities",
		    getResourceBundle().getString("diploma.supplement.six.one.extracurricularactivity.none"));
	}
    }

    private String applyMessageArguments(String message, String... args) {
	for (int i = 0; i < args.length; i++) {
	    message = message.replaceAll("\\{" + i + "\\}", args[i]);
	}
	return message;
    }

    private void addEntriesParameters(final Registration registration) {
	final List<DiplomaSupplementEntry> entries = new ArrayList<DiplomaSupplementEntry>();
	final Map<Unit, String> academicUnitIdentifiers = new HashMap<Unit, String>();

	// for (ICurriculumEntry entry :
	// registration.getCurriculum(cycleType).getCurriculumEntries()) {
	// System.out.println("old format: " + entry.getName() + " grade: " +
	// entry.getGradeValue());
	// }
	// System.out.println();
	// System.out.println();
	// System.out.println();
	// System.out.println();

	for (ICurriculumEntry entry : registration.getCurriculum(getRequestedCycle()).getCurriculumEntries()) {
	    entries.add(new DiplomaSupplementEntry(entry, academicUnitIdentifiers));
	}

	Collections.sort(entries);
	addParameter("entries", entries);

	final List<AcademicUnitEntry> identifiers = new ArrayList<AcademicUnitEntry>();
	for (final Entry<Unit, String> entry2 : academicUnitIdentifiers.entrySet()) {
	    identifiers.add(new AcademicUnitEntry(entry2));
	}
	addParameter("academicUnitIdentifiers", identifiers);
    }

    static final public Comparator<DiplomaSupplementEntry> COMPARATOR = new Comparator<DiplomaSupplementEntry>() {

	@Override
	public int compare(DiplomaSupplementEntry o1, DiplomaSupplementEntry o2) {
	    final int c = o1.getExecutionYear().compareTo(o2.getExecutionYear());
	    return c == 0 ? Collator.getInstance().compare(o1.getName(), o2.getName()) : c;
	}

    };

    public class DiplomaSupplementEntry implements Comparable<DiplomaSupplementEntry> {

	private final ICurriculumEntry entry;

	private final String executionYear;

	private final String name;

	private final String type;

	private final String duration;

	private final BigDecimal ectsCreditsForCurriculum;

	private final String gradeValue;

	private final String ectsScale;

	private final String academicUnitId;

	public DiplomaSupplementEntry(final ICurriculumEntry entry, final Map<Unit, String> academicUnitIdentifiers) {
	    this.entry = entry;
	    this.executionYear = entry.getExecutionYear().getYear();
	    this.name = getMLSTextContent(entry.getName());
	    if (entry instanceof IEnrolment) {
		IEnrolment enrolment = (IEnrolment) entry;
		this.type = getEnumerationBundle().getString(enrolment.getEnrolmentTypeName());
		this.duration = getResourceBundle().getString(
			enrolment.isAnual() ? "diploma.supplement.annual" : "diploma.supplement.semestral");
		this.ectsScale = enrolment.getEctsGrade(getRegistration().getLastStudentCurricularPlan()).getValue();
	    } else if (entry instanceof Dismissal && ((Dismissal) entry).getCredits().isEquivalence()) {
		Dismissal dismissal = (Dismissal) entry;
		this.type = getEnumerationBundle().getString(dismissal.getEnrolmentTypeName());
		this.duration = getResourceBundle().getString(
			dismissal.isAnual() ? "diploma.supplement.annual" : "diploma.supplement.semestral");
		this.ectsScale = dismissal.getEctsGrade().getValue();
	    } else {
		throw new Error("The roof is on fire");
	    }
	    this.ectsCreditsForCurriculum = entry.getEctsCreditsForCurriculum();
	    this.academicUnitId = obtainAcademicUnitIdentifier(academicUnitIdentifiers);
	    this.gradeValue = entry.getGrade().getValue();
	}

	public ICurriculumEntry getEntry() {
	    return entry;
	}

	public String getExecutionYear() {
	    return executionYear;
	}

	public String getName() {
	    return name;
	}

	public String getType() {
	    return type;
	}

	public String getDuration() {
	    return duration;
	}

	public BigDecimal getEctsCreditsForCurriculum() {
	    return ectsCreditsForCurriculum;
	}

	public String getGradeValue() {
	    return gradeValue;
	}

	public String getEctsScale() {
	    return ectsScale;
	}

	public String getAcademicUnitId() {
	    return academicUnitId;
	}

	private String obtainAcademicUnitIdentifier(final Map<Unit, String> academicUnitIdentifiers) {
	    final Unit unit = entry instanceof ExternalEnrolment ? ((ExternalEnrolment) entry).getAcademicUnit()
		    : RootDomainObject.getInstance().getInstitutionUnit();
	    return getAcademicUnitIdentifier(academicUnitIdentifiers, unit);
	}

	@Override
	public int compareTo(final DiplomaSupplementEntry o) {
	    return COMPARATOR.compare(this, o);
	}

    }

    public class AcademicUnitEntry {
	private final String identifier;

	private final String name;

	public AcademicUnitEntry(final Entry<Unit, String> entry) {
	    this.identifier = entry.getValue();
	    this.name = getMLSTextContent(entry.getKey().getNameI18n());
	}

	public String getIdentifier() {
	    return identifier;
	}

	public String getName() {
	    return name;
	}
    }

}
