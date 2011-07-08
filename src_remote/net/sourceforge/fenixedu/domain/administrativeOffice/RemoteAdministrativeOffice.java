package net.sourceforge.fenixedu.domain.administrativeOffice;

import java.util.HashSet;
import java.util.Collection;

import net.sourceforge.fenixedu.domain.RemoteDegree;
import pt.ist.fenixframework.plugins.remote.domain.RemoteHost;

public class RemoteAdministrativeOffice extends RemoteAdministrativeOffice_Base {

    public RemoteAdministrativeOffice() {
	super();
    }

    public Collection<RemoteDegree> getAdministratedDegrees() {
	return (java.util.Collection) readRemoteDomainObjectsByMethod("getAdministratedDegrees", null);
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
