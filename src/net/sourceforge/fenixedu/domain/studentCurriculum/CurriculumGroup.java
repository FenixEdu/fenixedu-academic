package net.sourceforge.fenixedu.domain.studentCurriculum;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriodType;
import net.sourceforge.fenixedu.domain.curricularRules.CreditsLimit;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRuleType;
import net.sourceforge.fenixedu.domain.curricularRules.DegreeModulesSelectionLimit;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentCondition;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.enrolment.CurriculumModuleEnroledWrapper;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.joda.time.YearMonthDay;

import dml.runtime.RelationAdapter;

public class CurriculumGroup extends CurriculumGroup_Base {

    static {
	CurriculumModule.CurriculumModuleCurriculumGroup
		.addListener(new RelationAdapter<CurriculumModule, CurriculumGroup>() {
		    @Override
		    final public void afterRemove(CurriculumModule curriculumModule,
			    CurriculumGroup curriculumGroup) {
			super.afterRemove(curriculumModule, curriculumGroup);
			if (curriculumGroup != null && curriculumGroup.isNoCourseGroupCurriculumGroup()) {
			    if (!curriculumGroup.hasAnyCurriculumModules()) {
				curriculumGroup.delete();
			    }
			}
		    }
		});
    }

    static final public Comparator<CurriculumGroup> COMPARATOR_BY_CHILD_ORDER_AND_ID = new Comparator<CurriculumGroup>() {
	public int compare(CurriculumGroup o1, CurriculumGroup o2) {
	    int result = o1.getChildOrder().compareTo(o2.getChildOrder());
	    return (result != 0) ? result : o1.getIdInternal().compareTo(o2.getIdInternal());
	}
    };

    protected CurriculumGroup() {
	super();
    }

    public CurriculumGroup(final CurriculumGroup curriculumGroup,final CourseGroup courseGroup) {
	this();
	init(curriculumGroup, courseGroup);
    }
    
    protected void init(final CurriculumGroup curriculumGroup, final CourseGroup courseGroup) {
	if (courseGroup == null || curriculumGroup == null) {
	    throw new DomainException(
		    "error.studentCurriculum.curriculumGroup.courseGroup.cannot.be.null");
	}
	checkInitConstraints(curriculumGroup.getStudentCurricularPlan(), courseGroup);
	setDegreeModule(courseGroup);
	setCurriculumGroup(curriculumGroup);
    }

    protected void checkInitConstraints(final StudentCurricularPlan studentCurricularPlan,
	    final CourseGroup courseGroup) {
	if (studentCurricularPlan.getRoot().hasCourseGroup(courseGroup)) {
	    throw new DomainException("error.studentCurriculum.CurriculumGroup.duplicate.courseGroup",
		    courseGroup.getName());
	}
    }

    final protected void checkParameters(CourseGroup courseGroup, ExecutionPeriod executionPeriod) {
	if (courseGroup == null) {
	    throw new DomainException(
		    "error.studentCurriculum.curriculumGroup.courseGroup.cannot.be.null");
	}
	if (executionPeriod == null) {
	    throw new DomainException(
		    "error.studentCurriculum.curriculumGroup.executionPeriod.cannot.be.null");
	}
    }

    public CurriculumGroup(CurriculumGroup parentCurriculumGroup, CourseGroup courseGroup,
	    ExecutionPeriod executionPeriod) {
	super();
	init(parentCurriculumGroup, courseGroup, executionPeriod);
    }

    final protected void init(final CurriculumGroup curriculumGroup, final CourseGroup courseGroup,
	    final ExecutionPeriod executionPeriod) {
	checkInitConstraints(curriculumGroup.getStudentCurricularPlan(), courseGroup);
	checkParameters(curriculumGroup, courseGroup, executionPeriod);
	setDegreeModule(courseGroup);
	setCurriculumGroup(curriculumGroup);
	addChildCurriculumGroups(courseGroup, executionPeriod);
    }

    private void checkParameters(CurriculumGroup parentCurriculumGroup, CourseGroup courseGroup,
	    ExecutionPeriod executionPeriod) {
	if (parentCurriculumGroup == null) {
	    throw new DomainException(
		    "error.studentCurriculum.curriculumGroup.parentCurriculumGroup.cannot.be.null");
	}
	checkParameters(courseGroup, executionPeriod);
    }

