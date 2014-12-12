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
/**
 * 
 */
package org.fenixedu.academic.service.services.messaging;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.messaging.ConversationMessage;
import org.fenixedu.academic.domain.messaging.ConversationThread;
import org.fenixedu.academic.domain.messaging.ForumSubscription;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.domain.util.email.Message;
import org.fenixedu.academic.domain.util.email.Recipient;
import org.fenixedu.academic.domain.util.email.SystemSender;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.HtmlToTextConverterUtil;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.UserGroup;
import org.fenixedu.bennu.core.i18n.BundleUtil;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt"> Goncalo Luiz</a><br/>
 *         Created on May 23, 2006, 3:48:23 PM
 * 
 */
public abstract class ForumService {

    protected static final Locale DEFAULT_LOCALE = new Locale("pt");

    private static String getString(final String key) {
        return BundleUtil.getString(Bundle.GLOBAL, DEFAULT_LOCALE, key);
    }

    protected void sendNotifications(ConversationMessage conversationMessage) {
        this.notifyEmailSubscribers(conversationMessage);
        this.notifyLastReplier(conversationMessage);
    }

    private void notifyEmailSubscribers(ConversationMessage conversationMessage) {
        final Set<User> readers = conversationMessage.getConversationThread().getForum().getReadersGroup().getMembers();
        final Set<Person> teachers = new HashSet<Person>();
        final Set<Person> students = new HashSet<Person>();
        final Set<ForumSubscription> subscriptionsToRemove = new HashSet<ForumSubscription>();

        for (final ForumSubscription subscription : conversationMessage.getConversationThread().getForum()
                .getForumSubscriptionsSet()) {
            Person subscriber = subscription.getPerson();
            if (!readers.contains(subscriber.getUser())) {
                subscriptionsToRemove.add(subscription);
            }

            if (subscription.getReceivePostsByEmail()) {
                if (subscriber.getEmail() == null) {
                    subscription.setReceivePostsByEmail(false);
                } else {
                    if (RoleType.TEACHER.isMember(subscriber.getUser())) {
                        teachers.add(subscriber);
                    } else {
                        students.add(subscriber);
                    }
                }
            }
        }

        for (final ForumSubscription subscriptionToRemove : subscriptionsToRemove) {
            conversationMessage.getConversationThread().getForum().removeForumSubscriptions(subscriptionToRemove);
            subscriptionToRemove.delete();
        }

        sendEmailWithConversationMessage(teachers, students, conversationMessage);

    }

    private void notifyLastReplier(ConversationMessage conversationMessage) {
        ConversationMessage nextToLastConversationMessage =
                conversationMessage.getConversationThread().getNextToLastConversationMessage();

        if (nextToLastConversationMessage != null) {
            Person nextToLastMessageReplier = nextToLastConversationMessage.getCreator();
            if (!conversationMessage.getConversationThread().getForum()
                    .isPersonReceivingMessagesByEmail(nextToLastMessageReplier)) {
                final Set<Person> teachers = new HashSet<Person>();
                final Set<Person> students = new HashSet<Person>();
                if (RoleType.TEACHER.isMember(nextToLastMessageReplier.getUser())) {
                    teachers.add(nextToLastMessageReplier);
                } else {
                    students.add(nextToLastMessageReplier);
                }

                sendEmailWithConversationMessage(teachers, students, conversationMessage);
            }
        }

    }

    private void sendEmailToPersons(Set<Person> persons, String personsName, String subject, String body) {
        if (!persons.isEmpty()) {
            final Recipient recipient = new Recipient(getString("label.teachers"), UserGroup.of(Person.convertToUsers(persons)));
            SystemSender systemSender = Bennu.getInstance().getSystemSender();
            new Message(systemSender, systemSender.getConcreteReplyTos(), recipient.asCollection(), subject, body, "");
        }
    }

    private void sendEmailWithConversationMessage(Set<Person> teachers, Set<Person> students,
            ConversationMessage conversationMessage) {
        final String emailSubject = getEmailFormattedSubject(conversationMessage.getConversationThread());

        sendEmailToPersons(teachers, getString("label.teachers"), emailSubject, getEmailFormattedBody(conversationMessage, true));
        sendEmailToPersons(students, getString("label.students"), emailSubject, getEmailFormattedBody(conversationMessage, false));
    }

    private String getEmailFormattedSubject(ConversationThread conversationThread) {
        String emailSubject = MessageFormat.format(getString("forum.email.subject"), conversationThread.getTitle());

        return emailSubject;
    }

    private String getEmailFormattedBody(ConversationMessage conversationMessage, boolean isForTeacher) {
        String emailBodyAsText = HtmlToTextConverterUtil.convertToText(conversationMessage.getBody().getContent());

        String emailFormattedBody =
                MessageFormat.format(getString("forum.email.body"), conversationMessage.getCreator().getName(),
                        conversationMessage.getConversationThread().getTitle(), conversationMessage.getConversationThread()
                                .getForum().getName(), emailBodyAsText);

        return emailFormattedBody;
    }
}