package net.sourceforge.fenixedu.domain.exceptions;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors.RuleResult;

public class EnrollmentDomainException extends DomainException {

    private List<RuleResult> falseRuleResults;

    public EnrollmentDomainException(String key, String... args) {
	super(key, args);
    }

    public EnrollmentDomainException(String key, Throwable cause, String... args) {
	super(key, cause, args);
    }

    public EnrollmentDomainException(final List<RuleResult> falseRuleResults) {
	super();
	this.falseRuleResults = falseRuleResults;
    }

    public List<RuleResult> getFalseRuleResults() {
	return Collections.unmodifiableList(falseRuleResults);
    }

}
