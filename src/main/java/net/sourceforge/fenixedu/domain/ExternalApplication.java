package net.sourceforge.fenixedu.domain;

import java.util.List;

public class ExternalApplication extends ExternalApplication_Base {

    public ExternalApplication() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setOauthKey("" + Math.random() * 100);
        setClientID("" + Math.random());
    }

    public void setScopes(List<AuthScope> scopes) {
        getScopes().clear();
        getScopes().addAll(scopes);
    }

}
