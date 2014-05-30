/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.presentationTier.servlets.filters;

import net.sourceforge.fenixedu.domain.AppUserSession;
import net.sourceforge.fenixedu.presentationTier.Action.externalServices.OAuthUtils;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Base64;

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

        String accessTokenDecoded = new String(Base64.getDecoder().decode(oAuthToken));
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
        return Base64.getEncoder().encodeToString(token.getBytes());
    }

    private static AppUserSession appUserSession(String appUserSessionId) {
        return OAuthUtils.getDomainObject(appUserSessionId, AppUserSession.class);
    }

    public AppUserSession getAppUserSession() {
        return appUserSession;
    }

}
