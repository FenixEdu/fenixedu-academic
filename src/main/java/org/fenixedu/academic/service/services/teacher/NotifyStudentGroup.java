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

import java.util.Set;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.ProjectSubmission;
import org.fenixedu.academic.predicate.RolePredicates;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.groups.NamedGroup;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.messaging.core.domain.Message;
import pt.ist.fenixframework.Atomic;

public class NotifyStudentGroup {

    @Atomic
    public static void run(ProjectSubmission submission, ExecutionCourse course, Person person) {
        check(RolePredicates.TEACHER_PREDICATE);

        Set<Person> receivers = submission.getStudentGroup().getAttendsSet().stream()
                .map(attend -> attend.getRegistration().getStudent().getPerson()).collect(Collectors.toSet());

        final LocalizedString groupName =
                BundleUtil.getLocalizedString(Bundle.GLOBAL, "label.group", submission.getStudentGroup().getGroupNumber().toString());

        Message.from(course.getSender())
                .replyToSender()
                .to(new NamedGroup(groupName, Person.convertToUserGroup(receivers)))
                .subject(submission.getProject().getName())
                .textBody(submission.getTeacherObservation())
                .send();
    }
}