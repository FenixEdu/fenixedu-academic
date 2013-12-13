package net.sourceforge.fenixedu.domain.log.requests;

import pt.ist.bennu.core.domain.Bennu;

public class RequestLogYear extends RequestLogYear_Base {

    public RequestLogYear() {
        super();
        setRootDomainObject(Bennu.getInstance());
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
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

}
