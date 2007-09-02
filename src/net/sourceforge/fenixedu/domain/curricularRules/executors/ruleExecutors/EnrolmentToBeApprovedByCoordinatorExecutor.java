package net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.executors.RuleResult;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.person.RoleType;

public class EnrolmentToBeApprovedByCoordinatorExecutor extends CurricularRuleExecutor {

    @Override
    protected RuleResult executeEnrolmentWithRules(final ICurricularRule curricularRule,
	    final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {
	final Person responsiblePerson = enrolmentContext.getResponsiblePerson();
	if (responsiblePerson.hasRole(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE) || responsiblePerson.hasRole(RoleType.MANAGER)) {
	    return RuleResult
		    .createWarning(sourceDegreeModuleToEvaluate.getDegreeModule(),
			    "curricularRules.ruleExecutors.EnrolmentToBeApprovedByCoordinatorExecutor.degree.module.needs.aproval.by.coordinator",
			    sourceDegreeModuleToEvaluate.getName());
	}

	return RuleResult
		.createFalse(
			sourceDegreeModuleToEvaluate.getDegreeModule(),
			"curricularRules.ruleExecutors.EnrolmentToBeApprovedByCoordinatorExecutor.degree.module.needs.aproval.by.coordinator",
			sourceDegreeModuleToEvaluate.getName());
    }

    @Override
    protected RuleResult executeEnrolmentWithRulesAndTemporaryEnrolment(final ICurricularRule curricularRule,
	    IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {
	return executeEnrolmentWithRules(curricularRule, sourceDegreeModuleToEvaluate, enrolmentContext);
    }

    @Override
    protected RuleResult executeEnrolmentInEnrolmentEvaluation(final ICurricularRule curricularRule,
	    final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {
	return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
    }

}
