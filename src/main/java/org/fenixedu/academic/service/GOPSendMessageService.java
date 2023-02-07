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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.util.MessageResources;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.domain.util.email.Message;
import org.fenixedu.academic.domain.util.email.Sender;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.spaces.core.service.NotificationService;
import org.fenixedu.spaces.domain.Space;
import org.fenixedu.spaces.domain.occupation.Occupation;
import org.fenixedu.spaces.domain.occupation.requests.OccupationComment;
import org.fenixedu.spaces.domain.occupation.requests.OccupationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

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
            if (members.equals(RoleType.RESOURCE_ALLOCATION_MANAGER.actualGroup())) {
                return sender;
            }
        }
        return null;
    }

    @Override
    public boolean notify(OccupationRequest request) {
        MessageResources messages = MessageResources.getMessageResources("resources/ResourceAllocationManagerResources");
        String body = messages.getMessage("message.room.reservation.solved") + "\n\n"
                + messages.getMessage("message.room.reservation.request.number") + "\n" + request.getIdentification() + "\n\n";
        body += messages.getMessage("message.room.reservation.request") + "\n";
        if (request.getSubject() != null) {
            body += request.getSubject();
        } else {
            body += "-";
        }
        body += "\n\n" + messages.getMessage("label.rooms.reserve.periods") + ":";
        for (Occupation occupation : request.getOccupationSet()) {
            body += "\n\t" + occupation.getSummary() + " - "
                    + occupation.getSpaces().stream().map(Space::getName).collect(Collectors.joining(" "));
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
