package net.sourceforge.fenixedu.domain.studentCurriculum;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
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
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentCondition;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.enrolment.CurriculumModuleEnroledWrapper;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import dml.runtime.RelationAdapter;

public class CurriculumGroup extends CurriculumGroup_Base {

    static {
	CurriculumModule.CurriculumModuleCurriculumGroup
		.addListener(new RelationAdapter<CurriculumModule, CurriculumGroup>() {
		    @Override
		    public void afterRemove(CurriculumModule curriculumModule,
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

    protected CurriculumGroup() {
	super();
    }

    public CurriculumGroup(StudentCurricularPlan studentCurricularPlan, CourseGroup courseGroup,
	    ExecutionPeriod executionPeriod) {
	super();
	init(studentCurricularPlan, courseGroup, executionPeriod);
    }

    public CurriculumGroup(CurriculumGroup curriculumGroup, CourseGroup courseGroup) {
	this();

	if (courseGroup == null || curriculumGroup == null) {
	    throw new DomainException(
		    "error.studentCurriculum.curriculumGroup.courseGroup.cannot.be.null");
	}
	checkInitConstraints(curriculumGroup.getStudentCurricularPlan(), courseGroup);
	setCurriculumGroup(curriculumGroup);
	setDegreeModule(courseGroup);
    }

    private void checkInitConstraints(StudentCurricularPlan studentCurricularPlan,
	    CourseGroup courseGroup) {
	if (studentCurricularPlan.getRoot().hasCourseGroup(courseGroup)) {
	    throw new DomainException("error.studentCurriculum.CurriculumGroup.duplicate.courseGroup",
		    courseGroup.getName());
	}
    }

    private void init(StudentCurricularPlan studentCurricularPlan, CourseGroup courseGroup,
	    ExecutionPeriod executionPeriod) {
	checkParameters(studentCurricularPlan, courseGroup, executionPeriod);
	setDegreeModule(courseGroup);
	setParentStudentCurricularPlan(studentCurricularPlan);
	addChildCurriculumGroups(courseGroup, executionPeriod);
    }

    private void checkParameters(StudentCurricularPlan studentCurricularPlan, CourseGroup courseGroup,
	    ExecutionPeriod executionPeriod) {
	if (studentCurricularPlan == null) {
	    throw new DomainException(
		    "error.studentCurriculum.curriculumGroup.studentCurricularPlan.cannot.be.null");
	}
	checkParameters(courseGroup, executionPeriod);
    }

    private void checkParameters(CourseGroup courseGroup, ExecutionPeriod executionPeriod) {
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

    private void init(CurriculumGroup curriculumGroup, CourseGroup courseGroup,
	    ExecutionPeriod executionPeriod) {
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

    private void addChildCurriculumGroups(final CourseGroup courseGroup,
	    final ExecutionPeriod executionPeriod) {
	for (final CourseGroup childCourseGroup : courseGroup
		.getNotOptionalChildCourseGroups(executionPeriod)) {
	    new CurriculumGroup(this, childCourseGroup, executionPeriod);
	}
    }

    public boolean isLeaf() {
	return false;
    }

    public boolean canBeDeleted() {
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
    public StringBuilder print(String tabs) {
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
    public CourseGroup getDegreeModule() {
	return (CourseGroup) super.getDegreeModule();
    }

    public List<Enrolment> getEnrolments() {
	final List<Enrolment> result = new ArrayList<Enrolment>();
	for (final CurriculumModule curriculumModule : this.getCurriculumModulesSet()) {
	    result.addAll(curriculumModule.getEnrolments());
	}
	return result;
    }

    public Set<Enrolment> getEnrolmentsSet() {
	final Set<Enrolment> result = new HashSet<Enrolment>();
	for (final CurriculumModule curriculumModule : this.getCurriculumModulesSet()) {
	    result.addAll(curriculumModule.getEnrolments());
	}
	return result;
    }
    
    public void collectDismissals(final List<Dismissal> result) {
	for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
	    curriculumModule.collectDismissals(result);
	}
    }

    public boolean isRoot() {
	return hasParentStudentCurricularPlan();
    }

    @Override
    public StudentCurricularPlan getStudentCurricularPlan() {
	return isRoot() ? getParentStudentCurricularPlan() : getCurriculumGroup()
		.getStudentCurricularPlan();
    }

    // alterar isto, colocar as regras
    private Collection<Context> getDegreeModulesFor(ExecutionPeriod executionPeriod) {
	Collection<Context> result = new HashSet<Context>();
	for (Context context : this.getDegreeModule().getChildContexts(executionPeriod)) {
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
	Set<CurricularCourse> result = new HashSet<CurricularCourse>();
	for (Context context : this.getDegreeModule().getChildContexts(CurricularCourse.class,
		(ExecutionPeriod) null)) {
	    CurricularCourse curricularCourse = (CurricularCourse) context.getChildDegreeModule();
	    if (!getStudentCurricularPlan().getRoot().isAproved(curricularCourse, null)) {
		result.add(curricularCourse);
	    }
	}
	return result;
    }

    @Override
    public boolean isAproved(CurricularCourse curricularCourse, ExecutionPeriod executionPeriod) {
	for (final CurriculumModule curriculumModule : this.getCurriculumModules()) {
	    if (curriculumModule.isAproved(curricularCourse, executionPeriod)) {
		return true;
	    }
	}
	return false;
    }

    @Override
    public boolean isEnroledInExecutionPeriod(CurricularCourse curricularCourse, ExecutionPeriod executionPeriod) {
	for (final CurriculumModule curriculumModule : this.getCurriculumModules()) {
	    if (curriculumModule.isEnroledInExecutionPeriod(curricularCourse, executionPeriod)) {
		return true;
	    }
	}
	return false;
    }
    
    @Override
    public boolean hasEnrolmentWithEnroledState(final CurricularCourse curricularCourse, final ExecutionPeriod executionPeriod) {
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
    public boolean hasCurriculumModule(CurriculumModule curriculumModule) {
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
    public Enrolment findEnrolmentFor(final CurricularCourse curricularCourse, final ExecutionPeriod executionPeriod) {
	for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
	    final Enrolment search = curriculumModule.findEnrolmentFor(curricularCourse, executionPeriod);
	    if (search != null) {
		return search;
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
		final CurriculumGroup searchCurriculumGroup = curriculumGroup.findCurriculumGroupFor(courseGroup);
		if (searchCurriculumGroup != null) {
		    return searchCurriculumGroup;
		}
	    }
	}
	return null;
    }

    public Set<CurriculumLine> getCurriculumLines() {
	Set<CurriculumLine> result = new TreeSet<CurriculumLine>(CurriculumModule.COMPARATOR_BY_NAME);

	for (final CurriculumModule curriculumModule : getCurriculumModules()) {
	    if (curriculumModule.isLeaf()) {
		result.add((CurriculumLine) curriculumModule);
	    }
	}

	return result;
    }

    public Set<CurriculumGroup> getCurriculumGroups() {
	Set<CurriculumGroup> result = new TreeSet<CurriculumGroup>(CurriculumModule.COMPARATOR_BY_NAME);

	for (final CurriculumModule curriculumModule : getCurriculumModules()) {
	    if (!curriculumModule.isLeaf()) {
		result.add((CurriculumGroup) curriculumModule);
	    }
	}

	return result;
    }

    public Integer getChildOrder() {
	return getChildOrder(ExecutionPeriod.readActualExecutionPeriod());
    }

    public Integer getChildOrder(final ExecutionPeriod executionPeriod) {
	return getParentCurriculumGroup().searchChildOrderForChild(this, executionPeriod);
    }

    private CurriculumGroup getParentCurriculumGroup() {
	return getCurriculumGroup();
    }

    protected Integer searchChildOrderForChild(final CurriculumGroup child,
	    final ExecutionPeriod executionPeriod) {
	for (final Context context : getDegreeModule().getChildContexts(executionPeriod)) {
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

    public void createNoCourseGroupCurriculumGroupEnrolment(
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

    public NoCourseGroupCurriculumGroup getNoCourseGroupCurriculumGroup(
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
    public Double getEctsCredits() {
	BigDecimal bigDecimal = BigDecimal.ZERO;
	for (CurriculumModule curriculumModule : getCurriculumModulesSet()) {
	    bigDecimal = bigDecimal.add(new BigDecimal(curriculumModule.getEctsCredits()));
	}
	return Double.valueOf(bigDecimal.doubleValue());
    }

    @Override
    public Double getAprovedEctsCredits() {
	BigDecimal bigDecimal = BigDecimal.ZERO;
	for (CurriculumModule curriculumModule : getCurriculumModulesSet()) {
	    bigDecimal = bigDecimal.add(new BigDecimal(curriculumModule.getAprovedEctsCredits()));
	}
	return Double.valueOf(bigDecimal.doubleValue());
    }

    @Override
    public Double getEnroledEctsCredits(final ExecutionPeriod executionPeriod) {
	BigDecimal bigDecimal = BigDecimal.ZERO;
	for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
	    bigDecimal = bigDecimal.add(new BigDecimal(curriculumModule.getEnroledEctsCredits(executionPeriod)));
	}
	return Double.valueOf(bigDecimal.doubleValue());
    }

    public int getNumberOfChildCurriculumGroupsWithCourseGroup() {
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

    public int getNumberOfApprovedEnrolments() {
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

    public int getNumberOfEnrolments(final ExecutionPeriod executionPeriod) {
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
    public Collection<Enrolment> getSpecialSeasonEnrolments(final ExecutionYear executionYear){
	Collection<Enrolment> result = new HashSet<Enrolment>();
	for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
	    result.addAll(curriculumModule.getSpecialSeasonEnrolments(executionYear));
	}
	return result;
    }

    public CurriculumGroup getFirstCycleCurriculumGroup() {
	if (getDegreeModule().isFirstCycle()) {
	    return this;
	}
	
	for (final CurriculumModule curriculumModule : getCurriculumModules()) {
	    if (curriculumModule.isLeaf()) {
		continue;
	    }
	    final CurriculumGroup firstCycleCurriculumGroup = ((CurriculumGroup) curriculumModule).getFirstCycleCurriculumGroup();
	    if (firstCycleCurriculumGroup != null) {
		return firstCycleCurriculumGroup;
	    }
	}
	
	return null;
    }

    public CurriculumGroup getSecondCycleCurriculumGroup() {
	if (getDegreeModule().isSecondCycle()) {
	    return this;
	}
	
	for (final CurriculumModule curriculumModule : getCurriculumModules()) {
	    if (curriculumModule.isLeaf()) {
		continue;
	    }
	    final CurriculumGroup secondCycleCurriculumGroup = ((CurriculumGroup) curriculumModule).getSecondCycleCurriculumGroup();
	    if (secondCycleCurriculumGroup != null) {
		return secondCycleCurriculumGroup;
	    }
	}
	
	return null;
    }
}
