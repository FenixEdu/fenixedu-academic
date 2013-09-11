package net.sourceforge.fenixedu.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;

public class ExternalApplication extends ExternalApplication_Base {

    public ExternalApplication() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setSecret(RandomStringUtils.randomAlphanumeric(128));
    }

    public void setScopes(List<AuthScope> scopes) {
        getScopes().clear();
        getScopes().addAll(scopes);
    }

    public boolean matchesUrl(String redirectUrl) {
        return !StringUtils.isBlank(redirectUrl) && redirectUrl.equals(getRedirectUrl());
    }

    public boolean matches(String redirectUrl, String secret) {
        return matchesUrl(redirectUrl) && (!StringUtils.isBlank(secret) && secret.equals(getSecret()));
    }

    public AppUserSession getAppUserSession(String code) {
        for (AppUserSession appUserSession : getAppUserSessionSet()) {
            if (appUserSession.matchesCode(code)) {
                return appUserSession;
            }
        }
        return null;
    }

    public void scopeHasChanged() {
        final Set<AppUserSession> sessions = new HashSet<AppUserSession>(getAppUserSessionSet());
        for (AppUserSession appUserSession : sessions) {
            appUserSession.invalidate();
        }
    }
}
