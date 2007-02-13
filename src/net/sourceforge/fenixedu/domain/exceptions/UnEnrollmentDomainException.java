package net.sourceforge.fenixedu.domain.exceptions;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors.RuleResult;

public class UnEnrollmentDomainException extends DomainException {

    private List<RuleResult> falseRuleResults;

    public UnEnrollmentDomainException(String key, String... args) {
	super(key, args);
    }

    public UnEnrollmentDomainException(String key, Throwable cause, String... args) {
	super(key, cause, args);
    }

    public UnEnrollmentDomainException(final List<RuleResult> falseRuleResults) {
	super();
	this.falseRuleResults = falseRuleResults;
    }

    public List<RuleResult> getFalseRuleResults() {
	return Collections.unmodifiableList(falseRuleResults);
    }

}
