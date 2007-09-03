package net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors;

import net.sourceforge.fenixedu.domain.curricularRules.EvenOddRule;
import net.sourceforge.fenixedu.domain.curricularRules.Exclusiveness;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.executors.RuleResult;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;

public class EvenOddExecuter extends CurricularRuleExecutor {

    @Override
    protected RuleResult executeEnrolmentInEnrolmentEvaluation(ICurricularRule curricularRule,
	    IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, EnrolmentContext enrolmentContext) {
	return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
    }

    @Override
    protected RuleResult executeEnrolmentWithRules(ICurricularRule curricularRule,
	    IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, EnrolmentContext enrolmentContext) {
	final EvenOddRule evenOddRule = (EvenOddRule) curricularRule;
	if(!canApplyRule(enrolmentContext, evenOddRule)) {
	    return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
	}
	
	if(evenOddRule.getEven() && ((enrolmentContext.getRegistration().getStudent().getNumber().intValue() & 1) == 0) || 
		!evenOddRule.getEven() && ((enrolmentContext.getRegistration().getStudent().getNumber().intValue() & 1) != 0)) {
	    return RuleResult.createTrue(sourceDegreeModuleToEvaluate.getDegreeModule());
	}
	
	return createFalseRuleResult(evenOddRule, sourceDegreeModuleToEvaluate);
    }

    @Override
    protected RuleResult executeEnrolmentWithRulesAndTemporaryEnrolment(ICurricularRule curricularRule,
	    IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, EnrolmentContext enrolmentContext) {
	return executeEnrolmentWithRules(curricularRule, sourceDegreeModuleToEvaluate, enrolmentContext);
    }

    private RuleResult createFalseRuleResult(final EvenOddRule rule, final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate) {
	return RuleResult.createFalse(sourceDegreeModuleToEvaluate.getDegreeModule(),
		"curricularRules.ruleExecutors.EvenOddExecutor.invalid.number", rule.getEvenOddString(), rule.getDegreeModuleToApplyRule()
			.getName());
    }


}
