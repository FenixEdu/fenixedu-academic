package net.sourceforge.fenixedu.domain;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.student.RemoteRegistration;
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

    public RemoteExecutionYear getPreviousExecutionYear() {
	return (RemoteExecutionYear) readRemoteDomainObjectByMethod("getPreviousExecutionYear", null);
    }

    public Collection<RemoteRegistration> getRegistrationsOfSeniorStudentsForExecutionYear() {
	return (java.util.Collection) readRemoteDomainObjectsByMethod("getRegistrationsOfSeniorStudentsForExecutionYear", null);
    }

    public Collection<RemoteRegistration> getConcludedRegistrationsOfStudentsForExecutionYear() {
	return (java.util.Collection) readRemoteDomainObjectsByMethod("getConcludedRegistrationsOfStudentsForExecutionYear", null);
    }

}
