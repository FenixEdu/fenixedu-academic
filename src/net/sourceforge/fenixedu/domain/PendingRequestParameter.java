package net.sourceforge.fenixedu.domain;

public class PendingRequestParameter extends PendingRequestParameter_Base {
    
    public PendingRequestParameter(String key, String value) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setParameterKey(key);
        setParameterValue(value);
        setAttribute(false);
    }

    public void delete() {
	removeRootDomainObject();
	removePendingRequest();
	deleteDomainObject();
    }
    
}
