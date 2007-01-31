package net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors;

import java.util.HashMap;
import java.util.Map;

import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.EnrolmentToBeApprovedByCoordinator;
import net.sourceforge.fenixedu.domain.curricularRules.RestrictionDoneDegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class RuleFactory {

    private static Map<Class<? extends CurricularRule>, RuleExecutor> executors = new HashMap<Class<? extends CurricularRule>, RuleExecutor>();

    static {
	executors.put(RestrictionDoneDegreeModule.class, new RestrictionDoneDegreeModuleExecutor());
	executors.put(EnrolmentToBeApprovedByCoordinator.class,
		new EnrolmentToBeApprovedByCoordinatorExecutor());

    }

    public static RuleExecutor findExecutor(final CurricularRule curricularRule) {
	return findExecutor(curricularRule.getClass());
    }

    public static RuleExecutor findExecutor(final Class<? extends CurricularRule> clazz) {
	if (!executors.containsKey(clazz)) {
	    throw new DomainException(
		    "error.curricularRules.RuleFactory.cannot.find.RuleExecutor.for.class", clazz
			    .getName());
	}

	return executors.get(clazz);
    }
}
