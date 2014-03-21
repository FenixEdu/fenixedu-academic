package net.sourceforge.fenixedu.domain.util.email;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.DomainObjectUtil;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.FixedSetGroup;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.contacts.EmailAddress;

import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

public class Recipient extends Recipient_Base {

    public static final Comparator<Recipient> COMPARATOR_BY_NAME = new Comparator<Recipient>() {

        @Override
        public int compare(Recipient r1, Recipient r2) {
            final int c = r1.getToName().compareTo(r2.getToName());
            return c == 0 ? DomainObjectUtil.COMPARATOR_BY_ID.compare(r1, r2) : c;
        }

    };

    public Recipient() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public Recipient(final Group group) {
        this(group.getName(), group);
    }

    public Recipient(final Collection<Person> persons) {
        this(new FixedSetGroup(persons));
    }

    public Recipient(final String toName, final Group members) {
        this();
        setToName(toName);
        setMembers(members);
    }

    @Atomic(mode = TxMode.WRITE)
    public void delete() {
        for (final Sender sender : getSendersSet()) {
            removeSenders(sender);
        }
        for (Message message : getMessagesSet()) {
            if (message.getRootDomainObjectFromPendingRelation() == null) {
                message.delete();
            }
        }
        for (Message message : getMessagesCcsSet()) {
            if (message.getRootDomainObjectFromPendingRelation() == null) {
                message.delete();
            }
        }
        for (Message message : getMessagesTosSet()) {
            if (message.getRootDomainObjectFromPendingRelation() == null) {
                message.delete();
            }
        }
        setRootDomainObject(null);
        deleteDomainObject();
    }

    public void addDestinationEmailAddresses(final Set<String> emailAddresses) {
        for (final Person person : getMembers().getElements()) {
            final EmailAddress emailAddress = person.getEmailAddressForSendingEmails();
            if (emailAddress != null) {
                final String value = emailAddress.getValue();
                if (value != null && !value.isEmpty()) {
                    emailAddresses.add(value);
                }
            }
        }
    }

    @Override
    public String getToName() {
        if (super.getToName() == null) {
            initToName();
        }
        return super.getToName();
    }

    @Atomic
    private void initToName() {
        setToName(getMembers().getName());
    }

    @Override
    public void setToName(final String toName) {
        super.setToName(toName);
    }

    @Atomic
    public static Recipient newInstance(final String toName, final Group members) {
        return new Recipient(toName, members);
    }

    @Atomic
    public static Recipient newInstance(Group group) {
        return new Recipient(group);
    }

    @Atomic
    public static List<Recipient> newInstance(final List<? extends Group> groups) {
        List<Recipient> recipients = new ArrayList<Recipient>();
        for (Group group : groups) {
            recipients.add(new Recipient(group));
        }
        return recipients;
    }

    public static Set<Recipient> newInstance(final Set<? extends Group> groups) {
        Set<Recipient> recipients = new HashSet<Recipient>();
        for (Group group : groups) {
            recipients.add(new Recipient(group));
        }
        return recipients;
    }

    public Collection<Recipient> asCollection() {
        return Collections.singletonList(this);
    }

    public String getMembersEmailInText() {
        if (getMembers() == null) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        Group membersGroup = getMembers();

        Set<Person> elements = membersGroup.getElements();

        for (Person person : elements) {
            builder.append(person.getName()).append(" (").append(person.getEmailForSendingEmails()).append(")").append("\n");
        }

        return builder.toString();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.util.email.Sender> getSenders() {
        return getSendersSet();
    }

    @Deprecated
    public boolean hasAnySenders() {
        return !getSendersSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.util.email.Message> getMessagesTos() {
        return getMessagesTosSet();
    }

    @Deprecated
    public boolean hasAnyMessagesTos() {
        return !getMessagesTosSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.util.email.Message> getMessagesCcs() {
        return getMessagesCcsSet();
    }

    @Deprecated
    public boolean hasAnyMessagesCcs() {
        return !getMessagesCcsSet().isEmpty();
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
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasMembersSize() {
        return getMembersSize() != null;
    }

    @Deprecated
    public boolean hasMembers() {
        return getMembers() != null;
    }

    @Deprecated
    public boolean hasToName() {
        return getToName() != null;
    }

}
