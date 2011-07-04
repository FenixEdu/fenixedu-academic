package net.sourceforge.fenixedu.domain.administrativeOffice;

import java.util.Set;

import net.sourceforge.fenixedu.domain.RemoteDegree;
import pt.ist.fenixframework.plugins.remote.domain.RemoteHost;

public class RemoteAdministrativeOffice extends RemoteAdministrativeOffice_Base {

    public RemoteAdministrativeOffice() {
	super();
    }

    public Set<RemoteDegree> getAdministratedDegrees() {
	return (java.util.Set) readRemoteDomainObjectsByMethod("getAdministratedDegrees", null);
    }

    public static RemoteAdministrativeOffice readDegreeAdministrativeOffice(RemoteHost remoteHost) {
	String externalOid = remoteHost.readRemoteStaticMethod(
		"net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice", "readDegreeAdministrativeOffice",
		null);
	return (RemoteAdministrativeOffice) remoteHost.getRemoteDomainObject(externalOid);
    }

    public static RemoteAdministrativeOffice readMasterDegreeAdministrativeOffice(RemoteHost remoteHost) {
	String externalOid = remoteHost.readRemoteStaticMethod(
		"net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice",
		"readMasterDegreeAdministrativeOffice", null);
	return (RemoteAdministrativeOffice) remoteHost.getRemoteDomainObject(externalOid);
    }

}
