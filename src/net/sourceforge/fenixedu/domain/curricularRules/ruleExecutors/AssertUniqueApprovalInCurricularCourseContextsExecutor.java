package net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.util.CurricularRuleLabelFormatter;

public class AssertUniqueApprovalInCurricularCourseContextsExecutor extends CurricularRuleExecutor {

    @Override
    protected RuleResult executeEnrolmentWithRules(final ICurricularRule curricularRule, final EnrolmentContext enrolmentContext) {
	final CurricularCourse curricularCourse = (CurricularCourse) curricularRule.getDegreeModuleToApplyRule();

	if (isApproved(enrolmentContext, curricularCourse)) {
	    return RuleResult.createFalseWithLiteralMessage(CurricularRuleLabelFormatter.getLabel(curricularRule));
	} else {
	    return RuleResult.createTrue();
	}
    }
    
    @Override
    protected RuleResult executeEnrolmentWithRulesAndTemporaryEnrolment(final ICurricularRule curricularRule, final EnrolmentContext enrolmentContext) {
	final CurricularCourse curricularCourse = (CurricularCourse) curricularRule.getDegreeModuleToApplyRule();
	final ExecutionPeriod previousExecutionPeriod = enrolmentContext.getExecutionPeriod().getPreviousExecutionPeriod();
	
	if (isApproved(enrolmentContext, curricularCourse)) {
	    return RuleResult.createFalseWithLiteralMessage(CurricularRuleLabelFormatter.getLabel(curricularRule));
	    
	} else if (hasEnrolmentWithEnroledState(enrolmentContext, curricularCourse, previousExecutionPeriod)) {
	    return RuleResult.createTrue(EnrolmentResultType.TEMPORARY);
	    
	} else {
	    return RuleResult.createTrue();
	}

    }

    @Override
    protected RuleResult executeEnrolmentWithNoRules(final ICurricularRule curricularRule, final EnrolmentContext enrolmentContext) {
	return RuleResult.createTrue();
    }

}
