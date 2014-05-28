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

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.StudentGroup;

import org.fenixedu.bennu.core.annotation.GroupArgument;
import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.joda.time.DateTime;

import com.google.common.base.Objects;

@GroupOperator("studentGroup")
public class StudentGroupGroup extends FenixGroup {
    private static final long serialVersionUID = -4888060704338485808L;

    @GroupArgument
    private StudentGroup studentGroup;

    private StudentGroupGroup() {
        super();
    }

    private StudentGroupGroup(StudentGroup studentGroup) {
        this();
        this.studentGroup = studentGroup;
    }

    public static StudentGroupGroup get(StudentGroup studentGroup) {
        return new StudentGroupGroup(studentGroup);
    }

    @Override
    public String[] getPresentationNameKeyArgs() {
        return new String[] { studentGroup.getGrouping().getName() + " - " + studentGroup.getGroupNumber() };
    }

    @Override
    public Set<User> getMembers() {
        Set<User> users = new HashSet<>();
        for (Attends attends : studentGroup.getAttendsSet()) {
            User user = attends.getRegistration().getPerson().getUser();
            if (user != null) {
                users.add(user);
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
        if (user == null || user.getPerson().getStudent() == null) {
            return false;
        }
        for (final Attends attends : studentGroup.getAttendsSet()) {
            if (attends.getRegistration().getStudent().equals(user.getPerson().getStudent())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        return isMember(user);
    }

    @Override
    public PersistentGroup toPersistentGroup() {
        return PersistentStudentGroupGroup.getInstance(studentGroup);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof StudentGroupGroup) {
            return Objects.equal(studentGroup, ((StudentGroupGroup) object).studentGroup);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(studentGroup);
    }
}
