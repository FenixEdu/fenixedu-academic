package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.accessControl.Checked;
import net.sourceforge.fenixedu.applicationTier.strategy.degreeCurricularPlan.DegreeCurricularPlanStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.degreeCurricularPlan.IDegreeCurricularPlanStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.degreeCurricularPlan.strategys.IDegreeCurricularPlanStrategy;
import net.sourceforge.fenixedu.dataTransferObject.CurricularPeriodInfoDTO;
import net.sourceforge.fenixedu.dataTransferObject.ExecutionCourseView;
import net.sourceforge.fenixedu.domain.accessControl.FixedSetGroup;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.branch.BranchType;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriodType;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.IEnrollmentRule;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.MaximumNumberEctsCreditsEnrolmentRule;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.PrecedencesEnrollmentRule;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.PreviousYearsCurricularCourseEnrollmentRule;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.degreeStructure.RegimeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.tools.enrollment.AreaType;
import net.sourceforge.fenixedu.util.DateFormatUtil;
import net.sourceforge.fenixedu.util.MarkType;
import net.sourceforge.fenixedu.util.PeriodState;
import net.sourceforge.fenixedu.util.SituationName;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.joda.time.YearMonthDay;

public class DegreeCurricularPlan extends DegreeCurricularPlan_Base {

    /**
         * This might look a strange comparator, but the idea is to show a list
         * of degree curricular plans according to, in the following order: 1.
         * It's degree type 2. Reverse order of ExecutionDegrees 3. It's degree
         * code (in order to roughly order them by prebolonha/bolonha) OR
         * reverse order of their own name
         * 
         * For an example, see the coordinator's portal.
         */
    public static final Comparator DEGREE_CURRICULAR_PLAN_COMPARATOR_BY_DEGREE_TYPE_AND_EXECUTION_DEGREE_AND_DEGREE_CODE = new Comparator<DegreeCurricularPlan>() {

	public int compare(DegreeCurricularPlan degreeCurricularPlan1,
		DegreeCurricularPlan degreeCurricularPlan2) {
	    final int degreeTypeCompare = degreeCurricularPlan1.getDegree().getDegreeType().toString()
		    .compareTo(degreeCurricularPlan2.getDegree().getDegreeType().toString());
	    if (degreeTypeCompare != 0) {
		return degreeTypeCompare;
	    }

	    int finalCompare = degreeCurricularPlan1.getDegree().getSigla().compareTo(
		    degreeCurricularPlan2.getDegree().getSigla());
	    if (finalCompare == 0) {
		finalCompare = degreeCurricularPlan2.getName()
			.compareTo(degreeCurricularPlan1.getName());
	    }

	    ExecutionDegree mostRecentExecutionDegree1 = degreeCurricularPlan1
		    .getMostRecentExecutionDegree();
	    ExecutionDegree mostRecentExecutionDegree2 = degreeCurricularPlan2
		    .getMostRecentExecutionDegree();

	    if (mostRecentExecutionDegree1 == null && mostRecentExecutionDegree2 == null) {
		return finalCompare;
	    } else if (mostRecentExecutionDegree1 == null) {
		return 1;
	    } else if (mostRecentExecutionDegree2 == null) {
		return -1;
	    } else {
		final int executionDegreeCompare = mostRecentExecutionDegree2
			.compareTo(mostRecentExecutionDegree1);

		return executionDegreeCompare == 0 ? finalCompare : executionDegreeCompare;
	    }
	}

    };

