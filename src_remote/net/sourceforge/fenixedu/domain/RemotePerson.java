package net.sourceforge.fenixedu.domain;


public class RemotePerson extends RemotePerson_Base {
    
    public RemotePerson() {
        super();
    }

    public String getEmailForSendingEmails() {
	return (String) readRemoteMethod("getEmailForSendingEmails");
    }

    public String getWorkingPlaceCostCenter() {
	return (String) readRemoteMethod("getWorkingPlaceCostCenter");
    }

    public String getEmployeeRoleDescription() {
	return (String) readRemoteMethod("getEmployeeRoleDescription");
    }

    public String readAllTeacherInformation() {
	return (String) readRemoteMethod("readAllTeacherInformation");
    }

    public String readAllResearcherInformation() {
	return (String) readRemoteMethod("readAllTeacherInformation");
    }

    public String readAllEmployeeInformation() {
	return (String) readRemoteMethod("readAllTeacherInformation");
    }

}
