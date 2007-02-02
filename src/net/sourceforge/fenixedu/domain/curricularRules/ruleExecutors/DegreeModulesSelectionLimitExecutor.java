package net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors;

import java.util.List;

import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.DegreeModulesSelectionLimit;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.enrolment.DegreeModuleToEnrol;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;

public class DegreeModulesSelectionLimitExecutor extends CurricularRuleExecutor {

    @Override
    protected RuleResult executeWithRules(final CurricularRule curricularRule, final EnrolmentContext enrolmentContext) {
	
	final DegreeModulesSelectionLimit rule = (DegreeModulesSelectionLimit) curricularRule;
	final DegreeModuleToEnrol moduleToEnrol = getDegreeModuleToEnrol(enrolmentContext, rule.getDegreeModuleToApplyRule());

	if (!rule.appliesToContext(moduleToEnrol.getContext())) {
	    return RuleResult.createNA();
	}
	
	final CourseGroup courseGroup = (CourseGroup) rule.getDegreeModuleToApplyRule();
	final List<Context> childContexts = courseGroup.getChildContexts(enrolmentContext.getExecutionPeriod());
	
	int numberOfDegreeModulesToEnrol = countNumberOfDegreeModulesToEnrol(enrolmentContext, childContexts);
	int numberOfEnroledDegreeModules = countNumberOfEnroledDegreeModules(enrolmentContext, childContexts);
	
	if (rule.allowNumberOfDegreeModules(numberOfDegreeModulesToEnrol + numberOfEnroledDegreeModules)) {
	    return RuleResult.createTrue();
	}
	
	if (rule.getMinimumLimit().equals(rule.getMaximumLimit())) {
	    return RuleResult.createFalse(
		    "curricularRules.ruleExecutors.DegreeModulesSelectionLimitExecutor.limit.exceded", 
		    rule.getDegreeModuleToApplyRule().getName(), rule.getMinimumLimit().toString());
	} else {
	    return RuleResult.createFalse(
		    "curricularRules.ruleExecutors.DegreeModulesSelectionLimitExecutor.limits.exceded", 
		    rule.getDegreeModuleToApplyRule().getName(), rule.getMinimumLimit().toString(), rule.getMaximumLimit().toString());
	}
    }

    private int countNumberOfEnroledDegreeModules(final EnrolmentContext enrolmentContext, final List<Context> childContexts) {
	int result = 0;
	for (final Context context : childContexts) {
	    if (isEnroled(enrolmentContext, context.getChildDegreeModule())) {
		result++;
	    }
	}
	return result;
    }

    private int countNumberOfDegreeModulesToEnrol(final EnrolmentContext enrolmentContext, final List<Context> childContexts) {
	int result = 0;
	for (final Context context : childContexts) {
	    if (isToEnrol(enrolmentContext, context.getChildDegreeModule())) {
		result++;
	    }
	}
	return result;
    }

    @Override
    protected RuleResult executeWithRulesAndTemporaryEnrolment(final CurricularRule curricularRule, final EnrolmentContext enrolmentContext) {
	return executeWithRules(curricularRule, enrolmentContext);
    }

    @Override
    protected RuleResult executeNoRules(final CurricularRule curricularRule, final EnrolmentContext enrolmentContext) {
	return RuleResult.createTrue();
    }
}
