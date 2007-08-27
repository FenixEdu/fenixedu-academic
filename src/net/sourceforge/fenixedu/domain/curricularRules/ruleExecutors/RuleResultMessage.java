package net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors;

import org.apache.commons.lang.ArrayUtils;

public class RuleResultMessage {

    private String message;

    private String[] args;

    private boolean toTranslate;

    public RuleResultMessage(final String message, final String... args) {
	this(message, true, args);
    }

    public RuleResultMessage(final String message, final boolean toTranslate, final String... args) {
	super();
	this.message = message;
	this.args = args;
	this.toTranslate = toTranslate;
    }

    public String[] getArgs() {
	return args;
    }

    public void setArgs(String[] args) {
	this.args = args;
    }

    public String getMessage() {
	return message;
    }

    public void setMessage(String message) {
	this.message = message;
    }

    public boolean isToTranslate() {
	return toTranslate;
    }

    @Override
    public boolean equals(Object obj) {
	if (!(obj instanceof RuleResultMessage)) {
	    return false;
	}

	final RuleResultMessage other = (RuleResultMessage) obj;
	return this.toTranslate == other.toTranslate && hasSameMessage(other) && hasSameArgs(other);
    }

    private boolean hasSameArgs(RuleResultMessage other) {
	if (this.args == null && other.args == null) {
	    return true;
	}

	if (this.args != null && other.args != null && this.args.length == other.args.length) {
	    return ArrayUtils.isEquals(this.args, other.args);
	}

	return false;
    }

    private boolean hasSameMessage(final RuleResultMessage other) {
	if (this.message == null && other.message == null) {
	    return true;
	}

	if (this.message != null && other.message != null) {
	    return this.message.equals(other.message);
	}

	return false;
    }

    @Override
    public int hashCode() {
	return this.message.hashCode() + (this.args != null ? this.args.length : 0);
    }
}
