package net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors;

import java.util.HashMap;
import java.util.Map;

import net.sourceforge.fenixedu.domain.curricularRules.AnyCurricularCourse;
import net.sourceforge.fenixedu.domain.curricularRules.CreditsLimit;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.DegreeModulesSelectionLimit;
import net.sourceforge.fenixedu.domain.curricularRules.EnrolmentToBeApprovedByCoordinator;
import net.sourceforge.fenixedu.domain.curricularRules.Exclusiveness;
import net.sourceforge.fenixedu.domain.curricularRules.MaximumNumberOfCreditsForEnrolmentPeriod;
import net.sourceforge.fenixedu.domain.curricularRules.MinimumNumberOfCreditsToEnrol;
import net.sourceforge.fenixedu.domain.curricularRules.RestrictionBetweenDegreeModules;
import net.sourceforge.fenixedu.domain.curricularRules.RestrictionDoneDegreeModule;
import net.sourceforge.fenixedu.domain.curricularRules.RestrictionEnroledDegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class CurricularRuleExecutorFactory {

    private static Map<Class<? extends CurricularRule>, CurricularRuleExecutor> executors = new HashMap<Class<? extends CurricularRule>, CurricularRuleExecutor>();

    static {
	executors.put(RestrictionDoneDegreeModule.class, 		new RestrictionDoneDegreeModuleExecutor());
	executors.put(RestrictionEnroledDegreeModule.class,		new RestrictionEnroledDegreeModuleExecutor());	
	executors.put(RestrictionBetweenDegreeModules.class,		new RestrictionBetweenDegreeModulesExecutor());
	executors.put(EnrolmentToBeApprovedByCoordinator.class, 	new EnrolmentToBeApprovedByCoordinatorExecutor());
	executors.put(Exclusiveness.class, 				new ExclusivenessExecutor());
	executors.put(MinimumNumberOfCreditsToEnrol.class,		new MinimumNumberOfCreditsToEnrolExecutor());
	executors.put(DegreeModulesSelectionLimit.class,		new DegreeModulesSelectionLimitExecutor());
	executors.put(CreditsLimit.class,				new CreditsLimitExecutor());
	executors.put(AnyCurricularCourse.class,			new AnyCurricularCourseExecutor());
	executors.put(MaximumNumberOfCreditsForEnrolmentPeriod.class,	new MaximumNumberOfCreditsForEnrolmentPeriodExecutor());
    }

    public static CurricularRuleExecutor findExecutor(final CurricularRule curricularRule) {
	return findExecutor(curricularRule.getClass());
    }

    public static CurricularRuleExecutor findExecutor(final Class<? extends CurricularRule> clazz) {
	if (!executors.containsKey(clazz)) {
	    throw new DomainException("error.curricularRules.RuleFactory.cannot.find.RuleExecutor.for.class", clazz.getName());
	}
	return executors.get(clazz);
    }
}
