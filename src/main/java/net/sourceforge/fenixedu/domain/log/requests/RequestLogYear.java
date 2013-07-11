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

}
