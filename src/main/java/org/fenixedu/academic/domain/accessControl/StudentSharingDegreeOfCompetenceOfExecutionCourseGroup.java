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

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import org.fenixedu.academic.domain.Attends;
import org.fenixedu.academic.domain.CompetenceCourse;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.bennu.core.annotation.GroupArgument;
import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.joda.time.DateTime;

import com.google.common.base.Objects;

@GroupOperator("studentSharingDegreeOfCompetenceOfExecutionCourse")
public class StudentSharingDegreeOfCompetenceOfExecutionCourseGroup extends FenixGroup {
    private static final long serialVersionUID = -16490512164795235L;

    @GroupArgument
    private ExecutionCourse executionCourse;

    private StudentSharingDegreeOfCompetenceOfExecutionCourseGroup() {
        super();
    }

    private StudentSharingDegreeOfCompetenceOfExecutionCourseGroup(ExecutionCourse executionCourse) {
        this();
        this.executionCourse = executionCourse;
    }

    public static StudentSharingDegreeOfCompetenceOfExecutionCourseGroup get(ExecutionCourse executionCourse) {
        return new StudentSharingDegreeOfCompetenceOfExecutionCourseGroup(executionCourse);
    }

    @Override
    public String[] getPresentationNameKeyArgs() {
        return new String[] { executionCourse.getNameI18N().getContent() };
    }

    @Override
    public Stream<User> getMembers() {
        Set<User> users = new HashSet<User>();

        Set<Degree> degrees = new HashSet<>();
        for (CompetenceCourse competenceCourse : executionCourse.getCompetenceCourses()) {
            for (CurricularCourse curricularCourse : competenceCourse.getAssociatedCurricularCoursesSet()) {
                degrees.add(curricularCourse.getDegree());
            }
        }
        // students of any degree sharing the same competence of the given execution course
        for (Degree degree : degrees) {
            for (Registration registration : degree.getActiveRegistrations()) {
                User user = registration.getPerson().getUser();
                if (user != null) {
                    users.add(user);
                }
            }
        }
        // students attending the given execution course (most will be in the previous case but some may not)
        for (Attends attends : executionCourse.getAttendsSet()) {
            User user = attends.getRegistration().getPerson().getUser();
            if (user != null) {
                users.add(user);
            }
        }
        return users.stream();
    }

    @Override
    public Stream<User> getMembers(DateTime when) {
        return getMembers();
    }

    @Override
    public boolean isMember(User user) {
        if (user == null) {
            return false;
        }
        if (user.getPerson().getStudent() != null) {
            final Set<CompetenceCourse> competenceCourses = executionCourse.getCompetenceCourses();
            for (Registration registration : user.getPerson().getStudent().getRegistrationsSet()) {
                // students of any degree sharing the same competence of the given execution course
                for (StudentCurricularPlan studentCurricularPlan : registration.getStudentCurricularPlansSet()) {
                    for (Enrolment enrolment : studentCurricularPlan.getEnrolmentsSet()) {
                        CompetenceCourse competenceCourse = enrolment.getCurricularCourse().getCompetenceCourse();
                        if (competenceCourses.contains(competenceCourse)) {
                            return true;
                        }
                    }
                }
                // students attending the given execution course (most will be in the previous case but some may not)
                if (registration.getAttendingExecutionCoursesFor().contains(executionCourse)) {
                    return true;
                }
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
        return PersistentStudentSharingDegreeOfCompetenceOfExecutionCourseGroup.getInstance(executionCourse);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof StudentSharingDegreeOfCompetenceOfExecutionCourseGroup) {
            return Objects.equal(executionCourse,
                    ((StudentSharingDegreeOfCompetenceOfExecutionCourseGroup) object).executionCourse);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(executionCourse);
    }
}
