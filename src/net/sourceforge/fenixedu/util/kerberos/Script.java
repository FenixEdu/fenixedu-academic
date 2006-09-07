package net.sourceforge.fenixedu.util.kerberos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import pt.utl.ist.fenix.tools.util.StringAppender;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class Script {

    private static String createCommand(final String scriptName, final String user) {
	final String script = PropertiesManager.getProperty(scriptName);
	return StringAppender.append(script, " ", user);
    }

    public static void runCommand(final String commandString, final String user, final String pass)
    		throws ExcepcaoPersistencia, KerberosException {
	final String command = createCommand(commandString, user);

	ScriptResult scriptResult = runCmd(command, pass);

	if (scriptResult.getExitCode() == -1) {
	    throw new ExcepcaoPersistencia(scriptResult.getReturnCode());
	} else {
	    if (scriptResult.getExitCode() == 1) {
		throw new KerberosException(scriptResult.getExitCode(), scriptResult.getReturnCode());
	    }
	}
    }

    public static void changeKerberosPass(String user, String pass) throws ExcepcaoPersistencia,
	    KerberosException {
	runCommand("changePassScript", user, pass);
    }

    public static void createUser(String user, String pass) throws ExcepcaoPersistencia,
	    KerberosException {
	runCommand("createUserScript", user, pass);
    }

    public static void verifyPass(String user, String pass) throws ExcepcaoPersistencia,
	    KerberosException {
	runCommand("verifyPassScript", user, pass);
    }

    private static ScriptResult runCmd(String cmd, String pass) {
	Process process = null;
	BufferedWriter outCommand = null;
	BufferedReader bufferedReader = null;
	ScriptWatchDog watchDog = new ScriptWatchDog(Float.valueOf(
		PropertiesManager.getProperty("scriptTimeout")).longValue());

	try {
	    process = Runtime.getRuntime().exec(cmd);
	    outCommand = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
	    bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
	    outCommand.write(pass);
	    outCommand.newLine();
	    outCommand.flush();

	    watchDog.start(process);
	    int exitCode = process.waitFor();
	    watchDog.stop();
	    String returnCode;
	    if (watchDog.killedProcess()) {
		exitCode = -1;
		returnCode = "Timeout";
	    } else {
		returnCode = bufferedReader.readLine();
		if (exitCode != 1 && exitCode != 0) {
		    exitCode = -1;
		}
	    }
	    return new ScriptResult(exitCode, returnCode);

	} catch (Exception e) {
	    e.printStackTrace();
	    return new ScriptResult(-1, e.getMessage());
	} finally {
	    try {
		if (outCommand != null)
		    outCommand.close();
		if (bufferedReader != null)
		    bufferedReader.close();
		if (process != null)
		    process.destroy();
		watchDog.stop();
	    } catch (Exception e) {

	    }
	}
    }

    private static class ScriptResult {
	private int exitCode;

	private String returnCode;

	public ScriptResult(int exitCode, String returnCode) {
	    this.exitCode = exitCode;
	    this.returnCode = returnCode;
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
}
