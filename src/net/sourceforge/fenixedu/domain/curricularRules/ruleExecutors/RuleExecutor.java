package net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors;

import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.enrolment.DegreeModuleToEnrol;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public abstract class RuleExecutor {

    protected RuleExecutor() {
    }

    public RuleResult execute(final CurricularRule curricularRule, final CurricularRuleLevel level,
	    final EnrolmentContext enrolmentContext) {
	switch (level) {
	case ENROLMENT_WITH_RULES:
	    return executeWithRules(curricularRule, enrolmentContext);

	case ENROLMENT_WITH_RULES_AND_TEMPORARY_ENROLMENT:
	    return executeWithRulesAndTemporaryEnrolment(curricularRule, enrolmentContext);

	case ENROLMENT_NO_RULES:
	    return executeNoRules(enrolmentContext);

	default:
	    throw new DomainException("error.curricularRules.RuleExecutor.unimplemented.rule.level");
	}
    }

    protected DegreeModuleToEnrol getDegreeModuleToEnrolFor(final EnrolmentContext enrolmentContext,
	    final DegreeModule degreeModule) {
	for (final DegreeModuleToEnrol degreeModuleToEnrol : enrolmentContext.getDegreeModuleToEnrol()) {
	    if (degreeModuleToEnrol.getContext().getChildDegreeModule() == degreeModule) {
		return degreeModuleToEnrol;
	    }
	}

	throw new DomainException("error.curricularRules.RuleExecutor.cannot.find.degreeModuleToEnrol");
    }

    abstract protected RuleResult executeNoRules(final EnrolmentContext enrolmentContext);

    abstract protected RuleResult executeWithRules(final CurricularRule curricularRule,
	    final EnrolmentContext enrolmentContext);

    abstract protected RuleResult executeWithRulesAndTemporaryEnrolment(CurricularRule curricularRule,
	    EnrolmentContext enrolmentContext);
}
