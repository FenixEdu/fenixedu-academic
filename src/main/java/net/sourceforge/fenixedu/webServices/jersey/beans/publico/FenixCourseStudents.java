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
package net.sourceforge.fenixedu.webServices.jersey.beans.publico;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.student.Registration;

public class FenixCourseStudents {

    public static class FenixCourseStudent {

        String username;
        String name;
        FenixDegree degree;

        public FenixCourseStudent(final Attends attends) {
            final Registration registration = attends.getRegistration();
            final Person person = registration.getPerson();
            setUsername(person.getUsername());
            setName(person.getName());
            setDegree(new FenixDegree(registration.getDegree()));
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public FenixDegree getDegree() {
            return degree;
        }

        public void setDegree(FenixDegree degree) {
            this.degree = degree;
        }

//        public String getName() {
//            return name;
//        }

        public void setName(String name) {
            this.name = name;
        }

    }

    int enrolmentCount;
    int attendingCount;
    List<FenixCourseStudent> students = new ArrayList<>();

    public FenixCourseStudents(final ExecutionCourse executionCourse) {
        final Set<Attends> attendsSet = executionCourse.getAttendsSet();
        attendingCount = attendsSet.size();

        for (final Attends attends : attendsSet) {
            final Enrolment enrolment = attends.getEnrolment();
            if (enrolment != null) {
                enrolmentCount++;
            }

            students.add(new FenixCourseStudent(attends));
        }
    }

    public int getEnrolmentCount() {
        return enrolmentCount;
    }

    public void setEnrolmentCount(int enrolmentCount) {
        this.enrolmentCount = enrolmentCount;
    }

    public int getAttendingCount() {
        return attendingCount;
    }

    public void setAttendingCount(int attendingCount) {
        this.attendingCount = attendingCount;
    }

    public List<FenixCourseStudent> getStudents() {
        return students;
    }

    public void setStudents(List<FenixCourseStudent> students) {
        this.students = students;
    }

}
