package net.sourceforge.fenixedu.util.kerberos;

import org.apache.tools.ant.util.TimeoutObserver;
import org.apache.tools.ant.util.Watchdog;

public class ScriptWatchDog implements TimeoutObserver {

    private Watchdog watchdog;
    private Process process;

    private boolean watch;
    private boolean killedProcess;
    private Exception exception;

    public ScriptWatchDog(long timeOut) {
	watchdog = new Watchdog(timeOut);
	watchdog.addTimeoutObserver(this);

    }

    public void start(Process process) {
	if (process == null) {
	    throw new NullPointerException("no process defined");
	}
	if (this.process != null) {
	    throw new IllegalStateException("process already defined");
	}
	this.process = process;
	exception = null;
	watch = true;
	killedProcess = false;
	watchdog.start();
    }

    public void stop() {
	watchdog.stop();
	watch = false;
	process = null;
    }

    public void timeoutOccured(Watchdog w) {
	try {
	    try {
		process.exitValue();
	    } catch (IllegalThreadStateException e) {
		if (watch) {
		    process.destroy();
		    killedProcess = true;
		}
	    }
	} catch (Exception e) {
	    exception = e;
	} finally {
	    cleanUp();
	}

    }

    protected void cleanUp() {
	watch = false;
	process = null;
    }

    public boolean isWatching() {
	return watch;
    }

    public boolean killedProcess() {
	return killedProcess;
    }

    public Exception getException() {
	return exception;
    }

}
