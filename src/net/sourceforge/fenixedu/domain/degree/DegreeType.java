/*
 * DegreeType.java
 *
 * Created on 20 de Dezembro de 2002, 14:12
 */

package net.sourceforge.fenixedu.domain.degree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.cardGeneration.Category;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicPeriod;
import net.sourceforge.fenixedu.util.StringUtils;
import pt.utl.ist.fenix.tools.util.StringAppender;
import pt.utl.ist.fenix.tools.util.i18n.Language;

/**
 * 
 * @author Nuno Nunes & Joana Mota
 */
public enum DegreeType {

    DEGREE(GradeScale.TYPE20, AcademicPeriod.FIVE_YEAR, true, // canCreateStudent
	    false, // canCreateStudentOnlyWithCandidacy
	    AdministrativeOfficeType.DEGREE, false, // isFirstCycle
	    false, // isSecondCycle
	    false, // isThirdCycle
	    true // qualifiesForGraduateTitle
    ) {

	@Override
	public Collection<CycleType> getCycleTypes() {
	    return Collections.emptySet();
	}

	@Override
	public Collection<CycleType> getSupportedCyclesToEnrol() {
	    return Collections.emptyList();
	}

    },

    MASTER_DEGREE(GradeScale.TYPE5, AcademicPeriod.TWO_YEAR, false, // canCreateStudent
	    true, // canCreateStudentOnlyWithCandidacy
	    AdministrativeOfficeType.MASTER_DEGREE, false, // isFirstCycle
	    false, // isSecondCycle
	    false, // isThirdCycle
	    true // qualifiesForGraduateTitle
    ) {

	@Override
	public Collection<CycleType> getCycleTypes() {
	    return Collections.emptySet();
	}

	@Override
	public Collection<CycleType> getSupportedCyclesToEnrol() {
	    return Collections.emptyList();
	}

    },

    BOLONHA_DEGREE(GradeScale.TYPE20, AcademicPeriod.THREE_YEAR, true, // canCreateStudent
	    false, // canCreateStudentOnlyWithCandidacy
	    AdministrativeOfficeType.DEGREE, true, // isFirstCycle
	    false, // isSecondCycle
	    false, // isThirdCycle
	    true // qualifiesForGraduateTitle
    ) {

	@Override
	public Collection<CycleType> getCycleTypes() {
	    return FIRST_CYCLE_TYPE_SET;
	}

	@Override
	public Collection<CycleType> getSupportedCyclesToEnrol() {
	    return FIRST_AND_SECOND_CYCLE_TYPE_LIST;
	}

	@Override
	public boolean canRemoveEnrolmentIn(CycleType cycleType) {
	    return cycleType == CycleType.SECOND_CYCLE;
	}

    },

    BOLONHA_MASTER_DEGREE(GradeScale.TYPE20, AcademicPeriod.TWO_YEAR, true, // canCreateStudent
	    false, // canCreateStudentOnlyWithCandidacy
	    AdministrativeOfficeType.DEGREE, false, // isFirstCycle
	    true, // isSecondCycle
	    false, // isThirdCycle
	    true // qualifiesForGraduateTitle
    ) {

	@Override
	public Collection<CycleType> getCycleTypes() {
	    return SECOND_CYCLE_TYPE_SET;
	}

	@Override
	public Collection<CycleType> getSupportedCyclesToEnrol() {
	    return SECOND_CYCLE_TYPE_SET;
	}

    },

    BOLONHA_INTEGRATED_MASTER_DEGREE(GradeScale.TYPE20, AcademicPeriod.FIVE_YEAR, true, // canCreateStudent
	    false, // canCreateStudentOnlyWithCandidacy
	    AdministrativeOfficeType.DEGREE, true, // isFirstCycle
	    true, // isSecondCycle
	    false, // isThirdCycle
	    true // qualifiesForGraduateTitle
    ) {

	@Override
	public Collection<CycleType> getCycleTypes() {
	    return FIRST_AND_SECOND_CYCLE_TYPE_LIST;
	}

	@Override
	public Collection<CycleType> getSupportedCyclesToEnrol() {
	    return FIRST_AND_SECOND_CYCLE_TYPE_LIST;
	}

	@Override
	public boolean canRemoveEnrolmentIn(CycleType cycleType) {
	    return cycleType == CycleType.SECOND_CYCLE;
	}

	@Override
	public Integer getYears(final CycleType cycleType) {
	    if (cycleType == null) {
		return getYears();
	    }

	    switch (cycleType) {
	    case FIRST_CYCLE:
		return BOLONHA_DEGREE.getYears(cycleType);
	    case SECOND_CYCLE:
		return BOLONHA_MASTER_DEGREE.getYears(cycleType);
	    }

	    return null;
	}

    },

