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
import net.sourceforge.fenixedu.domain.DegreeInfo;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Grade;
import net.sourceforge.fenixedu.domain.GradeDistribution;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.GradeDistribution.Distribution;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.organizationalStructure.AcademicalInstitutionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DiplomaRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.curriculum.ICurriculumEntry;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.ExternalEnrolment;
import net.sourceforge.fenixedu.util.NameUtils;
import net.sourceforge.fenixedu.util.StringFormatter;

import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class DiplomaSupplement extends AdministrativeOfficeDocument {

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
	addParameter("day", new YearMonthDay().toString(DD_SLASH_MM_SLASH_YYYY, getLocale()));
	addUniversityParameters();
	addPersonParameters();
	addRegistrationParameters();
    }

    protected CycleType getRequestedCycle() {
	return ((DiplomaRequest) getDocumentRequest()).getWhatShouldBeRequestedCycle();
    }

    private void addUniversityParameters() {
	final UniversityUnit institutionsUniversityUnit = UniversityUnit.getInstitutionsUniversityUnit();
	addParameter("universityName", institutionsUniversityUnit.getName());
	addParameter("universityPrincipalName", institutionsUniversityUnit.getInstitutionsUniversityPrincipal()
		.getValidatedName());
	addParameter("universityStatus", getEnumerationBundle()
		.getString(
			AcademicalInstitutionType.class.getSimpleName() + "."
				+ institutionsUniversityUnit.getInstitutionType().getName()));
	addParameter("institutionName", RootDomainObject.getInstance().getInstitutionUnit().getName());
	addParameter("institutionStatus", getEnumerationBundle().getString(
		RootDomainObject.getInstance().getInstitutionUnit().getType().getName())
		+ SINGLE_SPACE
		+ getResourceBundle().getString("diploma.supplement.of")
		+ SINGLE_SPACE
		+ institutionsUniversityUnit.getName());
    }

    protected void addPersonParameters() {
	final Person person = getRegistration().getPerson();
	final String name = StringFormatter.prettyPrint(person.getName().trim());
	addParameter("name", name);
	addParameter("familyName", NameUtils.getLastName(name));
	addParameter("givenName", NameUtils.getFirstName(name));
	addParameter("birthDay", person.getDateOfBirthYearMonthDay().toString(DD_SLASH_MM_SLASH_YYYY, getLocale()));
	addParameter("nationality", StringFormatter.prettyPrint(person.getCountry().getNationality()));
	addDocumentIdParameters(person);
    }

    private void addDocumentIdParameters(final Person person) {
	addParameter("documentIdType", getResourceBundle().getString("diploma.supplement.one.five.one").replace("{0}",
		getEnumerationBundle().getString(person.getIdDocumentType().getName())));
	addParameter("documentIdNumber", person.getDocumentIdNumber());
	if (person.getExpirationDateOfDocumentIdYearMonthDay() != null) {
	    addParameter("documentIdExpiration", " / "
		    + person.getExpirationDateOfDocumentIdYearMonthDay().toString(DD_SLASH_MM_SLASH_YYYY, getLocale()));
	} else {
	    addParameter("documentIdExpiration", EMPTY_STR);
	}
    }

    private Integer addRegistrationParameters() {
	final Registration registration = getRegistration();
	ExecutionYear conclusion = registration.getLastStudentCurricularPlan().getCycle(getRequestedCycle())
		.getConclusionProcess().getConclusionYear();
	CycleCurriculumGroup cycleGroup = registration.getLastStudentCurricularPlan().getCycle(getRequestedCycle());
	long ectsCredits = Math.round(cycleGroup.getDefaultEcts(conclusion));
	StringBuilder areas = new StringBuilder();
	int counter = 0;
	for (CurriculumGroup group : cycleGroup.getChildCurriculumGroups()) {
	    if (group.getAprovedEctsCredits() != 0) {
		counter++;
		areas.append(group.getName().getContent(Language.getDefaultLanguage()));
		areas.append(" (");
		areas.append(group.getAprovedEctsCredits());
		areas.append(") ");
	    }
	}
	addParameter("programmeRequirements", String.format(
		"O curso de %s em %s é constituido por %d ECTS e oferece %d àreas de especialização: %s", "licenciatura",
		"engenharia qq coisa", ectsCredits, counter, areas.toString().trim()));

	addParameter("registrationNumber", registration.getNumber());
	return addDegreeParameters(registration, conclusion);
    }

    private Integer addDegreeParameters(final Registration registration, ExecutionYear conclusion) {
	final Degree degree = registration.getDegree();
	String degreeName = degree.getFilteredName(conclusion);
	addParameter("degreeFilteredName", degree.getDegreeType().getFilteredName() + SINGLE_SPACE
		+ getResourceBundle().getString("label.in") + SINGLE_SPACE + degreeName);
	addParameter("prevailingScientificArea", degreeName);
	addDegreeInfoParameters(registration, degree, conclusion);

	return addCycleTypeParameters(registration, degree, conclusion);
    }

    private void addDegreeInfoParameters(final Registration registration, final Degree degree, ExecutionYear conclusion) {
	final DegreeInfo degreeInfo = degree.getDegreeInfoFor(conclusion);
	addParameter("professionalExits", getMLSTextContent(degreeInfo.getProfessionalExits()));

	final MultiLanguageString dcpDescription = registration.getLastDegreeCurricularPlan().getDescriptionI18N();
	addParameter("degreeCurricularPlanDescription", getMLSTextContent(dcpDescription));

	addParameter("aditionalInfoSources", buildAditionalInfoSources(degreeInfo, dcpDescription));
    }

    private String buildAditionalInfoSources(final DegreeInfo degreeInfo, final MultiLanguageString dcpDescription) {
	final StringBuilder result = new StringBuilder();

	getMLSHrefContent(result, degreeInfo.getAdditionalInfo());
	getMLSHrefContent(result, degreeInfo.getLinks());
	getMLSHrefContent(result, degreeInfo.getDescription());
	getMLSHrefContent(result, dcpDescription);
	getMLSHrefContent(result, degreeInfo.getOperationalRegime());
	getMLSHrefContent(result, degreeInfo.getHistory());
	getMLSHrefContent(result, degreeInfo.getClassifications());
	getMLSHrefContent(result, degreeInfo.getDesignedFor());
	getMLSHrefContent(result, degreeInfo.getObjectives());
	getMLSHrefContent(result, degreeInfo.getProfessionalExits());
	getMLSHrefContent(result, degreeInfo.getRecognitions());

	return result.toString();
    }

    private void getMLSHrefContent(final StringBuilder builder, final MultiLanguageString mls) {
	if (mls == null) {
	    return;
	}

	final String content = getMLSTextContent(mls);
	for (String ahref : content.split("<a href")) {
	    if (ahref.contains("</a>")) {
		ahref = "<a href" + ahref.substring(0, ahref.indexOf("</a>")) + "</a>";
		ahref = ahref.replace(LINE_BREAK, EMPTY_STR);
		String result = convert(ahref);
		final String betweenBrackets = result.substring(result.indexOf("("), result.indexOf(")") + 1).trim();
		final String outsideBrackets = result.replace(betweenBrackets, EMPTY_STR).trim();
		if (betweenBrackets.contains("mailto") || betweenBrackets.contains(outsideBrackets)) {
		    result = outsideBrackets;
		}
		if (!builder.toString().contains(result)) {
		    builder.append(result);
		    builder.append(LINE_BREAK);
		}
	    }
	}
    }

    private Integer addCycleTypeParameters(final Registration registration, final Degree degree, ExecutionYear conclusion) {
	final CycleType cycleType = getRequestedCycle();
	addEntriesParameters(registration, cycleType);
	addParameter("qualificationLevel", getResourceBundle().getString("diploma.supplement.qualification." + cycleType));
	addParameter("weeksOfStudyPerYear", getResourceBundle().getString("diploma.supplement.weeksOfStudyPerYear." + cycleType));

	final DegreeType degreeType = degree.getDegreeType();
	// TODO: Confirmar com o João
	addParameter("ectsCredits", Math.round(registration.getLastStudentCurricularPlan().getCycle(cycleType).getDefaultEcts(
		conclusion)));
	addParameter("years", degreeType.getYears(cycleType));
	addParameter("semesters", degreeType.getSemesters(cycleType));
	if (cycleType.equals(CycleType.FIRST_CYCLE)) {
	    addParameter("languages", getEnumerationBundle().getString("pt"));
	} else {
	    addParameter("languages", getEnumerationBundle().getString("pt") + SINGLE_SPACE
		    + getEnumerationBundle().getString("AND").toLowerCase() + SINGLE_SPACE
		    + getEnumerationBundle().getString("en"));
	}
	addDegreeTypeParameters(cycleType, degreeType);

	return addGradesParameters(registration, cycleType);
    }

    private void addEntriesParameters(final Registration registration, final CycleType cycleType) {
	final List<DiplomaSupplementEntry> entries = new ArrayList<DiplomaSupplementEntry>();
	final Map<Unit, String> academicUnitIdentifiers = new HashMap<Unit, String>();

	for (ICurriculumEntry entry : registration.getCurriculum(cycleType).getCurriculumEntries()) {
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

    private void addDegreeTypeParameters(final CycleType cycleType, final DegreeType degreeType) {
	final String graduateTitle = degreeType.getGraduateTitle(cycleType, getLocale());
	addParameter("graduateTitle", graduateTitle);

	final StringBuilder access = new StringBuilder();
	if (cycleType == CycleType.THIRD_CYCLE) {
	    access.append(getResourceBundle().getString("diploma.supplement.five.one.three"));
	} else {
	    access.append(getResourceBundle().getString("diploma.supplement.five.one.one")).append(SINGLE_SPACE);
	    access.append(graduateTitle).append(SINGLE_SPACE);
	    access.append(getResourceBundle().getString("diploma.supplement.five.one.two"));
	}
	addParameter("accessToHigherLevelOfEducation", access.toString());
    }

    private Integer addGradesParameters(final Registration registration, final CycleType cycleType) {
	final Integer result = registration.getFinalAverage(cycleType);
	addParameter("finalAverage", result);

	final GradeDistribution gradeDistr = GradeDistribution.ECTS_SCALE_20;
	addParameter("gradeDistribution", gradeDistr);

	final Distribution distribution = gradeDistr.getDistribution(result);
	addParameter("finalAverageDistribution", distribution);

	addParameter("finalAverageQualified", getApplicationBundle().getString(
		"label.grade." + distribution.getScale().toLowerCase()));
	return result;
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

	private final BigDecimal ectsCreditsForCurriculum;

	private final String gradeValue;

	private final String ectsScale;

	private final String academicUnitId;

	public DiplomaSupplementEntry(final ICurriculumEntry entry, final Map<Unit, String> academicUnitIdentifiers) {
	    this.entry = entry;
	    this.executionYear = entry.getExecutionYear().getYear();
	    this.name = getMLSTextContent(entry.getName());
	    this.ectsCreditsForCurriculum = entry.getEctsCreditsForCurriculum();

	    final Grade grade = entry.getGrade();
	    this.gradeValue = grade.getValue();
	    this.ectsScale = grade.getEctsScale();

	    this.academicUnitId = obtainAcademicUnitIdentifier(academicUnitIdentifiers);
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
