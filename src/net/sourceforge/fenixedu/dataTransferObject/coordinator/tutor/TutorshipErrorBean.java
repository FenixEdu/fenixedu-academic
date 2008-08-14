package net.sourceforge.fenixedu.dataTransferObject.coordinator.tutor;

import java.io.Serializable;

public class TutorshipErrorBean implements Serializable {
    private String message;

    private String[] args;

    public TutorshipErrorBean(String message, String[] args) {
	this.message = message;
	this.args = args;
    }

    public String getMessage() {
	return message;
    }

    public void setMessage(String message) {
	this.message = message;
    }

    public String[] getArgs() {
	return args;
    }

    public void setArgs(String[] args) {
	this.args = args;
    }

}
