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

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.candidacy.Candidacy;
import org.fenixedu.academic.domain.candidacy.CandidacySituationType;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.GroupStrategy;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;

@GroupOperator("candidate")
public class CandidateGroup extends GroupStrategy {

    @Override
    public String getPresentationName() {
        return BundleUtil.getString(Bundle.GROUP, "label.name.CandidateGroup");
    }

    @Override
    public Set<User> getMembers() {
        return Bennu.getInstance().getCandidaciesSet().stream().filter(candidacy -> {
            CandidacySituationType situation = candidacy.getActiveCandidacySituationType();
            return situation != null && situation.isActive() && !situation.equals(CandidacySituationType.REGISTERED);
        }).map(candidacy -> candidacy.getPerson().getUser()).collect(Collectors.toSet());
    }

    @Override
    public Set<User> getMembers(DateTime when) {
        return getMembers();
    }

    @Override
    public boolean isMember(User user) {
        return user != null && user.getPerson() != null && hasActiveCandidacies(user.getPerson());
    }

    private boolean hasActiveCandidacies(Person person) {
        for (Candidacy candidacy : person.getCandidaciesSet()) {
            CandidacySituationType situation = candidacy.getActiveCandidacySituationType();
            if (situation != null && situation.isActive() && !situation.equals(CandidacySituationType.REGISTERED)
                    && !situation.equals(CandidacySituationType.ADMITTED)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        return isMember(user);
    }

}