    BOLONHA_PHD_PROGRAM(GradeScale.TYPE20, AcademicPeriod.YEAR, true, // canCreateStudent
	    false, // canCreateStudentOnlyWithCandidacy
	    AdministrativeOfficeType.MASTER_DEGREE, false, // isFirstCycle
	    false, // isSecondCycle
	    true, // isThirdCycle
	    true // qualifiesForGraduateTitle
    ) {

	@Override
	public Collection<CycleType> getCycleTypes() {
	    return THIRD_CYCLE_TYPE_SET;
	}

	@Override
	public Collection<CycleType> getSupportedCyclesToEnrol() {
	    return THIRD_CYCLE_TYPE_SET;
	}

    },

    BOLONHA_ADVANCED_FORMATION_DIPLOMA(GradeScale.TYPE20, AcademicPeriod.YEAR, true, // canCreateStudent
	    true, // canCreateStudentOnlyWithCandidacy
	    AdministrativeOfficeType.MASTER_DEGREE, false, // isFirstCycle
	    false, // isSecondCycle
	    true, // isThirdCycle
	    false // qualifiesForGraduateTitle
    ) {

	@Override
	public Collection<CycleType> getCycleTypes() {
	    return THIRD_CYCLE_TYPE_SET;
	}

	@Override
	public Collection<CycleType> getSupportedCyclesToEnrol() {
	    return THIRD_CYCLE_TYPE_SET;
	}

    },

    BOLONHA_SPECIALIZATION_DEGREE(GradeScale.TYPE20, AcademicPeriod.YEAR, true, // canCreateStudent
	    false, // canCreateStudentOnlyWithCandidacy
	    AdministrativeOfficeType.MASTER_DEGREE, false, // isFirstCycle
	    false, // isSecondCycle
	    false, // isThirdCycle
	    false // qualifiesForGraduateTitle
    ) {

	@Override
	public Collection<CycleType> getCycleTypes() {
	    return Collections.emptySet();
	}

	@Override
	public Collection<CycleType> getSupportedCyclesToEnrol() {
	    return Collections.emptyList();
	}

    };

    private static final Set<CycleType> FIRST_CYCLE_TYPE_SET = Collections.singleton(CycleType.FIRST_CYCLE);

    private static final Set<CycleType> SECOND_CYCLE_TYPE_SET = Collections.singleton(CycleType.SECOND_CYCLE);

    private static final Set<CycleType> THIRD_CYCLE_TYPE_SET = Collections.singleton(CycleType.THIRD_CYCLE);

    private static final List<CycleType> FIRST_AND_SECOND_CYCLE_TYPE_LIST = Arrays.asList(new CycleType[] {
	    CycleType.FIRST_CYCLE, CycleType.SECOND_CYCLE });

    private GradeScale gradeScale;

    private AcademicPeriod academicPeriod;

    private boolean canCreateStudent;

    private boolean canCreateStudentOnlyWithCandidacy;

    private AdministrativeOfficeType administrativeOfficeType;

    private boolean isFirstCycle;

    private boolean isSecondCycle;

    private boolean isThirdCycle;

    private boolean qualifiesForGraduateTitle;

    private Category studentCategoryCode;

    private DegreeType(GradeScale gradeScale, AcademicPeriod academicPeriod, boolean canCreateStudent,
	    boolean canCreateStudentOnlyWithCandidacy, AdministrativeOfficeType administrativeOfficeType, boolean isFirstCycle,
	    boolean isSecondCycle, boolean isThirdCycle, boolean qualifiesForGraduateTitle) {
	this.gradeScale = gradeScale;
	this.academicPeriod = academicPeriod;
	this.canCreateStudent = canCreateStudent;
	this.canCreateStudentOnlyWithCandidacy = canCreateStudentOnlyWithCandidacy;
	this.administrativeOfficeType = administrativeOfficeType;
	this.isFirstCycle = isFirstCycle;
	this.isSecondCycle = isSecondCycle;
	this.isThirdCycle = isThirdCycle;
	this.qualifiesForGraduateTitle = qualifiesForGraduateTitle;
    }

