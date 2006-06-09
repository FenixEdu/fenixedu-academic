package net.sourceforge.fenixedu.domain.system;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTime;

public class CronScriptInvocation extends CronScriptInvocation_Base {
    
    public CronScriptInvocation(final CronScriptState cronScriptState, final String serverID,
    		final DateTime startTime, final DateTime endTime, final Boolean successful) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setCronScriptState(cronScriptState);
        setServerID(serverID);
        setStartTime(startTime);
        setEndTime(endTime);
        setSuccessful(successful);
    }
    
}
