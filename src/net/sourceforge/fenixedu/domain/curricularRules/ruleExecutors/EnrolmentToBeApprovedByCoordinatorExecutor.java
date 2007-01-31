package net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors;

import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;

public class EnrolmentToBeApprovedByCoordinatorExecutor extends RuleExecutor {

    @Override
    protected RuleResult executeWithRules(CurricularRule curricularRule, EnrolmentContext enrolmentContext) {
	return RuleResult.createTrue();
    }
}
