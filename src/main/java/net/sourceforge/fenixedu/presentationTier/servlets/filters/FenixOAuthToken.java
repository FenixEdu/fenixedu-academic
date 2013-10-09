package net.sourceforge.fenixedu.presentationTier.servlets.filters;

import net.sourceforge.fenixedu.domain.AppUserSession;
import net.sourceforge.fenixedu.presentationTier.Action.externalServices.OAuthUtils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;

public class FenixOAuthToken {

    private static final Logger LOGGER = LoggerFactory.getLogger(FenixOAuthToken.class);

    private final AppUserSession appUserSession;
    private final String nounce;

    public static class FenixOAuthTokenException extends Exception {

    }

    public FenixOAuthToken(AppUserSession appUserSession, String token) {
        this.appUserSession = appUserSession;
        this.nounce = token;
    }

    public static FenixOAuthToken parse(String oAuthToken) throws FenixOAuthTokenException {

        if (StringUtils.isBlank(oAuthToken)) {
            throw new FenixOAuthTokenException();
        }

        String accessTokenDecoded = new String(Base64.decodeBase64(oAuthToken));
        String[] accessTokenBuilder = accessTokenDecoded.split(":");

        if (accessTokenBuilder.length != 2) {
            throw new FenixOAuthTokenException();
        }

        String appUserSessionExternalId = accessTokenBuilder[0];
        String accessToken = accessTokenBuilder[1];

        LOGGER.info("AccessToken: {}", accessTokenDecoded);
        LOGGER.info("[0] AppUserSesson ID: {}", appUserSessionExternalId);
        LOGGER.info("[1] Random: {}", accessTokenBuilder[1]);

        AppUserSession appUserSession = appUserSession(appUserSessionExternalId);

        if (appUserSession == null || !appUserSession.isActive()) {
            throw new FenixOAuthTokenException();
        }

        return new FenixOAuthToken(appUserSession, accessToken);
    }

    public String format() {
        String token = Joiner.on(":").join(appUserSession.getExternalId(), nounce);
        return Base64.encodeBase64String(token.getBytes());
    }

    private static AppUserSession appUserSession(String appUserSessionId) {
        return OAuthUtils.getDomainObject(appUserSessionId, AppUserSession.class);
    }

    public AppUserSession getAppUserSession() {
        return appUserSession;
    }

}
