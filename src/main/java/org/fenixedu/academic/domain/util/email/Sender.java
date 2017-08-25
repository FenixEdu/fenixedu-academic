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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.fenixedu.academic.domain.DomainObjectUtil;
import org.fenixedu.academic.domain.Installation;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixframework.Atomic;

public class Sender extends Sender_Base {

    public static Comparator<Sender> COMPARATOR_BY_FROM_NAME = new Comparator<Sender>() {

        @Override
        public int compare(final Sender sender1, final Sender sender2) {
            final int c = sender1.getFromName().compareTo(sender2.getFromName());
            return c == 0 ? DomainObjectUtil.COMPARATOR_BY_ID.compare(sender1, sender2) : c;
        }

    };

    public Sender() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public Sender(final String fromName, final String fromAddress, final Group members) {
        this();
        setFromName(fromName);
        setFromAddress(fromAddress);
        setMembers(members);
    }

    public Group getMembers() {
        return getMembersGroup().toGroup();
    }

    public void setMembers(Group members) {
        setMembersGroup(members.toPersistentGroup());
    }

    public void delete() {
        for (final Message message : getMessagesSet()) {
            message.delete();
        }
        for (final Recipient recipient : getRecipientsSet()) {
            if (recipient.getSendersSet().size() == 1) {
                recipient.delete();
            } else {
                removeRecipients(recipient);
            }
        }
        for (ReplyTo replyTo : getReplyTosSet()) {
            removeReplyTos(replyTo);
            replyTo.safeDelete();
        }
        setMembersGroup(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    public static String getNoreplyMail() {
        return Installation.getInstance().getInstituitionalEmailAddress("noreply");
    }

    public static boolean hasAvailableSender() {
        final User userView = Authenticate.getUser();
        if (userView != null) {
            if (Group.managers().isMember(userView)) {
                return true;
            }

            final Person person = userView.getPerson();
            if (person != null && !person.getMessagesSet().isEmpty()) {
                return true;
            }

            for (final Sender sender : Bennu.getInstance().getUtilEmailSendersSet()) {
                if (sender.allows(userView)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean allows(final User userView) {
        return getMembers().isMember(userView);
    }

    public static Set<Sender> getAvailableSenders() {
        final User userView = Authenticate.getUser();

        final Set<Sender> senders = new TreeSet<Sender>(Sender.COMPARATOR_BY_FROM_NAME);
        for (final Sender sender : Bennu.getInstance().getUtilEmailSendersSet()) {
            if (sender.getMembers().isMember(userView) || (userView != null && Group.managers().isMember(userView))) {
                senders.add(sender);
            }
        }

        return senders;
    }

    @Atomic
    public List<ReplyTo> getConcreteReplyTos() {
        List<ReplyTo> replyTos = new ArrayList<ReplyTo>();
        for (ReplyTo replyTo : getReplyTosSet()) {
            if (replyTo instanceof CurrentUserReplyTo) {
                if (AccessControl.getPerson().getReplyTo() == null) {
                    ReplyTo concreteReplyTo = new PersonReplyTo(AccessControl.getPerson());
                    replyTos.add(concreteReplyTo);
                } else {
                    replyTos.add(AccessControl.getPerson().getReplyTo());
                }
            } else {
                replyTos.add(replyTo);
            }
        }
        return replyTos;
    }

    public int deleteOldMessages() {
        int deletedCounter = 0;
        final SortedSet<Message> messages = new TreeSet<Message>(Message.COMPARATOR_BY_CREATED_DATE_OLDER_LAST);
        messages.addAll(getMessagesSet());
        int sentCounter = 0;
        for (final Message message : messages) {
            if (message.getSent() != null) {
                ++sentCounter;
                if (sentCounter > Message.NUMBER_OF_SENT_EMAILS_TO_STAY) {
                    deletedCounter++;
                    message.delete();
                }
            }
        }
        return deletedCounter;
    }

}
