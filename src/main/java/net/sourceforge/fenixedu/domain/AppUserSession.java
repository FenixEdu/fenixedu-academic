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
        return getCode().equals(code) && getCodeExpirationDate().plusMinutes(1).isBeforeNow();
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
}
