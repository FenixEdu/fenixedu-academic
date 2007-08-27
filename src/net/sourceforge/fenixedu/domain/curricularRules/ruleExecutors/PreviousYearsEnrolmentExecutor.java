package net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Map.Entry;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.curricularRules.CreditsLimit;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRuleType;
import net.sourceforge.fenixedu.domain.curricularRules.DegreeModulesSelectionLimit;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.PreviousYearsEnrolmentCurricularRule;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
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

	if (isEnrollingInCourseGroupsOnly(enrolmentContext)) {
	    return RuleResult.createNA();
	}

	final PreviousYearsEnrolmentCurricularRule previousYearsEnrolmentCurricularRule = (PreviousYearsEnrolmentCurricularRule) curricularRule;
	final Map<Integer, Set<CurricularCourse>> curricularCoursesToEnrolByYear = getCurricularCoursesToEnrolByYear(
		previousYearsEnrolmentCurricularRule, enrolmentContext);

	System.out.println(curricularCoursesToEnrolByYear.size());

	for (final Entry<Integer, Set<CurricularCourse>> each : curricularCoursesToEnrolByYear.entrySet()) {
	    System.out.println("Year " + each.getKey().toString());
	    for (final CurricularCourse curricularCourse : each.getValue()) {
		System.out.println(curricularCourse.getName());
	    }
	}

	return RuleResult.createFalseWithLiteralMessage("Implementar");
    }

    private boolean isEnrollingInCourseGroupsOnly(EnrolmentContext enrolmentContext) {
	for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : enrolmentContext.getDegreeModulesToEvaluate()) {
	    if (degreeModuleToEvaluate.isLeaf()) {
		return false;
	    }
	}

	return true;
    }

    private Map<Integer, Set<CurricularCourse>> getCurricularCoursesToEnrolByYear(
	    final PreviousYearsEnrolmentCurricularRule previousYearsEnrolmentCurricularRule,
	    final EnrolmentContext enrolmentContext) {
	final Map<Integer, Set<CurricularCourse>> result = new HashMap<Integer, Set<CurricularCourse>>();

	collectCourseGroupCurricularCoursesToEnrol(result, previousYearsEnrolmentCurricularRule.getDegreeModuleToApplyRule(),
		new CollectContext(), enrolmentContext);

	return result;
    }

    private void collectCourseGroupCurricularCoursesToEnrol(final Map<Integer, Set<CurricularCourse>> result,
	    final CourseGroup courseGroup, final CollectContext collectContext, final EnrolmentContext enrolmentContext) {

	// Falta os 2º ciclos (mestrados e mestrados integrados)

	int childDegreeModules = courseGroup.getChildDegreeModulesValidOn(enrolmentContext.getExecutionPeriod()).size();

	collectCurricularCoursesToEnrol(result, courseGroup, collectContext, enrolmentContext);

	final int minModules = getMinModules(courseGroup, enrolmentContext.getExecutionPeriod());
	final int maxModules = getMaxModules(courseGroup, enrolmentContext.getExecutionPeriod());
	if (minModules == maxModules) {
	    if (maxModules == childDegreeModules) {
		// N-N == Nchilds
		collectChildCourseGroupsCurricularCoursesToEnrol(result, courseGroup, collectContext, enrolmentContext);
	    } else {
		// N-N <> Nchilds

	    }
	} else {
	    // N-M
	}

    }

    private int getMinModules(final CourseGroup courseGroup, final ExecutionPeriod executionPeriod) {
	final DegreeModulesSelectionLimit degreeModulesSelectionLimit = getDegreeModulesSelectionLimitRule(courseGroup,
		executionPeriod);
	if (degreeModulesSelectionLimit != null) {
	    return degreeModulesSelectionLimit.getMinimumLimit();
	}

	final CreditsLimit creditsLimit = getCreditsLimitRule(courseGroup, executionPeriod);
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
	final DegreeModulesSelectionLimit degreeModulesSelectionLimit = getDegreeModulesSelectionLimitRule(courseGroup,
		executionPeriod);
	if (degreeModulesSelectionLimit != null) {
	    return degreeModulesSelectionLimit.getMaximumLimit();
	}

	final CreditsLimit creditsLimit = getCreditsLimitRule(courseGroup, executionPeriod);
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
	    final CollectContext collectContext, final EnrolmentContext enrolmentContext) {

	final CurriculumGroup curriculumGroup = enrolmentContext.getStudentCurricularPlan().findCurriculumGroupFor(courseGroup);
	collectContext.ectsCredits = curriculumGroup != null ? curriculumGroup.getChildCreditsDismissalEcts() : 0;

	final SortedSet<Context> sortedContexts = new TreeSet<Context>(Context.COMPARATOR_BY_CURRICULAR_YEAR);
	sortedContexts.addAll(courseGroup.getChildContextsForCurricularCourses(enrolmentContext.getExecutionPeriod()));

	final Iterator<Context> iterator = sortedContexts.iterator();
	while (iterator.hasNext()) {
	    final Context context = iterator.next();
	    final CurricularCourse curricularCourse = (CurricularCourse) context.getChildDegreeModule();
	    if (isApproved(enrolmentContext, curricularCourse) || isEnroled(enrolmentContext, curricularCourse)
		    || isEnrolling(enrolmentContext, curricularCourse)) {
		iterator.remove();
	    } else if (!canUseCurricularCourse(enrolmentContext, curricularCourse)) {
		iterator.remove();
	    } else if (collectContext.hasCreditsToSpent(curricularCourse.getEctsCredits())) {
		collectContext.useCredits(curricularCourse.getEctsCredits());
		iterator.remove();
	    }
	}

	addContexts(result, sortedContexts);

    }

    private boolean canUseCurricularCourse(EnrolmentContext enrolmentContext, CurricularCourse curricularCourse) {
	// TODO: adicionar modo de correr regras em modo anos anteriores
	return true;
    }

    private void addContexts(final Map<Integer, Set<CurricularCourse>> result, Set<Context> contexts) {
	for (final Context context : contexts) {
	    addCurricularCourse(result, context.getCurricularYear(), (CurricularCourse) context.getChildDegreeModule());
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
	    final CourseGroup courseGroup, final CollectContext parentCollectContext, final EnrolmentContext enrolmentContext) {
	for (final DegreeModule childDegreeModule : courseGroup.getChildDegreeModulesValidOn(enrolmentContext
		.getExecutionPeriod())) {
	    if (childDegreeModule.isCourseGroup()) {
		collectCourseGroupCurricularCoursesToEnrol(result, (CourseGroup) childDegreeModule, new CollectContext(
			parentCollectContext), enrolmentContext);
	    }
	}
    }

    @SuppressWarnings("unchecked")
    private DegreeModulesSelectionLimit getDegreeModulesSelectionLimitRule(final CourseGroup courseGroup,
	    final ExecutionPeriod executionPeriod) {
	final List<DegreeModulesSelectionLimit> result = (List<DegreeModulesSelectionLimit>) courseGroup.getCurricularRules(
		CurricularRuleType.DEGREE_MODULES_SELECTION_LIMIT, executionPeriod);

	if (result.isEmpty()) {
	    return null;
	}

	if (result.size() > 1) {
	    throw new DomainException(
		    "error.net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors.PreviousYearsEnrolmentExecutor.more.than.one.degree.module.selection.limit.rule.for.execution.period");
	}

	return result.iterator().next();
    }

    @SuppressWarnings("unchecked")
    private CreditsLimit getCreditsLimitRule(final CourseGroup courseGroup, final ExecutionPeriod executionPeriod) {
	final List<CreditsLimit> result = (List<CreditsLimit>) courseGroup.getCurricularRules(CurricularRuleType.CREDITS_LIMIT,
		executionPeriod);

	if (result.isEmpty()) {
	    return null;
	}

	if (result.size() > 1) {
	    throw new DomainException(
		    "error.net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors.PreviousYearsEnrolmentExecutor.more.than.one.credits.limit.rule.for.execution.period");
	}

	return result.iterator().next();
    }

    @Override
    protected RuleResult executeEnrolmentWithRulesAndTemporaryEnrolment(final ICurricularRule curricularRule,
	    IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {

	if (isEnrollingInCourseGroupsOnly(enrolmentContext)) {
	    return RuleResult.createNA();
	}

	return RuleResult.createFalseWithLiteralMessage("Implementar");
    }

    @Override
    protected RuleResult executeEnrolmentInEnrolmentEvaluation(final ICurricularRule curricularRule,
	    final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {
	return RuleResult.createNA();
    }
}
