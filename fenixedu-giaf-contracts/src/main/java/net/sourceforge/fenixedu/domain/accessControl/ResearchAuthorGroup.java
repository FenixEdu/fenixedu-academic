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
package org.fenixedu.academic.domain.accessControl;

import java.util.Set;

import org.fenixedu.academic.domain.person.RoleType;

import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.joda.time.DateTime;

import com.google.common.base.Objects;

@GroupOperator("researchAuthor")
public class ResearchAuthorGroup extends FenixGroup {
    private static final long serialVersionUID = 5155805498915779696L;

    private static final ResearchAuthorGroup INSTANCE = new ResearchAuthorGroup();

    private ResearchAuthorGroup() {
        super();
    }

    public static ResearchAuthorGroup get() {
        return INSTANCE;
    }

    @Override
    public Set<User> getMembers() {
        return RoleType.RESEARCHER.actualGroup().getMembers();
    }

    @Override
    public Set<User> getMembers(DateTime when) {
        return getMembers();
    }

    @Override
    public boolean isMember(User user) {
        return RoleType.RESEARCHER.actualGroup().isMember(user);
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        return isMember(user);
    }

    @Override
    public PersistentGroup toPersistentGroup() {
        return PersistentResearchAuthorGroup.getInstance();
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof ResearchAuthorGroup;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(ResearchAuthorGroup.class);
    }
}
