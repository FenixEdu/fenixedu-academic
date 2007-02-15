package net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RuleResult {

    private RuleResultType result;

    private List<RuleResultMessage> messages;

    private EnrolmentResultType enrolmentResultType;

    private RuleResult(final RuleResultType result, final EnrolmentResultType enrolmentResultType) {
	this.result = result;
	this.enrolmentResultType = enrolmentResultType;
	this.messages = new ArrayList<RuleResultMessage>();
    }

    private RuleResult(final RuleResultType result, final EnrolmentResultType enrolmentResultType,
	    final List<RuleResultMessage> messages) {
	this(result, enrolmentResultType);
	this.messages.addAll(messages);
    }

    private RuleResultType getResult() {
	return this.result;
    }

    public List<RuleResultMessage> getMessages() {
	return messages;
    }
    
    public RuleResult and(final RuleResult ruleResult) {
	return and(ruleResult, true);
    }

    public RuleResult and(final RuleResult ruleResult, final boolean copyMessages) {
	final RuleResultType andResult = this.getResult().and(ruleResult.getResult());
	final List<RuleResultMessage> messages = new ArrayList<RuleResultMessage>(getMessages());
	if (copyMessages && (andResult == RuleResultType.FALSE) && ruleResult.isFalse()) {
	    messages.addAll(ruleResult.getMessages());
	}

	return new RuleResult(andResult, getEnrolmentResultType().and(
		ruleResult.getEnrolmentResultType()), messages);
    }
    
    public RuleResult or(final RuleResult ruleResult) {
	return or(ruleResult, true);
    }

    public RuleResult or(final RuleResult ruleResult, final boolean copyMessages) {
	final RuleResultType orResult = this.getResult().or(ruleResult.getResult());
	final List<RuleResultMessage> messages = new ArrayList<RuleResultMessage>(); 
	if (orResult == RuleResultType.FALSE) {
	    messages.addAll(getMessages());
	    if (copyMessages) {
		messages.addAll(ruleResult.getMessages());
	    }
	}

	return new RuleResult(orResult, getEnrolmentResultType()
		.and(ruleResult.getEnrolmentResultType()), messages);
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

    public boolean isFinalEnrolment() {
	return this.enrolmentResultType == EnrolmentResultType.FINAL;
    }

    public boolean isTemporaryEnrolment() {
	return this.enrolmentResultType == EnrolmentResultType.TEMPORARY;
    }
    
    public boolean isImpossibleEnrolment() {
	return this.enrolmentResultType == EnrolmentResultType.IMPOSSIBLE;
    }


    public EnrolmentResultType getEnrolmentResultType() {
	return enrolmentResultType;
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
	return new RuleResult(RuleResultType.TRUE, EnrolmentResultType.FINAL);
    }

    static public RuleResult createTrue(final EnrolmentResultType enrolmentResultType) {
	return new RuleResult(RuleResultType.TRUE, enrolmentResultType);
    }
    
    static public RuleResult createFalse() {
	return createFalse(EnrolmentResultType.FINAL);
    }
    
    static public RuleResult createFalse(final EnrolmentResultType enrolmentResultType) {
	return new RuleResult(RuleResultType.FALSE, enrolmentResultType);
    }

    static public RuleResult createFalse(final String message, final String... args) {
	return createFalse(EnrolmentResultType.FINAL, message, args);
    }
    
    static public RuleResult createFalse(final EnrolmentResultType enrolmentResultType, final String message, final String... args) {
	return new RuleResult(RuleResultType.FALSE, enrolmentResultType, Collections.singletonList(new RuleResultMessage(message, args)));
    }
    
    static public RuleResult createFalseWithLiteralMessage(final String message) {
	return new RuleResult(RuleResultType.FALSE, EnrolmentResultType.FINAL, Collections.singletonList(new RuleResultMessage(message, false)));
    }

    static public RuleResult createNA() {
	return new RuleResult(RuleResultType.NA, EnrolmentResultType.NULL);
    }
}