    final protected void addChildCurriculumGroups(final CourseGroup courseGroup,
	    final ExecutionPeriod executionPeriod) {
	for (final CourseGroup childCourseGroup : courseGroup
		.getNotOptionalChildCourseGroups(executionPeriod)) {
	    new CurriculumGroup(this, childCourseGroup, executionPeriod);
	}
    }

    @Override
    final public boolean isLeaf() {
	return false;
    }

    final public boolean canBeDeleted() {
	return !hasAnyCurriculumModules();
    }

    @Override
    public void delete() {
	if (canBeDeleted()) {
	    super.delete();
	} else {
	    throw new DomainException(
		    "error.studentCurriculum.CurriculumGroup.notEmptyCurriculumGroupModules", getName()
			    .getContent());
	}
    }

    @Override
    final public StringBuilder print(String tabs) {
	final StringBuilder builder = new StringBuilder();
	builder.append(tabs);
	builder.append("[CG ").append(getName().getContent()).append(" ]\n");
	final String tab = tabs + "\t";
	for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
	    builder.append(curriculumModule.print(tab));
	}
	return builder;
    }

    @Override
    final public CourseGroup getDegreeModule() {
	return (CourseGroup) super.getDegreeModule();
    }

    @Override
    final public List<Enrolment> getEnrolments() {
	final List<Enrolment> result = new ArrayList<Enrolment>();
	for (final CurriculumModule curriculumModule : this.getCurriculumModulesSet()) {
	    result.addAll(curriculumModule.getEnrolments());
	}
	return result;
    }

    final public Set<Enrolment> getEnrolmentsSet() {
	final Set<Enrolment> result = new HashSet<Enrolment>();
	for (final CurriculumModule curriculumModule : this.getCurriculumModulesSet()) {
	    result.addAll(curriculumModule.getEnrolments());
	}
	return result;
    }

    @Override
    final public boolean hasAnyEnrolments() {
	for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
	    if (curriculumModule.hasAnyEnrolments()) {
		return true;
	    }
	}
	
	return false;
    }
    
    @Override
    final public void collectDismissals(final List<Dismissal> result) {
	for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
	    curriculumModule.collectDismissals(result);
	}
    }

    final public List<Dismissal> getChildDismissals() {
	final List<Dismissal> result = new ArrayList<Dismissal>();
	for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
	    if (curriculumModule.isDismissal()) {
		result.add((Dismissal) curriculumModule);
	    }
	}
	return result;
    }

    final public List<Enrolment> getChildEnrolments() {
	final List<Enrolment> result = new ArrayList<Enrolment>();
	for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
	    if (curriculumModule.isEnrolment()) {
		result.add((Enrolment) curriculumModule);
	    }
	}

	return result;
    }

    @Override
    public boolean isRoot() {
	return false;
    }

    @Override
    public StudentCurricularPlan getStudentCurricularPlan() {
	return getCurriculumGroup().getStudentCurricularPlan();
    }

    // alterar isto, colocar as regras
    private Collection<Context> getDegreeModulesFor(ExecutionPeriod executionPeriod) {
	Collection<Context> result = new HashSet<Context>();
	for (Context context : this.getDegreeModule().getValidChildContexts(executionPeriod)) {
	    if (context.getCurricularPeriod() == null
		    || context.getCurricularPeriod().contains(CurricularPeriodType.SEMESTER,
			    executionPeriod.getSemester()) != null) {
		result.add(context);
	    }
	}
	return result;
    }

    public List<Context> getCurricularCourseContextsToEnrol(ExecutionPeriod executionPeriod) {
	List<Context> result = new ArrayList<Context>();
	for (Context context : this.getDegreeModulesFor(executionPeriod)) {
	    if (context.getChildDegreeModule().isLeaf()) {
		CurricularCourse curricularCourse = (CurricularCourse) context.getChildDegreeModule();
		if (!this.getStudentCurricularPlan().isApproved(curricularCourse, executionPeriod)
			&& !this.getStudentCurricularPlan().isEnroledInExecutionPeriod(curricularCourse,
				executionPeriod)) {
		    result.add(context);
		}
	    }
	}
	return result;
    }

    public List<Context> getCourseGroupContextsToEnrol(ExecutionPeriod executionPeriod) {
	List<Context> result = new ArrayList<Context>();
	for (Context context : this.getDegreeModulesFor(executionPeriod)) {
	    if (!context.getChildDegreeModule().isLeaf()) {
		if (!this.getStudentCurricularPlan().getRoot().hasDegreeModule(
			context.getChildDegreeModule())) {
		    result.add(context);
		}
	    }
	}
	return result;
    }

    public Collection<CurricularCourse> getCurricularCoursesToDismissal() {
	final Set<CurricularCourse> result = new HashSet<CurricularCourse>();
	for (final Context context : this.getDegreeModule().getValidChildContexts(
		CurricularCourse.class, (ExecutionPeriod) null)) {
	    final CurricularCourse curricularCourse = (CurricularCourse) context.getChildDegreeModule();
	    if (!getStudentCurricularPlan().getRoot().isApproved(curricularCourse, null)) {
		result.add(curricularCourse);
	    }
	}
	return result;
    }

    @Override
    final public boolean isApproved(CurricularCourse curricularCourse, ExecutionPeriod executionPeriod) {
	for (final CurriculumModule curriculumModule : this.getCurriculumModules()) {
	    if (curriculumModule.isApproved(curricularCourse, executionPeriod)) {
		return true;
	    }
	}
	return false;
    }

    @Override
    final public boolean isEnroledInExecutionPeriod(CurricularCourse curricularCourse,
	    ExecutionPeriod executionPeriod) {
	for (final CurriculumModule curriculumModule : this.getCurriculumModules()) {
	    if (curriculumModule.isEnroledInExecutionPeriod(curricularCourse, executionPeriod)) {
		return true;
	    }
	}
	return false;
    }

    @Override
    final public boolean hasEnrolmentWithEnroledState(final CurricularCourse curricularCourse,
	    final ExecutionPeriod executionPeriod) {
	for (final CurriculumModule curriculumModule : this.getCurriculumModules()) {
	    if (curriculumModule.hasEnrolmentWithEnroledState(curricularCourse, executionPeriod)) {
		return true;
	    }
	}
	return false;
    }

    @Override
    public boolean hasDegreeModule(DegreeModule degreeModule) {
	if (super.hasDegreeModule(degreeModule)) {
	    return true;
	} else {
	    for (final CurriculumModule curriculumModule : this.getCurriculumModules()) {
		if (curriculumModule.hasDegreeModule(degreeModule)) {
		    return true;
		}
	    }
	    return false;
	}
    }

    @Override
    final public boolean hasCurriculumModule(CurriculumModule curriculumModule) {
	if (super.hasCurriculumModule(curriculumModule)) {
	    return true;
	}
	for (final CurriculumModule module : getCurriculumModulesSet()) {
	    if (module.hasCurriculumModule(module)) {
		return true;
	    }
	}
	return false;
    }

    @Override
    final public Enrolment findEnrolmentFor(final CurricularCourse curricularCourse,
	    final ExecutionPeriod executionPeriod) {
	for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
	    final Enrolment search = curriculumModule
		    .findEnrolmentFor(curricularCourse, executionPeriod);
	    if (search != null) {
		return search;
	    }
	}
	return null;
    }

    @Override
    final public Enrolment getApprovedEnrolment(final CurricularCourse curricularCourse) {
	for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
	    final Enrolment enrolment = curriculumModule.getApprovedEnrolment(curricularCourse);
	    if (enrolment != null) {
		return enrolment;
	    }
	}
	return null;
    }

    @Override
    final public Dismissal getDismissal(final CurricularCourse curricularCourse) {
	for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
	    final Dismissal dismissal = curriculumModule.getDismissal(curricularCourse);
	    if (dismissal != null) {
		return dismissal;
	    }
	}
	return null;
    }

    public CurriculumGroup findCurriculumGroupFor(final CourseGroup courseGroup) {
	if (getDegreeModule() == courseGroup) {
	    return this;
	}
	for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
	    if (!curriculumModule.isLeaf()) {
		final CurriculumGroup curriculumGroup = (CurriculumGroup) curriculumModule;
		final CurriculumGroup searchCurriculumGroup = curriculumGroup
			.findCurriculumGroupFor(courseGroup);
		if (searchCurriculumGroup != null) {
		    return searchCurriculumGroup;
		}
	    }
	}
	return null;
    }

    final public Set<CurriculumLine> getCurriculumLines() {
	Set<CurriculumLine> result = new TreeSet<CurriculumLine>(
		CurriculumModule.COMPARATOR_BY_NAME_AND_ID);

	for (final CurriculumModule curriculumModule : getCurriculumModules()) {
	    if (curriculumModule.isLeaf()) {
		result.add((CurriculumLine) curriculumModule);
	    }
	}

	return result;
    }
    
    final public boolean hasCurriculumLines() {
	for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
	    if (curriculumModule.isLeaf()) {
		return true;
	    }
	}

	return false;
    }

    @Override
    final public void addApprovedCurriculumLines(final Collection<CurriculumLine> result) {
	for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
	    curriculumModule.addApprovedCurriculumLines(result);
	}
    }
    
    @Override
    final public boolean hasAnyApprovedCurriculumLines() {
	for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
	    if (curriculumModule.hasAnyApprovedCurriculumLines()) {
		return true;
	    }
	}

	return false;
    }

    final public Set<CurriculumGroup> getCurriculumGroups() {
	Set<CurriculumGroup> result = new TreeSet<CurriculumGroup>(
		CurriculumModule.COMPARATOR_BY_NAME_AND_ID);

	for (final CurriculumModule curriculumModule : getCurriculumModules()) {
	    if (!curriculumModule.isLeaf()) {
		result.add((CurriculumGroup) curriculumModule);
	    }
	}

	return result;
    }

    final public Set<CurriculumGroup> getAllCurriculumGroups() {
	Set<CurriculumGroup> result = new HashSet<CurriculumGroup>();
	for (final CurriculumModule curriculumModule : getCurriculumModules()) {
	    if (!curriculumModule.isLeaf()) {
		result.add((CurriculumGroup) curriculumModule);
		result.addAll(((CurriculumGroup) curriculumModule).getAllCurriculumGroups());
	    }
	}
	return result;
    }
    
    public Set<CurriculumLine> getAllCurriculumLines() {
	Set<CurriculumLine> result = new HashSet<CurriculumLine>();
	for (final CurriculumModule curriculumModule : getCurriculumModules()) {
	    if (curriculumModule.isLeaf()) {
		result.add((CurriculumLine) curriculumModule);
	    } else {
		final CurriculumGroup curriculumGroup = (CurriculumGroup) curriculumModule;
		result.addAll(curriculumGroup.getAllCurriculumLines());
	    }
	}
	return result;
    }
    
    public Integer getChildOrder() {
	return getChildOrder(null);
    }

    public Integer getChildOrder(final ExecutionPeriod executionPeriod) {
	return getParentCurriculumGroup().searchChildOrderForChild(this, executionPeriod);
    }

    private CurriculumGroup getParentCurriculumGroup() {
	return getCurriculumGroup();
    }

    protected Integer searchChildOrderForChild(final CurriculumGroup child,
	    final ExecutionPeriod executionPeriod) {
	for (final Context context : getDegreeModule().getValidChildContexts(executionPeriod)) {
	    if (context.getChildDegreeModule() == child.getDegreeModule()) {
		return context.getChildOrder();
	    }
	}
	return null;
    }

    public boolean hasCourseGroup(final CourseGroup courseGroup) {
	if (getDegreeModule().equals(courseGroup)) {
	    return true;
	}

	for (final CurriculumModule curriculumModule : getCurriculumModules()) {
	    if (!curriculumModule.isLeaf()) {
		CurriculumGroup group = (CurriculumGroup) curriculumModule;
		if (group.hasCourseGroup(courseGroup)) {
		    return true;
		}
	    }
	}

	return false;
    }

    final public void createNoCourseGroupCurriculumGroupEnrolment(
	    final StudentCurricularPlan studentCurricularPlan, final CurricularCourse curricularCourse,
	    final ExecutionPeriod executionPeriod, final NoCourseGroupCurriculumGroupType groupType) {
	if (!isRoot()) {
	    throw new DomainException("error.no.root.curriculum.group");
	}

	CurriculumGroup extraCurricularGroup = getNoCourseGroupCurriculumGroup(groupType);
	if (extraCurricularGroup == null) {
	    extraCurricularGroup = NoCourseGroupCurriculumGroup.createNewNoCourseGroupCurriculumGroup(
		    groupType, this);
	}

	new Enrolment(studentCurricularPlan, extraCurricularGroup, curricularCourse, executionPeriod,
		EnrollmentCondition.VALIDATED, AccessControl.getUserView().getUtilizador());
    }

    final public NoCourseGroupCurriculumGroup getNoCourseGroupCurriculumGroup(
	    NoCourseGroupCurriculumGroupType groupType) {
	for (final CurriculumGroup curriculumGroup : getCurriculumGroups()) {
	    if (curriculumGroup.isNoCourseGroupCurriculumGroup()) {
		NoCourseGroupCurriculumGroup noCourseGroupCurriculumGroup = (NoCourseGroupCurriculumGroup) curriculumGroup;
		if (noCourseGroupCurriculumGroup.getNoCourseGroupCurriculumGroupType().equals(groupType)) {
		    return noCourseGroupCurriculumGroup;
		}
	    }
	}

	return null;
    }

    public boolean isNoCourseGroupCurriculumGroup() {
	return false;
    }

    @Override
    final public Double getEctsCredits() {
	BigDecimal bigDecimal = BigDecimal.ZERO;
	for (CurriculumModule curriculumModule : getCurriculumModulesSet()) {
	    bigDecimal = bigDecimal.add(new BigDecimal(curriculumModule.getEctsCredits()));
	}
	return Double.valueOf(bigDecimal.doubleValue());
    }

    @Override
    final public Double getAprovedEctsCredits() {
	BigDecimal bigDecimal = BigDecimal.ZERO;
	for (CurriculumModule curriculumModule : getCurriculumModulesSet()) {
	    bigDecimal = bigDecimal.add(new BigDecimal(curriculumModule.getAprovedEctsCredits()));
	}
	return Double.valueOf(bigDecimal.doubleValue());
    }

    @Override
    final public Double getEnroledEctsCredits(final ExecutionPeriod executionPeriod) {
	BigDecimal bigDecimal = BigDecimal.ZERO;
	for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
	    bigDecimal = bigDecimal.add(new BigDecimal(curriculumModule
		    .getEnroledEctsCredits(executionPeriod)));
	}
	return Double.valueOf(bigDecimal.doubleValue());
    }

    final public int getNumberOfChildCurriculumGroupsWithCourseGroup() {
	int result = 0;
	for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
	    if (!curriculumModule.isLeaf()) {
		final CurriculumGroup curriculumGroup = (CurriculumGroup) curriculumModule;
		if (!curriculumGroup.isNoCourseGroupCurriculumGroup()) {
		    result++;
		}
	    }
	}
	return result;
    }

    final public int getNumberOfApprovedEnrolments() {
	int result = 0;
	for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
	    if (curriculumModule.isLeaf()) {
		final CurriculumLine curriculumLine = (CurriculumLine) curriculumModule;
		if (curriculumLine.isEnrolment() && ((Enrolment) curriculumLine).isApproved()) {
		    result++;
		}
	    }
	}
	return result;
    }

    final public int getNumberOfEnrolments(final ExecutionPeriod executionPeriod) {
	int result = 0;
	for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
	    if (curriculumModule instanceof Enrolment) {
		final Enrolment enrolment = (Enrolment) curriculumModule;
		if (enrolment.isValid(executionPeriod) && enrolment.isEnroled()) {
		    result++;
		}
	    }
	}
	return result;
    }

    @Override
    public Set<IDegreeModuleToEvaluate> getDegreeModulesToEvaluate(final ExecutionPeriod executionPeriod) {
	final Set<IDegreeModuleToEvaluate> result = new HashSet<IDegreeModuleToEvaluate>();
	result.add(new CurriculumModuleEnroledWrapper(this, executionPeriod));

	for (final CurriculumModule curriculumModule : getCurriculumModules()) {
	    result.addAll(curriculumModule.getDegreeModulesToEvaluate(executionPeriod));
	}
	return result;
    }

    @Override
    final public Collection<Enrolment> getSpecialSeasonEnrolments(final ExecutionYear executionYear) {
	Collection<Enrolment> result = new HashSet<Enrolment>();
	for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
	    result.addAll(curriculumModule.getSpecialSeasonEnrolments(executionYear));
	}
	return result;
    }

    @Override
    final public void getAllDegreeModules(final Collection<DegreeModule> degreeModules) {
	degreeModules.add(getDegreeModule());
	for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
	    curriculumModule.getAllDegreeModules(degreeModules);
	}
    }

    @Override
    public boolean isConcluded(ExecutionYear executionYear) {
	return checkCreditsLimits(executionYear) || checkDegreeModulesSelectionLimit(executionYear)
		|| checkAllModules(executionYear);
    }

    private boolean checkAllModules(final ExecutionYear executionYear) {
	for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
	    if (!curriculumModule.isConcluded(executionYear)) {
		return false;
	    }
	}
	return hasAnyCurriculumModules();
    }

    @SuppressWarnings("unchecked")
    private boolean checkDegreeModulesSelectionLimit(ExecutionYear executionYear) {
	List<DegreeModulesSelectionLimit> curricularRules = (List<DegreeModulesSelectionLimit>) getDegreeModule()
		.getCurricularRules(CurricularRuleType.DEGREE_MODULES_SELECTION_LIMIT, executionYear);
	if (curricularRules.size() > 1) {
	    throw new DomainException(
		    "error.degree.module.has.more.than.one.degree.modules.limit.for.executionPeriod");
	}

	if (curricularRules.isEmpty()) {
	    return false;
	} else {
	    DegreeModulesSelectionLimit degreeModulesSelectionLimit = curricularRules.get(0);
	    int modulesConcluded = 0;
	    for (CurriculumModule curriculumModule : getCurriculumModulesSet()) {
		if (curriculumModule.isConcluded(executionYear)) {
		    modulesConcluded++;
		}
	    }

	    return modulesConcluded >= degreeModulesSelectionLimit.getMinimumLimit();
	}
    }

    @SuppressWarnings("unchecked")
    private boolean checkCreditsLimits(ExecutionYear executionYear) {
	List<CreditsLimit> curricularRules = (List<CreditsLimit>) getDegreeModule().getCurricularRules(
		CurricularRuleType.CREDITS_LIMIT, executionYear);
	if (curricularRules.size() > 1) {
	    throw new DomainException(
		    "error.degree.module.has.more.than.one.credits.limit.for.executionPeriod");
	}

	if (curricularRules.isEmpty()) {
	    return false;
	} else {
	    CreditsLimit creditsLimit = curricularRules.get(0);
	    Double creditsConcluded = 0d;
	    for (CurriculumModule curriculumModule : getCurriculumModulesSet()) {
		creditsConcluded += curriculumModule.getCreditsConcluded(executionYear);
	    }

	    return creditsConcluded >= creditsLimit.getMinimumCredits();
	}
    }

    @Override
    @SuppressWarnings("unchecked")
    public Double getCreditsConcluded(ExecutionYear executionYear) {
	
	final CourseGroup courseGroup = isRoot() ? getDegreeModule() : getCurriculumGroup().getDegreeModule();
	final List<CreditsLimit> curricularRules = (List<CreditsLimit>) getDegreeModule().getCurricularRules(
		CurricularRuleType.CREDITS_LIMIT, courseGroup, executionYear);
	if (curricularRules.size() > 1) {
	    throw new DomainException(
		    "error.degree.module.has.more.than.one.credits.limit.for.executionPeriod");
	}

	Double creditsConcluded = 0d;
	for (CurriculumModule curriculumModule : getCurriculumModulesSet()) {
	    creditsConcluded += curriculumModule.getCreditsConcluded(executionYear);
	}

	if (curricularRules.isEmpty()) {
	    return creditsConcluded;
	} else {
	    CreditsLimit creditsLimit = curricularRules.get(0);
	    return Math.min(creditsLimit.getMaximumCredits(), creditsConcluded);
	}
    }

    @Override
    final public YearMonthDay getConclusionDate() {
	final Collection<CurriculumModule> curriculumModules = new HashSet<CurriculumModule>(getCurriculumModulesSet());
	
	YearMonthDay result = curriculumModules.iterator().next().getConclusionDate();
	for (final CurriculumModule curriculumModule : curriculumModules) {
	    final YearMonthDay curriculumModuleConclusionDate = curriculumModule.getConclusionDate();
	    if (curriculumModuleConclusionDate.isAfter(result)) {
		result = curriculumModuleConclusionDate;
	    }
	}
	
	return result;
    }
    
    @Override
    public boolean isPropaedeutic() {
	return hasCurriculumGroup() && getCurriculumGroup().isPropaedeutic();
    }

}
