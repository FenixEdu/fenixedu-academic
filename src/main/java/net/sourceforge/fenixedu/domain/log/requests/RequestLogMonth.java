package net.sourceforge.fenixedu.domain.log.requests;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class RequestLogMonth extends RequestLogMonth_Base {

    public RequestLogMonth() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
        this.removeRootDomainObject();
        if (this.getYear() != null) {
            this.getYear().removeMonths(this);
        }
        this.removeYear();
        super.deleteDomainObject();

    }

}
