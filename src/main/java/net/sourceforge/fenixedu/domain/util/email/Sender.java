package net.sourceforge.fenixedu.domain.util.email;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.DomainObjectUtil;
import net.sourceforge.fenixedu.domain.Instalation;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
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
        setRootDomainObject(null);
        deleteDomainObject();
    }

    public static String getNoreplyMail() {
        return Instalation.getInstance().getInstituitionalEmailAddress("noreply");
    }

    public static boolean hasAvailableSender() {
        final User userView = Authenticate.getUser();
        if (userView != null) {
            if (userView.getPerson().hasRole(RoleType.MANAGER)) {
                return true;
            }

            final Person person = userView.getPerson();
            if (person != null && person.hasAnyMessages()) {
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
        return getMembers().allows(userView);
    }

    public static Set<Sender> getAvailableSenders() {
        final User userView = Authenticate.getUser();

        final Set<Sender> senders = new TreeSet<Sender>(Sender.COMPARATOR_BY_FROM_NAME);
        for (final Sender sender : Bennu.getInstance().getUtilEmailSendersSet()) {
            if (sender.getMembers().allows(userView) || (userView != null && userView.getPerson().hasRole(RoleType.MANAGER))) {
                senders.add(sender);
            }
        }

        return senders;
    }

    @Atomic
    public List<ReplyTo> getConcreteReplyTos() {
        List<ReplyTo> replyTos = new ArrayList<ReplyTo>();
        for (ReplyTo replyTo : getReplyTos()) {
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

    public String getFromName(Person person) {
        return getFromName();
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

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.util.email.Recipient> getRecipients() {
        return getRecipientsSet();
    }

    @Deprecated
    public boolean hasAnyRecipients() {
        return !getRecipientsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.util.email.Message> getMessages() {
        return getMessagesSet();
    }

    @Deprecated
    public boolean hasAnyMessages() {
        return !getMessagesSet().isEmpty();
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
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasFromName() {
        return getFromName() != null;
    }

    @Deprecated
    public boolean hasFromAddress() {
        return getFromAddress() != null;
    }

    @Deprecated
    public boolean hasMembers() {
        return getMembers() != null;
    }

}
