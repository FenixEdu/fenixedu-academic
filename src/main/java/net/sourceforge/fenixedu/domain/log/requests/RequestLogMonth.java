package net.sourceforge.fenixedu.domain.log.requests;

import pt.ist.bennu.core.domain.Bennu;

public class RequestLogMonth extends RequestLogMonth_Base {

    public RequestLogMonth() {
        super();
        setRootDomainObject(Bennu.getInstance());
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
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasMonthOfYear() {
        return getMonthOfYear() != null;
    }

}
