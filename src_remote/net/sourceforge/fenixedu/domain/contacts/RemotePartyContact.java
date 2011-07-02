package net.sourceforge.fenixedu.domain.contacts;

public class RemotePartyContact extends RemotePartyContact_Base {

    public RemotePartyContact() {
	super();
    }

    public Boolean isWebAddress() {
	return toBoolean(readRemoteMethod("isWebAddress", null));
    }

    public Boolean isPhysicalAddress() {
	return toBoolean(readRemoteMethod("isPhysicalAddress", null));
    }

    public Boolean isEmailAddress() {
	return toBoolean(readRemoteMethod("isEmailAddress", null));
    }

    public Boolean isPhone() {
	return toBoolean(readRemoteMethod("isPhone", null));
    }

    public Boolean isMobile() {
	return toBoolean(readRemoteMethod("isMobile", null));
    }

    public String getPartyContactTypeString() {
	return readRemoteMethod("getPartyContactTypeString", null);
    }
}
