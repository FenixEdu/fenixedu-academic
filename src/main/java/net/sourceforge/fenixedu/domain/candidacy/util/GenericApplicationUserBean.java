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
package net.sourceforge.fenixedu.domain.candidacy.util;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.period.GenericApplicationPeriod;

import org.fenixedu.bennu.core.domain.User;

import pt.ist.fenixframework.Atomic;

public class GenericApplicationUserBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private final GenericApplicationPeriod genericApplicationPeriod;
    private String username;

    public GenericApplicationUserBean(final GenericApplicationPeriod genericApplicationPeriod) {
        this.genericApplicationPeriod = genericApplicationPeriod;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Atomic
    public void addManagerUser() {
        if (genericApplicationPeriod.isCurrentUserAllowedToMange()) {
            final User user = User.findByUsername(username);
            if (user != null) {
                genericApplicationPeriod.addManager(user);
            }
        }
    }

}
