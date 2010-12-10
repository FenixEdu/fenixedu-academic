package net.sourceforge.fenixedu.domain.contacts;

public class RemotePhysicalAddress extends RemotePhysicalAddress_Base {

    public RemotePhysicalAddress() {
	super();
    }

    @Override
    public Boolean isWebAddress() {
	return toBoolean(readRemoteMethod("isWebAddress"));
    }

    @Override
    public Boolean isPhysicalAddress() {
	return toBoolean(readRemoteMethod("isPhysicalAddress"));
    }

    @Override
    public Boolean isEmailAddress() {
	return toBoolean(readRemoteMethod("isEmailAddress"));
    }

    @Override
    public Boolean isPhone() {
	return toBoolean(readRemoteMethod("isPhone"));
    }

    @Override
    public Boolean isMobile() {
	return toBoolean(readRemoteMethod("isMobile"));
    }
}
