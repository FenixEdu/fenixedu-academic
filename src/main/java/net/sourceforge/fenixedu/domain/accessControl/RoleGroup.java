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
package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.fenixedu.bennu.core.annotation.GroupArgument;
import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.joda.time.DateTime;

@GroupOperator("role")
public class RoleGroup extends FenixGroup {
    private static final long serialVersionUID = -2312726475879576571L;

    @GroupArgument("")
    private Role role;

    private RoleGroup() {
        super();
    }

    private RoleGroup(Role role) {
        this.role = role;
    }

    public static RoleGroup get(Role role) {
        Objects.requireNonNull(role, "role");
        return new RoleGroup(role);
    }

    public static RoleGroup get(RoleType roleType) {
        return get(Role.getRoleByRoleType(roleType));
    }

    @Override
    public String getPresentationName() {
        return role.getRoleType().getLocalizedName();
    }

    @Override
    public Set<User> getMembers() {
        return role.getAssociatedPersonsSet().stream().filter((person) -> person.getUser() != null).map(Person::getUser)
                .collect(Collectors.toSet());
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
        if (user == null) {
            return false;
        }
        return user.getPerson().hasPersonRoles(role);
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
        return PersistentRoleGroup.getInstance(role);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof RoleGroup) {
            return role.equals(((RoleGroup) object).role);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(role);
    }
}
