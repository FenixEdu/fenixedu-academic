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
package net.sourceforge.fenixedu.domain.util.email;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.accessControl.StudentGroup;
import net.sourceforge.fenixedu.domain.accessControl.TeacherGroup;
import net.sourceforge.fenixedu.domain.accessControl.TeacherResponsibleOfExecutionCourseGroup;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import pt.ist.fenixframework.Atomic;

public class ExecutionCourseSender extends ExecutionCourseSender_Base {

    public static Comparator<ExecutionCourseSender> COMPARATOR_BY_EXECUTION_COURSE_SENDER =
            new Comparator<ExecutionCourseSender>() {

                @Override
                public int compare(final ExecutionCourseSender executionCourseSender1,
                        final ExecutionCourseSender executionCourseSender2) {
                    final ExecutionCourse executionCourse1 = executionCourseSender1.getCourse();
                    final ExecutionCourse executionCourse2 = executionCourseSender2.getCourse();
                    final ExecutionSemester executionSemester1 = executionCourse1.getExecutionPeriod();
                    final ExecutionSemester executionSemester2 = executionCourse2.getExecutionPeriod();
                    final int p = executionSemester1.compareTo(executionSemester2);
                    if (p == 0) {
                        final int n = executionCourse1.getName().compareTo(executionCourse2.getName());
                        return n == 0 ? executionCourseSender1.hashCode() - executionCourseSender2.hashCode() : n;
                    }
                    return p;
                }
            };

    public ExecutionCourseSender(ExecutionCourse executionCourse) {
        super();
        setCourse(executionCourse);
        setFromAddress(Sender.getNoreplyMail());
        addReplyTos(new ExecutionCourseReplyTo());
        addReplyTos(new CurrentUserReplyTo());
        setMembers(TeacherGroup.get(executionCourse));
        final String labelECTeachers =
                BundleUtil.getString(Bundle.SITE,
                        "label.net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseTeachersGroupWithName",
                        new String[] { executionCourse.getNome() });
        final String labelECStudents =
                BundleUtil.getString(Bundle.SITE,
                        "label.net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseStudentsGroupWithName",
                        new String[] { executionCourse.getNome() });
        final String labelECResponsibleTeachers =
                BundleUtil.getString(Bundle.SITE,
                        "label.net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseResponsibleTeachersGroupWithName",
                        new String[] { executionCourse.getNome() });
        // fixed recipients
        addRecipients(new Recipient(labelECTeachers, TeacherGroup.get(executionCourse)));
        addRecipients(new Recipient(labelECStudents, StudentGroup.get(executionCourse)));
        addRecipients(new Recipient(labelECResponsibleTeachers, TeacherResponsibleOfExecutionCourseGroup.get(executionCourse)));
        setFromName(createFromName());
    }

    public String createFromName() {
        if (getCourse() != null && getCourse().getExecutionPeriod() != null
                && getCourse().getExecutionPeriod().getQualifiedName() != null) {
            String degreeName = getCourse().getDegreePresentationString();
            String courseName = getCourse().getNome();
            String period = getCourse().getExecutionPeriod().getQualifiedName().replace('/', '-');
            return String.format("%s (%s: %s, %s)", Unit.getInstitutionAcronym(), degreeName, courseName, period);
        } else {
            return getFromName();
        }

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

    @Deprecated
    public boolean hasCourse() {
        return getCourse() != null;
    }

}
