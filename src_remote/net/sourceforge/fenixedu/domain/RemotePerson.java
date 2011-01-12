package net.sourceforge.fenixedu.domain;

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

}
