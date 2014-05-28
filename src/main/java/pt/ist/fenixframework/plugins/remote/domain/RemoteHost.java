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
package pt.ist.fenixframework.plugins.remote.domain;

import org.apache.commons.lang.StringUtils;

import pt.utl.ist.fenix.tools.util.Strings;

public class RemoteHost extends RemoteHost_Base {

    public RemoteHost() {
        super();
        setRemoteSystem(RemoteSystem.getInstance());
        setAllowInvocationAccess(Boolean.FALSE);
    }

    public RemoteHost(final Strings url, final String username, final String password, final Boolean allowInvocationAccess) {
        this();
        setUrl(url);
        setUsername(username);
        setPassword(password);
        setAllowInvocationAccess(allowInvocationAccess);
    }

    public void delete() {
        setRemoteSystem(null);
        deleteDomainObject();
    }

    @Override
    public Boolean getAllowInvocationAccess() {
        final Boolean value = super.getAllowInvocationAccess();
        return value == null ? Boolean.FALSE : value;
    }

    public boolean matches(final String host, final String username, final String password) {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return false;
        }
        return getUrl().contains(host) && username.equalsIgnoreCase(getUsername()) && password.equals(getPassword());
    }

    @Deprecated
    public boolean hasName() {
        return getName() != null;
    }

    @Deprecated
    public boolean hasUrl() {
        return getUrl() != null;
    }

    @Deprecated
    public boolean hasPassword() {
        return getPassword() != null;
    }

    @Deprecated
    public boolean hasUsername() {
        return getUsername() != null;
    }

    @Deprecated
    public boolean hasRemoteSystem() {
        return getRemoteSystem() != null;
    }

    @Deprecated
    public boolean hasAllowInvocationAccess() {
        return getAllowInvocationAccess() != null;
    }

}
