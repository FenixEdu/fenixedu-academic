package net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.curricularRules.CreditsLimit;
import net.sourceforge.fenixedu.domain.curricularRules.DegreeModulesSelectionLimit;
import net.sourceforge.fenixedu.domain.curricularRules.Exclusiveness;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.PreviousYearsEnrolmentCurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.executors.RuleResult;
import net.sourceforge.fenixedu.domain.curricularRules.executors.verifyExecutors.VerifyRuleLevel;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.degreeStructure.OptionalCurricularCourse;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;

public class PreviousYearsEnrolmentExecutor extends CurricularRuleExecutor {

    private static class CollectContext {

	public double ectsCredits;

	public CollectContext parentContext;

	public CollectContext() {
	    this(null);
	}

	public CollectContext(final CollectContext parentContext) {
	    this.parentContext = parentContext;

	}

	public boolean hasCreditsToSpent(final double ectsCredits) {
	    if (this.ectsCredits >= ectsCredits) {
		return true;
	    }

	    if (this.parentContext == null) {
		return false;
	    }

	    return this.parentContext.hasCreditsToSpent(ectsCredits - this.ectsCredits);

	}

	public void useCredits(final double ectsCredits) {
	    if (this.ectsCredits >= ectsCredits) {
		this.ectsCredits = this.ectsCredits - ectsCredits;
	    } else {
		double creditsMissing = ectsCredits - this.ectsCredits;
		if (this.parentContext == null) {
		    throw new DomainException(
			    "error.net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors.CollectContext.parentContent.is.expected");
		}

		this.parentContext.useCredits(creditsMissing);
	    }

	}

    }

    @Override
    protected RuleResult executeEnrolmentWithRules(final ICurricularRule curricularRule,
	    IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {

	if (isEnrollingInCourseGroupsOnly(enrolmentContext, sourceDegreeModuleToEvaluate)) {
	    return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
	}

	final PreviousYearsEnrolmentCurricularRule previousYearsEnrolmentCurricularRule = (PreviousYearsEnrolmentCurricularRule) curricularRule;
	final Map<Integer, Set<CurricularCourse>> curricularCoursesToEnrolByYear = getCurricularCoursesToEnrolByYear(
		previousYearsEnrolmentCurricularRule, enrolmentContext, sourceDegreeModuleToEvaluate, false);

	return hasAnyCurricularCoursesToEnrolInPreviousYears(enrolmentContext, curricularCoursesToEnrolByYear,
		sourceDegreeModuleToEvaluate);

    }

    private boolean hasCurricularCoursesToEnrolInPreviousYears(
	    Map<Integer, Set<CurricularCourse>> curricularCoursesToEnrolByYear, Integer curricularYear) {
	for (int i = curricularYear; i > 0; i--) {
	    final int previousYear = i - 1;

	    if (!curricularCoursesToEnrolByYear.containsKey(previousYear)) {
		continue;
	    }

	    if (!curricularCoursesToEnrolByYear.get(previousYear).isEmpty()) {
		return true;
	    }
	}

	return false;

    }

    private RuleResult hasAnyCurricularCoursesToEnrolInPreviousYears(final EnrolmentContext enrolmentContext,
	    final Map<Integer, Set<CurricularCourse>> curricularCoursesToEnrolByYear,
	    final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate) {

	RuleResult result = RuleResult.createTrue(sourceDegreeModuleToEvaluate.getDegreeModule());
	for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : enrolmentContext
		.getAllChildDegreeModulesToEvaluateFor(sourceDegreeModuleToEvaluate.getDegreeModule())) {
	    if (degreeModuleToEvaluate.isLeaf()
		    && hasCurricularCoursesToEnrolInPreviousYears(curricularCoursesToEnrolByYear, degreeModuleToEvaluate
			    .getContext().getCurricularYear())) {

		if (degreeModuleToEvaluate.isEnroled()) {
		    result = result.and(RuleResult.createTrue(EnrolmentResultType.IMPOSSIBLE, degreeModuleToEvaluate
			    .getDegreeModule()));

		} else {
		    result = result.and(RuleResult.createFalse(sourceDegreeModuleToEvaluate.getDegreeModule(),
			    "curricularRules.ruleExecutors.PreviousYearsEnrolmentExecutor", sourceDegreeModuleToEvaluate
				    .getName(), degreeModuleToEvaluate.getContext().getCurricularYear().toString()));
		}
	    }
	}

	return result;
    }