    public DegreeCurricularPlan() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());

	super.setOjbConcreteClass(getClass().getName());
    }

    private DegreeCurricularPlan(Degree degree, String name, GradeScale gradeScale) {
	this();

	if (degree == null) {
	    throw new DomainException("degreeCurricularPlan.degree.not.null");
	}
	super.setDegree(degree);

	if (name == null) {
	    throw new DomainException("degreeCurricularPlan.name.not.null");
	}
	super.setName(name);

	super.setGradeScale(gradeScale);
    }

    protected DegreeCurricularPlan(Degree degree, String name, DegreeCurricularPlanState state,
	    Date inicialDate, Date endDate, Integer degreeDuration,
	    Integer minimalYearForOptionalCourses, Double neededCredits, MarkType markType,
	    Integer numerusClausus, String annotation, GradeScale gradeScale) {

	this(degree, name, gradeScale);
	super.setCurricularStage(CurricularStage.OLD);
	this
		.setConcreteClassForStudentCurricularPlans(degree
			.getConcreteClassForDegreeCurricularPlans());
	super.setState(state);

	oldStructureFieldsChange(inicialDate, endDate, degreeDuration, minimalYearForOptionalCourses,
		neededCredits, markType, numerusClausus, annotation);
    }

    private void oldStructureFieldsChange(Date inicialDate, Date endDate, Integer degreeDuration,
	    Integer minimalYearForOptionalCourses, Double neededCredits, MarkType markType,
	    Integer numerusClausus, String annotation) {

	if (inicialDate == null) {
	    throw new DomainException("degreeCurricularPlan.inicialDate.not.null");
	} else if (degreeDuration == null) {
	    throw new DomainException("degreeCurricularPlan.degreeDuration.not.null");
	} else if (minimalYearForOptionalCourses == null) {
	    throw new DomainException("degreeCurricularPlan.minimalYearForOptionalCourses.not.null");
	}

	this.setInitialDate(inicialDate);
	this.setEndDate(endDate);
	this.setDegreeDuration(degreeDuration);
	this.setMinimalYearForOptionalCourses(minimalYearForOptionalCourses);
	this.setNeededCredits(neededCredits);
	this.setMarkType(markType);
	this.setNumerusClausus(numerusClausus);
	this.setAnotation(annotation);
    }

    protected DegreeCurricularPlan(Degree degree, String name, GradeScale gradeScale, Person creator,
	    CurricularPeriod curricularPeriod) {
	this(degree, name, gradeScale);

	if (creator == null) {
	    throw new DomainException("degreeCurricularPlan.creator.not.null");
	}
	this.setCurricularPlanMembersGroup(new FixedSetGroup(creator));

	if (curricularPeriod == null) {
	    throw new DomainException("degreeCurricularPlan.curricularPeriod.not.null");
	}
	this.setDegreeStructure(curricularPeriod);

	this.setRoot(new CourseGroup(name, name));
	this.setState(DegreeCurricularPlanState.ACTIVE);
	newStructureFieldsChange(CurricularStage.DRAFT, null);
    }

    private void newStructureFieldsChange(CurricularStage curricularStage,
	    ExecutionYear beginExecutionYear) {

	if (curricularStage == null) {
	    throw new DomainException("degreeCurricularPlan.curricularStage.not.null");
	} else if (hasAnyExecutionDegrees() && curricularStage == CurricularStage.DRAFT) {
	    throw new DomainException("degreeCurricularPlan.has.already.been.executed");
	} else if (curricularStage == CurricularStage.APPROVED) {
	    approve(beginExecutionYear);
	} else {
	    this.setCurricularStage(curricularStage);
	}
    }

    private void commonFieldsChange(String name, GradeScale gradeScale) {
	if (name == null) {
	    throw new DomainException("degreeCurricularPlan.name.not.null");
	}

	// assert unique pair name/degree
	for (final DegreeCurricularPlan dcp : this.getDegree().getDegreeCurricularPlans()) {
	    if (dcp != this && dcp.getName().equalsIgnoreCase(name)) {
		throw new DomainException("error.degreeCurricularPlan.existing.name.and.degree");
	    }
	}
	this.setName(name);

	this.setGradeScale(gradeScale);
    }

    public void edit(String name, DegreeCurricularPlanState state, Date inicialDate, Date endDate,
	    Integer degreeDuration, Integer minimalYearForOptionalCourses, Double neededCredits,
	    MarkType markType, Integer numerusClausus, String annotation, GradeScale gradeScale) {

	commonFieldsChange(name, gradeScale);
	oldStructureFieldsChange(inicialDate, endDate, degreeDuration, minimalYearForOptionalCourses,
		neededCredits, markType, numerusClausus, annotation);

	this.setState(state);
    }

    public void edit(String name, CurricularStage curricularStage, DegreeCurricularPlanState state,
	    GradeScale gradeScale, ExecutionYear beginExecutionYear) {

	if (isApproved()
		&& ((name != null && !getName().equals(name)) || (gradeScale != null && !getGradeScale()
			.equals(gradeScale)))) {
	    throw new DomainException("error.degreeCurricularPlan.already.approved");
	} else {
	    commonFieldsChange(name, gradeScale);
	}

	newStructureFieldsChange(curricularStage, beginExecutionYear);

	this.setState(state);

	this.getRoot().setName(name);
	this.getRoot().setNameEn(name);
    }

    private void approve(ExecutionYear beginExecutionYear) {
	if (isApproved()) {
	    throw new DomainException("error.degreeCurricularPlan.already.approved");
	}

	final ExecutionPeriod beginExecutionPeriod;
	if (beginExecutionYear == null) {
	    throw new DomainException("error.invalid.execution.year");
	} else {
	    beginExecutionPeriod = beginExecutionYear.readExecutionPeriodForSemester(Integer.valueOf(1));
	    if (beginExecutionPeriod == null) {
		throw new DomainException("error.invalid.execution.period");
	    }
	}

	checkIfCurricularCoursesBelongToApprovedCompetenceCourses();
	initBeginExecutionPeriodForDegreeCurricularPlan(getRoot(), beginExecutionPeriod);
	setCurricularStage(CurricularStage.APPROVED);
    }

    private void checkIfCurricularCoursesBelongToApprovedCompetenceCourses() {
	final List<String> notApprovedCompetenceCourses = new ArrayList<String>();
	for (final DegreeModule degreeModule : getDcpDegreeModules(CurricularCourse.class, null)) {
	    final CurricularCourse curricularCourse = (CurricularCourse) degreeModule;
	    if (!curricularCourse.isOptional() && !curricularCourse.getCompetenceCourse().isApproved()) {
		notApprovedCompetenceCourses.add(curricularCourse.getCompetenceCourse()
			.getDepartmentUnit().getName()
			+ " > " + curricularCourse.getCompetenceCourse().getName());
	    }
	}
	if (!notApprovedCompetenceCourses.isEmpty()) {
	    final String[] result = new String[notApprovedCompetenceCourses.size()];
	    throw new DomainException("error.not.all.competence.courses.are.approved",
		    notApprovedCompetenceCourses.toArray(result));
	}
    }

    private void initBeginExecutionPeriodForDegreeCurricularPlan(final CourseGroup courseGroup,
	    final ExecutionPeriod beginExecutionPeriod) {
	
	if (beginExecutionPeriod == null) {
	    throw new DomainException("");
	}

	for (final CurricularRule curricularRule : courseGroup.getCurricularRules()) {
	    curricularRule.setBegin(beginExecutionPeriod);
	}
	for (final Context context : courseGroup.getChildContexts()) {
	    context.setBeginExecutionPeriod(beginExecutionPeriod);
	    if (!context.getChildDegreeModule().isLeaf()) {
		initBeginExecutionPeriodForDegreeCurricularPlan((CourseGroup) context
			.getChildDegreeModule(), beginExecutionPeriod);
	    }
	}
    }

    public boolean isBolonha() {
	return !getCurricularStage().equals(CurricularStage.OLD);
    }

    public boolean isApproved() {
	return getCurricularStage() == CurricularStage.APPROVED;
    }

    public boolean isDraft() {
	return getCurricularStage() == CurricularStage.DRAFT;
    }
    
    public boolean isActive() {
	return getState() == DegreeCurricularPlanState.ACTIVE;
    }

    private Boolean getCanBeDeleted() {
	return (getRoot().getCanBeDeleted() && !(hasAnyStudentCurricularPlans()
		|| hasAnyCurricularCourseEquivalences() || hasAnyEnrolmentPeriods()
		|| hasAnyCurricularCourses() || hasAnyExecutionDegrees() || hasAnyAreas()));
    }

    public void delete() {
	if (getCanBeDeleted()) {
	    removeDegree();
	    if (hasRoot()) {
		getRoot().delete();
	    }
	    if (hasDegreeStructure()) {
		getDegreeStructure().delete();
	    }
	    removeRootDomainObject();
	    deleteDomainObject();
	} else
	    throw new DomainException("error.degree.curricular.plan.cant.delete");
    }

    public String print() {
	if (this.isBolonha()) {
	    StringBuilder dcp = new StringBuilder();

	    dcp.append("[DCP ").append(this.getIdInternal()).append("] ").append(this.getName()).append(
		    "\n");
	    this.getRoot().print(dcp, "", null);

	    return dcp.toString();
	} else {
	    return "";
	}
    }

    public GradeScale getGradeScaleChain() {
	return super.getGradeScale() != null ? super.getGradeScale() : getDegree().getGradeScaleChain();
    }

    public StudentCurricularPlan getNewStudentCurricularPlan() {
	StudentCurricularPlan studentCurricularPlan = null;

	try {
	    Class classDefinition = Class.forName(getConcreteClassForStudentCurricularPlans());
	    studentCurricularPlan = (StudentCurricularPlan) classDefinition.newInstance();
	} catch (InstantiationException e) {
	    throw new RuntimeException(e);
	} catch (IllegalAccessException e) {
	    throw new RuntimeException(e);
	} catch (ClassNotFoundException e) {
	    throw new RuntimeException(e);
	}

	return studentCurricularPlan;
    }

    public List<ExecutionDegree> getSortedExecutionDegrees() {
	List<ExecutionDegree> result = new ArrayList<ExecutionDegree>(getExecutionDegrees());

	Collections.sort(result,
		ExecutionDegree.EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME_AND_EXECUTION_YEAR);

	return result;
    }

    public ExecutionDegree getExecutionDegreeByYear(ExecutionYear executionYear) {
	for (final ExecutionDegree executionDegree : getExecutionDegreesSet()) {
	    if (executionDegree.getExecutionYear() == executionYear) {
		return executionDegree;
	    }
	}
	return null;
    }

    public boolean hasExecutionDegreeFor(ExecutionYear executionYear) {
	return getExecutionDegreeByYear(executionYear) != null;
    }

    public ExecutionDegree getMostRecentExecutionDegree() {
	final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
	ExecutionDegree result = getExecutionDegreeByYear(currentExecutionYear);

	if (result != null) {
	    return result;
	}

	for (final ExecutionDegree executionDegree : getSortedExecutionDegrees()) {
	    if (result == null || result.isBefore(executionDegree)) {
		result = executionDegree;
	    }
	}
	return result;
    }

    public ExecutionDegree getFirstExecutionDegree() {
	if(getExecutionDegrees().isEmpty()){
	    return null;
	}
	return Collections.min(getExecutionDegrees(),
		ExecutionDegree.EXECUTION_DEGREE_COMPARATORY_BY_YEAR);
    }

    public Set<ExecutionCourse> getExecutionCoursesByExecutionPeriod(ExecutionPeriod executionPeriod) {
	final Set<ExecutionCourse> result = new HashSet<ExecutionCourse>();
	for (final CurricularCourse curricularCourse : super.getCurricularCourses()) {
	    for (final ExecutionCourse executionCourse : curricularCourse
		    .getAssociatedExecutionCourses()) {
		if (executionCourse.getExecutionPeriod() == executionPeriod) {
		    result.add(executionCourse);
		}
	    }
	}
	if (getRoot() != null) {
	    addExecutionCoursesForExecutionPeriod(result, executionPeriod, getRoot()
		    .getChildContextsSet());
	}
	return result;
    }

    public SortedSet<DegreeModuleScope> getDegreeModuleScopes() {
        final SortedSet<DegreeModuleScope> degreeModuleScopes = new TreeSet<DegreeModuleScope>(DegreeModuleScope.COMPARATOR_BY_CURRICULAR_YEAR_AND_SEMESTER_AND_CURRICULAR_COURSE_NAME);
        for (final CurricularCourse curricularCourse : this.getCurricularCourses()) {
            degreeModuleScopes.addAll(curricularCourse.getDegreeModuleScopes());
        }
        return degreeModuleScopes;
    }

    public void addExecutionCoursesForExecutionPeriod(final Set<ExecutionCourse> executionCourses,
	    final ExecutionPeriod executionPeriod, final Set<Context> contexts) {
	for (final Context context : contexts) {
	    final DegreeModule degreeModule = context.getChildDegreeModule();
	    if (degreeModule instanceof CurricularCourse) {
		final CurricularCourse curricularCourse = (CurricularCourse) degreeModule;
		executionCourses.addAll(curricularCourse
			.getExecutionCoursesByExecutionPeriod(executionPeriod));
	    } else if (degreeModule instanceof CourseGroup) {
		final CourseGroup courseGroup = (CourseGroup) degreeModule;
		addExecutionCoursesForExecutionPeriod(executionCourses, executionPeriod, courseGroup
			.getChildContextsSet());
	    }
	}
    }

    public List<ExecutionCourse> getExecutionCoursesByExecutionPeriodAndSemesterAndYear(
	    ExecutionPeriod executionPeriod, Integer curricularYear, Integer semester) {

	List<ExecutionCourse> result = new ArrayList<ExecutionCourse>();
	for (final CurricularCourse curricularCourse : this.getCurricularCourses()) {
	    for (final DegreeModuleScope degreeModuleScope : curricularCourse.getDegreeModuleScopes()) {
		if (degreeModuleScope.getCurricularSemester().equals(semester)
			&& degreeModuleScope.getCurricularYear().equals(curricularYear)) {
		    for (final ExecutionCourse executionCourse : curricularCourse
			    .getAssociatedExecutionCourses()) {
			if (executionCourse.getExecutionPeriod().equals(executionPeriod)) {
			    result.add(executionCourse);
			}
		    }
		    break;
		}
	    }
	}
	return result;
    }

    public List<CurricularCourse> getCurricularCoursesWithExecutionIn(ExecutionYear executionYear) {
	List<CurricularCourse> curricularCourses = new ArrayList<CurricularCourse>();
	for (CurricularCourse curricularCourse : getCurricularCourses()) {
	    for (ExecutionPeriod executionPeriod : executionYear.getExecutionPeriods()) {
		List<ExecutionCourse> executionCourses = curricularCourse
			.getExecutionCoursesByExecutionPeriod(executionPeriod);
		if (!executionCourses.isEmpty()) {
		    curricularCourses.add(curricularCourse);
		    break;
		}
	    }
	}
	return curricularCourses;
    }

    public List<CurricularCourse> getCurricularCoursesByBasicAttribute(Boolean basic) {
	List<CurricularCourse> curricularCourses = new ArrayList<CurricularCourse>();
	for (CurricularCourse curricularCourse : getCurricularCourses()) {
	    if (!curricularCourse.isBolonha() && curricularCourse.getBasic().equals(basic)) {
		curricularCourses.add(curricularCourse);
	    }
	}
	return curricularCourses;
    }

    public EnrolmentPeriodInCurricularCourses getActualEnrolmentPeriod() {
	for (EnrolmentPeriod enrolmentPeriod : this.getEnrolmentPeriods()) {
	    if ((enrolmentPeriod instanceof EnrolmentPeriodInCurricularCourses)
		    && enrolmentPeriod.isValid()) {
		return (EnrolmentPeriodInCurricularCourses) enrolmentPeriod;
	    }
	}
	return null;
    }

    public EnrolmentPeriodInCurricularCourses getNextEnrolmentPeriod() {
	List<EnrolmentPeriodInCurricularCourses> result = new ArrayList<EnrolmentPeriodInCurricularCourses>();
	for (EnrolmentPeriod enrolmentPeriod : this.getEnrolmentPeriods()) {
	    if ((enrolmentPeriod instanceof EnrolmentPeriodInCurricularCourses)
		    && DateFormatUtil
			    .isAfter("yyyyMMddHHmm", enrolmentPeriod.getStartDate(), new Date())) {
		result.add((EnrolmentPeriodInCurricularCourses) enrolmentPeriod);
	    }
	}
	if (!result.isEmpty()) {
	    Collections.sort(result, new BeanComparator("startDate"));
	    return result.get(0);
	}
	return null;
    }

    public EnrolmentPeriodInClasses getCurrentClassesEnrollmentPeriod() {
	for (final EnrolmentPeriod enrolmentPeriod : this.getEnrolmentPeriodsSet()) {
	    if ((enrolmentPeriod instanceof EnrolmentPeriodInClasses)
		    && enrolmentPeriod.getExecutionPeriod().getState().equals(PeriodState.CURRENT)) {
		return (EnrolmentPeriodInClasses) enrolmentPeriod;
	    }
	}
	return null;
    }

    // -------------------------------------------------------------
    // BEGIN: Only for enrollment purposes
    // -------------------------------------------------------------

    public List<IEnrollmentRule> getListOfEnrollmentRules(StudentCurricularPlan studentCurricularPlan,
	    ExecutionPeriod executionPeriod) {

	final List<IEnrollmentRule> result = new ArrayList<IEnrollmentRule>(4);

	result.add(new MaximumNumberEctsCreditsEnrolmentRule(studentCurricularPlan, executionPeriod));
	result.add(new PrecedencesEnrollmentRule(studentCurricularPlan, executionPeriod));
	result.add(new PreviousYearsCurricularCourseEnrollmentRule(studentCurricularPlan,
		executionPeriod));

	return result;
    }

    public List<CurricularCourse> getCurricularCoursesFromArea(Branch area, AreaType areaType) {
	List<CurricularCourse> curricularCourses = new ArrayList<CurricularCourse>();

	List scopes = area.getScopes();

	int scopesSize = scopes.size();

	for (int i = 0; i < scopesSize; i++) {
	    CurricularCourseScope curricularCourseScope = (CurricularCourseScope) scopes.get(i);

	    CurricularCourse curricularCourse = curricularCourseScope.getCurricularCourse();

	    if (!curricularCourses.contains(curricularCourse)) {
		curricularCourses.add(curricularCourse);
	    }
	}

	return curricularCourses;
    }

    public List getCurricularCoursesFromAnyArea() {
	List curricularCourses = new ArrayList();
	for (Iterator iter = getAreas().iterator(); iter.hasNext();) {
	    Branch branch = (Branch) iter.next();
	    getCurricularCoursesFromArea(branch, null);
	}
	return curricularCourses;
    }

    public CurricularCourse getCurricularCourseByCode(String code) {
	for (CurricularCourse curricularCourse : getCurricularCourses()) {
	    if (curricularCourse.getCode().equals(code))
		return curricularCourse;
	}
	return null;
    }

    public CurricularCourse getCurricularCourseByAcronym(String acronym) {
	for (CurricularCourse curricularCourse : getCurricularCourses()) {
	    if (curricularCourse.getAcronym().equals(acronym))
		return curricularCourse;
	}
	return null;
    }

    /**
         * Method to get an unfiltered list of a dcp's curricular courses
         * 
         * @return All curricular courses that were or still are present in the
         *         dcp
         */
    @Override
    public List<CurricularCourse> getCurricularCourses() {
	if (this.isBolonha()) {
	    return this.getCurricularCourses((ExecutionYear) null);
	} else {
	    return super.getCurricularCourses();
	}
    }

    @Override
    public Set<CurricularCourse> getCurricularCoursesSet() {
        if (this.isBolonha()) {
            return new HashSet<CurricularCourse>(this.getCurricularCourses((ExecutionYear) null));
        } else {
            return super.getCurricularCoursesSet();
        }
    }


    public Set<CurricularCourse> getCurricularCourses(final ExecutionPeriod executionPeriod) {
	final Set<CurricularCourse> curricularCourses = new HashSet<CurricularCourse>();
	for (final CurricularCourse curricularCourse : super.getCurricularCoursesSet()) {
	    if (curricularCourse.hasScopeInGivenSemesterAndCurricularYearInDCP(null, null,
		    executionPeriod)) {
		curricularCourses.add(curricularCourse);
	    }
	}
	final ExecutionYear executionYear = executionPeriod.getExecutionYear();
	for (final DegreeModule degreeModule : getDcpDegreeModules(CurricularCourse.class, executionYear)) {
	    curricularCourses.add((CurricularCourse) degreeModule);
	}
	return curricularCourses;
    }

    /**
         * Method to get a filtered list of a dcp's curricular courses, with at
         * least one open context in the given execution year
         * 
         * @return All curricular courses that are present in the dcp
         */
    private List<CurricularCourse> getCurricularCourses(ExecutionYear executionYear) {
	List<CurricularCourse> result = new ArrayList<CurricularCourse>();
	if (isBolonha()) {
	    for (DegreeModule degreeModule : getDcpDegreeModules(CurricularCourse.class, executionYear)) {
		result.add((CurricularCourse) degreeModule);
	    }
	}
	return result;
    }

    /**
         * Method to get an unfiltered list of a bolonha dcp's competence
         * courses
         * 
         * @return All competence courses that were or still are present in the
         *         dcp, ordered by name
         */
    public List<CompetenceCourse> getCompetenceCourses() {
	if (this.isBolonha()) {
	    return this.getCompetenceCourses(null);
	} else {
	    return new ArrayList<CompetenceCourse>();
	}

    }

    /**
         * Method to get a filtered list of a dcp's competence courses in the
         * given execution year. Each competence courses is connected with a
         * curricular course with at least one open context in the execution
         * year
         * 
         * @return All competence courses that are present in the dcp
         */
    public List<CompetenceCourse> getCompetenceCourses(ExecutionYear executionYear) {
	SortedSet<CompetenceCourse> result = new TreeSet<CompetenceCourse>(
		CompetenceCourse.COMPETENCE_COURSE_COMPARATOR_BY_NAME);
	if (this.isBolonha()) {
	    for (final CurricularCourse curricularCourse : getCurricularCourses(executionYear)) {
		result.add(curricularCourse.getCompetenceCourse());
	    }
	    return new ArrayList<CompetenceCourse>(result);
	} else {
	    return new ArrayList<CompetenceCourse>();
	}
    }

    public List<Branch> getCommonAreas() {
	return (List) CollectionUtils.select(getAreas(), new Predicate() {
	    public boolean evaluate(Object obj) {
		Branch branch = (Branch) obj;
		if (branch.getBranchType() == null) {
		    return branch.getName().equals("") && branch.getCode().equals("");
		}
		return branch.getBranchType().equals(BranchType.COMNBR);

	    }
	});
    }

    public List<CurricularCourse> getTFCs() {
	List<CurricularCourse> curricularCourses = (List<CurricularCourse>) CollectionUtils.select(
		getCurricularCourses(), new Predicate() {
		    public boolean evaluate(Object obj) {
			CurricularCourse cc = (CurricularCourse) obj;
			return cc.getType().equals(CurricularCourseType.TFC_COURSE);
		    }
		});

	return curricularCourses;
    }

    public List getSpecializationAreas() {

	return (List) CollectionUtils.select(getAreas(), new Predicate() {

	    public boolean evaluate(Object arg0) {
		Branch branch = (Branch) arg0;
		return branch.getBranchType().equals(BranchType.SPECBR);
	    }

	});
    }

    public List getSecundaryAreas() {
	return (List) CollectionUtils.select(getAreas(), new Predicate() {

	    public boolean evaluate(Object arg0) {
		Branch branch = (Branch) arg0;
		return branch.getBranchType().equals(BranchType.SECNBR);
	    }

	});
    }

    public List<CurricularCourse> getActiveCurricularCoursesByYearAndSemester(int year, Integer semester) {
	final List<CurricularCourse> result = new ArrayList<CurricularCourse>();
	for (final CurricularCourse curricularCourse : getCurricularCourses()) {
	    for (final CurricularCourseScope curricularCourseScope : curricularCourse.getScopes()) {
		final CurricularSemester curricularSemester = curricularCourseScope
			.getCurricularSemester();
		if (curricularSemester.getSemester().equals(semester)
			&& curricularSemester.getCurricularYear().getYear().intValue() == year
			&& curricularCourseScope.isActive()) {
		    result.add(curricularCourse);
		    break;
		}
	    }
	}
	return result;
    }

    public Set<CurricularCourse> getActiveCurricularCourses() {
	final Set<CurricularCourse> result = new HashSet<CurricularCourse>();
	for (final CurricularCourse curricularCourse : getCurricularCourses()) {
	    if (curricularCourse.hasAnyActiveDegreModuleScope()) {
		result.add(curricularCourse);
	    }
	}
	return result;
    }

    public Set<CurricularCourse> getActiveCurricularCourses(final ExecutionPeriod executionPeriod) {
	final Set<CurricularCourse> result = new HashSet<CurricularCourse>();
	for (final CurricularCourse curricularCourse : getCurricularCourses()) {
	    if (curricularCourse.hasAnyActiveDegreModuleScope(executionPeriod)) {
		result.add(curricularCourse);
	    }
	}
	return result;
    }

    public List<CurricularCourseScope> getActiveCurricularCourseScopes() {
	final List<CurricularCourseScope> result = new ArrayList<CurricularCourseScope>();
	for (final CurricularCourse course : getCurricularCourses()) {
	    result.addAll(course.getActiveScopes());
	}
	return result;
    }

    public List getSpecialListOfCurricularCourses() {
	return new ArrayList();
    }

    public List<CurricularCourseGroup> getAllOptionalCurricularCourseGroups() {
	List<CurricularCourseGroup> result = new ArrayList<CurricularCourseGroup>();
	for (Branch branch : this.getAreas()) {
	    for (CurricularCourseGroup curricularCourseGroup : branch.getCurricularCourseGroups()) {
		if (curricularCourseGroup instanceof OptionalCurricularCourseGroup) {
		    result.add(curricularCourseGroup);
		}
	    }
	}
	return result;
    }

    public boolean isGradeValid(String grade) {

	IDegreeCurricularPlanStrategyFactory degreeCurricularPlanStrategyFactory = DegreeCurricularPlanStrategyFactory
		.getInstance();
	IDegreeCurricularPlanStrategy degreeCurricularPlanStrategy = degreeCurricularPlanStrategyFactory
		.getDegreeCurricularPlanStrategy(this);

	if (grade == null || grade.length() == 0)
	    return false;

	return degreeCurricularPlanStrategy.checkMark(grade.toUpperCase());
    }

    public EnrolmentPeriodInCurricularCoursesSpecialSeason getEnrolmentPeriodInCurricularCoursesSpecialSeasonByExecutionPeriod(
	    ExecutionPeriod executionPeriod) {
	for (EnrolmentPeriod enrolmentPeriod : getEnrolmentPeriods()) {
	    if ((enrolmentPeriod instanceof EnrolmentPeriodInCurricularCoursesSpecialSeason)
		    && (enrolmentPeriod.getExecutionPeriod().equals(executionPeriod))) {
		return (EnrolmentPeriodInCurricularCoursesSpecialSeason) enrolmentPeriod;
	    }
	}
	return null;
    }

    public CurricularCourse createCurricularCourse(String name, String code, String acronym,
	    Boolean enrolmentAllowed, CurricularStage curricularStage) {
	checkAttributes(name, code, acronym);
	final CurricularCourse curricularCourse = new CurricularCourse(name, code, acronym,
		enrolmentAllowed, curricularStage);
	this.addCurricularCourses(curricularCourse);
	return curricularCourse;
    }

    private void checkAttributes(String name, String code, String acronym) {
	for (final CurricularCourse curricularCourse : this.getCurricularCourses()) {
	    if (curricularCourse.getName().equals(name) && curricularCourse.getCode().equals(code)) {
		throw new DomainException("error.curricularCourseWithSameNameAndCode");
	    }
	    if (curricularCourse.getAcronym().equals(acronym)) {
		throw new DomainException("error.curricularCourseWithSameAcronym");
	    }
	}
    }

    public CourseGroup createCourseGroup(CourseGroup parentCourseGroup, String name, String nameEn,
	    CurricularPeriod curricularPeriod, ExecutionPeriod beginExecutionPeriod,
	    ExecutionPeriod endExecutionPeriod) {
	parentCourseGroup.checkDuplicateChildNames(name, nameEn);
	final CourseGroup courseGroup = new CourseGroup(name, nameEn);
	new Context(parentCourseGroup, courseGroup, curricularPeriod, beginExecutionPeriod,
		endExecutionPeriod);
	return courseGroup;
    }

    public CurricularCourse createCurricularCourse(Double weight, String prerequisites,
	    String prerequisitesEn, CurricularStage curricularStage, CompetenceCourse competenceCourse,
	    CourseGroup parentCourseGroup, CurricularPeriod curricularPeriod,
	    ExecutionPeriod beginExecutionPeriod, ExecutionPeriod endExecutionPeriod) {

	checkIfPresentInDegreeCurricularPlan(competenceCourse, this);
	checkIfAnualBeginsInFirstPeriod(competenceCourse, curricularPeriod);

	return new CurricularCourse(weight, prerequisites, prerequisitesEn, curricularStage,
		competenceCourse, parentCourseGroup, curricularPeriod, beginExecutionPeriod,
		endExecutionPeriod);
    }

    public CurricularCourse createCurricularCourse(CourseGroup parentCourseGroup, String name,
	    String nameEn, CurricularStage curricularStage, CurricularPeriod curricularPeriod,
	    ExecutionPeriod beginExecutionPeriod, ExecutionPeriod endExecutionPeriod) {

	return new CurricularCourse(parentCourseGroup, name, nameEn, curricularStage, curricularPeriod,
		beginExecutionPeriod, endExecutionPeriod);
    }

    private void checkIfPresentInDegreeCurricularPlan(final CompetenceCourse competenceCourse,
	    final DegreeCurricularPlan degreeCurricularPlan) {

	final List<DegreeModule> curricularCoursesFromDegreeCurricularPlan = degreeCurricularPlan
		.getDcpDegreeModules(CurricularCourse.class, null);
	for (CurricularCourse curricularCourse : competenceCourse.getAssociatedCurricularCourses()) {
	    if (curricularCoursesFromDegreeCurricularPlan.contains(curricularCourse)) {
		throw new DomainException(
			"competenceCourse.already.has.a.curricular.course.in.degree.curricular.plan");
	    }
	}
    }

    private void checkIfAnualBeginsInFirstPeriod(final CompetenceCourse competenceCourse,
	    final CurricularPeriod curricularPeriod) {

	if (competenceCourse.getRegime().equals(RegimeType.ANUAL)
		&& (curricularPeriod.getOrder() == null || curricularPeriod.getOrder() != 1)) {
	    throw new DomainException(
		    "competenceCourse.anual.but.trying.to.associate.curricular.course.not.to.first.period");
	}
    }

    public List<DegreeModule> getDcpDegreeModules(Class<? extends DegreeModule> clazz,
	    ExecutionYear executionYear) {
	final Set<DegreeModule> result = new HashSet<DegreeModule>();
	if (this.getRoot() != null) {
	    this.getRoot().collectChildDegreeModules(clazz, result, executionYear);
	}
	return new ArrayList<DegreeModule>(result);
    }

    public List<List<DegreeModule>> getDcpDegreeModulesIncludingFullPath(
	    Class<? extends DegreeModule> clazz, ExecutionYear executionYear) {

	final List<List<DegreeModule>> result = new ArrayList<List<DegreeModule>>();
	final List<DegreeModule> path = new ArrayList<DegreeModule>();

	if (clazz.equals(CourseGroup.class)) {
	    path.add(this.getRoot());

	    result.add(path);
	}

	this.getRoot().collectChildDegreeModulesIncludingFullPath(clazz, result, path, executionYear);

	return result;
    }

    public Branch getBranchByName(final String branchName) {
	for (final Branch branch : getAreas()) {
	    if (branchName.equals(branch.getName())) {
		return branch;
	    }
	}
	return null;
    }

    public Boolean getUserCanBuild() {
	Person person = AccessControl.getUserView().getPerson();
	return (this.getCurricularPlanMembersGroup().isMember(person)
		|| person.hasRole(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER) || person
		.hasRole(RoleType.MANAGER));
    }

    @Override
    @Checked("DegreeCurricularPlanPredicates.scientificCouncilWritePredicate")
    public void removeDegree() {
	super.removeDegree();
    }

    @Override
    @Checked("DegreeCurricularPlanPredicates.scientificCouncilWritePredicate")
    public void setCurricularPlanMembersGroup(Group curricularPlanMembersGroup) {
	super.setCurricularPlanMembersGroup(curricularPlanMembersGroup);
    }

    @Override
    @Checked("DegreeCurricularPlanPredicates.scientificCouncilWritePredicate")
    public void setCurricularStage(CurricularStage curricularStage) {
	super.setCurricularStage(curricularStage);
    }

    @Override
    @Checked("DegreeCurricularPlanPredicates.scientificCouncilWritePredicate")
    public void setDegree(Degree degree) {
	super.setDegree(degree);
    }

    @Override
    @Checked("DegreeCurricularPlanPredicates.scientificCouncilWritePredicate")
    public void setRoot(CourseGroup courseGroup) {
	super.setRoot(courseGroup);
    }

    @Override
    @Checked("DegreeCurricularPlanPredicates.scientificCouncilWritePredicate")
    public void setDegreeStructure(CurricularPeriod degreeStructure) {
	super.setDegreeStructure(degreeStructure);
    }

    @Override
    @Checked("DegreeCurricularPlanPredicates.scientificCouncilWritePredicate")
    public void setGradeScale(GradeScale gradeScale) {
	super.setGradeScale(gradeScale);
    }

    @Override
    @Checked("DegreeCurricularPlanPredicates.scientificCouncilWritePredicate")
    public void setName(String name) {
	super.setName(name);
    }

    @Override
    @Checked("DegreeCurricularPlanPredicates.scientificCouncilWritePredicate")
    public void setOjbConcreteClass(String ojbConcreteClass) {
	super.setOjbConcreteClass(ojbConcreteClass);
    }

    public String getPresentationName() {
	return getDegree().getPresentationName() + " " + getName();
    }

    public boolean hasAnyExecutionDegreeFor(ExecutionYear executionYear) {
	for (final ExecutionDegree executionDegree : this.getExecutionDegreesSet()) {
	    if (executionDegree.getExecutionYear() == executionYear) {
		return true;
	    }
	}
	return false;
    }

    public static Set<DegreeCurricularPlan> readBolonhaDegreeCurricularPlans() {
        final Set<DegreeCurricularPlan> result = new HashSet<DegreeCurricularPlan>();

        for (final Degree degree : Degree.readBolonhaDegrees()) {
            result.addAll(degree.getDegreeCurricularPlans());
        }
        
        return result;
    }

    public static Set<DegreeCurricularPlan> readPreBolonhaDegreeCurricularPlans() {
        final Set<DegreeCurricularPlan> result = new HashSet<DegreeCurricularPlan>();

        for (final Degree degree : Degree.readOldDegrees()) {
            result.addAll(degree.getDegreeCurricularPlans());
        }
        
        return result;
    }

    public Set<MasterDegreeCandidate> readMasterDegreeCandidates() {
	final Set<MasterDegreeCandidate> result = new HashSet<MasterDegreeCandidate>();
	for (final ExecutionDegree executionDegree : this.getExecutionDegreesSet()) {
	    result.addAll(executionDegree.getMasterDegreeCandidatesSet());
	}
	return result;
    }

    public Set<MasterDegreeCandidate> readMasterDegreeCandidatesBySpecialization(
	    final Specialization specialization) {
	final Set<MasterDegreeCandidate> result = new HashSet<MasterDegreeCandidate>();
	for (final MasterDegreeCandidate masterDegreeCandidate : readMasterDegreeCandidates()) {
	    if (masterDegreeCandidate.getSpecialization() == specialization) {
		result.add(masterDegreeCandidate);
	    }
	}
	return result;
    }

    public Set<MasterDegreeCandidate> readMasterDegreeCandidatesBySituatioName(
	    final SituationName situationName) {
	final Set<MasterDegreeCandidate> result = new HashSet<MasterDegreeCandidate>();
	for (final MasterDegreeCandidate masterDegreeCandidate : readMasterDegreeCandidates()) {
	    if (masterDegreeCandidate.hasCandidateSituationWith(situationName)) {
		result.add(masterDegreeCandidate);
	    }
	}
	return result;
    }

    public Set<MasterDegreeCandidate> readMasterDegreeCandidatesByCourseAssistant(boolean courseAssistant) {
	final Set<MasterDegreeCandidate> result = new HashSet<MasterDegreeCandidate>();
	for (final MasterDegreeCandidate masterDegreeCandidate : readMasterDegreeCandidates()) {
	    if (masterDegreeCandidate.getCourseAssistant() == courseAssistant) {
		result.add(masterDegreeCandidate);
	    }
	}
	return result;
    }

    public List<MasterDegreeThesisDataVersion> readActiveMasterDegreeThesisDataVersions() {
	List<MasterDegreeThesisDataVersion> masterDegreeThesisDataVersions = new ArrayList<MasterDegreeThesisDataVersion>();
	for (StudentCurricularPlan studentCurricularPlan : this.getStudentCurricularPlans()) {
	    MasterDegreeThesisDataVersion masterDegreeThesisDataVersion = studentCurricularPlan
		    .readActiveMasterDegreeThesisDataVersion();
	    if (masterDegreeThesisDataVersion != null) {
		masterDegreeThesisDataVersions.add(masterDegreeThesisDataVersion);
	    }
	}
	return masterDegreeThesisDataVersions;
    }

    // -------------------------------------------------------------
    // read static methods
    // -------------------------------------------------------------

    public static List<DegreeCurricularPlan> readByCurricularStage(CurricularStage curricularStage) {
	List<DegreeCurricularPlan> result = new ArrayList<DegreeCurricularPlan>();
	for (DegreeCurricularPlan degreeCurricularPlan : RootDomainObject.getInstance()
		.getDegreeCurricularPlans()) {
	    if (degreeCurricularPlan.getCurricularStage().equals(curricularStage)) {
		result.add(degreeCurricularPlan);
	    }
	}
	return result;
    }

    public static List<DegreeCurricularPlan> readByDegreeTypeAndState(DegreeType degreeType,
	    DegreeCurricularPlanState state) {
	List<DegreeCurricularPlan> result = new ArrayList<DegreeCurricularPlan>();
	for (DegreeCurricularPlan degreeCurricularPlan : RootDomainObject.getInstance()
		.getDegreeCurricularPlans()) {
	    if (!degreeCurricularPlan.isBolonha()) {
		if (degreeCurricularPlan.getDegree().getTipoCurso().equals(degreeType)
			&& degreeCurricularPlan.getState().equals(state)
			&& !degreeCurricularPlan.isBolonha()) {
		    result.add(degreeCurricularPlan);
		}
	    }
	}
	return result;
    }

    public static DegreeCurricularPlan readByNameAndDegreeSigla(String name, String degreeSigla) {
	for (final DegreeCurricularPlan degreeCurricularPlan : RootDomainObject.getInstance()
		.getDegreeCurricularPlans()) {
	    if (degreeCurricularPlan.getName().equalsIgnoreCase(name)
		    && degreeCurricularPlan.getDegree().getSigla().equalsIgnoreCase(degreeSigla)) {
		return degreeCurricularPlan;
	    }
	}
	return null;
    }

    public Set<CurricularCourseScope> findCurricularCourseScopesIntersectingPeriod(final Date beginDate,
	    final Date endDate) {
	final Set<CurricularCourseScope> curricularCourseScopes = new HashSet<CurricularCourseScope>();
	for (final CurricularCourse curricularCourse : getCurricularCoursesSet()) {
	    curricularCourseScopes.addAll(curricularCourse.findCurricularCourseScopesIntersectingPeriod(
		    beginDate, endDate));
	}
	return curricularCourseScopes;
    }

    public ExecutionDegree createExecutionDegree(ExecutionYear executionYear, Campus campus,
	    Boolean temporaryExamMap) {
	if (this.isBolonha() && this.isDraft()) {
	    throw new DomainException(
		    "degree.curricular.plan.not.approved.cannot.create.execution.degree", this.getName());
	}

	if (this.hasAnyExecutionDegreeFor(executionYear)) {
	    throw new DomainException(
		    "degree.curricular.plan.already.has.execution.degree.for.this.year", this.getName(),
		    executionYear.getYear());
	}

	return new ExecutionDegree(this, executionYear, campus, temporaryExamMap);
    }

    public CurricularPeriod getCurricularPeriodFor(int year, int semester) {
	final CurricularPeriodInfoDTO[] curricularPeriodInfos = buildCurricularPeriodInfoDTOsFor(year, semester);
	return getDegreeStructure().getCurricularPeriod(curricularPeriodInfos);
    }

    private CurricularPeriodInfoDTO[] buildCurricularPeriodInfoDTOsFor(int year, int semester) {
	final CurricularPeriodInfoDTO[] curricularPeriodInfos;
	if (getDegree().getDegreeType().getYears() > 1) {

	    curricularPeriodInfos = new CurricularPeriodInfoDTO[] {
		    new CurricularPeriodInfoDTO(year, CurricularPeriodType.YEAR),
		    new CurricularPeriodInfoDTO(semester, CurricularPeriodType.SEMESTER) };

	} else {
	    curricularPeriodInfos = new CurricularPeriodInfoDTO[] { new CurricularPeriodInfoDTO(
		    semester, CurricularPeriodType.SEMESTER) };
	}
	return curricularPeriodInfos;
    }
    
    public CurricularPeriod createCurricularPeriodFor(int year, int semester) {
	final CurricularPeriodInfoDTO[] curricularPeriodInfos = buildCurricularPeriodInfoDTOsFor(year, semester);
	
	return getDegreeStructure().addCurricularPeriod(curricularPeriodInfos);
    }

    @Override
    @Deprecated
    public Date getInitialDate() {
	YearMonthDay ymd = this.getInitialDateYearMonthDay();
	return (ymd == null) ? null : new Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
    }

    @Override
    public YearMonthDay getInitialDateYearMonthDay() {
	if (isBolonha() && hasAnyExecutionDegrees()) {
	     final ExecutionDegree firstExecutionDegree = getExecutionDegrees().get(0);
	     return firstExecutionDegree.getExecutionYear().getBeginDateYearMonthDay();
	} else {
	    return super.getInitialDateYearMonthDay();
	}
    }

    @Override
    @Deprecated
    public Date getEndDate() {
	YearMonthDay ymd = this.getEndDateYearMonthDay();
	return (ymd == null) ? null : new Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
    }

    @Override
    public YearMonthDay getEndDateYearMonthDay() {
	if (isBolonha() && hasAnyExecutionDegrees()) {
	     final ExecutionDegree mostRecentExecutionDegree = getMostRecentExecutionDegree();
	     if (mostRecentExecutionDegree.getExecutionYear() == ExecutionYear.readCurrentExecutionYear()) {
		 return null;
	     } else {
		 return mostRecentExecutionDegree.getExecutionYear().getBeginDateYearMonthDay(); 
	     }
	} else {
	    return super.getEndDateYearMonthDay();
	}
    }



    public void addExecutionCourses(final Collection<ExecutionCourseView> executionCourseViews, final ExecutionPeriod ... executionPeriods) {
	if (executionCourseViews != null && executionPeriods != null) {
	    // Pre-Bolonha structure search
	    for (final CurricularCourse curricularCourse : super.getCurricularCoursesSet()) {
		for (final ExecutionPeriod executionPeriod : executionPeriods) {
		    for (final ExecutionCourse executionCourse : curricularCourse.getAssociatedExecutionCoursesSet()) {
			if (executionCourse.getExecutionPeriod() == executionPeriod) {
			    for (final CurricularCourseScope curricularCourseScope : curricularCourse.getScopesSet()) {
				if (curricularCourseScope.isActiveForExecutionPeriod(executionPeriod)) {
				    executionCourseViews.add(constructExecutionCourseView(executionCourse, curricularCourseScope));
				}
			    }
			}
		    }
		}
	    }

	    // Bolonha structure search
	    CourseGroup root = getRoot();
	    if (root != null) {
		addExecutionCourses(root, executionCourseViews, executionPeriods);
	    }
	}
    }

    private void addExecutionCourses(final CourseGroup courseGroup, final Collection<ExecutionCourseView> executionCourseViews, final ExecutionPeriod ... executionPeriods) {
	for (final Context context : courseGroup.getChildContextsSet()) {
	    for (final ExecutionPeriod executionPeriod : executionPeriods) {
		if (context.isValid(executionPeriod)) {
		    final DegreeModule degreeModule = context.getChildDegreeModule();
		    if (degreeModule.isLeaf()) {
			final CurricularCourse curricularCourse = (CurricularCourse) degreeModule;
			for (final ExecutionCourse executionCourse : curricularCourse.getAssociatedExecutionCoursesSet()) {
			    if (executionCourse.getExecutionPeriod() == executionPeriod) {
				final Integer curricularYear = context.getCurricularYear();
				executionCourseViews.add(constructExecutionCourseView(executionCourse, curricularYear));
			    }
			}
		    } else {
			final CourseGroup childCourseGroup = (CourseGroup) degreeModule;
			addExecutionCourses(childCourseGroup, executionCourseViews, executionPeriods);
		    }
		}
	    }
	}
    }

    private ExecutionCourseView constructExecutionCourseView(final ExecutionCourse executionCourse, final Integer curricularYear) {
	final ExecutionCourseView executionCourseView = new ExecutionCourseView(executionCourse);
	executionCourseView.setCurricularYear(curricularYear);
	executionCourseView.setDegreeCurricularPlanAnotation(getAnotation());
	return executionCourseView;
    }

    private ExecutionCourseView constructExecutionCourseView(final ExecutionCourse executionCourse, final CurricularCourseScope curricularCourseScope) {
	final Integer curricularYear = curricularCourseScope.getCurricularSemester().getCurricularYear().getYear();
	final ExecutionCourseView executionCourseView = constructExecutionCourseView(executionCourse, curricularYear);
	executionCourseView.setAnotation(curricularCourseScope.getAnotation());
	return executionCourseView;
    }

}
