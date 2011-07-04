package net.sourceforge.fenixedu.domain;

import pt.ist.fenixframework.plugins.remote.domain.RemoteHost;

public class RemoteRootDomainObject extends RemoteRootDomainObject_Base {

    public RemoteRootDomainObject() {
	super();
    }

    public static RemoteRootDomainObject getInstance(final RemoteHost remoteHost) {
	final String remoteOid = remoteHost.readRemoteStaticMethod("net.sourceforge.fenixedu.domain.RootDomainObject",
		"getInstance", null);
	return remoteOid == null ? null : (RemoteRootDomainObject) remoteHost.getRemoteDomainObject(remoteOid);
    }

}