    public String getName() {
	return name();
    }

    public Category getStudentCategoryCode() {
	return studentCategoryCode;
    }

    public GradeScale getGradeScale() {
	return this.gradeScale;
    }

    public AcademicPeriod getAcademicPeriod() {
	return academicPeriod;
    }

    public int getYears() {
	return Float.valueOf(this.academicPeriod.getWeight()).intValue();
    }

    public int getSemesters() {
	return Float.valueOf(this.academicPeriod.getWeight() / AcademicPeriod.SEMESTER.getWeight()).intValue();
    }

    public Integer getYears(final CycleType cycleType) {
	if (cycleType == null) {
	    return getYears();
	}

	return hasCycleTypes(cycleType) ? Float.valueOf(getAcademicPeriod().getWeight()).intValue() : null;
    }

    public Integer getSemesters(final CycleType cycleType) {
	return Float.valueOf(getYears(cycleType) / AcademicPeriod.SEMESTER.getWeight()).intValue();
    }

    final public boolean hasExactlyOneCurricularYear() {
	return getYears() == 1;
    }

    public double getDefaultEctsCredits() {
	if (getAcademicPeriod().equals(AcademicPeriod.YEAR)) {
	    return 30d;
	} else if (getAcademicPeriod().equals(AcademicPeriod.TWO_YEAR)) {
	    return 120d;
	} else if (getAcademicPeriod().equals(AcademicPeriod.THREE_YEAR)) {
	    return 180d;
	} else if (getAcademicPeriod().equals(AcademicPeriod.FIVE_YEAR)) {
	    return 300d;
	} else {
	    return 0d;
	}
    }

    final public String getCreditsDescription() {
	return this == DegreeType.MASTER_DEGREE ? " Créd." : " ECTS";
    }

    public boolean isBolonhaType() {
	return this != DegreeType.DEGREE && this != DegreeType.MASTER_DEGREE;
    }

    public boolean isDegree() {
	return this == DegreeType.DEGREE || this == DegreeType.BOLONHA_DEGREE;
    }

    public boolean isMasterDegree() {
	return this == DegreeType.MASTER_DEGREE || this == DegreeType.BOLONHA_MASTER_DEGREE;
    }

    public boolean isDegreeOrBolonhaDegreeOrBolonhaIntegratedMasterDegree() {
	return this.isDegree() || this == DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE;
    }

    public boolean canCreateStudent() {
	return canCreateStudent;
    }

    public boolean canCreateStudentOnlyWithCandidacy() {
	return canCreateStudentOnlyWithCandidacy;
    }

    public AdministrativeOfficeType getAdministrativeOfficeType() {
	return administrativeOfficeType;
    }

    public String getPrefix() {
	return getPrefix(Language.getLocale());
    }

    public String getPrefix(final Locale locale) {
	final StringBuilder result = new StringBuilder();

	final ResourceBundle bundle = ResourceBundle.getBundle("resources.AcademicAdminOffice", locale);
	switch (this) {
	case BOLONHA_PHD_PROGRAM:
	    return result.toString();
	case BOLONHA_ADVANCED_FORMATION_DIPLOMA:
	    result.append(bundle.getString("degree.DegreeType.prefix.one")).append(StringUtils.SINGLE_SPACE);
	    return result.toString();
	default:
	    final String string = bundle.getString("degree.DegreeType.prefix.two");
	    result.append(string).append(string.isEmpty() ? StringUtils.EMPTY : StringUtils.SINGLE_SPACE);
	    return result.toString();
	}
    }

    public String getLocalizedName() {
	return getLocalizedName(Language.getLocale());
    }

    public String getLocalizedName(final Locale locale) {
	return ResourceBundle.getBundle("resources.EnumerationResources", locale).getString(getQualifiedName());
    }

    public String getQualifiedName() {
	return StringAppender.append(DegreeType.class.getSimpleName(), ".", name());
    }

    final public String getFilteredName() {
	return getFilteredName(Language.getLocale());
    }

