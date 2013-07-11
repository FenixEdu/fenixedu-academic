package net.sourceforge.fenixedu.domain.log.requests;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class RequestLogMonth extends RequestLogMonth_Base {

    public RequestLogMonth() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
        this.setRootDomainObject(null);
        if (this.getYear() != null) {
            this.getYear().removeMonths(this);
        }
        this.setYear(null);
        super.deleteDomainObject();

    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.log.requests.RequestLogDay> getDays() {
        return getDaysSet();
    }

    @Deprecated
    public boolean hasAnyDays() {
        return !getDaysSet().isEmpty();
    }

    @Deprecated
    public boolean hasYear() {
        return getYear() != null;
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasMonthOfYear() {
        return getMonthOfYear() != null;
    }

}
