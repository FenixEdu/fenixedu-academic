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

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.util.Email;
import net.sourceforge.fenixedu.domain.util.EmailAddressList;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

public class Message extends Message_Base {

    static final public Comparator<Message> COMPARATOR_BY_CREATED_DATE_OLDER_FIRST = new Comparator<Message>() {
        @Override
        public int compare(Message o1, Message o2) {
            return o1.getCreated().compareTo(o2.getCreated());
        }
    };

    static final public Comparator<Message> COMPARATOR_BY_CREATED_DATE_OLDER_LAST = new Comparator<Message>() {
        @Override
        public int compare(Message o1, Message o2) {
            return o2.getCreated().compareTo(o1.getCreated());
        }
    };

    public static final int NUMBER_OF_SENT_EMAILS_TO_STAY = 500;

    public Message() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public Message(final Sender sender, String to, String subject, String body) {
        this(sender, sender.getReplyTos(), null, subject, body, to);
    }

    public Message(final Sender sender, final Collection<? extends ReplyTo> replyTos, final Collection<Recipient> tos,
            final Collection<Recipient> ccs, final Collection<Recipient> recipientsBccs, final String subject, final String body,
            final Set<String> bccs) {
        this(sender, replyTos, recipientsBccs, subject, body, bccs);
        if (tos != null) {
            for (final Recipient recipient : tos) {
                addTos(recipient);
            }
        }
        if (ccs != null) {
            for (final Recipient recipient : ccs) {
                addCcs(recipient);
            }
        }
    }

    public Message(final Sender sender, final Collection<? extends ReplyTo> replyTos, final Collection<Recipient> tos,
            final Collection<Recipient> ccs, final Collection<Recipient> recipientsBccs, final String subject, final String body,
            final Set<String> bccs, final String htmlBody) {
        this(sender, replyTos, recipientsBccs, subject, body, bccs);
        if (tos != null) {
            for (final Recipient recipient : tos) {
                addTos(recipient);
            }
        }
        if (ccs != null) {
            for (final Recipient recipient : ccs) {
                addCcs(recipient);
            }
        }
    }

    public Message(final Sender sender, final Recipient recipient, final String subject, final String body) {
        this(sender, sender.getConcreteReplyTos(), Collections.singleton(recipient), subject, body, new EmailAddressList(
                Collections.EMPTY_LIST).toString());
    }

    public Message(final Sender sender, final Collection<? extends ReplyTo> replyTos, final Collection<Recipient> recipients,
            final String subject, final String body, final Set<String> bccs) {
        this(sender, replyTos, recipients, subject, body, new EmailAddressList(bccs).toString());
    }

    public Message(final Sender sender, final Collection<? extends ReplyTo> replyTos, final Collection<Recipient> recipients,
            final String subject, final String body, final String bccs) {
        super();
        final Bennu rootDomainObject = Bennu.getInstance();
        setRootDomainObject(rootDomainObject);
        setRootDomainObjectFromPendingRelation(rootDomainObject);
        setSender(sender);
        if (replyTos != null) {
            for (final ReplyTo replyTo : replyTos) {
                addReplyTos(replyTo);
            }
        }
        if (recipients != null) {
            for (final Recipient recipient : recipients) {
                addRecipients(recipient);
            }
        }
        setSubject(subject);
        setBody(body);
        setBccs(bccs);
        final Person person = AccessControl.getPerson();
        setPerson(person);
        setCreated(new DateTime());
    }

    public Message(final Sender sender, final Collection<? extends ReplyTo> replyTos, final Collection<Recipient> recipients,
            final String subject, final String body, final String bccs, final String htmlBody) {
        super();
        final Bennu rootDomainObject = Bennu.getInstance();
        setRootDomainObject(rootDomainObject);
        setRootDomainObjectFromPendingRelation(rootDomainObject);
        setSender(sender);
        if (replyTos != null) {
            for (final ReplyTo replyTo : replyTos) {
                addReplyTos(replyTo);
            }
        }
        if (recipients != null) {
            for (final Recipient recipient : recipients) {
                addRecipients(recipient);
            }
        }
        setSubject(subject);
        setBody(body);
        setHtmlBody(htmlBody);
        setBccs(bccs);
        final Person person = AccessControl.getPerson();
        setPerson(person);
        setCreated(new DateTime());
    }

    public void safeDelete() {
        if (getSent() == null) {
            delete();
        }
    }