    final public String getFilteredName(final Locale locale) {
	final StringBuilder result = new StringBuilder(getLocalizedName(locale));
	final String toRemove;

	if (isBolonhaType()) {
	    toRemove = " Bolonha";
	} else if (this == DegreeType.DEGREE) {
	    final ResourceBundle bundle = ResourceBundle.getBundle("resources.ApplicationResources", locale);
	    toRemove = StringAppender.append(" (", Integer.toString(getYears()), " ", bundle.getString("years"), ")");
	} else {
	    toRemove = StringUtils.EMPTY;
	}

	if (result.toString().contains(toRemove)) {
	    result.replace(result.indexOf(toRemove), result.indexOf(toRemove) + toRemove.length(), StringUtils.EMPTY);
	}

	return result.toString();
    }

    final public boolean getQualifiesForGraduateTitle() {
	return qualifiesForGraduateTitle;
    }

    @Deprecated
    final public String getSeniorTitle() {
	return getGraduateTitle();
    }

    final public String getGraduateTitle() {
	if (getQualifiesForGraduateTitle()) {
	    return ResourceBundle.getBundle("resources.EnumerationResources", Language.getLocale()).getString(
		    getQualifiedName() + ".graduate.title");
	}

	return StringUtils.EMPTY;
    }

    final public String getGraduateTitle(final CycleType cycleType) {
	if (getQualifiesForGraduateTitle()) {

	    if (cycleType == null) {
		return getGraduateTitle();
	    }

	    if (getCycleTypes().isEmpty()) {
		throw new DomainException("DegreeType.has.no.cycle.type");
	    }

	    if (!hasCycleTypes(cycleType)) {
		throw new DomainException("DegreeType.doesnt.have.such.cycle.type");
	    }

	    if (getQualifiesForGraduateTitle()) {
		return ResourceBundle.getBundle("resources.EnumerationResources", Language.getLocale()).getString(
			getQualifiedName() + (isComposite() ? "." + cycleType.name() : "") + ".graduate.title");
	    }

	}

	return StringUtils.EMPTY;
    }

    private static final List<DegreeType> BOLONHA_DEGREE_TYPES;
    static {
	final List<DegreeType> result = new ArrayList<DegreeType>();
	for (final DegreeType degreeType : values()) {
	    if (degreeType.isBolonhaType()) {
		result.add(degreeType);
	    }
	}
	BOLONHA_DEGREE_TYPES = Collections.unmodifiableList(result);
    }

    public static List<DegreeType> getBolonhaDegreeTypes() {
	return BOLONHA_DEGREE_TYPES;
    }

    public boolean isFirstCycle() {
	return isFirstCycle;
    }

    public boolean isSecondCycle() {
	return isSecondCycle;
    }

    public boolean isThirdCycle() {
	return isThirdCycle;
    }

    final public boolean hasAnyCycleTypes() {
	return !getCycleTypes().isEmpty();
    }

    final public boolean hasCycleTypes(final CycleType cycleType) {
	return getCycleTypes().contains(cycleType);
    }

    final public boolean isComposite() {
	return getCycleTypes().size() > 1;
    }

    final public boolean hasExactlyOneCycleType() {
	return getCycleTypes().size() == 1;
    }

    final public CycleType getCycleType() {
	if (hasExactlyOneCycleType()) {
	    return getCycleTypes().iterator().next();
	}

	throw new DomainException("DegreeType.has.more.than.one.cycle.type");
    }

    final public boolean isStrictlyFirstCycle() {
	return hasExactlyOneCycleType() && getCycleTypes().contains(CycleType.FIRST_CYCLE);
    }

    public CycleType getFirstCycleType() {
	final Collection<CycleType> cycleTypes = getCycleTypes();
	return cycleTypes.isEmpty() ? null : Collections.min(cycleTypes, CycleType.COMPARATOR_BY_LESS_WEIGHT);
    }

    public CycleType getLastCycleType() {
	final Collection<CycleType> cycleTypes = getCycleTypes();
	return cycleTypes.isEmpty() ? null : Collections.max(cycleTypes, CycleType.COMPARATOR_BY_LESS_WEIGHT);
    }

    public Collection<CycleType> getOrderedCycleTypes() {
	TreeSet<CycleType> result = new TreeSet<CycleType>(CycleType.COMPARATOR_BY_LESS_WEIGHT);
	result.addAll(getCycleTypes());
	return result;
    }

    public boolean canRemoveEnrolmentIn(final CycleType cycleType) {
	return false;
    }

    abstract public Collection<CycleType> getCycleTypes();

    abstract public Collection<CycleType> getSupportedCyclesToEnrol();

}
