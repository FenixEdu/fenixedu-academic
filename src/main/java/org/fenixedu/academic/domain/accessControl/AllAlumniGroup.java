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

import org.fenixedu.academic.domain.degreeStructure.ProgramConclusion;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.GroupStrategy;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;

@GroupOperator("allAlumni")
public class AllAlumniGroup extends GroupStrategy {

    private static final long serialVersionUID = -2926898164196025354L;

    @Override
    public String getPresentationName() {
        return BundleUtil.getString(Bundle.GROUP, "label.name.AllAlumniGroup");
    }

    /**
     * Returns true if any of the student registrations has a curriculum group
     * with a conclusion process associated of a program conclusion that provides alumni
     * 
     * @param student
     * @return
     */
    private boolean isAlumni(Student student) {

        return student.getRegistrationsSet().stream().anyMatch(registration -> ProgramConclusion.conclusionsFor(registration)
                .filter(ProgramConclusion::isAlumniProvider).anyMatch(conclusion -> conclusion.groupFor(registration).isPresent()
                        && conclusion.groupFor(registration).get().isConclusionProcessed()));

    }

    @Override
    public Stream<User> getMembers() {
        return Bennu.getInstance().getStudentsSet().stream()
                .filter(student -> student.getAlumni() != null || hasAnyRegistrationConcluded(student) || isAlumni(student))
                .map(student -> student.getPerson().getUser());
    }

    @Override
    public Stream<User> getMembers(DateTime when) {
        return getMembers();
    }

    @Override
    public boolean isMember(User user) {
        return user != null && user.getPerson() != null && user.getPerson().getStudent() != null
                && (user.getPerson().getStudent().getAlumni() != null
                        || hasAnyRegistrationConcluded(user.getPerson().getStudent()) || isAlumni(user.getPerson().getStudent()));
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        return isMember(user);
    }

    private static boolean hasAnyRegistrationConcluded(final Student student) {
        return student.getRegistrationStream().anyMatch(Registration::isConcluded);
    }

}