    public void delete() {
        for (final Recipient recipient : getRecipientsSet()) {
            removeRecipients(recipient);
        }
        for (final Recipient recipient : getTosSet()) {
            removeTos(recipient);
        }
        for (final Recipient recipient : getCcsSet()) {
            removeCcs(recipient);
        }
        for (final ReplyTo replyTo : getReplyTosSet()) {
            replyTo.safeDelete();
        }
        for (final MessageId messageId : getMessageIdsSet()) {
            messageId.delete();
        }
        for (final Email email : getEmailsSet()) {
            email.delete();
        }

        setSender(null);
        setPerson(null);
        setRootDomainObjectFromPendingRelation(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    public String getRecipientsAsText() {
        final StringBuilder stringBuilder = new StringBuilder();
        recipients2Text(stringBuilder, getRecipientsSet());
        if (getBccs() != null && !getBccs().isEmpty()) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append("\n");
            }
            stringBuilder.append(getBccs());
        }
        return stringBuilder.toString();
    }

    public String getRecipientsAsToText() {
        return recipients2Text(getTosSet());
    }

    public String getRecipientsAsCcText() {
        return recipients2Text(getCcsSet());
    }

    protected static String recipients2Text(final Set<Recipient> recipients) {
        final StringBuilder stringBuilder = new StringBuilder();
        recipients2Text(stringBuilder, recipients);
        return stringBuilder.toString();
    }

