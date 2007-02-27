package net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors;

import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;

abstract public class CurricularRuleExecutor {

    protected CurricularRuleExecutor() {
    }

    public RuleResult execute(final ICurricularRule curricularRule, final EnrolmentContext enrolmentContext) {
	switch (enrolmentContext.getCurricularRuleLevel()) {
	case ENROLMENT_WITH_RULES:
	    return executeEnrolmentWithRules(curricularRule, enrolmentContext);

	case ENROLMENT_WITH_RULES_AND_TEMPORARY_ENROLMENT:
	    return executeEnrolmentWithRulesAndTemporaryEnrolment(curricularRule, enrolmentContext);

	case ENROLMENT_NO_RULES:
	    return executeEnrolmentWithNoRules(curricularRule, enrolmentContext);

	default:
	    throw new DomainException("error.curricularRules.RuleExecutor.unimplemented.rule.level");
	}
    }

    protected IDegreeModuleToEvaluate searchDegreeModuleToEvaluate(final EnrolmentContext enrolmentContext, final DegreeModule degreeModule) {
	for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : enrolmentContext.getDegreeModuleToEvaluate()) {
	    if (degreeModuleToEvaluate.getDegreeModule() == degreeModule) {
		return degreeModuleToEvaluate;
	    }
	}
	return null;
    }

    protected IDegreeModuleToEvaluate searchDegreeModuleToEvaluate(final EnrolmentContext enrolmentContext, final ICurricularRule curricularRule) {
	return searchDegreeModuleToEvaluate(enrolmentContext, curricularRule.getDegreeModuleToApplyRule());
    }

    protected Collection<IDegreeModuleToEvaluate> collectDegreeModuleToEnrolFromCourseGroup(final EnrolmentContext enrolmentContext, final CourseGroup courseGroup) {
	final Collection<IDegreeModuleToEvaluate> result = new ArrayList<IDegreeModuleToEvaluate>();
	for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : enrolmentContext.getDegreeModuleToEvaluate()) {
	    if (!degreeModuleToEvaluate.isEnroled() && degreeModuleToEvaluate.getContext().getParentCourseGroup() == courseGroup) {
		result.add(degreeModuleToEvaluate);
	    }
	}
	return result;
    }

    protected boolean canApplyRule(final EnrolmentContext enrolmentContext, final ICurricularRule curricularRule) {
	if (curricularRule.getDegreeModuleToApplyRule().isRoot()) {
	    return true;
	}
	return curricularRule.appliesToContext(searchDegreeModuleToEvaluate(enrolmentContext, curricularRule).getContext());
    }

    protected CurriculumModule searchCurriculumModule(final EnrolmentContext enrolmentContext, final DegreeModule degreeModule) {
	if (degreeModule.isLeaf()) {
	    return enrolmentContext.getStudentCurricularPlan().findCurriculumLineFor((CurricularCourse) degreeModule, enrolmentContext.getExecutionPeriod());
	} else {
	    return enrolmentContext.getStudentCurricularPlan().findCurriculumGroupFor((CourseGroup) degreeModule);
	}
    }

    protected CurriculumModule searchCurriculumModule(final EnrolmentContext enrolmentContext,
	    final ICurricularRule curricularRule) {
	return searchCurriculumModule(enrolmentContext, curricularRule.getDegreeModuleToApplyRule());
    }

    protected boolean isApproved(final EnrolmentContext enrolmentContext, final CurricularCourse curricularCourse) {
	return enrolmentContext.getStudentCurricularPlan().isApproved(curricularCourse);
    }

    protected boolean isEnroled(final EnrolmentContext enrolmentContext, final DegreeModule degreeModule) {
	return degreeModule.isLeaf() ? isEnroled(enrolmentContext, (CurricularCourse) degreeModule) : isEnroled(enrolmentContext, (CourseGroup) degreeModule);
    }

    private boolean isEnroled(final EnrolmentContext enrolmentContext, final CurricularCourse curricularCourse) {
	final ExecutionPeriod executionPeriod = enrolmentContext.getExecutionPeriod();
	return enrolmentContext.getStudentCurricularPlan().isEnroledInExecutionPeriod(curricularCourse, executionPeriod);
    }

    private boolean isEnroled(final EnrolmentContext enrolmentContext, final CourseGroup courseGroup) {
	return enrolmentContext.getStudentCurricularPlan().hasDegreeModule(courseGroup);
    }

    protected boolean isEnroled(final EnrolmentContext enrolmentContext, final CurricularCourse curricularCourse, final ExecutionPeriod executionPeriod) {
	return enrolmentContext.getStudentCurricularPlan().isEnroledInExecutionPeriod(curricularCourse, executionPeriod);
    }

    protected boolean hasEnrolmentWithEnroledState(final EnrolmentContext enrolmentContext, final CurricularCourse curricularCourse, final ExecutionPeriod executionPeriod) {
	return enrolmentContext.getStudentCurricularPlan().getRoot().hasEnrolmentWithEnroledState( curricularCourse, executionPeriod);
    }

    protected boolean isEnrolling(final EnrolmentContext enrolmentContext, final DegreeModule degreeModule) {
	final IDegreeModuleToEvaluate degreeModuleToEvaluate = searchDegreeModuleToEvaluate(enrolmentContext, degreeModule);
	return degreeModuleToEvaluate != null && !degreeModuleToEvaluate.isEnroled();
    }

    
    protected RuleResult executeEnrolmentWithNoRules(final ICurricularRule curricularRule, final EnrolmentContext enrolmentContext) {
	final RuleResult ruleResult = executeEnrolmentWithRulesAndTemporaryEnrolment(curricularRule, enrolmentContext);
	return ruleResult.isFalse() ? RuleResult.createWarning(ruleResult.getMessages()) : ruleResult;
    }

    abstract protected RuleResult executeEnrolmentWithRules(final ICurricularRule curricularRule, final EnrolmentContext enrolmentContext);
    abstract protected RuleResult executeEnrolmentWithRulesAndTemporaryEnrolment(final ICurricularRule curricularRule, final EnrolmentContext enrolmentContext);
}
