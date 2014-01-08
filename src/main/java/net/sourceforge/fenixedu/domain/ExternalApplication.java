package net.sourceforge.fenixedu.domain;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixframework.Atomic;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;

public class ExternalApplication extends ExternalApplication_Base {

    private static final Logger logger = LoggerFactory.getLogger(ExternalApplication.class);

    public ExternalApplication() {
        super();
        setRootDomainObject(Bennu.getInstance());
        setSecret(RandomStringUtils.randomAlphanumeric(115));
    }

    public void setScopeList(List<AuthScope> newScopes) {
        Set<AuthScope> oldScopes = getScopesSet();
        if (CollectionUtils.subtract(newScopes, oldScopes).size() > 0) {
            deleteAuthorizations();
        }
        oldScopes.clear();
        oldScopes.addAll(newScopes);
    }

    public List<AuthScope> getScopeList() {
        return new ArrayList<AuthScope>(getScopesSet());
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

    public void deleteAuthorizations() {
        for (AppUserAuthorization authorization : new HashSet<AppUserAuthorization>(getAppUserAuthorizationSet())) {
            authorization.delete();
        }
    }

    private Set<AppUserSession> getAppUserSessionSet() {
        return FluentIterable.from(getAppUserAuthorizationSet())
                .transformAndConcat(new Function<AppUserAuthorization, Iterable<AppUserSession>>() {

                    @Override
                    public Iterable<AppUserSession> apply(AppUserAuthorization auth) {
                        return auth.getSessionSet();
                    }

                }).toSet();
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
            logger.error(e.getMessage(), e);
        }
    }

    public void setLogoStream(ByteArrayInputStream stream) {
        setLogoStream((InputStream) stream);
    }

    @Atomic
    public void delete() {
        setRootDomainObject(null);
        setAuthor(null);
        getScopesSet().clear();
        deleteAuthorizations();
        deleteDomainObject();
    }

    public AppUserAuthorization getAppUserAuthorization(User user) {
        for (AppUserAuthorization authorization : getAppUserAuthorizationSet()) {
            if (authorization.getUser().equals(user)) {
                return authorization;
            }
        }
        return null;
    }

    public boolean hasAppUserAuthorization(User user) {
        return getAppUserAuthorization(user) != null;
    }

}
