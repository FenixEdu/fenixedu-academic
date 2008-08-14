package net.sourceforge.fenixedu.util.kerberos;

public class KerberosException extends Exception {
    public static final String CHANGE_PASSWORD_TOO_SHORT = "CHANGE_PASSWORD_TOO_SHORT";
    public static final String CHANGE_PASSWORD_NOT_ENOUGH_CHARACTER_CLASSES = "CHANGE_PASSWORD_NOT_ENOUGH_CHARACTER_CLASSES";
    public static final String CHANGE_PASSWORD_CANNOT_REUSE = "CHANGE_PASSWORD_CANNOT_REUSE";
    public static final String CHANGE_PASSWORD_EXPIRED = "CHECK_PASSWORD_EXPIRED";
    public static final String WRONG_PASSWORD = "CHECK_PASSWORD_WRONG";
    public static final String ADD_TOO_SHORT = "ADD_TOO_SHORT";
    public static final String ADD_NOT_ENOUGH_CHARACTER_CLASSES = "ADD_NOT_ENOUGH_CHARACTER_CLASSES";
    public static final String CHECK_PASSWORD_LOW_QUALITY = "CHECK_PASSWORD_LOW_QUALITY";

    private int exitCode;
    private String returnCode;

    public KerberosException(int exitCode, String returnCode) {
	super(returnCode);
	setExitCode(exitCode);
	setReturnCode(returnCode);
    }

    public int getExitCode() {
	return exitCode;
    }

    public void setExitCode(int exitCode) {
	this.exitCode = exitCode;
    }

    public String getReturnCode() {
	return returnCode;
    }

    public void setReturnCode(String returnCode) {
	this.returnCode = returnCode;
    }

}
