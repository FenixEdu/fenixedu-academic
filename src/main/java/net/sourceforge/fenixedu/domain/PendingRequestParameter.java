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

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasParameterValue() {
        return getParameterValue() != null;
    }

    @Deprecated
    public boolean hasAttribute() {
        return getAttribute() != null;
    }

    @Deprecated
    public boolean hasPendingRequest() {
        return getPendingRequest() != null;
    }

    @Deprecated
    public boolean hasParameterKey() {
        return getParameterKey() != null;
    }

}
