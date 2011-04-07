package net.sourceforge.fenixedu.domain.contacts;

public class RemotePartyContact extends RemotePartyContact_Base {

    public RemotePartyContact() {
	super();
    }

    public Boolean isWebAddress() {
	return toBoolean(readRemoteMethod("isWebAddress"));
    }

    public Boolean isPhysicalAddress() {
	return toBoolean(readRemoteMethod("isPhysicalAddress"));
    }

    public Boolean isEmailAddress() {
	return toBoolean(readRemoteMethod("isEmailAddress"));
    }

    public Boolean isPhone() {
	return toBoolean(readRemoteMethod("isPhone"));
    }

    public Boolean isMobile() {
	return toBoolean(readRemoteMethod("isMobile"));
    }

    public String getPartyContactTypeString() {
	return readRemoteMethod("getPartyContactTypeString");
    }
}
