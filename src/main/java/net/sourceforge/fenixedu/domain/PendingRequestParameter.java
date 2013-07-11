package net.sourceforge.fenixedu.domain;

public class PendingRequestParameter extends PendingRequestParameter_Base {

    public PendingRequestParameter(String key, String value, boolean isAttribute) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setParameterKey(key);
        setParameterValue(value);
        setAttribute(Boolean.valueOf(isAttribute));
    }

    public void delete() {
        setRootDomainObject(null);
        setPendingRequest(null);
        deleteDomainObject();
    }

}
