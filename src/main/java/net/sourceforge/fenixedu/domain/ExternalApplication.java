package net.sourceforge.fenixedu.domain;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;

import pt.ist.fenixWebFramework.services.Service;

public class ExternalApplication extends ExternalApplication_Base {

    public ExternalApplication() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setSecret(RandomStringUtils.randomAlphanumeric(115));
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

    public InputStream getLogoStream() {
        return null;
    }

    public void setLogoStream(InputStream stream) {
        try {
            if (stream != null) {
                byte[] byteArray = IOUtils.toByteArray(stream);
                if (byteArray.length > 0) {
                    setLogo(byteArray);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setLogoStream(ByteArrayInputStream stream) {
        setLogoStream((InputStream) stream);
    }

    @Service
    public void delete() {
        setRootDomainObject(null);
        setAuthor(null);
        getScopes().clear();
        Set<AppUserSession> sessions = new HashSet<AppUserSession>(getAppUserSessionSet());
        for (AppUserSession session : sessions) {
            session.delete();
        }
        deleteDomainObject();
    }

    public boolean hasUserPermission(User user) {
        for (AppUserSession session : getAppUserSession()) {
            if (session.getUser().equals(user)) {
                return true;
            }
        }
        return false;
    }

}
