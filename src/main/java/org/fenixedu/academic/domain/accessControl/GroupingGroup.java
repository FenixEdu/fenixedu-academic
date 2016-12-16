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

import java.util.stream.Stream;

import org.fenixedu.academic.domain.Grouping;
import org.fenixedu.bennu.core.annotation.GroupArgument;
import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.joda.time.DateTime;

import com.google.common.base.Objects;

@GroupOperator("grouping")
public class GroupingGroup extends FenixGroup {
    private static final long serialVersionUID = 4708634747611078101L;

    @GroupArgument
    private Grouping grouping;

    private GroupingGroup() {
        super();
    }

    private GroupingGroup(Grouping grouping) {
        this();
        this.grouping = grouping;
    }

    public static GroupingGroup get(Grouping grouping) {
        return new GroupingGroup(grouping);
    }

    @Override
    public String[] getPresentationNameKeyArgs() {
        return new String[] { grouping.getName() };
    }

    @Override
    public Stream<User> getMembers() {
        return grouping.getAttendsSet().stream().map(att -> att.getRegistration().getStudent().getPerson().getUser())
                .filter(u -> u != null);
    }

    @Override
    public Stream<User> getMembers(DateTime when) {
        return getMembers();
    }

    @Override
    public boolean isMember(User user) {
        return getMembers().anyMatch(u -> u == user);
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        return isMember(user);
    }

    @Override
    public PersistentGroup toPersistentGroup() {
        return PersistentGroupingGroup.getInstance(grouping);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof GroupingGroup) {
            return Objects.equal(grouping, ((GroupingGroup) object).grouping);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(grouping);
    }
}
