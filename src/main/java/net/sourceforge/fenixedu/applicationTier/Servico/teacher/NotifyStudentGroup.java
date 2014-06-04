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
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.ProjectSubmission;
import net.sourceforge.fenixedu.domain.util.email.ExecutionCourseSender;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.domain.util.email.Sender;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import net.sourceforge.fenixedu.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import org.fenixedu.bennu.core.groups.UserGroup;

import pt.ist.fenixframework.Atomic;

public class NotifyStudentGroup {

    @Atomic
    public static void run(ProjectSubmission submission, ExecutionCourse course, Person person) {
        check(RolePredicates.TEACHER_PREDICATE);

        Set<Person> recievers = new HashSet<Person>();

        for (Attends attend : submission.getStudentGroup().getAttends()) {
            recievers.add(attend.getRegistration().getStudent().getPerson());
        }

        final String groupName =
                BundleUtil.getString(Bundle.GLOBAL, "label.group", new String[] { submission
                        .getStudentGroup().getGroupNumber().toString() });
        Sender sender = ExecutionCourseSender.newInstance(course);
        Recipient recipient = new Recipient(groupName, UserGroup.of(Person.convertToUsers(recievers)));
        new Message(sender, sender.getConcreteReplyTos(), recipient.asCollection(), submission.getProject().getName(),
                submission.getTeacherObservation(), "");
    }
}