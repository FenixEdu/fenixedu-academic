package net.sourceforge.fenixedu.applicationTier.Servico.exceptions;

public class FenixServiceException extends Exception {
    private int errorType;
    private String[] args;

    /**
     * @return
     */
    public int getErrorType() {
	return this.errorType;
    }

    public FenixServiceException() {
    }

    public FenixServiceException(int errorType) {
	this.errorType = errorType;
    }

    public FenixServiceException(String s) {
	super(s);
    }

    public FenixServiceException(String s, String[] args) {
	super(s);
	this.args = args;
    }

    public FenixServiceException(Throwable cause) {
	super(cause);
    }

    public FenixServiceException(String message, Throwable cause) {
	super(message, cause);
    }

    public String toString() {
	String result = "[" + this.getClass().getName() + "\n";
	result += "message " + this.getMessage() + "\n";
	result += "cause " + this.getCause() + "\n";
	result += "]";
	return result;
    }

    public String[] getArgs() {
	return args;
    }

    public void setArgs(String[] args) {
	this.args = args;
    }

}

/* Created by Nuno Antão */