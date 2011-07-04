package net.sourceforge.fenixedu.domain.contacts;

public class RemoteWebAddress extends RemoteWebAddress_Base {

    public RemoteWebAddress() {
	super();
    }

    @Override
    public Boolean isWebAddress() {
	return toBoolean(readRemoteMethod("isWebAddress", null));
    }

    @Override
    public Boolean isPhysicalAddress() {
	return toBoolean(readRemoteMethod("isPhysicalAddress", null));
    }

    @Override
    public Boolean isEmailAddress() {
	return toBoolean(readRemoteMethod("isEmailAddress", null));
    }

    @Override
    public Boolean isPhone() {
	return toBoolean(readRemoteMethod("isPhone", null));
    }

    @Override
    public Boolean isMobile() {
	return toBoolean(readRemoteMethod("isMobile", null));
    }
}
