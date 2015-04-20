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
import java.util.stream.Stream;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.GroupStrategy;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;

@GroupOperator("activeStudents")
public class ActiveStudentsGroup extends GroupStrategy {

    private static final long serialVersionUID = 2139482012047494196L;

    @Override
    public String getPresentationName() {
        return BundleUtil.getString(Bundle.GROUP, "label.name.ActiveStudentsGroup");
    }

    @Override
    public Set<User> getMembers() {
        return Stream
                .concat(Bennu.getInstance().getPhdProgramsSet().stream()
                        .flatMap(program -> program.getIndividualProgramProcessesSet().stream())
                        .filter(PhdIndividualProgramProcess::isProcessActive).map(PhdIndividualProgramProcess::getPerson),
                        Bennu.getInstance().getStudentsSet().stream().filter(Student::hasActiveRegistrations)
                                .map(Student::getPerson)).map(Person::getUser).collect(Collectors.toSet());
    }

    @Override
    public Set<User> getMembers(DateTime when) {
        return getMembers();
    }

    @Override
    public boolean isMember(User user) {
        if (user == null || user.getPerson() == null) {
            return false;
        }
        return (user.getPerson().getStudent() != null && user.getPerson().getStudent().hasActiveRegistrations())
                || user.getPerson().getPhdIndividualProgramProcessesSet().stream()
                        .anyMatch(PhdIndividualProgramProcess::isProcessActive);
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        return isMember(user);
    }

}
