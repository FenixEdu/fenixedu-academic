package net.sourceforge.fenixedu.domain.exceptions;

import net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors.RuleResult;

public class EnrollmentDomainException extends DomainException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private RuleResult falseResult;

    public EnrollmentDomainException(String key, String... args) {
	super(key, args);
    }

    public EnrollmentDomainException(String key, Throwable cause, String... args) {
	super(key, cause, args);
    }

    public EnrollmentDomainException(final RuleResult falseRuleResult) {
	super();
	this.falseResult = falseRuleResult;
    }

    public RuleResult getFalseResult() {
	return this.falseResult;
    }

}
