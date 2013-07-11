package net.sourceforge.fenixedu.domain.log.requests;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class RequestLogYear extends RequestLogYear_Base {

    public RequestLogYear() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
        this.setRootDomainObject(null);
        super.deleteDomainObject();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.log.requests.RequestLogMonth> getMonths() {
        return getMonthsSet();
    }

    @Deprecated
    public boolean hasAnyMonths() {
        return !getMonthsSet().isEmpty();
    }

    @Deprecated
    public boolean hasYear() {
        return getYear() != null;
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

}
