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
package org.fenixedu.core.service;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.WrittenTest;
import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.Sender;
import net.sourceforge.fenixedu.util.Bundle;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.util.MessageResources;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.spaces.core.service.NotificationService;
import org.fenixedu.spaces.domain.Space;
import org.fenixedu.spaces.domain.occupation.Occupation;
import org.fenixedu.spaces.domain.occupation.requests.OccupationComment;
import org.fenixedu.spaces.domain.occupation.requests.OccupationRequest;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import pt.ist.fenixframework.Atomic;

import com.google.common.base.Strings;

@Service
public class GOPSendMessageService implements NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(GOPSendMessageService.class);

    private static Sender GOP_SENDER = null;

    private static Sender getGOPSender() {
        if (GOP_SENDER == null) {
            GOP_SENDER = initGOPSender();
            if (GOP_SENDER == null) {
                logger.warn("WARN: GOPSender couldn't be found, using SystemSender ...");
                GOP_SENDER = Bennu.getInstance().getSystemSender();
            }
        }
        return GOP_SENDER;
    }

    private static Sender initGOPSender() {
        for (Sender sender : Sender.getAvailableSenders()) {
            final Group members = sender.getMembers();
            if (members.equals(RoleGroup.get(RoleType.RESOURCE_ALLOCATION_MANAGER))) {
                return sender;
            }
        }
        return null;
    }

    @Atomic
    public static void requestRoom(WrittenTest test) {
        final String date = new SimpleDateFormat("dd/MM/yyyy").format(test.getDay().getTime());
        final String time = new SimpleDateFormat("HH:mm").format(test.getBeginning().getTime());
        final String endTime = new SimpleDateFormat("HH:mm").format(test.getEnd().getTime());

        // Foi efectuado um pedido de requisição de sala para {0} da
        // disciplina
        // {1} do(s) curso(s) {4} no dia {2} das {3} às {5}

        final Set<String> courseNames = new HashSet<String>();
        final Set<String> degreeNames = new HashSet<String>();
        final Set<ExecutionDegree> degrees = new HashSet<ExecutionDegree>();
        for (ExecutionCourse course : test.getAssociatedExecutionCourses()) {
            courseNames.add(course.getName());
            degreeNames.add(course.getDegreePresentationString());
            degrees.addAll(course.getExecutionDegrees());
        }

        final String degreesString = StringUtils.join(degreeNames, ",");
        final String coursesString = StringUtils.join(courseNames, ",");
        final String subject =
                BundleUtil.getString(Bundle.APPLICATION, "email.request.room.subject",
                        coursesString, test.getDescription());

        final String body =
                BundleUtil.getString(Bundle.APPLICATION, "email.request.room.body",
                        test.getDescription(), coursesString, date, time, degreesString, endTime);
        for (String email : getGOPEmail(degrees)) {
            new Message(getGOPSender(), email, subject, body);
        }
        test.setRequestRoomSentDate(new DateTime());
    }

    @Atomic
    public static void requestChangeRoom(WrittenTest test, Date oldDay, Date oldBeginning, Date oldEnd) {

        final String oldDate = new SimpleDateFormat("dd/MM/yyyy").format(oldDay);
        final String oldStartTime = new SimpleDateFormat("HH:mm").format(oldBeginning);
        final String oldEndTime = new SimpleDateFormat("HH:mm").format(oldEnd);

        final String date = new SimpleDateFormat("dd/MM/yyyy").format(test.getDay().getTime());
        final String startTime = new SimpleDateFormat("HH:mm").format(test.getBeginning().getTime());
        final String endTime = new SimpleDateFormat("HH:mm").format(test.getEnd().getTime());

        final Set<String> courseNames = new HashSet<String>();
        final Set<String> degreeNames = new HashSet<String>();
        final Set<ExecutionDegree> degrees = new HashSet<ExecutionDegree>();
        for (ExecutionCourse course : test.getAssociatedExecutionCourses()) {
            courseNames.add(course.getName());
            degreeNames.add(course.getDegreePresentationString());
            degrees.addAll(course.getExecutionDegrees());
        }

        String coursesString = StringUtils.join(courseNames, ",");
        String degreesString = StringUtils.join(degreeNames, ",");

        final String subject =
                BundleUtil.getString(Bundle.APPLICATION, "email.request.room.subject.edit",
                        coursesString, test.getDescription());

        // O pedido de requisição de sala para {0} da disciplina {1} do(s)
        // cursos(s) {2} efecuado em {3} para o dia {4} das {5} às {6} foi
        // alterado para o dia {7} das {8} às {9}
        final String body =
                BundleUtil.getString(Bundle.APPLICATION, "email.request.room.body.edit",
                        test.getDescription(), coursesString, degreesString, test.getRequestRoomSentDateString(), oldDate,
                        oldStartTime, oldEndTime, date, startTime, endTime);
        for (String email : getGOPEmail(degrees)) {
            new Message(getGOPSender(), email, subject, body);
        }
        test.setRequestRoomSentDate(new DateTime());
    }

    private static Set<String> getGOPEmail(Collection<ExecutionDegree> degrees) {
        Set<String> emails = new HashSet<String>();
        for (ExecutionDegree executionDegree : degrees) {
            String emailFromApplicationResources =
                    BundleUtil.getString(Bundle.APPLICATION, "email.gop."
                            + executionDegree.getCampus().getName());
            if (!StringUtils.isEmpty(emailFromApplicationResources)) {
                emails.add(emailFromApplicationResources);
            }
        }
        return emails;
    }

    @Override
    public boolean notify(OccupationRequest request) {
        MessageResources messages = MessageResources.getMessageResources("resources/ResourceAllocationManagerResources");
        String body =
                messages.getMessage("message.room.reservation.solved") + "\n\n"
                        + messages.getMessage("message.room.reservation.request.number") + "\n" + request.getIdentification()
                        + "\n\n";
        body += messages.getMessage("message.room.reservation.request") + "\n";
        if (request.getSubject() != null) {
            body += request.getSubject();
        } else {
            body += "-";
        }
        body += "\n\n" + messages.getMessage("label.rooms.reserve.periods") + ":";
        for (Occupation occupation : request.getOccupationSet()) {
            body += "\n\t" + occupation.getSummary() + " - " + occupation.getSpaces().stream().map(Space::getName).collect(Collectors.joining(" "));
        }
        if (request.getOccupationSet().isEmpty()) {
            body += "\n" + messages.getMessage("label.rooms.reserve.periods.none");
        }
        body += "\n\n" + messages.getMessage("message.room.reservation.description") + "\n";
        if (request.getDescription() != null) {
            body += request.getDescription();
        } else {
            body += "-";
        }
        OccupationComment occupationComment =
                request.getCommentSet().stream().sorted(OccupationComment.COMPARATOR_BY_INSTANT.reversed()).findFirst().get();

        body += "\n\n" + messages.getMessage("message.room.reservation.last.comment") + "\n";

        if (occupationComment != null) {
            body += occupationComment.getDescription();
        } else {
            body += "-";
        }
        sendEmail(request.getRequestor().getPerson().getEmailForSendingEmails(), messages.getMessage("message.room.reservation"),
                body);
        return true;
    }

    @Override
    public boolean sendEmail(String emails, String subject, String body) {
        if (!Strings.isNullOrEmpty(emails)) {
            final Sender sender = getGOPSender();
            new Message(sender, sender.getConcreteReplyTos(), null, subject, body, emails);
            return true;
        }
        return false;
    }
}


