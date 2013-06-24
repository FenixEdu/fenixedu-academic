package net.sourceforge.fenixedu.domain;

public class AuthScope extends AuthScope_Base {

    public AuthScope() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

}
