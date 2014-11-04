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
package org.fenixedu.academic.service.services.person.vigilancy;

import java.util.Collections;

import org.fenixedu.academic.domain.EvaluationManagementLog;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.WrittenEvaluation;
import org.fenixedu.academic.domain.accessControl.VigilancyGroup;
import org.fenixedu.academic.domain.util.email.ConcreteReplyTo;
import org.fenixedu.academic.domain.util.email.Message;
import org.fenixedu.academic.domain.util.email.PersonSender;
import org.fenixedu.academic.domain.util.email.Recipient;
import org.fenixedu.academic.domain.vigilancy.ExamCoordinator;
import org.fenixedu.academic.domain.vigilancy.Vigilancy;
import org.fenixedu.academic.domain.vigilancy.VigilantGroup;
import org.fenixedu.academic.util.Bundle;

import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class ChangeConvokeActive {

    @Atomic
    public static void run(Vigilancy convoke, Boolean bool, Person person) {

        convoke.setActive(bool);
        sendEmailNotification(bool, person, convoke);
        for (ExecutionCourse ec : convoke.getAssociatedExecutionCourses()) {
            EvaluationManagementLog.createLog(ec, Bundle.MESSAGING, "log.executionCourse.evaluation.generic.edited.vigilancy",
                    convoke.getWrittenEvaluation().getPresentationName(), ec.getName(), ec.getDegreePresentationString());
        }
    }

    private static void sendEmailNotification(Boolean bool, Person person, Vigilancy convoke) {

        String replyTo = person.getEmail();

        VigilantGroup group = convoke.getAssociatedVigilantGroup();
        String groupEmail = group.getContactEmail();

        if (groupEmail != null) {
            final ExamCoordinator coordinator =
                    ExamCoordinator.getExamCoordinatorForGivenExecutionYear(person, group.getExecutionYear());
            if (coordinator == null ? Boolean.FALSE : group.getExamCoordinatorsSet().contains(coordinator)) {
                replyTo = groupEmail;
            }
        }

        WrittenEvaluation writtenEvaluation = convoke.getWrittenEvaluation();

        String emailMessage = generateMessage(bool, convoke);
        DateTime date = writtenEvaluation.getBeginningDateTime();
        String time = writtenEvaluation.getBeginningDateHourMinuteSecond().toString();
        String beginDateString = date.getDayOfMonth() + "-" + date.getMonthOfYear() + "-" + date.getYear();

        String subject =
                BundleUtil.getString("resources.VigilancyResources", "email.convoke.subject",
                        new String[] { writtenEvaluation.getName(), group.getName(), beginDateString, time });

        new Message(PersonSender.newInstance(person), new ConcreteReplyTo(replyTo).asCollection(), new Recipient(
                VigilancyGroup.get(convoke)).asCollection(), Collections.EMPTY_LIST, Collections.EMPTY_LIST, subject,
                emailMessage, convoke.getSitesAndGroupEmails());

    }

    private static String generateMessage(Boolean bool, Vigilancy convoke) {

        WrittenEvaluation writtenEvaluation = convoke.getWrittenEvaluation();
        DateTime beginDate = writtenEvaluation.getBeginningDateTime();
        String date = beginDate.getDayOfMonth() + "-" + beginDate.getMonthOfYear() + "-" + beginDate.getYear();

        return BundleUtil.getString(
                "resources.VigilancyResources",
                "email.convoke.active.body",
                new String[] {
                        convoke.getVigilantWrapper().getPerson().getName(),
                        (bool) ? BundleUtil.getString("resources.VigilancyResources", "email.convoke.convokedAgain") : BundleUtil.getString(
                                "resources.VigilancyResources", "email.convoke.uncovoked"), writtenEvaluation.getFullName(), date,
                        writtenEvaluation.getBeginningDateHourMinuteSecond().toString() });
    }
}