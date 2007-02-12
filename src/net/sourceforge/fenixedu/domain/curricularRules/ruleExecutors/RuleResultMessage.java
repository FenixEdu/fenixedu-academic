package net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors;

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
}
