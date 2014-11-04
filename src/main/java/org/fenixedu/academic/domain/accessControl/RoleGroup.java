/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain.accessControl;

import java.util.Objects;
import java.util.Set;

import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.bennu.core.annotation.GroupArgument;
import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.joda.time.DateTime;

@Deprecated
@GroupOperator("role")
public class RoleGroup extends FenixGroup {
    private static final long serialVersionUID = -2312726475879576571L;

    @GroupArgument("")
    private RoleType roleType;

    private RoleGroup() {
        super();
    }

    @Override
    public String getPresentationName() {
        return roleType.getLocalizedName();
    }

    @Override
    public Set<User> getMembers() {
        return roleType.actualGroup().getMembers();
    }

    /*
     * Time Machine method not available
     */
    @Override
    public Set<User> getMembers(DateTime when) {
        return getMembers();
    }

    @Override
    public boolean isMember(User user) {
        return roleType.actualGroup().isMember(user);
    }

    /*
     * Time Machine method not available
     */
    @Override
    public boolean isMember(User user, DateTime when) {
        return isMember(user);
    }

    @Override
    public PersistentGroup toPersistentGroup() {
        return roleType.actualGroup().toPersistentGroup();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof RoleGroup) {
            return roleType.equals(((RoleGroup) object).roleType);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(roleType);
    }
}
