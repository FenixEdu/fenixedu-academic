package net.sourceforge.fenixedu.util.kerberos;

public class KerberosException extends Exception {
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
