package net.sourceforge.fenixedu.domain;

import pt.ist.fenixframework.plugins.remote.domain.RemoteHost;

public class RemotePerson extends RemotePerson_Base {

    public RemotePerson() {
	super();
    }

    public String getUserAliass() {
	return readRemoteMethod("getUserAliass");
    }

    public String getEmailForSendingEmails() {
	return readRemoteMethod("getEmailForSendingEmails");
    }

    public String getWorkingPlaceCostCenter() {
	return readRemoteMethod("getWorkingPlaceCostCenter");
    }

    public String getEmployeeRoleDescription() {
	return readRemoteMethod("getEmployeeRoleDescription");
    }

    public String readAllTeacherInformation() {
	return readRemoteMethod("readAllTeacherInformation");
    }

    public String readAllResearcherInformation() {
	return readRemoteMethod("readAllResearcherInformation");
    }

    public String readAllEmployeeInformation() {
	return readRemoteMethod("readAllEmployeeInformation");
    }

    public String readAllGrantOwnerInformation() {
	return readRemoteMethod("readAllGrantOwnerInformation");
    }

    public String readAllExternalResearcherInformation() {
	return readRemoteMethod("readAllExternalResearcherInformation");
    }

    public String getWorkingPlaceForAnyRoleType() {
	return readRemoteMethod("getWorkingPlaceForAnyRoleType");
    }

    public static RemotePerson readByUsername(final RemoteHost remoteHost, final String username) {
	final String externalOid = remoteHost.readRemoteStaticMethod(
		"net.sourceforge.fenixedu.domain.Person", "findByUsername", username);
	return (RemotePerson) remoteHost.getRemoteDomainObject(externalOid);
    }

    public String getName() {
	return readRemoteMethod("getName");
    }
    
}
