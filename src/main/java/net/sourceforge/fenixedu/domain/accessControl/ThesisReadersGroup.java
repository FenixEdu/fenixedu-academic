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

import java.util.Collections;
import java.util.Set;

import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.thesis.Thesis;

import org.fenixedu.bennu.core.annotation.GroupArgument;
import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.fenixedu.bennu.core.groups.AnyoneGroup;
import org.joda.time.DateTime;

import com.google.common.base.Objects;

@GroupOperator("thesisReaders")
public class ThesisReadersGroup extends FenixGroup {
    private static final long serialVersionUID = -784604571687620343L;

    @GroupArgument
    private Thesis thesis;

    private ThesisReadersGroup() {
        super();
    }

    private ThesisReadersGroup(Thesis thesis) {
        this();
        this.thesis = thesis;
    }

    public static ThesisReadersGroup get(Thesis thesis) {
        return new ThesisReadersGroup(thesis);
    }

    @Override
    public String[] getPresentationNameKeyArgs() {
        return new String[] { thesis.getTitle().getContent(), thesis.getVisibility().getLocalizedName() };
    }

    @Override
    public Set<User> getMembers() {
        return getMembers(new DateTime());
    }

    @Override
    public Set<User> getMembers(DateTime when) {
        if (thesis.getDocumentsAvailableAfter() != null) {
            DateTime time = thesis.getDocumentsAvailableAfter();

            if (time.isAfter(when)) {
                return Collections.emptySet();
            }
        }

        if (thesis.getVisibility() == null) {
            return Collections.emptySet();
        }

        switch (thesis.getVisibility()) {
        case INTRANET:
            return PersistentRoleGroup.getInstance(RoleType.PERSON).getMembers(when);
        case PUBLIC:
            return AnyoneGroup.get().getMembers(when);
        default:
            return Collections.emptySet();
        }
    }

    @Override
    public boolean isMember(User user) {
        return isMember(user, new DateTime());
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        if (thesis.getDocumentsAvailableAfter() != null) {
            DateTime time = thesis.getDocumentsAvailableAfter();

            if (time.isAfter(when)) {
                return false;
            }
        }

        if (thesis.getVisibility() == null) {
            return false;
        }

        switch (thesis.getVisibility()) {
        case INTRANET:
            return PersistentRoleGroup.getInstance(RoleType.PERSON).isMember(user, when);
        case PUBLIC:
            return AnyoneGroup.get().isMember(user, when);
        default:
            return false;
        }
    }

    @Override
    public PersistentGroup toPersistentGroup() {
        return PersistentThesisReadersGroup.getInstance(thesis);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof ThesisReadersGroup) {
            return Objects.equal(thesis, ((ThesisReadersGroup) object).thesis);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(thesis);
    }
}
