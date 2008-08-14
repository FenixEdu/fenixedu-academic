package net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors;

import net.sourceforge.fenixedu.domain.curricularRules.EvenOddRule;
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
    protected RuleResult executeEnrolmentVerificationWithRules(ICurricularRule curricularRule,
	    IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, EnrolmentContext enrolmentContext) {
	final EvenOddRule evenOddRule = (EvenOddRule) curricularRule;
	if (!canApplyRule(enrolmentContext, evenOddRule)) {
	    return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
	}

	Integer studentNumber = enrolmentContext.getRegistration().getStudent().getNumber();
	if (evenOddRule.getEven() && isEven(studentNumber) || !evenOddRule.getEven() && isOdd(studentNumber)) {
	    return RuleResult.createTrue(sourceDegreeModuleToEvaluate.getDegreeModule());
	}
	return createFalseRuleResult(evenOddRule, sourceDegreeModuleToEvaluate);
    }

    @Override
    protected RuleResult executeEnrolmentWithRulesAndTemporaryEnrolment(ICurricularRule curricularRule,
	    IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, EnrolmentContext enrolmentContext) {
	return executeEnrolmentVerificationWithRules(curricularRule, sourceDegreeModuleToEvaluate, enrolmentContext);
    }

    private RuleResult createFalseRuleResult(final EvenOddRule rule, final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate) {
	return RuleResult.createFalse(sourceDegreeModuleToEvaluate.getDegreeModule(),
		"curricularRules.ruleExecutors.EvenOddExecutor.invalid.number", rule.getEvenOddString(), rule
			.getDegreeModuleToApplyRule().getName());
    }

    private boolean isEven(int number) {
	return (number & 1) == 0;
    }

    private boolean isOdd(int number) {
	return (number & 1) != 0;
    }

    @Override
    protected boolean canBeEvaluated(ICurricularRule curricularRule, IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate,
	    EnrolmentContext enrolmentContext) {
	return true;
    }

}
