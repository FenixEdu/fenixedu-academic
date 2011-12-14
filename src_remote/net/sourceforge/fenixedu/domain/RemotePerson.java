package net.sourceforge.fenixedu.domain;

import pt.ist.fenixframework.plugins.remote.domain.RemoteHost;

public class RemotePerson extends RemotePerson_Base {

    public RemotePerson() {
	super();
    }

    public String getUserAliass() {
	return readRemoteMethod("getUserAliass", null);
    }

    public String getEmailForSendingEmails() {
	return readRemoteMethod("getEmailForSendingEmails", null);
    }

    public String getWorkingPlaceCostCenter() {
	return readRemoteMethod("getWorkingPlaceCostCenter", null);
    }

    public String getEmployeeRoleDescription() {
	return readRemoteMethod("getEmployeeRoleDescription", null);
    }

    public String readAllTeacherInformation() {
	return readRemoteMethod("readAllTeacherInformation", null);
    }

    public String readAllResearcherInformation() {
	return readRemoteMethod("readAllResearcherInformation", null);
    }

    public String readAllEmployeeInformation() {
	return readRemoteMethod("readAllEmployeeInformation", null);
    }

    public String readAllGrantOwnerInformation() {
	return readRemoteMethod("readAllGrantOwnerInformation", null);
    }

    public String readAllExternalResearcherInformation() {
	return readRemoteMethod("readAllExternalResearcherInformation", null);
    }

    public String getWorkingPlaceForAnyRoleType() {
	return readRemoteMethod("getWorkingPlaceForAnyRoleType", null);
    }

    public static RemotePerson readByUsername(final RemoteHost remoteHost, final String username) {
	final String externalOid = remoteHost.readRemoteStaticMethod("net.sourceforge.fenixedu.domain.Person", "findByUsername",
		new Object[] { username });
	return (RemotePerson) remoteHost.getRemoteDomainObject(externalOid);
    }

    public String getName() {
	return readRemoteMethod("getName", null);
    }

    public static String readAllUserData(final RemoteHost remoteHost) {
	return remoteHost.readRemoteStaticMethod("net.sourceforge.fenixedu.domain.Person", "readAllUserData", new Object[0]);
    }

}
