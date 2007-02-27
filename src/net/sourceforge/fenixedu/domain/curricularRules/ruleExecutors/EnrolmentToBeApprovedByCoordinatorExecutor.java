package net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors;

import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;

public class EnrolmentToBeApprovedByCoordinatorExecutor extends CurricularRuleExecutor {

    @Override
    protected RuleResult executeEnrolmentWithRules(final ICurricularRule curricularRule,
	    final EnrolmentContext enrolmentContext) {
	return RuleResult.createNA();
    }

    @Override
    protected RuleResult executeEnrolmentWithRulesAndTemporaryEnrolment(
	    final ICurricularRule curricularRule, final EnrolmentContext enrolmentContext) {
	return executeEnrolmentWithRules(curricularRule, enrolmentContext);
    }

}
