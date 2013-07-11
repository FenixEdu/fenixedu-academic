package net.sourceforge.fenixedu.domain.util.email;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.util.Email;
import net.sourceforge.fenixedu.domain.util.EmailAddressList;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

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
        setRootDomainObject(RootDomainObject.getInstance());
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
        final RootDomainObject rootDomainObject = RootDomainObject.getInstance();
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
        final RootDomainObject rootDomainObject = RootDomainObject.getInstance();
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

        List<Recipient> recipients = getRecipients();
        for (Recipient recipient : recipients) {
            builder.append(recipient.getMembersEmailInText());
        }

        return builder.toString();
    }

    private static class Worker extends Thread {

        private final Set<Recipient> recipients;

        private final Set<String> emailAddresses = new HashSet<String>();

        private Worker(final Set<Recipient> recipients) {
            this.recipients = recipients;
        }

        @Atomic
        @Override
        public void run() {
            for (final Recipient recipient : recipients) {
                recipient.addDestinationEmailAddresses(emailAddresses);
            }
        }

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
        final Worker worker = new Worker(getRecipientsSet());
        worker.start();
        try {
            worker.join();
        } catch (final InterruptedException e) {
            throw new Error(e);
        }
        emailAddresses.addAll(worker.emailAddresses);
        return emailAddresses;
    }

    protected String[] getReplyToAddresses(final Person person) {
        final String[] replyToAddresses = new String[getReplyTosCount()];
        int i = 0;
        for (final ReplyTo replyTo : getReplyTosSet()) {
            replyToAddresses[i++] = replyTo.getReplyToAddress(person);
        }
        return replyToAddresses;
    }

    public void dispatch() {
        final Sender sender = getSender();
        final Person person = getPerson();
        final Set<String> destinationBccs = getDestinationBccs();
        for (final Set<String> bccs : split(destinationBccs)) {
            if (!bccs.isEmpty()) {
                final Email email =
                        new Email(sender.getFromName(person), sender.getFromAddress(), getReplyToAddresses(person),
                                Collections.EMPTY_SET, Collections.EMPTY_SET, bccs, getSubject(), getBody(), getHtmlBody());
                email.setMessage(this);
            }
        }
        final Set<String> tos = getRecipientAddresses(getTosSet());
        final Set<String> ccs = getRecipientAddresses(getCcsSet());
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
    // email.getMessageTransportResult().get(0).getDescription().trim().isEmpty()
    // &&
    // "No recipient addresses".equals(email.getMessageTransportResult().get(0).getDescription()))
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
            count += recipient.getMembers().getElements().size();
        }
        return count;
    }

    public int getRecipientsWithEmailCount() {
        int count = 0;
        for (Recipient recipient : getRecipients()) {
            final Set<Person> elements = recipient.getMembers().getElements();
            for (Person person : elements) {
                if (person.getEmailForSendingEmails() != null) {
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

}
