package net.sourceforge.fenixedu.caseHandling;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.caseHandling.Process;
import net.sourceforge.fenixedu.domain.caseHandling.ProcessLog;

public abstract class Activity<P extends Process> {

    public abstract void checkPreConditions(P process, IUserView userView);

    protected abstract P executeActivity(P process, IUserView userView, Object object);

    protected void executePosConditions(P process, IUserView userView, Object object) {
	new ProcessLog(process, userView, this);
    }

    final public P execute(P process, IUserView userView, Object object) {
	checkPreConditions(process, userView);
	P modifiedProcess = executeActivity(process, userView, object);
	executePosConditions(modifiedProcess, userView, object);
	return modifiedProcess;
    }

    public String getId() {
	return getClass().getSimpleName();
    }

}
