package net.sourceforge.fenixedu.domain;

import pt.ist.fenixframework.plugins.remote.domain.RemoteHost;

public class RemoteExecutionYear extends RemoteExecutionYear_Base {

    public RemoteExecutionYear() {
	super();
    }

    public static RemoteExecutionYear readCurrentExecutionYear(RemoteHost remoteHost) {
	String externalOid = remoteHost.readRemoteStaticMethod("net.sourceforge.fenixedu.domain.ExecutionYear",
		"readCurrentExecutionYear", null);
	return (RemoteExecutionYear) remoteHost.getRemoteDomainObject(externalOid);
    }
}
