package net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors;

import java.util.HashMap;
import java.util.Map;

import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.EnrolmentToBeApprovedByCoordinator;
import net.sourceforge.fenixedu.domain.curricularRules.RestrictionDoneDegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class RuleFactory {
    
    private static final RuleFactory INSTANCE = new RuleFactory();
    
    private Map<Class<? extends CurricularRule>, RuleExecutor> executors;
    
    private RuleFactory() {
	executors = new HashMap<Class<? extends CurricularRule>, RuleExecutor>();
	loadRuleExecutors();
    }
    
    private void loadRuleExecutors() {
	// TODO: For test only
	executors.put(RestrictionDoneDegreeModule.class, new RestrictionDoneDegreeModuleExecutor());
	executors.put(EnrolmentToBeApprovedByCoordinator.class, new EnrolmentToBeApprovedByCoordinatorExecutor());
    }

    public static RuleExecutor findExecutor(final CurricularRule curricularRule) {
	return findExecutor(curricularRule.getClass());
    }
    
    public static RuleExecutor findExecutor(final Class<? extends CurricularRule> clazz) {
	if (!INSTANCE.executors.containsKey(clazz)) {
	    throw new DomainException("error.curricularRules.RuleFactory.cannot.find.RuleExecutor.for.class", clazz.getName());
	}
	return INSTANCE.executors.get(clazz);
    }
}
