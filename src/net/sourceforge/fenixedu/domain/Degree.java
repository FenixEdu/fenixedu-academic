package net.sourceforge.fenixedu.domain;

import java.lang.ref.SoftReference;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.accessControl.DelegatesGroup;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.degreeStructure.EctsDegreeByCurricularYearConversionTable;
import net.sourceforge.fenixedu.domain.degreeStructure.EctsDegreeGraduationGradeConversionTable;
import net.sourceforge.fenixedu.domain.degreeStructure.EctsGraduationGradeConversionTable;
import net.sourceforge.fenixedu.domain.elections.DelegateElection;
import net.sourceforge.fenixedu.domain.elections.YearDelegateElection;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.oldInquiries.OldInquiriesCoursesRes;
import net.sourceforge.fenixedu.domain.oldInquiries.OldInquiriesSummary;
import net.sourceforge.fenixedu.domain.oldInquiries.OldInquiriesTeachersRes;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.domain.space.Space;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumLine;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;
import net.sourceforge.fenixedu.domain.studentCurriculum.Dismissal;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.injectionCode.IGroup;
import net.sourceforge.fenixedu.util.MarkType;
import net.sourceforge.fenixedu.util.StringUtils;

import org.apache.commons.collections.comparators.ReverseComparator;
import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.util.StringAppender;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class Degree extends Degree_Base implements Comparable<Degree> {

    public static final String DEFAULT_MINISTRY_CODE = "9999";

    private static final Collator collator = Collator.getInstance();

    static final public Comparator<Degree> COMPARATOR_BY_NAME = new Comparator<Degree>() {
	@Override
	public int compare(final Degree o1, final Degree o2) {
	    return collator.compare(o1.getName(), o2.getName());
	}
    };

    static final public Comparator<Degree> COMPARATOR_BY_NAME_AND_ID = new Comparator<Degree>() {
	@Override
	public int compare(final Degree o1, final Degree o2) {
	    final int nameResult = COMPARATOR_BY_NAME.compare(o1, o2);
	    return nameResult == 0 ? COMPARATOR_BY_ID.compare(o1, o2) : nameResult;
	}
    };

    static final private Comparator<Degree> COMPARATOR_BY_DEGREE_TYPE = new Comparator<Degree>() {
	@Override
	public int compare(final Degree o1, final Degree o2) {
	    return collator.compare(o1.getDegreeType().getLocalizedName(), o2.getDegreeType().getLocalizedName());
	}
    };

    static final public Comparator<Degree> COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID = new Comparator<Degree>() {
	@Override
	public int compare(final Degree o1, final Degree o2) {
	    final int typeResult = COMPARATOR_BY_DEGREE_TYPE.compare(o1, o2);
	    return typeResult == 0 ? COMPARATOR_BY_NAME_AND_ID.compare(o1, o2) : typeResult;
	}
    };

    static final public Comparator<Degree> COMPARATOR_BY_FIRST_ENROLMENTS_PERIOD_AND_ID = new Comparator<Degree>() {
	@Override
	public int compare(final Degree degree1, final Degree degree2) {
	    ExecutionSemester semester1 = degree1.getFirstDegreeCurricularPlan().getFirstExecutionPeriodEnrolments();
	    ExecutionSemester semester2 = degree2.getFirstDegreeCurricularPlan().getFirstExecutionPeriodEnrolments();
	    final int result = semester1.compareTo(semester2);
	    return result == 0 ? COMPARATOR_BY_ID.compare(degree1, degree2) : result;
	}
    };

    @Override
    public int compareTo(final Degree o) {
	return Degree.COMPARATOR_BY_NAME_AND_ID.compare(this, o);
    }

    protected Degree() {
	super();

	setRootDomainObject(RootDomainObject.getInstance());
	new DegreeSite(this);
    }

    public Degree(String name, String nameEn, String code, DegreeType degreeType, GradeScale gradeScale) {
	this(name, nameEn, code, degreeType, gradeScale, ExecutionYear.readCurrentExecutionYear());
    }

    public Degree(String name, String nameEn, String code, DegreeType degreeType, GradeScale gradeScale,
	    ExecutionYear executionYear) {
	this();
	commonFieldsChange(name, nameEn, code, gradeScale, executionYear);

	if (degreeType == null) {
	    throw new DomainException("degree.degree.type.not.null");
	}
	this.setDegreeType(degreeType);
    }

    public Degree(String name, String nameEn, String acronym, DegreeType degreeType, Double ectsCredits, GradeScale gradeScale,
	    String prevailingScientificArea) {
	this();
	commonFieldsChange(name, nameEn, acronym, gradeScale, ExecutionYear.readCurrentExecutionYear());
	newStructureFieldsChange(degreeType, ectsCredits, prevailingScientificArea);
    }

    private void commonFieldsChange(String name, String nameEn, String code, GradeScale gradeScale, ExecutionYear executionYear) {
	if (name == null) {
	    throw new DomainException("degree.name.not.null");
	} else if (nameEn == null) {
	    throw new DomainException("degree.name.en.not.null");
	} else if (code == null) {
	    throw new DomainException("degree.code.not.null");
	}

	DegreeInfo degreeInfo = getDegreeInfoFor(executionYear);
	if (degreeInfo == null) {
	    degreeInfo = tryCreateUsingMostRecentInfo(executionYear);
	}
	degreeInfo.setName(MultiLanguageString.i18n().add("pt", name.trim()).add("en", nameEn.trim()).finish());

	this.setNome(name);
	this.setNameEn(nameEn);
	this.setSigla(code.trim());
	this.setGradeScale(gradeScale);
    }

    private void newStructureFieldsChange(DegreeType degreeType, Double ectsCredits, String prevailingScientificArea) {
	if (degreeType == null) {
	    throw new DomainException("degree.degree.type.not.null");
	} else if (ectsCredits == null) {
	    throw new DomainException("degree.ectsCredits.not.null");
	}

	this.setDegreeType(degreeType);
	this.setEctsCredits(ectsCredits);
	this.setPrevailingScientificArea(prevailingScientificArea == null ? null : prevailingScientificArea.trim());
    }

    public void edit(String name, String nameEn, String code, DegreeType degreeType, GradeScale gradeScale,
	    ExecutionYear executionYear) {
	commonFieldsChange(name, nameEn, code, gradeScale, executionYear);

	if (degreeType == null) {
	    throw new DomainException("degree.degree.type.not.null");
	}
	this.setDegreeType(degreeType);
    }

    public void edit(String name, String nameEn, String acronym, DegreeType degreeType, Double ectsCredits,
	    GradeScale gradeScale, String prevailingScientificArea, ExecutionYear executionYear) {
	checkIfCanEdit(degreeType);
	commonFieldsChange(name, nameEn, acronym, gradeScale, executionYear);
	newStructureFieldsChange(degreeType, ectsCredits, prevailingScientificArea);
    }

    private void checkIfCanEdit(final DegreeType degreeType) {
	if (!this.getDegreeType().equals(degreeType) && hasAnyDegreeCurricularPlans()) {
	    throw new DomainException("degree.cant.edit.bolonhaDegreeType");
	}
    }

    public Boolean getCanBeDeleted() {
	if (hasAnyDegreeCurricularPlans()) {
	    return false;
	}

	if (hasSite() && getSite().isDeletable()) {
	    return false;
	}

	return true;
    }

    private void checkDeletion() {
	if (hasAnyDegreeCurricularPlans()) {
	    throw new DomainException("error.degree.has.degree.curricular.plans");
	}

	if (hasSite() && !getSite().isDeletable()) {
	    throw new DomainException("error.degree.has.site.undeletable");
	}
    }

    public void delete() {

	checkDeletion();

	Iterator<OldInquiriesCoursesRes> oicrIterator = getAssociatedOldInquiriesCoursesResIterator();
	while (oicrIterator.hasNext()) {
	    OldInquiriesCoursesRes oicr = oicrIterator.next();
	    oicrIterator.remove();
	    oicr.delete();
	}

	Iterator<OldInquiriesTeachersRes> oitrIterator = getAssociatedOldInquiriesTeachersResIterator();
	while (oitrIterator.hasNext()) {
	    OldInquiriesTeachersRes oitr = oitrIterator.next();
	    oitrIterator.remove();
	    oitr.delete();
	}

	Iterator<OldInquiriesSummary> oisIterator = getAssociatedOldInquiriesSummariesIterator();
	while (oisIterator.hasNext()) {
	    OldInquiriesSummary ois = oisIterator.next();
	    oisIterator.remove();
	    ois.delete();
	}

	// Iterator<Delegate> delegatesIterator = getDelegatesIterator();
	// while (delegatesIterator.hasNext()) {
	// Delegate delegate = delegatesIterator.next();
	// delegatesIterator.remove();
	// delegate.delete();
	// }

	Iterator<DegreeInfo> degreeInfosIterator = getDegreeInfosIterator();
	while (degreeInfosIterator.hasNext()) {
	    DegreeInfo degreeInfo = degreeInfosIterator.next();
	    degreeInfosIterator.remove();
	    degreeInfo.delete();
	}

	for (; !getParticipatingAnyCurricularCourseCurricularRules().isEmpty(); getParticipatingAnyCurricularCourseCurricularRules()
		.get(0).delete())
	    ;

	for (; hasAnyDelegateElections(); getDelegateElections().get(0).delete())
	    ;

	if (hasSite()) {
	    getSite().delete();
	}

	// checkDeletion assures that site is deletable
	if (hasSender()) {
	    removeSender();
	}

	removeUnit();
	removePhdProgram();
	removeRootDomainObject();

	deleteDomainObject();
    }

    public GradeScale getGradeScaleChain() {
	return super.getGradeScale() != null ? super.getGradeScale() : getDegreeType().getGradeScale();
    }

    public Grade convertGradeToEcts(CurriculumLine curriculumLine, Grade grade) {
	ExecutionYear executionYear = curriculumLine.getExecutionYear();
	CurricularYear curricularYear = CurricularYear.readByYear(curriculumLine.getParentCycleCurriculumGroup().getCurriculum(
		executionYear).getCurricularYear());
	EctsDegreeByCurricularYearConversionTable table = getEctsCourseConversionTable(executionYear.getAcademicInterval(),
		curricularYear);
	if (table != null)
	    return table.convert(grade);
	Unit school = RootDomainObject.getInstance().getInstitutionUnit();
	return school.convertGradeToEcts(curriculumLine, grade);
    }

    public EctsGraduationGradeConversionTable getGraduationConversionTable(AcademicInterval year, CycleType cycle) {
	EctsDegreeGraduationGradeConversionTable table = getEctsGraduationGradeConversionTable(year, cycle);
	if (table != null)
	    return table;
	Unit school = RootDomainObject.getInstance().getInstitutionUnit();
	return school.getGraduationConversionTable(year, cycle);
    }

    public EctsDegreeByCurricularYearConversionTable getEctsCourseConversionTable(AcademicInterval year,
	    CurricularYear curricularYear) {
	for (EctsDegreeByCurricularYearConversionTable table : getEctsCourseConversionTables()) {
	    if (table.getYear().equals(year) && table.getCurricularYear().equals(curricularYear)) {
		return table;
	    }
	}
	return null;
    }

    public EctsDegreeGraduationGradeConversionTable getEctsGraduationGradeConversionTable(AcademicInterval year, CycleType cycle) {
	for (EctsDegreeGraduationGradeConversionTable table : getEctsGraduationGradeConversionTables()) {
	    if (table.getYear().equals(year) && (!getDegreeType().isComposite() || table.getCycle().equals(cycle))) {
		return table;
	    }
	}
	return null;
    }

    public DegreeCurricularPlan createPreBolonhaDegreeCurricularPlan(String name, DegreeCurricularPlanState state,
	    Date initialDate, Date endDate, Integer degreeDuration, Integer minimalYearForOptionalCourses, Double neededCredits,
	    MarkType markType, Integer numerusClausus, String anotation, GradeScale gradeScale) {
	if (!this.isBolonhaDegree()) {
	    for (DegreeCurricularPlan dcp : this.getDegreeCurricularPlansSet()) {
		if (dcp.getName().equalsIgnoreCase(name)) {
		    throw new DomainException("DEGREE.degreeCurricularPlan.existing.name.and.degree");
		}
	    }

	    return new DegreeCurricularPlan(this, name, state, initialDate, endDate, degreeDuration,
		    minimalYearForOptionalCourses, neededCredits, markType, numerusClausus, anotation, gradeScale);
	} else {
	    throw new DomainException("DEGREE.calling.pre.bolonha.method.to.bolonha.degree");
	}
    }

    public DegreeCurricularPlan createBolonhaDegreeCurricularPlan(String name, GradeScale gradeScale, Person creator) {
	if (this.isBolonhaDegree()) {
	    if (name == null) {
		throw new DomainException("DEGREE.degree.curricular.plan.name.cannot.be.null");
	    }
	    for (DegreeCurricularPlan dcp : this.getDegreeCurricularPlansSet()) {
		if (dcp.getName().equalsIgnoreCase(name)) {
		    throw new DomainException("DEGREE.degreeCurricularPlan.existing.name.and.degree");
		}
	    }

	    if (creator == null) {
		throw new DomainException("DEGREE.degree.curricular.plan.creator.cannot.be.null");
	    }
	    final Role bolonhaRole = Role.getRoleByRoleType(RoleType.BOLONHA_MANAGER);
	    if (!creator.hasPersonRoles(bolonhaRole)) {
		creator.addPersonRoles(bolonhaRole);
	    }

	    CurricularPeriod curricularPeriod = new CurricularPeriod(this.getDegreeType().getAcademicPeriod());

	    return new DegreeCurricularPlan(this, name, gradeScale, creator, curricularPeriod);
	} else {
	    throw new DomainException("DEGREE.calling.bolonha.method.to.non.bolonha.degree");
	}
    }

    @Override
    @Deprecated
    public DegreeType getTipoCurso() {
	return getDegreeType();
    }

    @Override
    @Deprecated
    public void setTipoCurso(final DegreeType degreeType) {
	setDegreeType(degreeType);
    }

    public DegreeType getDegreeType() {
	return super.getTipoCurso();
    }

    public void setDegreeType(final DegreeType degreeType) {
	super.setTipoCurso(degreeType);
    }

    @Deprecated
    public DegreeType getBolonhaDegreeType() {
	return getDegreeType();
    }

    public boolean isBolonhaDegree() {
	return getDegreeType().isBolonhaType();
    }

    public boolean isEmpty() {
	return false;
    }

    public boolean isDegreeOrBolonhaDegreeOrBolonhaIntegratedMasterDegree() {
	return getDegreeType().isDegreeOrBolonhaDegreeOrBolonhaIntegratedMasterDegree();
    }

    public List<DegreeCurricularPlan> findDegreeCurricularPlansByState(DegreeCurricularPlanState state) {
	List<DegreeCurricularPlan> result = new ArrayList<DegreeCurricularPlan>();
	if (!isBolonhaDegree()) {
	    for (DegreeCurricularPlan degreeCurricularPlan : this.getDegreeCurricularPlansSet()) {
		if (degreeCurricularPlan.getState().equals(state)) {
		    result.add(degreeCurricularPlan);
		}
	    }
	}
	return result;
    }

    public List<DegreeCurricularPlan> getActiveDegreeCurricularPlans() {
	List<DegreeCurricularPlan> result = new ArrayList<DegreeCurricularPlan>();

	for (DegreeCurricularPlan degreeCurricularPlan : getDegreeCurricularPlansSet()) {
	    if (degreeCurricularPlan.getState() == DegreeCurricularPlanState.ACTIVE) {
		result.add(degreeCurricularPlan);
	    }
	}

	return result;
    }

    public List<DegreeCurricularPlan> getPastDegreeCurricularPlans() {
	List<DegreeCurricularPlan> result = new ArrayList<DegreeCurricularPlan>();
	for (DegreeCurricularPlan degreeCurricularPlan : getDegreeCurricularPlansSet()) {
	    if (degreeCurricularPlan.getState() == DegreeCurricularPlanState.PAST) {
		result.add(degreeCurricularPlan);
	    }
	}
	return result;
    }

    public List<DegreeCurricularPlan> getDegreeCurricularPlansForYear(ExecutionYear year) {
	List<DegreeCurricularPlan> result = new ArrayList<DegreeCurricularPlan>();
	for (final DegreeCurricularPlan degreeCurricularPlan : getDegreeCurricularPlansSet()) {
	    if (degreeCurricularPlan.hasExecutionDegreeFor(year)) {
		result.add(degreeCurricularPlan);
	    }
	}
	return result;
    }

    public List<ExecutionDegree> getExecutionDegrees() {
	List<ExecutionDegree> result = new ArrayList<ExecutionDegree>();
	for (final DegreeCurricularPlan degreeCurricularPlan : getDegreeCurricularPlansSet()) {
	    result.addAll(degreeCurricularPlan.getExecutionDegrees());
	}
	return result;
    }

    public List<ExecutionDegree> getExecutionDegreesForExecutionYear(final ExecutionYear executionYear) {
	final List<ExecutionDegree> result = new ArrayList<ExecutionDegree>();
	for (final DegreeCurricularPlan degreeCurricularPlan : getDegreeCurricularPlansSet()) {
	    final ExecutionDegree executionDegree = degreeCurricularPlan.getExecutionDegreeByYear(executionYear);
	    if (executionDegree != null) {
		result.add(executionDegree);
	    }
	}
	return result;
    }

    public List<ExecutionYear> getDegreeCurricularPlansExecutionYears() {
	Set<ExecutionYear> result = new TreeSet<ExecutionYear>();
	for (final DegreeCurricularPlan degreeCurricularPlan : getDegreeCurricularPlansSet()) {
	    for (final ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegreesSet()) {
		result.add(executionDegree.getExecutionYear());
	    }
	}
	return new ArrayList<ExecutionYear>(result);
    }

    public List<CurricularCourse> getExecutedCurricularCoursesByExecutionYear(final ExecutionYear executionYear) {

	if (isBolonhaDegree()) {
	    return Collections.emptyList();
	}

	final List<CurricularCourse> result = new ArrayList<CurricularCourse>();
	for (final DegreeCurricularPlan degreeCurricularPlan : getDegreeCurricularPlansSet()) {
	    if (degreeCurricularPlan.getState().equals(DegreeCurricularPlanState.ACTIVE)) {
		for (final CurricularCourse course : degreeCurricularPlan.getCurricularCourses()) {
		    for (final ExecutionCourse executionCourse : course.getAssociatedExecutionCourses()) {
			if (executionCourse.getExecutionPeriod().getExecutionYear().equals(executionYear)) {
			    result.add(course);
			    break;
			}
		    }
		}
	    }
	}
	return result;
    }

    public List<CurricularCourse> getExecutedCurricularCoursesByExecutionYearAndYear(final ExecutionYear executionYear,
	    final Integer curricularYear) {

	if (isBolonhaDegree()) {
	    return Collections.emptyList();
	}

	final List<CurricularCourse> result = new ArrayList<CurricularCourse>();
	for (final DegreeCurricularPlan degreeCurricularPlan : getDegreeCurricularPlansSet()) {
	    if (degreeCurricularPlan.getState().equals(DegreeCurricularPlanState.ACTIVE)) {
		for (final CurricularCourse course : degreeCurricularPlan.getCurricularCourses()) {
		    xpto: for (final ExecutionCourse executionCourse : course.getAssociatedExecutionCourses()) {
			if (executionCourse.getExecutionPeriod().getExecutionYear().equals(executionYear)) {
			    for (final CurricularCourseScope curricularCourseScope : course.getScopes()) {
				if (curricularCourseScope.getCurricularSemester().getCurricularYear().getYear().equals(
					curricularYear)) {
				    result.add(course);
				    break xpto;
				}
			    }
			}
		    }
		}
	    }
	}
	return result;
    }

    public List<ExecutionCourse> getExecutionCourses(String curricularCourseAcronym, ExecutionSemester executionSemester) {
	final List<ExecutionCourse> result = new ArrayList<ExecutionCourse>();
	for (final DegreeCurricularPlan degreeCurricularPlan : getDegreeCurricularPlansSet()) {
	    for (final CurricularCourse course : degreeCurricularPlan.getCurricularCourses()) {
		if (course.getAcronym() != null && course.getAcronym().equalsIgnoreCase(curricularCourseAcronym)) {
		    for (final ExecutionCourse executionCourse : course.getAssociatedExecutionCourses()) {
			if (executionSemester == executionCourse.getExecutionPeriod()) {
			    result.add(executionCourse);
			}
		    }
		}
	    }
	}
	return result;
    }

    public List<ExecutionCourse> getExecutionCourses(final CurricularYear curricularYear,
	    final ExecutionSemester executionSemester) {
	final List<ExecutionCourse> result = new ArrayList<ExecutionCourse>();
	for (final DegreeCurricularPlan degreeCurricularPlan : getDegreeCurricularPlansSet()) {
	    for (final CurricularCourse course : degreeCurricularPlan.getCurricularCourses()) {
		for (final ExecutionCourse executionCourse : course.getAssociatedExecutionCourses()) {
		    if (executionSemester == executionCourse.getExecutionPeriod()) {
			for (final DegreeModuleScope scope : course.getDegreeModuleScopes()) {
			    if (scope.isActiveForExecutionPeriod(executionSemester)
				    && scope.getCurricularYear() == curricularYear.getYear()
				    && scope.getCurricularSemester() == executionSemester.getSemester()) {
				result.add(executionCourse);
			    }
			}
		    }
		}
	    }
	}
	return result;
    }

    public List<ExecutionCourse> getExecutionCourses(final Integer curricularYear, final ExecutionSemester executionSemester) {
	final List<ExecutionCourse> result = new ArrayList<ExecutionCourse>();
	for (final DegreeCurricularPlan degreeCurricularPlan : getDegreeCurricularPlansSet()) {
	    for (final CurricularCourse course : degreeCurricularPlan.getCurricularCourses()) {
		for (final ExecutionCourse executionCourse : course.getAssociatedExecutionCourses()) {
		    if (executionSemester == executionCourse.getExecutionPeriod()) {
			for (final DegreeModuleScope scope : course.getDegreeModuleScopes()) {
			    if (scope.isActiveForExecutionPeriod(executionSemester)
				    && scope.getCurricularYear() == curricularYear
				    && scope.getCurricularSemester() == executionSemester.getSemester()) {
				result.add(executionCourse);
			    }
			}
		    }
		}
	    }
	}
	return result;
    }

    @Deprecated
    public MultiLanguageString getNameFor(final ExecutionYear executionYear) {
	DegreeInfo degreeInfo = executionYear == null ? getMostRecentDegreeInfo() : getMostRecentDegreeInfo(executionYear);
	return degreeInfo == null ? MultiLanguageString.i18n().add("pt", super.getNome()).add("en", super.getNameEn()).finish()
		: degreeInfo.getName();
    }

    @Deprecated
    public MultiLanguageString getNameFor(final ExecutionSemester executionSemester) {
	return getNameFor(executionSemester != null ? executionSemester.getExecutionYear() : null);
    }

    public MultiLanguageString getNameFor(final AcademicInterval academicInterval) {
	DegreeInfo degreeInfo = academicInterval == null ? getMostRecentDegreeInfo() : getMostRecentDegreeInfo(academicInterval);
	return degreeInfo == null ? MultiLanguageString.i18n().add("pt", super.getNome()).add("en", super.getNameEn()).finish()
		: degreeInfo.getName();
    }

    @Override
    @Deprecated
    public String getNome() {
	return getName();
    }

    /**
     * @deprecated Use {@link #getNameFor(ExecutionYear)}
     */
    @Deprecated
    public String getName() {
	DegreeInfo degreeInfo = getMostRecentDegreeInfo();
	return degreeInfo == null ? StringUtils.EMPTY : degreeInfo.getName().getContent(Language.pt);
    }

    /**
     * @deprecated Use {@link #getNameFor(ExecutionYear)}
     */
    @Override
    @Deprecated
    public String getNameEn() {
	DegreeInfo degreeInfo = getMostRecentDegreeInfo();
	return degreeInfo == null ? StringUtils.EMPTY : degreeInfo.getName().getContent(Language.en);
    }

    final public MultiLanguageString getNameI18N() {
	return getNameFor(ExecutionYear.readCurrentExecutionYear());
    }

    final public MultiLanguageString getNameI18N(ExecutionYear executionYear) {
	return getNameFor(executionYear);
    }

    final public String getPresentationName() {
	return getPresentationName(ExecutionYear.readCurrentExecutionYear(), Language.getLocale());
    }

    public String getPresentationName(final ExecutionYear executionYear) {
	return getPresentationName(executionYear, Language.getLocale());
    }

    protected String getPresentationName(final ExecutionYear executionYear, final Locale locale) {
	final StringBuilder res = new StringBuilder();

	final String degreeType = getDegreeType().getLocalizedName(locale);
	if (!StringUtils.isEmpty(degreeType)) {
	    res.append(degreeType).append(StringUtils.SINGLE_SPACE);
	    res.append(ResourceBundle.getBundle("resources.ApplicationResources", locale).getString("label.in"));
	    res.append(StringUtils.SINGLE_SPACE);
	}

	final MultiLanguageString mls = getNameFor(executionYear);
	final Language language = Language.valueOf(locale.getLanguage());
	res.append(mls.hasContent(language) ? mls.getContent(language) : mls.getPreferedContent());

	return res.toString();
    }

    final public String getFilteredName() {
	return getFilteredName(ExecutionYear.readCurrentExecutionYear(), Language.getLocale());
    }

    final public String getFilteredName(final ExecutionYear executionYear) {
	return getFilteredName(executionYear, Language.getLocale());
    }

    public String getFilteredName(final ExecutionYear executionYear, final Locale locale) {
	final StringBuilder res = new StringBuilder(getNameFor(executionYear).getContent(Language.valueOf(locale.getLanguage())));

	for (final net.sourceforge.fenixedu.domain.space.Campus campus : Space.getAllCampus()) {
	    final String toRemove = " - " + campus.getName();
	    if (res.toString().contains(toRemove)) {
		res.replace(res.indexOf(toRemove), res.indexOf(toRemove) + toRemove.length(), StringUtils.EMPTY);
	    }
	}

	return res.toString();
    }

    public OldInquiriesCoursesRes getOldInquiriesCoursesResByCourseCodeAndExecutionPeriod(String code,
	    ExecutionSemester executionSemester) {
	for (OldInquiriesCoursesRes oldInquiriesCoursesRes : this.getAssociatedOldInquiriesCoursesRes()) {
	    if (oldInquiriesCoursesRes.getCourseCode().equalsIgnoreCase(code)
		    && oldInquiriesCoursesRes.getExecutionPeriod().equals(executionSemester)) {
		return oldInquiriesCoursesRes;
	    }
	}
	return null;
    }

    public List<OldInquiriesSummary> getOldInquiriesSummariesByExecutionPeriod(ExecutionSemester executionSemester) {
	List<OldInquiriesSummary> result = new ArrayList<OldInquiriesSummary>();
	for (OldInquiriesSummary oldInquiriesSummary : this.getAssociatedOldInquiriesSummaries()) {
	    if (oldInquiriesSummary.getExecutionPeriod().equals(executionSemester)) {
		result.add(oldInquiriesSummary);
	    }
	}
	return result;
    }

    public List<OldInquiriesTeachersRes> getOldInquiriesTeachersResByExecutionPeriodAndCurricularYearAndCourseCode(
	    ExecutionSemester executionSemester, Integer curricularYear, String courseCode) {
	List<OldInquiriesTeachersRes> result = new ArrayList<OldInquiriesTeachersRes>();
	for (OldInquiriesTeachersRes oldInquiriesTeachersRes : this.getAssociatedOldInquiriesTeachersRes()) {
	    if (oldInquiriesTeachersRes.getExecutionPeriod().equals(executionSemester)
		    && oldInquiriesTeachersRes.getCurricularYear().equals(curricularYear)
		    && oldInquiriesTeachersRes.getCourseCode().equalsIgnoreCase(courseCode)) {
		result.add(oldInquiriesTeachersRes);
	    }
	}
	return result;
    }

    public List<OldInquiriesTeachersRes> getOldInquiriesTeachersResByExecutionPeriodAndCurricularYearAndCourseCodeAndTeacher(
	    ExecutionSemester executionSemester, Integer curricularYear, String courseCode, Teacher teacher) {
	List<OldInquiriesTeachersRes> result = new ArrayList<OldInquiriesTeachersRes>();
	for (OldInquiriesTeachersRes oldInquiriesTeachersRes : this.getAssociatedOldInquiriesTeachersRes()) {
	    if (oldInquiriesTeachersRes.getExecutionPeriod().equals(executionSemester)
		    && oldInquiriesTeachersRes.getCurricularYear().equals(curricularYear)
		    && oldInquiriesTeachersRes.getCourseCode().equalsIgnoreCase(courseCode)
		    && oldInquiriesTeachersRes.getTeacher().equals(teacher)) {
		result.add(oldInquiriesTeachersRes);
	    }
	}
	return result;
    }

    public DegreeCurricularPlan getMostRecentDegreeCurricularPlan() {
	ExecutionDegree mostRecentExecutionDegree = null;
	boolean mustGetByInitialDate = false;

	for (final DegreeCurricularPlan degreeCurricularPlan : this.getActiveDegreeCurricularPlans()) {
	    final ExecutionDegree executionDegree = degreeCurricularPlan.getMostRecentExecutionDegree();
	    if (mostRecentExecutionDegree == null) {
		mostRecentExecutionDegree = executionDegree;
	    } else {
		if (mostRecentExecutionDegree.getExecutionYear().equals(executionDegree.getExecutionYear())) {
		    mustGetByInitialDate = true;
		} else if (mostRecentExecutionDegree.isBefore(executionDegree)) {
		    mustGetByInitialDate = false;
		    mostRecentExecutionDegree = executionDegree;
		}
	    }
	}

	if (mustGetByInitialDate) {
	    // investigate dcps initial dates
	    return getMostRecentDegreeCurricularPlanByInitialDate();
	} else {
	    return (mostRecentExecutionDegree != null) ? mostRecentExecutionDegree.getDegreeCurricularPlan() : null;
	}
    }

    private DegreeCurricularPlan getMostRecentDegreeCurricularPlanByInitialDate() {
	DegreeCurricularPlan mostRecentDegreeCurricularPlan = null;
	for (final DegreeCurricularPlan degreeCurricularPlan : this.getActiveDegreeCurricularPlans()) {
	    if (mostRecentDegreeCurricularPlan == null
		    || degreeCurricularPlan.getInitialDateYearMonthDay().isAfter(
			    mostRecentDegreeCurricularPlan.getInitialDateYearMonthDay())) {
		mostRecentDegreeCurricularPlan = degreeCurricularPlan;
	    }
	}
	return mostRecentDegreeCurricularPlan;
    }

    public Collection<Registration> getActiveRegistrations() {
	final Collection<Registration> result = new HashSet<Registration>();

	for (final DegreeCurricularPlan degreeCurricularPlan : getActiveDegreeCurricularPlans()) {
	    result.addAll(degreeCurricularPlan.getActiveRegistrations());
	}

	return result;
    }

    public DegreeCurricularPlan getFirstDegreeCurricularPlan() {
	if (getDegreeCurricularPlans().isEmpty()) {
	    return null;
	}

	DegreeCurricularPlan firstDCP = getDegreeCurricularPlans().get(0);
	for (final DegreeCurricularPlan degreeCurricularPlan : getDegreeCurricularPlans()) {
	    if (degreeCurricularPlan.getInitialDateYearMonthDay() == null) {
		continue;
	    }
	    if ((firstDCP.getInitialDateYearMonthDay() == null)
		    || (degreeCurricularPlan.getInitialDateYearMonthDay().isBefore(firstDCP.getInitialDateYearMonthDay()))) {
		firstDCP = degreeCurricularPlan;
	    }
	}
	return (firstDCP.getInitialDateYearMonthDay() == null) ? null : firstDCP;
    }

    public DegreeCurricularPlan getLastActiveDegreeCurricularPlan() {
	DegreeCurricularPlan result = null;
	ExecutionDegree mostRecentExecutionDegree = null;

	for (final DegreeCurricularPlan degreeCurricularPlan : getActiveDegreeCurricularPlans()) {
	    final ExecutionDegree executionDegree = degreeCurricularPlan.getMostRecentExecutionDegree();

	    if (executionDegree == null) {
		result = degreeCurricularPlan;
	    } else if (mostRecentExecutionDegree == null || mostRecentExecutionDegree.isBefore(executionDegree)) {
		mostRecentExecutionDegree = executionDegree;
	    }
	}

	return (mostRecentExecutionDegree == null) ? result : mostRecentExecutionDegree.getDegreeCurricularPlan();
    }

    // -------------------------------------------------------------
    // read static methods
    // -------------------------------------------------------------

    private static final Map<String, SoftReference<Degree>> degrees = new Hashtable<String, SoftReference<Degree>>();

    private static void loadCache() {
	synchronized (degrees) {
	    degrees.clear();
	    for (final Degree degree : Degree.readNotEmptyDegrees()) {
		degrees.put(degree.getSigla().toLowerCase(), new SoftReference<Degree>(degree));
	    }
	}
    }

    private static void updateCache(final Degree degree, final String newLowerCaseSigla) {
	final String currentLowerCaseSigla = (degree.getSigla() != null) ? degree.getSigla().toLowerCase() : StringUtils.EMPTY;
	synchronized (degrees) {
	    degrees.remove(currentLowerCaseSigla);
	    degrees.put(newLowerCaseSigla, new SoftReference<Degree>(degree));
	}
    }

    @Override
    public void setSigla(final String sigla) {
	updateCache(this, sigla.toLowerCase());
	super.setSigla(sigla);
    }

    public static Degree readBySigla(final String sigla) {
	if (degrees.isEmpty()) {
	    loadCache();
	}
	final String lowerCaseString = sigla.toLowerCase();
	final SoftReference<Degree> degreeReference = degrees.get(lowerCaseString);
	if (degreeReference != null) {
	    final Degree degree = degreeReference.get();
	    if (degree != null && degree.getRootDomainObject() == RootDomainObject.getInstance()
		    && degree.getSigla().equalsIgnoreCase(lowerCaseString)) {
		return degree;
	    } else {
		loadCache();
		final SoftReference<Degree> otherDegreeReference = degrees.get(lowerCaseString);
		if (otherDegreeReference != null) {
		    final Degree otherDegree = otherDegreeReference.get();
		    if (otherDegree != null && otherDegree.getRootDomainObject() == RootDomainObject.getInstance()
			    && otherDegree.getSigla().equalsIgnoreCase(lowerCaseString)) {
			return otherDegree;
		    }
		}
	    }
	}

	return null;
    }

    public static List<Degree> readNotEmptyDegrees() {
	final List<Degree> result = new ArrayList<Degree>(RootDomainObject.getInstance().getDegrees());
	result.remove(readEmptyDegree());
	return result;
    }

    public static Degree readEmptyDegree() {
	return EmptyDegree.getInstance();
    }

    public static List<Degree> readOldDegrees() {
	List<Degree> result = new ArrayList<Degree>();
	for (final Degree degree : Degree.readNotEmptyDegrees()) {
	    if (!degree.isBolonhaDegree()) {
		result.add(degree);
	    }
	}
	return result;
    }

    public static List<Degree> readBolonhaDegrees() {
	List<Degree> result = new ArrayList<Degree>();
	for (final Degree degree : Degree.readNotEmptyDegrees()) {
	    if (degree.isBolonhaDegree()) {
		result.add(degree);
	    }
	}
	return result;
    }

    public static List<Degree> readBolonhaDegrees(final AdministrativeOfficeType type) {
	final List<Degree> result = new ArrayList<Degree>();
	for (final Degree degree : Degree.readNotEmptyDegrees()) {
	    if (degree.isBolonhaDegree() && degree.getDegreeType().getAdministrativeOfficeType() == type) {
		result.add(degree);
	    }
	}
	return result;
    }

    public static List<Degree> readAllByDegreeType(final DegreeType... degreeTypes) {
	final List<DegreeType> degreeTypesList = Arrays.asList(degreeTypes);
	final List<Degree> result = new ArrayList<Degree>();
	for (final Degree degree : Degree.readNotEmptyDegrees()) {
	    if (degree.getDegreeType() != null && degreeTypesList.contains(degree.getDegreeType())) {
		result.add(degree);
	    }
	}
	return result;
    }

    public static List<Degree> readAllByDegreeTypes(final Set<DegreeType> degreeTypes) {
	return readAllByDegreeType(degreeTypes.toArray(new DegreeType[degreeTypes.size()]));
    }

    public static List<Degree> readAllByDegreeCode(final String degreeCode) {
	final List<Degree> result = new ArrayList<Degree>();
	for (final Degree degree : Degree.readNotEmptyDegrees()) {
	    if (degree.hasMinistryCode() && degree.getMinistryCode().equals(degreeCode)) {
		result.add(degree);
	    }
	}

	return result;
    }

    private boolean hasMinistryCode() {
	return getMinistryCode() != null;
    }

    public MultiLanguageString getQualificationLevel(final ExecutionYear executionYear) {
	return getMostRecentDegreeInfo(executionYear).getQualificationLevel();
    }

    public MultiLanguageString getProfessionalExits(final ExecutionYear executionYear) {
	return getMostRecentDegreeInfo(executionYear).getProfessionalExits();
    }

    public DegreeInfo getMostRecentDegreeInfo() {
	return getMostRecentDegreeInfo(ExecutionYear.readCurrentExecutionYear());
    }

    public DegreeInfo getDegreeInfoFor(ExecutionYear executionYear) {
	return executionYear.getDegreeInfo(this);
    }

    @Deprecated
    public DegreeInfo getMostRecentDegreeInfo(ExecutionYear executionYear) {
	DegreeInfo result = null;
	for (final DegreeInfo degreeInfo : getDegreeInfosSet()) {
	    final ExecutionYear executionYear2 = degreeInfo.getExecutionYear();
	    if (executionYear == executionYear2) {
		return degreeInfo;
	    }
	    if (executionYear2.isBefore(executionYear)) {
		if (result == null || executionYear2.isAfter(result.getExecutionYear())) {
		    result = degreeInfo;
		}
	    }
	}
	return result;
    }

    public DegreeInfo getMostRecentDegreeInfo(AcademicInterval academicInterval) {
	DegreeInfo result = null;
	for (final DegreeInfo degreeInfo : getDegreeInfosSet()) {
	    AcademicInterval academicInterval2 = degreeInfo.getAcademicInterval();
	    if (academicInterval.equals(academicInterval2))
		return degreeInfo;

	    if (academicInterval2.isBefore(academicInterval)) {
		if (result == null || academicInterval2.isAfter(result.getAcademicInterval()))
		    result = degreeInfo;
	    }
	}
	return result;
    }

    private DegreeInfo createCurrentDegreeInfo(ExecutionYear executionYear) {
	// first let's check if the current degree info exists already
	final DegreeInfo shouldBeThisOne = executionYear.getDegreeInfo(this);
	if (shouldBeThisOne != null) {
	    return shouldBeThisOne;
	}

	// ok, so let's create a new one based on the most recent one, if
	// existing
	return tryCreateUsingMostRecentInfo(executionYear);
    }

    private DegreeInfo tryCreateUsingMostRecentInfo(final ExecutionYear executionYear) {
	final DegreeInfo mostRecentDegreeInfo = getMostRecentDegreeInfo(executionYear);
	return (mostRecentDegreeInfo != null) ? new DegreeInfo(mostRecentDegreeInfo, executionYear) : new DegreeInfo(this,
		executionYear);
    }

    public DegreeInfo createCurrentDegreeInfo() {
	return createCurrentDegreeInfo(ExecutionYear.readCurrentExecutionYear());
    }

    public List<Integer> buildFullCurricularYearList() {
	final List<Integer> result = new ArrayList<Integer>();
	for (int i = 1; i <= this.getDegreeType().getYears(); i++) {
	    result.add(i);
	}
	return result;
    }

    public ScientificCommission getMostRecentScientificCommission(Person person) {
	for (ExecutionYear ey = ExecutionYear.readCurrentExecutionYear(); ey != null; ey = ey.getPreviousExecutionYear()) {
	    for (ScientificCommission member : getScientificCommissionMembers(ey)) {
		if (member.getPerson() == person) {
		    return member;
		}
	    }
	}

	return null;
    }

    public boolean isMemberOfAnyScientificCommission(Person person) {
	return getMostRecentScientificCommission(person) != null;
    }

    public boolean isMemberOfCurrentScientificCommission(Person person) {
	for (ScientificCommission member : getCurrentScientificCommissionMembers()) {
	    if (member.getPerson() == person) {
		return true;
	    }
	}

	return false;
    }

    public boolean isMemberOfCurrentScientificCommission(final Person person, final ExecutionYear executionYear) {
	if (person != null) {
	    for (ScientificCommission member : getScientificCommissionMembers(executionYear)) {
		if (member.getPerson() == person) {
		    return true;
		}
	    }
	}
	return false;
    }

    public Collection<ScientificCommission> getCurrentScientificCommissionMembers() {
	for (ExecutionYear ey = ExecutionYear.readCurrentExecutionYear(); ey != null; ey = ey.getPreviousExecutionYear()) {
	    Collection<ScientificCommission> members = getScientificCommissionMembers(ey);

	    if (!members.isEmpty()) {
		return members;
	    }
	}

	return Collections.emptyList();
    }

    public Collection<ScientificCommission> getScientificCommissionMembers(ExecutionYear executionYear) {
	for (DegreeCurricularPlan degreeCurricularPlan : getDegreeCurricularPlansForYear(executionYear)) {
	    ExecutionDegree executionDegree = degreeCurricularPlan.getExecutionDegreeByYear(executionYear);

	    if (executionDegree != null) {
		return new ArrayList<ScientificCommission>(executionDegree.getScientificCommissionMembers());
	    }
	}

	return Collections.emptyList();
    }

    public boolean isCoordinator(final Person person, final ExecutionYear executionYear) {
	for (final Coordinator coordinator : getCoordinators(executionYear, false)) {
	    if (coordinator.getPerson() == person) {
		return true;
	    }
	}

	return false;
    }

    final public boolean isCurrentCoordinator(final Person person) {
	for (final Coordinator coordinator : getCurrentCoordinators(false)) {
	    if (coordinator.getPerson() == person) {
		return true;
	    }
	}

	return false;
    }

    /**
     * Verifies if the given person was a coordinator for this degree regardless
     * of the execution year.
     * 
     * @param person
     *            the person to check
     * @return <code>true</code> if the person was a coordinator for a certain
     *         execution degree
     */
    final public boolean isCoordinatorInSomeExecutionYear(final Person person) {
	for (Coordinator coordinator : person.getCoordinators()) {
	    if (coordinator.getExecutionDegree().getDegree() == this) {
		return true;
	    }
	}

	return false;
    }

    final private Collection<Coordinator> getCoordinators(final ExecutionYear executionYear, final boolean responsible) {
	final Collection<Coordinator> result = new HashSet<Coordinator>();

	for (final DegreeCurricularPlan degreeCurricularPlan : getDegreeCurricularPlansSet()) {
	    final ExecutionDegree executionDegree = degreeCurricularPlan.getExecutionDegreeByYear(executionYear);
	    if (executionDegree != null) {
		result.addAll(responsible ? executionDegree.getResponsibleCoordinators() : executionDegree.getCoordinatorsList());
	    }
	}

	return result;
    }

    final private Collection<Coordinator> getCurrentCoordinators(final boolean responsible) {
	SortedSet<ExecutionYear> years = new TreeSet<ExecutionYear>(new ReverseComparator(ExecutionYear.COMPARATOR_BY_YEAR));
	years.addAll(getDegreeCurricularPlansExecutionYears());

	ExecutionYear current = ExecutionYear.readCurrentExecutionYear();
	for (ExecutionYear year : years) {
	    if (year.isAfter(current)) {
		continue;
	    }

	    Collection<Coordinator> coordinators = getCoordinators(year, responsible);
	    if (!coordinators.isEmpty()) {
		return coordinators;
	    }
	}

	return Collections.emptyList();
    }

    public Collection<Coordinator> getResponsibleCoordinators(final ExecutionYear executionYear) {
	return getCoordinators(executionYear, true);
    }

    public Collection<Coordinator> getCurrentCoordinators() {
	return getCurrentCoordinators(false);
    }

    public Collection<Coordinator> getCurrentResponsibleCoordinators() {
	return getCurrentCoordinators(true);
    }

    public Collection<Teacher> getResponsibleCoordinatorsTeachers(final ExecutionYear executionYear) {
	final Collection<Teacher> result = new TreeSet<Teacher>(Teacher.TEACHER_COMPARATOR_BY_CATEGORY_AND_NUMBER);

	collectCoordinatorsTeachers(result, getResponsibleCoordinators(executionYear));

	return result;
    }

    public Collection<Teacher> getCurrentResponsibleCoordinatorsTeachers() {
	final Collection<Teacher> result = new TreeSet<Teacher>(Teacher.TEACHER_COMPARATOR_BY_CATEGORY_AND_NUMBER);

	collectCoordinatorsTeachers(result, getCurrentResponsibleCoordinators());

	return result;
    }

    final private void collectCoordinatorsTeachers(final Collection<Teacher> result, final Collection<Coordinator> coordinators) {
	for (final Coordinator coordinator : coordinators) {
	    final Teacher teacher = coordinator.getTeacher();
	    if (teacher != null) {
		result.add(teacher);
	    }
	}
    }

    public Collection<Campus> getCampus(ExecutionYear executionYear) {
	Set<Campus> result = new HashSet<Campus>();
	for (final DegreeCurricularPlan degreeCurricularPlan : getDegreeCurricularPlansSet()) {
	    final ExecutionDegree executionDegree = degreeCurricularPlan.getExecutionDegreeByYear(executionYear);
	    if (executionDegree != null && executionDegree.hasCampus()) {
		result.add(executionDegree.getCampus());
	    }
	}
	return new ArrayList<Campus>(result);
    }

    public Collection<Campus> getCurrentCampus() {
	ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
	Collection<Campus> result = this.getCampus(executionYear);

	if (!result.isEmpty()) {
	    return result;
	}

	for (; executionYear != null; executionYear = executionYear.getNextExecutionYear()) {
	    result = this.getCampus(executionYear);

	    if (!result.isEmpty()) {
		return result;
	    }
	}

	return new ArrayList<Campus>();
    }

    public String constructSchoolClassPrefix(final Integer curricularYear) {
	return isBolonhaDegree() ? StringAppender.append(getSigla(), "0", curricularYear.toString()) : StringUtils.EMPTY;
    }

    public List<StudentCurricularPlan> getLastStudentCurricularPlans() {
	final List<StudentCurricularPlan> result = new ArrayList<StudentCurricularPlan>();

	for (final DegreeCurricularPlan degreeCurricularPlan : this.getDegreeCurricularPlansSet()) {
	    for (final StudentCurricularPlan studentCurricularPlan : degreeCurricularPlan.getStudentCurricularPlans()) {
		if (studentCurricularPlan.getRegistration() == null) {
		    continue;
		}

		if (!result.contains(studentCurricularPlan)) {

		    result.add(studentCurricularPlan.getRegistration().getLastStudentDegreeCurricularPlansByDegree(this));
		}
	    }
	}

	return new ArrayList<StudentCurricularPlan>(result);
    }

    public List<StudentCurricularPlan> getStudentCurricularPlans(ExecutionYear executionYear) {
	List<StudentCurricularPlan> result = new ArrayList<StudentCurricularPlan>();
	for (final DegreeCurricularPlan degreeCurricularPlan : getDegreeCurricularPlansForYear(executionYear)) {
	    degreeCurricularPlan.getStudentsCurricularPlans(executionYear, result);
	}
	return result;
    }

    public boolean isFirstCycle() {
	return getDegreeType().isFirstCycle();
    }

    public boolean isSecondCycle() {
	return getDegreeType().isSecondCycle();
    }

    public boolean isAnyPublishedThesisAvailable() {
	for (DegreeCurricularPlan dcp : getDegreeCurricularPlans()) {
	    for (CurricularCourse curricularCourse : dcp.getDissertationCurricularCourses(null)) {
		List<IEnrolment> enrolments = new ArrayList<IEnrolment>();

		for (CurriculumModule module : curricularCourse.getCurriculumModules()) {
		    if (module.isEnrolment()) {
			enrolments.add((IEnrolment) module);
		    } else if (module.isDismissal()) {
			Dismissal dismissal = (Dismissal) module;

			enrolments.addAll(dismissal.getSourceIEnrolments());
		    }
		}

		for (IEnrolment enrolment : enrolments) {
		    Thesis thesis = enrolment.getThesis();

		    if (thesis != null && thesis.hasPublication()) {
			return true;
		    }
		}
	    }
	}

	return false;
    }

    public boolean isAnyThesisAvailable() {
	return hasAnyThesis();
    }

    /*
     * DELEGATE ELECTIONS
     */
    public List<YearDelegateElection> getYearDelegateElectionsGivenExecutionYear(ExecutionYear executionYear) {
	List<YearDelegateElection> elections = new ArrayList<YearDelegateElection>();
	for (DelegateElection election : this.getDelegateElections()) {
	    if (election instanceof YearDelegateElection && election.getExecutionYear().equals(executionYear)) {
		elections.add((YearDelegateElection) election);
	    }
	}
	return elections;
    }

    public List<YearDelegateElection> getYearDelegateElectionsGivenExecutionYearAndCurricularYear(ExecutionYear executionYear,
	    CurricularYear curricularYear) {
	List<YearDelegateElection> elections = new ArrayList<YearDelegateElection>();
	for (DelegateElection election : this.getDelegateElections()) {
	    YearDelegateElection yearDelegateElection = (YearDelegateElection) election;
	    if (yearDelegateElection.getExecutionYear().equals(executionYear)
		    && yearDelegateElection.getCurricularYear().equals(curricularYear)) {
		elections.add(yearDelegateElection);
	    }
	}
	return elections;
    }

    public YearDelegateElection getYearDelegateElectionWithLastCandidacyPeriod(ExecutionYear executionYear,
	    CurricularYear curricularYear) {
	List<YearDelegateElection> elections = getYearDelegateElectionsGivenExecutionYearAndCurricularYear(executionYear,
		curricularYear);

	YearDelegateElection lastYearDelegateElection = null;
	if (!elections.isEmpty()) {
	    lastYearDelegateElection = Collections.max(elections, DelegateElection.ELECTION_COMPARATOR_BY_CANDIDACY_START_DATE);
	}
	return lastYearDelegateElection;
    }

    public YearDelegateElection getYearDelegateElectionWithLastVotingPeriod(ExecutionYear executionYear,
	    CurricularYear curricularYear) {
	List<YearDelegateElection> elections = getYearDelegateElectionsGivenExecutionYearAndCurricularYear(executionYear,
		curricularYear);

	YearDelegateElection lastYearDelegateElection = null;
	if (!elections.isEmpty()) {
	    lastYearDelegateElection = Collections.max(elections,
		    DelegateElection.ELECTION_COMPARATOR_BY_VOTING_START_DATE_AND_CANDIDACY_START_DATE);
	}
	return lastYearDelegateElection;
    }

    /*
     * ACTIVE DELEGATES
     */
    public List<Student> getAllActiveDelegates() {
	List<Student> result = new ArrayList<Student>();
	for (FunctionType functionType : FunctionType.getAllDelegateFunctionTypes()) {
	    result.addAll(getAllActiveDelegatesByFunctionType(functionType, null));
	}
	return result;
    }

    public List<Student> getAllActiveYearDelegates() {
	return getAllActiveDelegatesByFunctionType(FunctionType.DELEGATE_OF_YEAR, null);
    }

    public Student getActiveYearDelegateByCurricularYear(CurricularYear curricularYear) {
	if (getUnit() == null) {
	    return null;
	}

	final PersonFunction delegateFunction = getUnit().getActiveYearDelegatePersonFunctionByCurricularYear(curricularYear);
	return (delegateFunction != null ? delegateFunction.getPerson().getStudent() : null);
    }

    public List<Student> getAllActiveDelegatesByFunctionType(FunctionType functionType, ExecutionYear executionYear) {
	List<Student> result = new ArrayList<Student>();
	if (getUnit() != null) {
	    final List<PersonFunction> delegateFunctions = getUnit().getAllActiveDelegatePersonFunctionsByFunctionType(
		    functionType, executionYear);
	    for (PersonFunction delegateFunction : delegateFunctions) {
		result.add(delegateFunction.getPerson().getStudent());
	    }
	}
	return result;
    }

    public boolean hasActiveDelegateFunctionForStudent(Student student, ExecutionYear executionYear,
	    FunctionType delegateFunctionType) {
	List<Student> delegates = getAllActiveDelegatesByFunctionType(delegateFunctionType, executionYear);
	for (Student delegate : delegates) {
	    if (delegate.equals(student)) {
		return true;
	    }
	}
	return false;
    }

    public boolean hasAnyActiveDelegateFunctionForStudent(Student student) {
	List<Student> delegates = getAllActiveDelegates();
	for (Student delegate : delegates) {
	    if (delegate.equals(student)) {
		return true;
	    }
	}
	return false;
    }

    public PersonFunction getActiveDelegatePersonFunctionByStudentAndFunctionType(Student student, ExecutionYear executionYear,
	    FunctionType functionType) {
	if (getUnit() != null) {
	    for (PersonFunction personFunction : getUnit().getAllActiveDelegatePersonFunctionsByFunctionType(functionType,
		    executionYear)) {
		if (personFunction.getPerson().getStudent().equals(student)) {
		    return personFunction;
		}
	    }
	}
	return null;
    }

    /*
     * DELEGATES FROM GIVEN EXECUTION YEAR (PAST DELEGATES)
     */
    public Student getYearDelegateByExecutionYearAndCurricularYear(ExecutionYear executionYear, CurricularYear curricularYear) {
	if (getUnit() == null) {
	    return null;
	}

	final PersonFunction delegateFunction = getUnit().getYearDelegatePersonFunctionByExecutionYearAndCurricularYear(
		executionYear, curricularYear);
	return (delegateFunction != null ? delegateFunction.getPerson().getStudent() : null);
    }

    public List<Student> getAllDelegatesByExecutionYearAndFunctionType(ExecutionYear executionYear, FunctionType functionType) {
	List<Student> result = new ArrayList<Student>();
	if (getUnit() != null) {
	    final List<PersonFunction> delegateFunctions = getUnit().getAllDelegatePersonFunctionsByExecutionYearAndFunctionType(
		    executionYear, functionType);
	    for (PersonFunction delegateFunction : delegateFunctions) {
		if (delegateFunction.belongsToPeriod(executionYear.getBeginDateYearMonthDay(), executionYear
			.getEndDateYearMonthDay())) {
		    result.add(delegateFunction.getPerson().getStudent());
		}
	    }
	}
	return result;
    }

    public List<PersonFunction> getAllDelegatePersonFunctionsByStudentAndFunctionType(Student student, FunctionType functionType) {
	if (getUnit() == null) {
	    return Collections.EMPTY_LIST;
	}
	return getUnit().getAllDelegatePersonFunctionsByFunctionType(functionType);
    }

    /*
     * STUDENTS FROM DEGREE
     */
    public List<Student> getAllStudents() {
	List<Student> result = new ArrayList<Student>();
	for (Registration registration : getActiveRegistrations()) {
	    result.add(registration.getStudent());
	}
	return result;
    }

    public List<Student> getStudentsFromGivenCurricularYear(int curricularYear, ExecutionYear executionYear) {
	List<Student> result = new ArrayList<Student>();
	for (Registration registration : getActiveRegistrations()) {
	    if (registration.getCurricularYear(executionYear) == curricularYear) {
		result.add(registration.getStudent());
	    }
	}
	return result;
    }

    /*
     * This method is directed to Bolonha Integrated Master Degrees
     */
    public List<Student> getSecondCycleStudents(ExecutionYear executionYear) {
	List<Student> result = new ArrayList<Student>();
	if (getDegreeType() == DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE) {
	    for (Registration registration : getActiveRegistrations()) {
		final int studentCurricularYear = registration.getCurricularYear(executionYear);

		// TODO how to make this not hardcoded?
		if (studentCurricularYear >= 4 && studentCurricularYear <= 5) {
		    result.add(registration.getStudent());
		}
	    }
	}
	return result;
    }

    /*
     * This method is directed to Bolonha Integrated Master Degrees
     */
    public List<Student> getFirstCycleStudents(ExecutionYear executionYear) {
	List<Student> result = new ArrayList<Student>();
	if (getDegreeType() == DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE) {
	    for (Registration registration : getActiveRegistrations()) {
		final int studentCurricularYear = registration.getCurricularYear(executionYear);

		// TODO how to make this not hardcoded?
		if (studentCurricularYear >= 1 && studentCurricularYear <= 3) {
		    result.add(registration.getStudent());
		}
	    }
	}
	return result;
    }

    /*
     * CURRICULAR COURSES FROM DEGREE
     */
    public Set<CurricularCourse> getAllCurricularCourses(ExecutionYear executionYear) {
	Set<CurricularCourse> result = new HashSet<CurricularCourse>();
	for (DegreeCurricularPlan dcp : getActiveDegreeCurricularPlans()) {
	    result.addAll(dcp.getCurricularCoursesWithExecutionIn(executionYear));
	}
	return result;
    }

    public Set<CurricularCourse> getCurricularCoursesFromGivenCurricularYear(int curricularYear, ExecutionYear executionYear) {
	Set<CurricularCourse> result = new HashSet<CurricularCourse>();
	for (DegreeCurricularPlan dcp : getActiveDegreeCurricularPlans()) {
	    result.addAll(dcp.getCurricularCoursesByExecutionYearAndCurricularYear(executionYear, curricularYear));
	}
	return result;
    }

    /*
     * This method is directed to Bolonha Integrated Master Degrees
     */
    public Set<CurricularCourse> getFirstCycleCurricularCourses(ExecutionYear executionYear) {
	Set<CurricularCourse> result = new HashSet<CurricularCourse>();
	for (DegreeCurricularPlan dcp : getActiveDegreeCurricularPlans()) {
	    // TODO how to make this nothardcoded?
	    for (int i = 1; i <= 3; i++) {
		result.addAll(dcp.getCurricularCoursesByExecutionYearAndCurricularYear(executionYear, i));
	    }
	}
	return result;
    }

    /*
     * This method is directed to Bolonha Integrated Master Degrees
     */
    public Set<CurricularCourse> getSecondCycleCurricularCourses(ExecutionYear executionYear) {
	Set<CurricularCourse> result = new HashSet<CurricularCourse>();
	for (DegreeCurricularPlan dcp : getActiveDegreeCurricularPlans()) {
	    for (int i = 4; i <= 5; i++) { // TODO: how to make this not
		// hardcoded?
		result.addAll(dcp.getCurricularCoursesByExecutionYearAndCurricularYear(executionYear, i));
	    }
	}
	return result;
    }

    public static List<IGroup> getDegreesDelegatesGroupByDegreeType(DegreeType degreeType) {
	List<IGroup> result = new ArrayList<IGroup>();

	List<Degree> degrees = Degree.readAllByDegreeType(degreeType);
	for (Degree degree : degrees) {
	    result.add(new DelegatesGroup(degree));
	}

	return result;
    }

    /**
     * @return <code>true</code> if any of the thesis associated with this
     *         degree is not final
     */
    public boolean hasPendingThesis() {
	for (Thesis thesis : getThesis()) {
	    if (!thesis.isFinalThesis()) {
		return true;
	    }
	}

	return false;
    }

    @Override
    public Double getEctsCredits() {
	final Double ectsCredits = super.getEctsCredits();
	return (ectsCredits != null) ? ectsCredits : getDegreeType().getDefaultEctsCredits();
    }

    public boolean hasEctsCredits() {
	return super.getEctsCredits() != null;
    }

    @Override
    public String getMinistryCode() {
	final String ministryCode = super.getMinistryCode();
	if (!StringUtils.isEmpty(ministryCode)) {
	    return ministryCode;
	}

	return DEFAULT_MINISTRY_CODE;
    }

    @Override
    public void setMinistryCode(final String ministryCode) {
	super.setMinistryCode(ministryCode == null || ministryCode.length() == 0 ? null : ministryCode);
    }

    @Override
    public void setIdCardName(final String idCardName) {
	super.setIdCardName(idCardName.toUpperCase());
    }

    @Override
    public void setNome(final String nome) {
	super.setNome(nome);
	setIdCardName(nome);
    }

    public boolean isActive() {
	ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
	for (DegreeCurricularPlan curricularPlan : getDegreeCurricularPlans()) {
	    if (curricularPlan.getExecutionDegreeByYear(currentExecutionYear) != null) {
		return true;
	    }
	}
	return false;
    }

    public PersonFunction getMostSignificantDelegateFunctionForStudent(Student student, ExecutionYear executionYear) {
	if (hasActiveDelegateFunctionForStudent(student, executionYear, FunctionType.DELEGATE_OF_INTEGRATED_MASTER_DEGREE)) {
	    return getActiveDelegatePersonFunctionByStudentAndFunctionType(student, executionYear,
		    FunctionType.DELEGATE_OF_INTEGRATED_MASTER_DEGREE);
	} else if (hasActiveDelegateFunctionForStudent(student, executionYear, FunctionType.DELEGATE_OF_MASTER_DEGREE)) {
	    return getActiveDelegatePersonFunctionByStudentAndFunctionType(student, executionYear,
		    FunctionType.DELEGATE_OF_MASTER_DEGREE);
	} else if (hasActiveDelegateFunctionForStudent(student, executionYear, FunctionType.DELEGATE_OF_DEGREE)) {
	    return getActiveDelegatePersonFunctionByStudentAndFunctionType(student, executionYear,
		    FunctionType.DELEGATE_OF_DEGREE);
	} else {
	    return getActiveDelegatePersonFunctionByStudentAndFunctionType(student, executionYear, FunctionType.DELEGATE_OF_YEAR);
	}
    }

    public boolean canCreateGratuityEvent() {
	return true;
    }

    public boolean isDEA() {
	return getDegreeType() == DegreeType.BOLONHA_ADVANCED_SPECIALIZATION_DIPLOMA;
    }

    public DegreeOfficialPublication getOfficialPublication(DateTime when) {
	DegreeOfficialPublication found = null;
	for (DegreeOfficialPublication publication : getOfficialPublicationSet()) {
	    DateTime publicationDate = publication.getPublication().toDateTimeAtStartOfDay();
	    if (found == null && publicationDate.isBefore(when)) {
		found = publication;
	    } else if (found != null && publicationDate.isBefore(when)
		    && publication.getPublication().isAfter(found.getPublication())) {
		found = publication;
	    }
	}
	return found;
    }
}
