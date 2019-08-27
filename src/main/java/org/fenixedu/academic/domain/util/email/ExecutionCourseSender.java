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
package org.fenixedu.academic.domain.util.email;

import java.util.Comparator;
import java.util.Objects;

import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.accessControl.StudentGroup;
import org.fenixedu.academic.domain.accessControl.TeacherGroup;
import org.fenixedu.academic.domain.accessControl.TeacherResponsibleOfExecutionCourseGroup;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.ist.fenixframework.Atomic;

public class ExecutionCourseSender extends ExecutionCourseSender_Base {

    public static Comparator<ExecutionCourseSender> COMPARATOR_BY_EXECUTION_COURSE_SENDER = Comparator.nullsFirst(
            Comparator.comparing(ExecutionCourseSender::getCourse, ExecutionCourse.EXECUTION_COURSE_EXECUTION_PERIOD_COMPARATOR
                    .thenComparing(ExecutionCourse.EXECUTION_COURSE_NAME_COMPARATOR)));

    public ExecutionCourseSender(ExecutionCourse executionCourse) {
        super();
        setCourse(Objects.requireNonNull(executionCourse));
        setFromAddress(Bennu.getInstance().getSystemSender().getFromAddress());
        addReplyTos(new ExecutionCourseReplyTo());
        addReplyTos(new CurrentUserReplyTo());
        setMembers(TeacherGroup.get(executionCourse));
        final String labelECTeachers = BundleUtil.getString(Bundle.SITE,
                "label.org.fenixedu.academic.domain.accessControl.ExecutionCourseTeachersGroupWithName",
                executionCourse.getNome());
        final String labelECStudents = BundleUtil.getString(Bundle.SITE,
                "label.org.fenixedu.academic.domain.accessControl.ExecutionCourseStudentsGroupWithName",
                executionCourse.getNome());
        final String labelECResponsibleTeachers = BundleUtil.getString(Bundle.SITE,
                "label.org.fenixedu.academic.domain.accessControl.ExecutionCourseResponsibleTeachersGroupWithName",
                executionCourse.getNome());
        // fixed recipients
        addRecipients(new Recipient(labelECTeachers, TeacherGroup.get(executionCourse)));
        addRecipients(new Recipient(labelECStudents, StudentGroup.get(executionCourse)));
        addRecipients(new Recipient(labelECResponsibleTeachers, TeacherResponsibleOfExecutionCourseGroup.get(executionCourse)));
    }

    @Override
    public String getFromName() {
        if (getCourse() != null && getCourse().getExecutionInterval() != null
                && getCourse().getExecutionInterval().getQualifiedName() != null) {
            String degreeName = getCourse().getDegreePresentationString();
            String courseCode = getCourse().getCode();
            String courseName = getCourse().getNome();
            String period = getCourse().getExecutionInterval().getQualifiedName().replace('/', '-');
            return String.format("%s - %s, %s (%s)", courseCode, courseName, period, degreeName);
        }
        return super.getFromName();
    }

    @Override
    public void delete() {
        setCourse(null);
        super.delete();
    }

    @Atomic
    public static ExecutionCourseSender newInstance(ExecutionCourse ec) {
        ExecutionCourseSender sender = ec.getSender();
        return sender == null ? new ExecutionCourseSender(ec) : sender;
    }

}
