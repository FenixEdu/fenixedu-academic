package net.sourceforge.fenixedu.domain;

import pt.ist.fenixframework.plugins.remote.domain.RemoteHost;

public class RemoteDegreeInfo extends RemoteDegreeInfo_Base {

    public RemoteDegreeInfo() {
	super();
    }

    public static String readAllDegreeInfos(RemoteHost host) {
	return host.readRemoteStaticMethod("net.sourceforge.fenixedu.domain.DegreeInfo", "readAllDegreeInfos", null);
    }
}
