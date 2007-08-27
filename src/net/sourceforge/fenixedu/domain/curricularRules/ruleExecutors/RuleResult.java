package net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class RuleResult {

    private RuleResultType result;

    private Set<RuleResultMessage> messages;

    private EnrolmentResultType enrolmentResultType;

    private RuleResult(final RuleResultType result, final EnrolmentResultType enrolmentResultType) {

	if (enrolmentResultType == null) {
	    throw new DomainException("error.curricularRules.ruleExecutors.RuleResult.enrolmentResultType.cannot.be.null");
	}

	this.result = result;
	this.enrolmentResultType = enrolmentResultType;
	this.messages = new HashSet<RuleResultMessage>();
    }

    private RuleResult(final RuleResultType result, final EnrolmentResultType enrolmentResultType,
	    final Set<RuleResultMessage> messages) {
	this(result, enrolmentResultType);
	this.messages.addAll(messages);
    }

    public RuleResultType getResult() {
	return this.result;
    }

    public Set<RuleResultMessage> getMessages() {
	return messages;
    }

    public RuleResult and(final RuleResult ruleResult) {
	final RuleResultType andResult = this.getResult().and(ruleResult.getResult());
	final Set<RuleResultMessage> messages = new HashSet<RuleResultMessage>(getMessages());
	messages.addAll(ruleResult.getMessages());

	return new RuleResult(andResult, getEnrolmentResultType().and(ruleResult.getEnrolmentResultType()), messages);
    }

    public RuleResult or(final RuleResult ruleResult) {
	final RuleResultType orResult = this.getResult().or(ruleResult.getResult());
	final Set<RuleResultMessage> messages = new HashSet<RuleResultMessage>(getMessages());
	messages.addAll(ruleResult.getMessages());

	return new RuleResult(orResult, getEnrolmentResultType().and(ruleResult.getEnrolmentResultType()), messages);
    }

    public boolean isTrue() {
	return getResult() == RuleResultType.TRUE;
    }

    public boolean isFalse() {
	return getResult() == RuleResultType.FALSE;
    }

    public boolean isNA() {
	return getResult() == RuleResultType.NA;
    }

    public boolean isWarning() {
	return getResult() == RuleResultType.WARNING;
    }

    public EnrolmentResultType getEnrolmentResultType() {
	return enrolmentResultType;
    }

    public boolean isTemporaryEnrolmentResultType() {
	return enrolmentResultType == EnrolmentResultType.TEMPORARY;
    }
    
    public boolean isNullEnrolmentResultType(){
	return enrolmentResultType == EnrolmentResultType.NULL;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj instanceof RuleResult) {
	    return this.result == ((RuleResult) obj).getResult();
	}
	return false;
    }

    static public RuleResult createTrue() {
	return new RuleResult(RuleResultType.TRUE, EnrolmentResultType.VALIDATED);
    }

    static public RuleResult createTrue(final EnrolmentResultType enrolmentResultType) {
	return new RuleResult(RuleResultType.TRUE, enrolmentResultType);
    }

    static public RuleResult createTrue(final EnrolmentResultType enrolmentResultType, final String message, final String... args) {
	return new RuleResult(RuleResultType.TRUE, enrolmentResultType, Collections.singleton(new RuleResultMessage(message,
		true, args)));
    }

    static public RuleResult createFalse() {
	return createFalse(EnrolmentResultType.VALIDATED);
    }

    static public RuleResult createFalse(final EnrolmentResultType enrolmentResultType) {
	return new RuleResult(RuleResultType.FALSE, enrolmentResultType);
    }

    static public RuleResult createFalse(final String message, final String... args) {
	return createFalse(EnrolmentResultType.VALIDATED, message, args);
    }

    static public RuleResult createFalse(final EnrolmentResultType enrolmentResultType, final String message,
	    final String... args) {
	return new RuleResult(RuleResultType.FALSE, enrolmentResultType, Collections.singleton(new RuleResultMessage(message,
		args)));
    }

    static public RuleResult createFalseWithLiteralMessage(final String message) {
	return new RuleResult(RuleResultType.FALSE, EnrolmentResultType.VALIDATED, Collections.singleton(new RuleResultMessage(
		message, false)));
    }

    static public RuleResult createNA() {
	return new RuleResult(RuleResultType.NA, EnrolmentResultType.NULL);
    }

    static public RuleResult createWarning(final Set<RuleResultMessage> ruleResultMessages) {
	return new RuleResult(RuleResultType.WARNING, EnrolmentResultType.VALIDATED, ruleResultMessages);
    }

    static public RuleResult createWarning(final String message, final String... args) {
	return createWarning(Collections.singleton(new RuleResultMessage(message, args)));

    }

}
