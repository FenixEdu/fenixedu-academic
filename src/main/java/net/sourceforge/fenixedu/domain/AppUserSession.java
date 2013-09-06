package net.sourceforge.fenixedu.domain;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.services.Service;

public class AppUserSession extends AppUserSession_Base {

    public AppUserSession() {
        super();
    }

    @Override
    public void setCode(String code) {
        super.setCode(code);
        setCodeExpirationDate(new DateTime());
    }

    public boolean matchesCode(String code) {
        if (StringUtils.isBlank(getCode()) || StringUtils.isBlank(code)) {
            return false;
        }
        return getCode().equals(code) && getCodeExpirationDate().plusMinutes(1).isAfterNow();
    }

    public boolean matchesAccessToken(String accessToken) {
        if (StringUtils.isBlank(getAccessToken()) || StringUtils.isBlank(accessToken)) {
            return false;
        }
        return getAccessToken().equals(accessToken);
    }

    public boolean isAccessTokenValid() {
        return getAccessToken().equals(accessToken) && getExpirationDate().plusMinutes(60).isAfterNow();
   }

    public boolean matchesRefreshToken(String refreshToken) {
        if (StringUtils.isBlank(getRefreshToken()) || StringUtils.isBlank(refreshToken)) {
            return false;
        }
        return getRefreshToken().equals(refreshToken);
    }

    public void resetCode() {
        setCode(null);
        setCodeExpirationDate(null);
    }

    public String getUsername() {
        return getUser().getPerson().getUsername();
    }

    @Service
    public void setTokens(String accessToken, String refreshToken) {
        setAccessToken(accessToken);
        setRefreshToken(refreshToken);
        setExpirationDate(new DateTime());
        resetCode();
    }

    @Service
    public void setNewAccessToken(String accessToken) {
        setAccessToken(accessToken);
        setExpirationDate(new DateTime());
        resetCode();
    }

    public void delete() {
        setUser(null);
        setApplication(null);
        deleteDomainObject();
    }
    public void invalidate() {
        // TODO notify user
        delete();
    }
}