    protected static void recipients2Text(final StringBuilder stringBuilder, final Set<Recipient> recipients) {
        for (final Recipient recipient : recipients) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append("\n");
            }
            stringBuilder.append(recipient.getToName());
        }
    }

    public String getRecipientsGroupMembersInText() {
        StringBuilder builder = new StringBuilder();

        Collection<Recipient> recipients = getRecipients();
        for (Recipient recipient : recipients) {
            builder.append(recipient.getMembersEmailInText());
        }

        return builder.toString();
    }

    protected static Set<String> getRecipientAddresses(Set<Recipient> recipients) {
        final Set<String> emailAddresses = new HashSet<String>();
        for (final Recipient recipient : recipients) {
            recipient.addDestinationEmailAddresses(emailAddresses);
        }
        return emailAddresses;
    }

    protected Set<String> getDestinationBccs() {
        final Set<String> emailAddresses = new HashSet<String>();
        if (getBccs() != null && !getBccs().isEmpty()) {
            for (final String emailAddress : getBccs().replace(',', ' ').replace(';', ' ').split(" ")) {
                final String trimmed = emailAddress.trim();
                if (!trimmed.isEmpty()) {
                    emailAddresses.add(emailAddress);
                }
            }
        }
        for (final Recipient recipient : getRecipientsSet()) {
            recipient.addDestinationEmailAddresses(emailAddresses);
        }
        return emailAddresses;
    }

    protected String[] getReplyToAddresses(final Person person) {
        final String[] replyToAddresses = new String[getReplyTosSet().size()];
        int i = 0;
        for (final ReplyTo replyTo : getReplyTosSet()) {
            replyToAddresses[i++] = replyTo.getReplyToAddress(person);
        }
        return replyToAddresses;
    }

    public int dispatch() {
        final Sender sender = getSender();
        final Person person = getPerson();
        final Set<String> destinationBccs = getDestinationBccs();
        final Set<String> tos = getRecipientAddresses(getTosSet());
        final Set<String> ccs = getRecipientAddresses(getCcsSet());
        createEmailBatch(sender, person, tos, ccs, split(destinationBccs));
        return destinationBccs.size() + tos.size() + ccs.size();
    }

    @Atomic(mode = TxMode.WRITE)
    private void createEmailBatch(final Sender sender, final Person person, final Set<String> tos, final Set<String> ccs,
            Set<Set<String>> destinationBccs) {
        for (final Set<String> bccs : destinationBccs) {
            if (!bccs.isEmpty()) {
                final Email email =
                        new Email(sender.getFromName(person), sender.getFromAddress(), getReplyToAddresses(person),
                                Collections.EMPTY_SET, Collections.EMPTY_SET, bccs, getSubject(), getBody(), getHtmlBody());
                email.setMessage(this);
            }
        }
        if (!tos.isEmpty() || !ccs.isEmpty()) {
            final Email email =
                    new Email(sender.getFromName(person), sender.getFromAddress(), getReplyToAddresses(person), tos, ccs,
                            Collections.EMPTY_SET, getSubject(), getBody(), getHtmlBody());
            email.setMessage(this);
        }
        setRootDomainObjectFromPendingRelation(null);
        setSent(new DateTime());
    }

    private Set<Set<String>> split(final Set<String> destinations) {
        final Set<Set<String>> result = new HashSet<Set<String>>();
        int i = 0;
        Set<String> subSet = new HashSet<String>();
        for (final String destination : destinations) {
            if (i++ == 50) {
                result.add(subSet);
                subSet = new HashSet<String>();
                i = 1;
            }
            subSet.add(destination);
        }
        result.add(subSet);
        return result;
    }

    // public int getPossibleErrorsCount() {
    // int count = 0;
    // for (Email email : getEmails()) {
    // if (email.getFailedAddresses() == null ||
    // email.getFailedAddresses().isEmpty()) {
    // if (email.getMessageTransportResult().size() == 1
    // &&
    // email.getMessageTransportResult().iterator().next().getDescription().trim().isEmpty()
    // &&
    // "No recipient addresses".equals(email.getMessageTransportResult().iterator().next().getDescription()))
    // {
    // continue;
    // } else {
    // count++;
    // for (MessageTransportResult result : email.getMessageTransportResult()) {
    // System.out.println(result.getDescription());
    // }
    // System.out.println("----");
    // }
    // }
    // }
    // return count;
    // }

    public int getPossibleRecipientsCount() {
        int count = 0;
        for (Recipient recipient : getRecipients()) {
            count += recipient.getMembers().getMembers().size();
        }
        return count;
    }

    public int getRecipientsWithEmailCount() {
        int count = 0;
        for (Recipient recipient : getRecipients()) {
            final Set<User> elements = recipient.getMembers().getMembers();
            for (User user : elements) {
                if (user.getPerson().getEmailForSendingEmails() != null) {
                    count++;
                }
            }
        }
        return count;
    }

    public int getSentMailsCount() {
        int count = 0;
        for (Email email : getEmails()) {
            final EmailAddressList confirmedAddresses = email.getConfirmedAddresses();
            if (confirmedAddresses != null && !confirmedAddresses.isEmpty()) {
                count += confirmedAddresses.toCollection().size();
            }
        }
        return count;
    }

    public int getFailedMailsCount() {
        int count = 0;
        for (Email email : getEmails()) {
            EmailAddressList failedAddresses = email.getFailedAddresses();

            if (failedAddresses != null && !failedAddresses.isEmpty()) {
                count += failedAddresses.size();
            }
        }
        return count;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.util.email.Recipient> getTos() {
        return getTosSet();
    }

    @Deprecated
    public boolean hasAnyTos() {
        return !getTosSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.util.Email> getEmails() {
        return getEmailsSet();
    }

    @Deprecated
    public boolean hasAnyEmails() {
        return !getEmailsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.util.email.Recipient> getCcs() {
        return getCcsSet();
    }

    @Deprecated
    public boolean hasAnyCcs() {
        return !getCcsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.util.email.MessageId> getMessageIds() {
        return getMessageIdsSet();
    }

    @Deprecated
    public boolean hasAnyMessageIds() {
        return !getMessageIdsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.util.email.ReplyTo> getReplyTos() {
        return getReplyTosSet();
    }

    @Deprecated
    public boolean hasAnyReplyTos() {
        return !getReplyTosSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.util.email.Recipient> getRecipients() {
        return getRecipientsSet();
    }

    @Deprecated
    public boolean hasAnyRecipients() {
        return !getRecipientsSet().isEmpty();
    }

    @Deprecated
    public boolean hasSent() {
        return getSent() != null;
    }

    @Deprecated
    public boolean hasSender() {
        return getSender() != null;
    }

    @Deprecated
    public boolean hasBody() {
        return getBody() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasCreated() {
        return getCreated() != null;
    }

    @Deprecated
    public boolean hasBennuFromPendingRelation() {
        return getRootDomainObjectFromPendingRelation() != null;
    }

    @Deprecated
    public boolean hasSubject() {
        return getSubject() != null;
    }

    @Deprecated
    public boolean hasHtmlBody() {
        return getHtmlBody() != null;
    }

    @Deprecated
    public boolean hasBccs() {
        return getBccs() != null;
    }

    @Deprecated
    public boolean hasPerson() {
        return getPerson() != null;
    }

}
