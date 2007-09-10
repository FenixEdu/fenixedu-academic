package net.sourceforge.fenixedu.dataTransferObject.pedagogicalCouncil.elections;

import java.io.Serializable;

public class ElectionErrorBean implements Serializable {
	private String message;
	
	private String[] args;

	public ElectionErrorBean(String message, String[] args) {
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
