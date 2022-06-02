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
package org.fenixedu.academic.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;

import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.WrittenTest;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.messaging.core.domain.Message;
import org.fenixedu.messaging.core.domain.MessagingSystem;
import org.fenixedu.messaging.core.domain.Sender;
import org.fenixedu.messaging.core.template.DeclareMessageTemplate;
import org.fenixedu.messaging.core.template.TemplateParameter;
import org.fenixedu.spaces.core.service.NotificationService;
import org.fenixedu.spaces.domain.occupation.requests.OccupationComment;
import org.fenixedu.spaces.domain.occupation.requests.OccupationRequest;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixframework.Atomic;

@DeclareMessageTemplate(id = "requestRoom.message.template",
        description = "requestRoom.message.description",
        subject = "requestRoom.message.subject",
        text = "requestRoom.message.body",
        parameters = {
                @TemplateParameter(id = "test", description = "requestRoom.message.parameter.test"),
                @TemplateParameter(id = "courseNames", description = "requestRoom.message.parameter.courseNames"),
                @TemplateParameter(id = "degreeNames", description = "requestRoom.message.parameter.degreeNames"),
        },
        bundle = Bundle.RESOURCE_ALLOCATION
)
@DeclareMessageTemplate(id = "requestChangeRoom.message.template",
        description = "requestChangeRoom.message.description",
        subject = "requestChangeRoom.message.subject",
        text = "requestChangeRoom.message.body",
        parameters = {
                @TemplateParameter(id = "test", description = "requestChangeRoom.message.parameter.test"),
                @TemplateParameter(id = "courseNames", description = "requestChangeRoom.message.parameter.courseNames"),
                @TemplateParameter(id = "degreeNames", description = "requestChangeRoom.message.parameter.degreeNames"),
                @TemplateParameter(id = "oldTestDates", description = "requestChangeRoom.message.parameter.oldTestDates")
        },
        bundle = Bundle.RESOURCE_ALLOCATION
)
@DeclareMessageTemplate(id = "reserve.room.notify.message.template",
        description = "reserve.room.notify.message.description",
        subject = "reserve.room.notify.message.subject",
        text = "reserve.room.notify.message.body",
        parameters = {
            @TemplateParameter(id = "request", description = "reserve.room.notify.message.parameter.request"),
            @TemplateParameter(id = "lastComment", description = "reserve.room.notify.message.parameter.lastComment"),
        },
        bundle = Bundle.RESOURCE_ALLOCATION
)
public class GOPSendMessageService implements NotificationService {


    private static final Logger logger = LoggerFactory.getLogger(GOPSendMessageService.class);

    private static Sender GOP_SENDER = null;

    private static Sender getGOPSender() {
        if (GOP_SENDER == null) {
            GOP_SENDER = initGOPSender();
            if (GOP_SENDER == null) {
                logger.warn("WARN: GOPSender couldn't be found, using SystemSender ...");
                GOP_SENDER = MessagingSystem.systemSender();
            }
        }
        return GOP_SENDER;
    }

    private static Sender initGOPSender() {
        return Sender.available().stream()
                .filter(sender -> sender.getMembers().equals(RoleType.RESOURCE_ALLOCATION_MANAGER.actualGroup()))
                .findFirst().orElse(null);
    }

    private static Set<String> getGOPEmail(Collection<ExecutionDegree> degrees) {
        return degrees.stream()
                .map(degree -> BundleUtil.getString(Bundle.APPLICATION, "email.gop." + degree.getCampus().getName()))
                .filter(StringUtils::isNotEmpty)
                .collect(Collectors.toSet());
    }

    @Atomic
    public static void requestRoom(WrittenTest test) {
        final Set<String> courseNames = new HashSet<>();
        final Set<String> degreeNames = new HashSet<>();
        final Set<ExecutionDegree> degrees = new HashSet<>();
        for (ExecutionCourse course : test.getAssociatedExecutionCoursesSet()) {
            courseNames.add(course.getNameI18N().getContent());
            degreeNames.add(course.getDegreePresentationString());
            degrees.addAll(course.getExecutionDegrees());
        }

        Message.from(getGOPSender()).replyToSender()
                .singleBcc(getGOPEmail(degrees))
                .template("requestRoom.message.template")
                    .parameter("test",test)
                    .parameter("courseNames", courseNames)
                    .parameter("degreeNames", degreeNames)
                    .and()
                .wrapped().send();

        test.setRequestRoomSentDate(new DateTime());
    }

    @Atomic
    public static void requestChangeRoom(WrittenTest test, Date oldDay, Date oldBeginning, Date oldEnd) {
        final Set<String> courseNames = new HashSet<>();
        final Set<String> degreeNames = new HashSet<>();
        final Set<ExecutionDegree> degrees = new HashSet<>();
        for (ExecutionCourse course : test.getAssociatedExecutionCoursesSet()) {
            courseNames.add(course.getNameI18N().getContent());
            degreeNames.add(course.getDegreePresentationString());
            degrees.addAll(course.getExecutionDegrees());
        }

        Message.from(getGOPSender()).replyToSender()
                .singleBcc(getGOPEmail(degrees))
                .template("requestChangeRoom.message.template")
                    .parameter("test",test)
                    .parameter("courseNames", courseNames)
                    .parameter("degreeNames", degreeNames)
                    .parameter("oldTestDates", Arrays.asList(oldDay,oldBeginning,oldEnd))
                    .and()
                .wrapped().send();

        test.setRequestRoomSentDate(new DateTime());
    }

    @Override
    public boolean notify(OccupationRequest request) {
        final OccupationComment lastOccupationComment =
                request.getCommentSet().stream().sorted(OccupationComment.COMPARATOR_BY_INSTANT.reversed()).findFirst().get();
        final String requestorEmail = request.getRequestor().getPerson().getEmailForSendingEmails();

        Message.from(getGOPSender()).replyToSender()
                .singleBcc(requestorEmail)
                .template("reserve.room.notify.message.template")
                    .parameter("request",request)
                    .parameter("lastComment", lastOccupationComment)
                    .and()
                .wrapped().send();
        return true;
    }

    @Override
    public boolean sendEmail(String emails, String subject, String body) {
        if (StringUtils.isNotEmpty(emails)) {
            Message.from(getGOPSender())
                    .replyToSender()
                    .singleBcc(emails.split(","))
                    .subject(subject)
                    .textBody(body)
                    .send();
            return true;
        }
        return false;
    }
}
