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
package org.fenixedu.academic.ui.spring.controller.academicAdministration;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.UserProfile;

public class MoveRegistrationParameters {
    private User source;
    private User target;
    private Set<User> similars;
    private User searched;

    public MoveRegistrationParameters() {
    }

    public MoveRegistrationParameters(User target) {
        setTarget(target);
    }

    public User getSource() {
        return source;
    }

    public void setSource(User source) {
        this.source = source;
    }

    public User getTarget() {
        return target;
    }

    public void setTarget(User target) {
        this.target = target;
        findSimilarAccounts();
    }

    public Set<User> getSimilars() {
        return similars;
    }

    public User getSearched() {
        return searched;
    }

    public void setSearched(User searched) {
        this.searched = searched;
    }

    private void findSimilarAccounts() {
        String[] parts = target.getProfile().getFullName().split(" ");
        String query = parts.length > 1 ? parts[0] + " " + parts[parts.length - 1] : target.getProfile().getFullName();
        similars =
                UserProfile.searchByName(query, Integer.MAX_VALUE).map(UserProfile::getUser).filter(Objects::nonNull)
                        .filter(u -> !u.equals(target)).filter(u -> barelyCompatibleStudent(u, target))
                        .collect(Collectors.toSet());
    }

    private static boolean barelyCompatibleStudent(User user, User target) {
        return user.getPerson().getStudent() != null && !user.getPerson().getStudent().getRegistrationsSet().isEmpty()
                && user.getPerson().getDateOfBirthYearMonthDay() != null
                && user.getPerson().getDateOfBirthYearMonthDay().equals(target.getPerson().getDateOfBirthYearMonthDay());
    }
}
