package net.sourceforge.fenixedu.domain.organizationalStructure;

import pt.ist.fenixframework.plugins.remote.domain.RemoteHost;

public class RemoteParty extends RemoteParty_Base {
    public RemoteParty() {
	super();
    }

    public static String readAllResearchInterests(final RemoteHost remoteHost) {
	return remoteHost.readRemoteStaticMethod("net.sourceforge.fenixedu.domain.organizationalStructure.Party",
		"readAllResearchInterests", new Object[0]);
    }
}
