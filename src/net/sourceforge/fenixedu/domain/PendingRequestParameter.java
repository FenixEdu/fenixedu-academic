package net.sourceforge.fenixedu.domain;

public class PendingRequestParameter extends PendingRequestParameter_Base {
    
    public  PendingRequestParameter(String key, String value) {
        super();
        setParameterKey(key);
        setParameterValue(value);
    }

    public void delete() {
	removeRootDomainObject();
	removePendingRequest();
	deleteDomainObject();
    }
    
}
