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
package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu._development.OAuthProperties;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

public class AppUserSession extends AppUserSession_Base {

    public AppUserSession() {
        super();
    }

    @Override
    public void setCode(String code) {
        super.setCode(code);
        setCreationDate(new DateTime());
    }

    public boolean matchesCode(String code) {
        if (StringUtils.isBlank(code) || StringUtils.isBlank(getCode())) {
            return false;
        }
        return code.equals(getCode());
    }

    public boolean isCodeValid() {
        return !StringUtils.isBlank(getCode())
                && getCreationDate().plusSeconds(OAuthProperties.getConfiguration().getCodeExpirationSeconds()).isAfterNow();
    }

    public boolean matchesAccessToken(String accessToken) {
        if (StringUtils.isBlank(getAccessToken()) || StringUtils.isBlank(accessToken)) {
            return false;
        }
        return getAccessToken().equals(accessToken);
    }

    public boolean isAccessTokenValid() {
        return getCreationDate() != null
                && getCreationDate().plusSeconds(OAuthProperties.getConfiguration().getAccessTokenExpirationSeconds())
                        .isAfterNow();
    }

    public boolean isRefreshTokenValid() {
        return !StringUtils.isBlank(getRefreshToken());
    }

    public boolean matchesRefreshToken(String refreshToken) {
        if (StringUtils.isBlank(getRefreshToken()) || StringUtils.isBlank(refreshToken)) {
            return false;
        }
        return getRefreshToken().equals(refreshToken);
    }

    public String getUsername() {
        return getAppUserAuthorization().getUser().getPerson().getUsername();
    }

    @Atomic
    public void setTokens(String accessToken, String refreshToken) {
        setAccessToken(accessToken);
        setRefreshToken(refreshToken);
        setCode(null);
        setCreationDate(new DateTime());
    }

    @Atomic
    public void setNewAccessToken(String accessToken) {
        setAccessToken(accessToken);
        setCode(null);
        setCreationDate(new DateTime());
    }

    @Atomic(mode = TxMode.WRITE)
    public void delete() {
        setCode(null);
        setAccessToken(null);
        setCreationDate(null);
        setDeviceId(null);
        setAppUserAuthorization(null);
        setRefreshToken(null);
        deleteDomainObject();
    }

    public boolean isActive() {
        return getCreationDate() != null;
    }

}
