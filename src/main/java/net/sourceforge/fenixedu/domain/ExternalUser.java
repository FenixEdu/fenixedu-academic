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

import org.fenixedu.bennu.core.domain.Bennu;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;

public class ExternalUser extends ExternalUser_Base {

    public ExternalUser() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public ExternalUser(final String username, final String password) {
        this();
        setUsername(username);
        final String hash = hash(password);
        setPasswordHash(hash);
    }

    private String hash(final String password) {
        return Hashing.sha1().hashString(password, Charsets.UTF_8).toString();
    }

    public boolean verify(final String username, final String password) {
        return username.equals(getUsername()) && hash(password).equals(getPasswordHash());
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasPasswordHash() {
        return getPasswordHash() != null;
    }

    @Deprecated
    public boolean hasUsername() {
        return getUsername() != null;
    }

    public static final boolean isExternalUser(final String username) {
        for (final ExternalUser externalUser : Bennu.getInstance().getExternalUserSet()) {
            if (externalUser.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }
}
