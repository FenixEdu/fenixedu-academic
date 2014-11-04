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

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;

import org.fenixedu.bennu.core.annotation.GroupArgument;
import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.joda.time.DateTime;

import com.google.common.base.Objects;

@GroupOperator("persistentMembers")
public class MembersLinkGroup extends FenixGroup {
    private static final long serialVersionUID = 179427579195576958L;

    @GroupArgument
    private PersistentGroupMembers persistentGroupMembers;

    private MembersLinkGroup() {
        super();
    }

    protected MembersLinkGroup(PersistentGroupMembers persistentGroupMembers) {
        this();
        this.persistentGroupMembers = persistentGroupMembers;
    }

    public static MembersLinkGroup get(PersistentGroupMembers persistentGroupMembers) {
        return new MembersLinkGroup(persistentGroupMembers);
    }

    @Override
    public String getPresentationName() {
        return persistentGroupMembers.getName();
    }

    @Override
    public Set<User> getMembers() {
        Set<User> users = new HashSet<>();
        for (Person person : persistentGroupMembers.getPersonsSet()) {
            if (person.getUser() != null) {
                users.add(person.getUser());
            }
        }
        return users;
    }

    @Override
    public Set<User> getMembers(DateTime when) {
        return getMembers();
    }

    @Override
    public boolean isMember(User user) {
        return user != null && persistentGroupMembers.getPersonsSet().contains(user.getPerson());
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        return isMember(user);
    }

    @Override
    public PersistentGroup toPersistentGroup() {
        return PersistentMembersLinkGroup.getInstance(persistentGroupMembers);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof MembersLinkGroup) {
            return Objects.equal(persistentGroupMembers, ((MembersLinkGroup) object).persistentGroupMembers);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(persistentGroupMembers);
    }
}
