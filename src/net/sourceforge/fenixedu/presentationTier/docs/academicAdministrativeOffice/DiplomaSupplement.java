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
import java.util.ResourceBundle;
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
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DiplomaRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.curriculum.ICurriculumEntry;
import net.sourceforge.fenixedu.domain.studentCurriculum.ExternalEnrolment;
import net.sourceforge.fenixedu.util.HtmlToTextConverterUtil;
import net.sourceforge.fenixedu.util.NameUtils;
import net.sourceforge.fenixedu.util.StringFormatter;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;
import org.joda.time.format.DateTimeFormat;

import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class DiplomaSupplement extends AdministrativeOfficeDocument {

    private Language language = null;
    static final private String DATE_FORMAT = "dd/MM/yyyy";
    static final public Locale[] suportedLocales = { Language.getDefaultLocale(), new Locale("en") };

    protected DiplomaSupplement(final DocumentRequest documentRequest, final Locale locale) {
	super(documentRequest, locale);
    }

    @Override
    public String getReportTemplateKey() {
	return getClass().getName();
    }

    @Override
    protected void fillReport() {
	language = Language.valueOf(resourceBundle.getLocale().getLanguage());
	addParameter("bundle", resourceBundle);
	addParameter("day", new YearMonthDay().toString(DATE_FORMAT));
	addUniversityParameters();
	addPersonParameters();
	addRegistrationParameters();
    }

    private void addUniversityParameters() {
	final UniversityUnit institutionsUniversityUnit = UniversityUnit.getInstitutionsUniversityUnit();
	addParameter("universityName", institutionsUniversityUnit.getName());
	addParameter("universityPrincipalName", institutionsUniversityUnit.getInstitutionsUniversityPrincipal()
		.getValidatedName());
	addParameter("institutionName", RootDomainObject.getInstance().getInstitutionUnit().getName());
    }

    private void addPersonParameters() {
	final Person person = getDocumentRequest().getRegistration().getPerson();
	final String name = StringFormatter.prettyPrint(person.getName().trim());
	addParameter("name", name);
	addParameter("familyName", NameUtils.getLastName(name));
	addParameter("givenName", NameUtils.getFirstName(name));
	addParameter("birthDay", person.getDateOfBirthYearMonthDay().toString(DateTimeFormat.forPattern(DATE_FORMAT)));
	addParameter("nationality", StringFormatter.prettyPrint(person.getCountry().getNationality()));
	addDocumentIdParameters(person);
    }

    private void addDocumentIdParameters(final Person person) {
	addParameter("documentIdType", ResourceBundle.getBundle("resources.EnumerationResources", resourceBundle.getLocale())
		.getString(person.getIdDocumentType().getName()));
	addParameter("documentIdNumber", person.getDocumentIdNumber());
	addParameter("documentIdExpiration", person.getExpirationDateOfDocumentIdYearMonthDay().toString(
		DateTimeFormat.forPattern(DATE_FORMAT)));
    }

    private Integer addRegistrationParameters() {
	final Registration reg = getDocumentRequest().getRegistration();
	addParameter("registrationNumber", reg.getNumber());
	return addDegreeParameters(reg);
    }

    private String getMLSTextContent(final MultiLanguageString mls) {
	if (mls == null) {
	    return StringUtils.EMPTY;
	}
	final String content = mls.hasContent(language) && !StringUtils.isEmpty(mls.getContent(language)) ? mls
		.getContent(language) : mls.getContent();
	return convert(content);
    }

    private String convert(final String content) {
	return HtmlToTextConverterUtil.convertToText(content).replace("\n\n", "\t").replace("\n", "").replace("\t", "\n\n")
		.trim();
    }

    private Integer addDegreeParameters(final Registration registration) {
	final Degree degree = registration.getDegree();
	addParameter("degreeFilteredName", degree.getDegreeType().getFilteredName() + " " + resourceBundle.getString("label.in")
		+ " " + degree.getFilteredName());
	addParameter("prevailingScientificArea", degree.getPrevailingScientificArea());
	addDegreeInfoParameters(registration, degree);

	return addCycleTypeParameters(registration, degree);
    }

    private void addDegreeInfoParameters(final Registration registration, final Degree degree) {
	final ExecutionYear conclusionYear = registration.getConclusionYear();
	final DegreeInfo degreeInfo = degree.getMostRecentDegreeInfo(conclusionYear);
	addParameter("qualificationLevel", getMLSTextContent(degreeInfo.getQualificationLevel()));
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

	final String content = mls.hasContent(language) ? mls.getContent(language) : mls.getContent();
	for (String ahref : content.split("<a href")) {
	    if (ahref.contains("</a>")) {
		ahref = "<a href" + ahref.substring(0, ahref.indexOf("</a>")) + "</a>";
		ahref = ahref.replace("\n", StringUtils.EMPTY);
		String result = convert(ahref);
		final String betweenBrackets = result.substring(result.indexOf("("), result.indexOf(")") + 1).trim();
		final String outsideBrackets = result.replace(betweenBrackets, StringUtils.EMPTY).trim();
		if (betweenBrackets.contains("mailto") || betweenBrackets.contains(outsideBrackets)) {
		    result = outsideBrackets;
		}
		if (!builder.toString().contains(result)) {
		    builder.append(result);
		    builder.append("\n");
		}
	    }
	}
    }

    private Integer addCycleTypeParameters(final Registration registration, final Degree degree) {
	final CycleType cycleType = ((DiplomaRequest) getDocumentRequest()).getWhatShouldBeRequestedCycle();
	addEntriesParameters(registration, cycleType);
	addParameter("weeksOfStudyPerYear", resourceBundle.getString("diploma.supplement.weeksOfStudyPerYear." + cycleType));
	addParameter("ectsCredits", cycleType.getDefaultEcts());

	final DegreeType degreeType = degree.getDegreeType();
	addParameter("years", degreeType.getYears(cycleType));
	addParameter("semesters", degreeType.getSemesters(cycleType));
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
	final String graduateTitle = degreeType.getGraduateTitle(cycleType);
	addParameter("graduateTitle", graduateTitle);

	final StringBuilder access = new StringBuilder();
	if (cycleType == CycleType.THIRD_CYCLE) {
	    access.append(resourceBundle.getString("diploma.supplement.five.one.three"));
	} else {
	    access.append(resourceBundle.getString("diploma.supplement.five.one.one")).append(" ");
	    access.append(graduateTitle).append(" ");
	    access.append(resourceBundle.getString("diploma.supplement.five.one.two"));
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

	final ResourceBundle resources = ResourceBundle.getBundle("resources.ApplicationResources", resourceBundle.getLocale());
	addParameter("finalAverageQualified", resources.getString("label.grade." + distribution.getScale().toLowerCase()));
	return result;
    }

    public class DiplomaSupplementEntry implements Comparable<DiplomaSupplementEntry> {
	final public Comparator<DiplomaSupplementEntry> COMPARATOR = new Comparator<DiplomaSupplementEntry>() {
	    public int compare(DiplomaSupplementEntry o1, DiplomaSupplementEntry o2) {
		final ComparatorChain chain = new ComparatorChain();
		chain.addComparator(new BeanComparator("executionYear"));
		chain.addComparator(new BeanComparator("name", Collator.getInstance()));
		return chain.compare(o1, o2);
	    }
	};

	private ICurriculumEntry entry;

	private String executionYear;

	private String name;

	private BigDecimal ectsCreditsForCurriculum;

	private String gradeValue;

	private String ectsScale;

	private String academicUnitId;

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
	private String identifier;

	private String name;

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
