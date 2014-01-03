package net.sourceforge.fenixedu.domain.system;

import java.util.Comparator;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;
import org.joda.time.Period;

public class CronScriptInvocation extends CronScriptInvocation_Base {

    public static final Comparator<CronScriptInvocation> COMPARATOR_BY_START_TIME = new Comparator<CronScriptInvocation>() {

        @Override
        public int compare(CronScriptInvocation o1, CronScriptInvocation o2) {
            return o1.getStartTime().compareTo(o2.getStartTime());
        }

    };

    public CronScriptInvocation(final CronScriptState cronScriptState, final String serverID, final DateTime startTime,
            final DateTime endTime, final Boolean successful, final String log) {
        super();
        setRootDomainObject(Bennu.getInstance());
        setCronScriptState(cronScriptState);
        setServerID(serverID);
        setStartTime(startTime);
        setEndTime(endTime);
        setSuccessful(successful);
        setLog(log);
    }

    public boolean hasReachedNextInvocationTime(final Period invocationPeriod) {
        return !getStartTime().plus(invocationPeriod).isAfterNow();
    }

    public void delete() {
        setRootDomainObject(null);
        setCronScriptState(null);
        deleteDomainObject();
    }

    @Deprecated
    public boolean hasSuccessful() {
        return getSuccessful() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasLog() {
        return getLog() != null;
    }

    @Deprecated
    public boolean hasCronScriptState() {
        return getCronScriptState() != null;
    }

    @Deprecated
    public boolean hasServerID() {
        return getServerID() != null;
    }

    @Deprecated
    public boolean hasEndTime() {
        return getEndTime() != null;
    }

    @Deprecated
    public boolean hasStartTime() {
        return getStartTime() != null;
    }

}
