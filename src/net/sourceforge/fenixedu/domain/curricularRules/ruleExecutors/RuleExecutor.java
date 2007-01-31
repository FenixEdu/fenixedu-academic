package net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors;

import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.enrolment.DegreeModuleToEnrol;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public abstract class RuleExecutor {
    
    protected RuleExecutor() {
    }

    public RuleResult execute(final CurricularRule curricularRule, final RuleLevel level, final EnrolmentContext enrolmentContext) {
        switch (level) {
	case WITH_RULES:
	    return executeWithRules(curricularRule, enrolmentContext);
	    
	case NO_RULES:
	    return executeNoRules(enrolmentContext);
	    
	default:
	    throw new DomainException("error.curricularRules.RuleExecutor.unimplemented.rule.level");
	}
    }
    
    protected DegreeModuleToEnrol getDegreeModuleToEnrolFor(final EnrolmentContext enrolmentContext, final DegreeModule degreeModule) {
	for (final DegreeModuleToEnrol degreeModuleToEnrol : enrolmentContext.getDegreeModuleToEnrol()) {
	    if (degreeModuleToEnrol.getContext().getChildDegreeModule() == degreeModule) {
		return degreeModuleToEnrol;
	    }
	}
	return null;
    }

    protected RuleResult executeNoRules(final EnrolmentContext enrolmentContext) {
	return RuleResult.createTrue();
    }

    abstract protected RuleResult executeWithRules(final CurricularRule curricularRule, final EnrolmentContext enrolmentContext);
}