    private boolean isEnrollingInCourseGroupsOnly(final EnrolmentContext enrolmentContext,
	    final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate) {
	for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : enrolmentContext
		.getAllChildDegreeModulesToEvaluateFor(sourceDegreeModuleToEvaluate.getDegreeModule())) {
	    if (degreeModuleToEvaluate.isLeaf()) {
		return false;
	    }
	}

	return true;
    }

    private Map<Integer, Set<CurricularCourse>> getCurricularCoursesToEnrolByYear(
	    final PreviousYearsEnrolmentCurricularRule previousYearsEnrolmentCurricularRule,
	    final EnrolmentContext enrolmentContext, final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate,
	    final boolean withTemporaryEnrolments) {
	final Map<Integer, Set<CurricularCourse>> result = new HashMap<Integer, Set<CurricularCourse>>();

	collectCourseGroupCurricularCoursesToEnrol(result, previousYearsEnrolmentCurricularRule.getDegreeModuleToApplyRule(),
		new CollectContext(), enrolmentContext, sourceDegreeModuleToEvaluate, withTemporaryEnrolments);

	return result;
    }

    private void collectCourseGroupCurricularCoursesToEnrol(final Map<Integer, Set<CurricularCourse>> result,
	    final CourseGroup courseGroup, final CollectContext collectContext, final EnrolmentContext enrolmentContext,
	    final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, boolean withTemporaryEnrolments) {

	if (!isToCollectCurricularCourses(courseGroup, enrolmentContext, sourceDegreeModuleToEvaluate, withTemporaryEnrolments)) {
	    return;
	}

	int childDegreeModulesCount = courseGroup.getChildDegreeModulesValidOn(enrolmentContext.getExecutionPeriod()).size();

	collectCurricularCoursesToEnrol(result, courseGroup, collectContext, enrolmentContext, sourceDegreeModuleToEvaluate,
		withTemporaryEnrolments);

	final int minModules = getMinModules(courseGroup, enrolmentContext.getExecutionPeriod());
	final int maxModules = getMaxModules(courseGroup, enrolmentContext.getExecutionPeriod());
	if (minModules == maxModules) {
	    if (maxModules == childDegreeModulesCount) {
		// N-N == Nchilds
		collectChildCourseGroupsCurricularCoursesToEnrol(result, courseGroup, collectContext, enrolmentContext,
			sourceDegreeModuleToEvaluate, withTemporaryEnrolments);
	    } else {
		// N-N <> Nchilds
		if (getSelectedChildDegreeModules(courseGroup, enrolmentContext).size() < minModules) {
		    collectChildCourseGroupsCurricularCoursesToEnrol(result, courseGroup, collectContext, enrolmentContext,
			    sourceDegreeModuleToEvaluate, withTemporaryEnrolments);
		} else {
		    collectSelectedChildCourseGroupsCurricularCoursesToEnrol(result, courseGroup, collectContext,
			    enrolmentContext, sourceDegreeModuleToEvaluate, withTemporaryEnrolments);
		}
	    }
	} else {
	    // N-M
	    if (getSelectedChildDegreeModules(courseGroup, enrolmentContext).size() < minModules) {
		collectChildCourseGroupsCurricularCoursesToEnrol(result, courseGroup, collectContext, enrolmentContext,
			sourceDegreeModuleToEvaluate, withTemporaryEnrolments);
	    } else {
		collectSelectedChildCourseGroupsCurricularCoursesToEnrol(result, courseGroup, collectContext, enrolmentContext,
			sourceDegreeModuleToEvaluate, withTemporaryEnrolments);
	    }
	}

    }

    private boolean isToCollectCurricularCourses(CourseGroup courseGroup, EnrolmentContext enrolmentContext,
	    IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, boolean withTemporaryEnrolments) {
	return !isConcluded(courseGroup, enrolmentContext, sourceDegreeModuleToEvaluate, withTemporaryEnrolments)
		|| !isExclusiveWithExisting(courseGroup, enrolmentContext);
    }

    private boolean isExclusiveWithExisting(CourseGroup courseGroup, EnrolmentContext enrolmentContext) {
	for (final Exclusiveness exclusiveness : courseGroup.getExclusivenessRules(enrolmentContext.getExecutionPeriod())) {
	    if (isEnroled(enrolmentContext, exclusiveness.getExclusiveDegreeModule())) {
		return true;
	    }
	}

	return false;
    }

    private boolean isConcluded(final CourseGroup courseGroup, final EnrolmentContext enrolmentContext,
	    final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final boolean withTemporaryEnrolments) {
	final CurriculumGroup curriculumGroup = enrolmentContext.getStudentCurricularPlan().findCurriculumGroupFor(courseGroup);

	if (curriculumGroup == null) {
	    return false;
	}

	final double minEctsToApprove = curriculumGroup.getDegreeModule().getMinEctsCredits();
	double totalEcts = curriculumGroup.getAprovedEctsCredits();

	totalEcts += curriculumGroup.getEnroledEctsCredits(enrolmentContext.getExecutionPeriod());

	if (withTemporaryEnrolments) {
	    totalEcts += curriculumGroup
		    .getEnroledEctsCredits(enrolmentContext.getExecutionPeriod().getPreviousExecutionPeriod());
	}

	return totalEcts >= minEctsToApprove;
    }

    private void collectSelectedChildCourseGroupsCurricularCoursesToEnrol(Map<Integer, Set<CurricularCourse>> result,
	    CourseGroup courseGroup, CollectContext collectContext, EnrolmentContext enrolmentContext,
	    IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, boolean withTemporaryEnrolments) {

	for (final DegreeModule degreeModule : getSelectedChildDegreeModules(courseGroup, enrolmentContext)) {
	    if (degreeModule.isCourseGroup()) {
		collectCurricularCoursesToEnrol(result, (CourseGroup) degreeModule, new CollectContext(collectContext),
			enrolmentContext, sourceDegreeModuleToEvaluate, withTemporaryEnrolments);
	    }
	}

    }

    private Set<DegreeModule> getSelectedChildDegreeModules(final CourseGroup courseGroup, final EnrolmentContext enrolmentContext) {
	final Set<DegreeModule> result = new HashSet<DegreeModule>();
	for (final DegreeModule degreeModule : courseGroup.getChildDegreeModulesValidOn(enrolmentContext.getExecutionPeriod())) {
	    if (enrolmentContext.getStudentCurricularPlan().hasDegreeModule(degreeModule))
		result.add(degreeModule);
	}

	for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : enrolmentContext.getDegreeModulesToEvaluate()) {
	    if (degreeModuleToEvaluate.getCurriculumGroup() != null
		    && degreeModuleToEvaluate.getCurriculumGroup().getDegreeModule() == courseGroup) {
		result.add(degreeModuleToEvaluate.getDegreeModule());
	    }
	}

	return result;

    }

    private int getMinModules(final CourseGroup courseGroup, final ExecutionPeriod executionPeriod) {
	final DegreeModulesSelectionLimit degreeModulesSelectionLimit = courseGroup
		.getDegreeModulesSelectionLimitRule(executionPeriod);
	if (degreeModulesSelectionLimit != null) {
	    return degreeModulesSelectionLimit.getMinimumLimit();
	}

	final CreditsLimit creditsLimit = courseGroup.getCreditsLimitRule(executionPeriod);
	if (creditsLimit != null) {
	    final SortedSet<DegreeModule> sortedChilds = new TreeSet<DegreeModule>(new DegreeModule.ComparatorByMinEcts(
		    executionPeriod));
	    sortedChilds.addAll(courseGroup.getChildDegreeModulesValidOn(executionPeriod));
	    int counter = 0;
	    double total = 0d;
	    for (final DegreeModule degreeModule : sortedChilds) {
		total += degreeModule.getMinEctsCredits();
		if (total > creditsLimit.getMinimumCredits()) {
		    break;
		} else {
		    counter++;
		}
	    }

	    return counter;
	}

	return courseGroup.getChildDegreeModulesValidOn(executionPeriod).size();
    }

    private int getMaxModules(final CourseGroup courseGroup, final ExecutionPeriod executionPeriod) {
	final DegreeModulesSelectionLimit degreeModulesSelectionLimit = courseGroup
		.getDegreeModulesSelectionLimitRule(executionPeriod);
	if (degreeModulesSelectionLimit != null) {
	    return degreeModulesSelectionLimit.getMaximumLimit();
	}

	final CreditsLimit creditsLimit = courseGroup.getCreditsLimitRule(executionPeriod);
	if (creditsLimit != null) {
	    final SortedSet<DegreeModule> sortedChilds = new TreeSet<DegreeModule>(new DegreeModule.ComparatorByMaxEcts(
		    executionPeriod));
	    sortedChilds.addAll(courseGroup.getChildDegreeModulesValidOn(executionPeriod));
	    int counter = 0;
	    double total = 0d;
	    for (final DegreeModule degreeModule : sortedChilds) {
		total += degreeModule.getMaxEctsCredits();
		if (total > creditsLimit.getMaximumCredits()) {
		    break;
		} else {
		    counter++;
		}
	    }

	    return counter;

	}

	return courseGroup.getChildDegreeModulesValidOn(executionPeriod).size();
    }

    private void collectCurricularCoursesToEnrol(final Map<Integer, Set<CurricularCourse>> result, final CourseGroup courseGroup,
	    final CollectContext collectContext, final EnrolmentContext enrolmentContext,
	    final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final boolean withTemporaryEnrolments) {

	final CurriculumGroup curriculumGroup = enrolmentContext.getStudentCurricularPlan().findCurriculumGroupFor(courseGroup);
	collectContext.ectsCredits = curriculumGroup != null ? curriculumGroup.getChildCreditsDismissalEcts() : 0;

	final SortedSet<CurricularCourse> sortedCurricularCourses = getChildCurricularCoursesToEvaluate(courseGroup,
		enrolmentContext);
	final Iterator<CurricularCourse> iterator = sortedCurricularCourses.iterator();
	while (iterator.hasNext()) {
	    final CurricularCourse curricularCourse = iterator.next();
	    if (isApproved(enrolmentContext, curricularCourse)
		    || isEnroled(enrolmentContext, curricularCourse, withTemporaryEnrolments)
		    || isEnrolling(enrolmentContext, curricularCourse)) {
		iterator.remove();
	    } else if (!isCurricularRulesSatisfied(enrolmentContext, curricularCourse, sourceDegreeModuleToEvaluate)) {
		iterator.remove();
	    } else {
		final double creditsRequiredToApprove = curricularCourse.isOptionalCurricularCourse() ? ((OptionalCurricularCourse) curricularCourse)
			.getMinEctsCredits(enrolmentContext.getExecutionPeriod())
			: curricularCourse.getEctsCredits(enrolmentContext.getExecutionPeriod());
		if (collectContext.hasCreditsToSpent(creditsRequiredToApprove)) {
		    collectContext.useCredits(creditsRequiredToApprove);
		    iterator.remove();

		}
	    }
	}

	addCurricularCourses(result, sortedCurricularCourses, courseGroup, enrolmentContext.getExecutionPeriod());
    }

    private boolean isEnroled(final EnrolmentContext enrolmentContext, final CurricularCourse curricularCourse,
	    final boolean withTemporaryEnrolments) {
	if (withTemporaryEnrolments) {
	    return isEnroled(enrolmentContext, curricularCourse, enrolmentContext.getExecutionPeriod())
		    || isEnroled(enrolmentContext, curricularCourse, enrolmentContext.getExecutionPeriod()
			    .getPreviousExecutionPeriod());
	}

	return isEnroled(enrolmentContext, curricularCourse, enrolmentContext.getExecutionPeriod());

    }

    private boolean isCurricularRulesSatisfied(EnrolmentContext enrolmentContext, CurricularCourse curricularCourse,
	    IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate) {
	RuleResult result = RuleResult.createTrue(sourceDegreeModuleToEvaluate.getDegreeModule());
	for (final ICurricularRule curricularRule : curricularCourse.getCurricularRules(enrolmentContext.getExecutionPeriod())) {
	    result = result.and(curricularRule.verify(getVerifyRuleLevel(enrolmentContext), enrolmentContext, curricularCourse,
		    (CourseGroup) sourceDegreeModuleToEvaluate.getDegreeModule()));
	}

	return result.isTrue();

    }

    private VerifyRuleLevel getVerifyRuleLevel(final EnrolmentContext enrolmentContext) {
	return enrolmentContext.getCurricularRuleLevel() == CurricularRuleLevel.ENROLMENT_WITH_RULES_AND_TEMPORARY_ENROLMENT ? VerifyRuleLevel.WITH_RULES_AND_TEMPORARY
		: VerifyRuleLevel.WITH_RULES;
    }

    private SortedSet<CurricularCourse> getChildCurricularCoursesToEvaluate(final CourseGroup courseGroup,
	    final EnrolmentContext enrolmentContext) {
	final ExecutionPeriod executionPeriod = enrolmentContext.getExecutionPeriod();

	final SortedSet<CurricularCourse> result = new TreeSet<CurricularCourse>(new Comparator<CurricularCourse>() {
	    public int compare(CurricularCourse left, CurricularCourse right) {
		final Integer leftCurricularYear = Collections.min(left.getParentContextsBy(executionPeriod, courseGroup),
			Context.COMPARATOR_BY_CURRICULAR_YEAR).getCurricularYear();
		final Integer rightCurricularYear = Collections.min(right.getParentContextsBy(executionPeriod, courseGroup),
			Context.COMPARATOR_BY_CURRICULAR_YEAR).getCurricularYear();

		int result = leftCurricularYear.compareTo(rightCurricularYear);

		return result == 0 ? left.getIdInternal().compareTo(right.getIdInternal()) : result;

	    }
	});

	final int minModules = getMinModules(courseGroup, executionPeriod);
	final int maxModules = getMaxModules(courseGroup, executionPeriod);
	final int childDegreeModulesCount = courseGroup.getChildDegreeModulesValidOn(executionPeriod).size();

	if (minModules == maxModules) {
	    if (maxModules == childDegreeModulesCount) {
		// N-N == Nchilds
		result.addAll(courseGroup.getChildCurricularCoursesValidOn(executionPeriod));
	    } else {
		// N-N <> Nchilds
		if (getSelectedChildDegreeModules(courseGroup, enrolmentContext).size() < minModules) {
		    result.addAll(courseGroup.getChildCurricularCoursesValidOn(executionPeriod));
		} else {
		    result.addAll(getSelectedChildCurricularCourses(courseGroup, enrolmentContext));
		}
	    }
	} else {
	    // N-M
	    if (getSelectedChildDegreeModules(courseGroup, enrolmentContext).size() < minModules) {
		result.addAll(courseGroup.getChildCurricularCoursesValidOn(executionPeriod));
	    } else {
		result.addAll(getSelectedChildCurricularCourses(courseGroup, enrolmentContext));
	    }
	}

	return result;

    }

    private Set<CurricularCourse> getSelectedChildCurricularCourses(final CourseGroup courseGroup,
	    final EnrolmentContext enrolmentContext) {
	final Set<CurricularCourse> result = new HashSet<CurricularCourse>();
	for (final DegreeModule degreeModule : getSelectedChildDegreeModules(courseGroup, enrolmentContext)) {
	    if (degreeModule.isCurricularCourse()) {
		result.add((CurricularCourse) degreeModule);
	    }
	}

	return result;
    }

    private void addCurricularCourses(final Map<Integer, Set<CurricularCourse>> result,
	    final Set<CurricularCourse> curricularCourses, final CourseGroup courseGroup, final ExecutionPeriod executionPeriod) {
	for (final CurricularCourse curricularCourse : curricularCourses) {
	    for (final Context context : curricularCourse.getParentContextsBy(executionPeriod, courseGroup)) {
		addCurricularCourse(result, context.getCurricularYear(), curricularCourse);
	    }
	}
    }

    private void addCurricularCourse(Map<Integer, Set<CurricularCourse>> result, Integer curricularYear,
	    CurricularCourse curricularCourse) {
	Set<CurricularCourse> curricularCourses = result.get(curricularYear);

	if (curricularCourses == null) {
	    curricularCourses = new HashSet<CurricularCourse>();
	    result.put(curricularYear, curricularCourses);
	}

	curricularCourses.add(curricularCourse);

    }

    private void collectChildCourseGroupsCurricularCoursesToEnrol(final Map<Integer, Set<CurricularCourse>> result,
	    final CourseGroup courseGroup, final CollectContext parentCollectContext, final EnrolmentContext enrolmentContext,
	    final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final boolean withTemporaryEnrolments) {
	for (final DegreeModule childDegreeModule : courseGroup.getChildDegreeModulesValidOn(enrolmentContext
		.getExecutionPeriod())) {
	    if (childDegreeModule.isCourseGroup()) {
		collectCourseGroupCurricularCoursesToEnrol(result, (CourseGroup) childDegreeModule, new CollectContext(
			parentCollectContext), enrolmentContext, sourceDegreeModuleToEvaluate, withTemporaryEnrolments);
	    }
	}
    }

    @Override
    protected RuleResult executeEnrolmentWithRulesAndTemporaryEnrolment(final ICurricularRule curricularRule,
	    IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {

	if (isEnrollingInCourseGroupsOnly(enrolmentContext, sourceDegreeModuleToEvaluate)) {
	    return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
	}

	final PreviousYearsEnrolmentCurricularRule previousYearsEnrolmentCurricularRule = (PreviousYearsEnrolmentCurricularRule) curricularRule;
	final Map<Integer, Set<CurricularCourse>> curricularCoursesToEnrolByYear = getCurricularCoursesToEnrolByYear(
		previousYearsEnrolmentCurricularRule, enrolmentContext, sourceDegreeModuleToEvaluate, false);
	final Map<Integer, Set<CurricularCourse>> curricularCoursesToEnrolByYearWithTemporaries = getCurricularCoursesToEnrolByYear(
		previousYearsEnrolmentCurricularRule, enrolmentContext, sourceDegreeModuleToEvaluate, true);

	return hasAnyCurricularCoursesToEnrolInPreviousYears(enrolmentContext, curricularCoursesToEnrolByYear,
		curricularCoursesToEnrolByYearWithTemporaries, sourceDegreeModuleToEvaluate);

    }

    private RuleResult hasAnyCurricularCoursesToEnrolInPreviousYears(final EnrolmentContext enrolmentContext,
	    final Map<Integer, Set<CurricularCourse>> curricularCoursesToEnrolByYear,
	    final Map<Integer, Set<CurricularCourse>> curricularCoursesToEnrolByYearWithTemporaries,
	    final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate) {

	RuleResult result = RuleResult.createTrue(sourceDegreeModuleToEvaluate.getDegreeModule());
	for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : enrolmentContext
		.getAllChildDegreeModulesToEvaluateFor(sourceDegreeModuleToEvaluate.getDegreeModule())) {
	    if (degreeModuleToEvaluate.isLeaf()) {
		if (hasCurricularCoursesToEnrolInPreviousYears(curricularCoursesToEnrolByYearWithTemporaries,
			degreeModuleToEvaluate.getContext().getCurricularYear())) {

		    if (degreeModuleToEvaluate.isEnroled()) {
			result = result.and(RuleResult.createTrue(EnrolmentResultType.IMPOSSIBLE, degreeModuleToEvaluate
				.getDegreeModule()));

		    } else {
			result = result.and(RuleResult.createFalse(sourceDegreeModuleToEvaluate.getDegreeModule(),
				"curricularRules.ruleExecutors.PreviousYearsEnrolmentExecutor", sourceDegreeModuleToEvaluate
					.getName(), degreeModuleToEvaluate.getContext().getCurricularYear().toString()));
		    }

		} else {
		    if (isAnyPreviousYearCurricularCoursesTemporary(curricularCoursesToEnrolByYear,
			    curricularCoursesToEnrolByYearWithTemporaries, degreeModuleToEvaluate.getContext()
				    .getCurricularYear())) {
			result = result.and(RuleResult.createTrue(EnrolmentResultType.TEMPORARY, degreeModuleToEvaluate
				.getDegreeModule()));

		    }
		}
	    }
	}

	return result;
    }

    private boolean isAnyPreviousYearCurricularCoursesTemporary(
	    Map<Integer, Set<CurricularCourse>> curricularCoursesToEnrolByYear,
	    Map<Integer, Set<CurricularCourse>> curricularCoursesToEnrolByYearWithTemporaries, Integer curricularYear) {

	for (int i = curricularYear; i > 0; i--) {
	    final int previousYear = i - 1;

	    if (!curricularCoursesToEnrolByYear.containsKey(previousYear)
		    && !curricularCoursesToEnrolByYearWithTemporaries.containsKey(previousYear)) {
		continue;
	    }

	    if ((curricularCoursesToEnrolByYearWithTemporaries.containsKey(previousYear) && !curricularCoursesToEnrolByYear
		    .containsKey(previousYear))
		    || (!curricularCoursesToEnrolByYearWithTemporaries.containsKey(previousYear) && curricularCoursesToEnrolByYear
			    .containsKey(previousYear))) {
		return true;
	    }

	    if (curricularCoursesToEnrolByYear.get(previousYear).size() != curricularCoursesToEnrolByYearWithTemporaries.get(
		    previousYear).size()) {
		return true;
	    }
	}

	return false;

    }

    @Override
    protected RuleResult executeEnrolmentInEnrolmentEvaluation(final ICurricularRule curricularRule,
	    final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {
	return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
    }
}
