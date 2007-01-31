package net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors;

public class RuleResultMessage {

    private String message;

    private String[] args;

    public RuleResultMessage(String message, String... args) {
	super();
	this.message = message;
	this.args = args;
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

}
