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
package org.fenixedu.academic.service.services.teacher;

import static org.fenixedu.academic.predicate.AccessControl.check;

import java.util.HashSet;
import java.util.Set;

import org.fenixedu.academic.domain.Attends;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.ProjectSubmission;
import org.fenixedu.academic.domain.util.email.ExecutionCourseSender;
import org.fenixedu.academic.domain.util.email.Message;
import org.fenixedu.academic.domain.util.email.Recipient;
import org.fenixedu.academic.domain.util.email.Sender;
import org.fenixedu.academic.predicate.RolePredicates;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.groups.UserGroup;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.ist.fenixframework.Atomic;

public class NotifyStudentGroup {

    @Atomic
    public static void run(ProjectSubmission submission, ExecutionCourse course, Person person) {
        check(RolePredicates.TEACHER_PREDICATE);

        Set<Person> recievers = new HashSet<Person>();

        for (Attends attend : submission.getStudentGroup().getAttendsSet()) {
            recievers.add(attend.getRegistration().getStudent().getPerson());
        }

        final String groupName =
                BundleUtil.getString(Bundle.GLOBAL, "label.group", new String[] { submission.getStudentGroup().getGroupNumber()
                        .toString() });
        Sender sender = ExecutionCourseSender.newInstance(course);
        Recipient recipient = new Recipient(groupName, UserGroup.of(Person.convertToUsers(recievers)));
        new Message(sender, sender.getConcreteReplyTos(), recipient.asCollection(), submission.getProject().getName(),
                submission.getTeacherObservation(), "");
    }
}