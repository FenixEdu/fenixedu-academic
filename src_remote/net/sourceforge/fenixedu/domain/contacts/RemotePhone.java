package net.sourceforge.fenixedu.domain.contacts;

public class RemotePhone extends RemotePhone_Base {

    public RemotePhone() {
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
