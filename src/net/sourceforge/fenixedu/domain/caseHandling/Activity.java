package net.sourceforge.fenixedu.domain.caseHandling;

import net.sourceforge.fenixedu.applicationTier.IUserView;

public abstract class Activity<P extends Process> {

    // TODO: change method return to boolean
    public abstract void checkPreConditions(P process, IUserView userView);

    protected abstract P executeActivity(P process, IUserView userView, Object object);

    protected void executePosConditions(P process, IUserView userView, Object object) {
        new ProcessLog(process, userView, this);
    }

    public Boolean isVisibleForAdminOffice() {
        return Boolean.TRUE;
    }

    public Boolean isVisibleForGriOffice() {
        return Boolean.TRUE;
    }

    public Boolean isVisibleForCoordinator() {
        return Boolean.FALSE;
    }

    final public P execute(P process, IUserView userView, Object object) {
        checkPreConditions(process, userView);
        P modifiedProcess = executeActivity(process, userView, object);
        executePosConditions(modifiedProcess, userView, object);

        log(modifiedProcess, userView, object);

        return modifiedProcess;
    }

    protected void log(P process, IUserView userView, Object object) {

    }

    public String getId() {
        return getClass().getSimpleName();
    }

}
