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

import java.util.Set;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.thesis.Thesis;
import org.fenixedu.bennu.core.annotation.GroupArgument;
import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.fenixedu.bennu.core.groups.AnyoneGroup;
import org.fenixedu.bennu.core.groups.LoggedGroup;
import org.joda.time.DateTime;

import com.google.common.base.Objects;

@GroupOperator("thesisReaders")
@Deprecated
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
        if (thesis.isEvaluated()
                && (thesis.getDocumentsAvailableAfter() == null || thesis.getDocumentsAvailableAfter().isBeforeNow())) {
            if (thesis.getVisibility() != null) {
                switch (thesis.getVisibility()) {
                case INTRANET:
                    return LoggedGroup.get().getMembers(when);
                case PUBLIC:
                    return AnyoneGroup.get().getMembers(when);
                }
            }
        }
        return getThesisMembers();
    }

    @Override
    public boolean isMember(User user) {
        return isMember(user, new DateTime());
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        if (thesis.isEvaluated()
                && (thesis.getDocumentsAvailableAfter() == null || thesis.getDocumentsAvailableAfter().isBeforeNow())) {
            if (thesis.getVisibility() != null) {
                switch (thesis.getVisibility()) {
                case INTRANET:
                    return LoggedGroup.get().isMember(user, when);
                case PUBLIC:
                    return AnyoneGroup.get().isMember(user, when);
                }
            }
        }
        return getThesisMembers().contains(user);
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

    private Set<User> getThesisMembers() {
        Set<User> members =
                thesis.getParticipationsSet().stream().filter(p -> p.getPerson() != null).map((p) -> p.getPerson().getUser())
                        .collect(Collectors.toSet());
        members.add(thesis.getStudent().getPerson().getUser());
        return members;
    }
}
