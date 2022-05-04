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
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.fenixedu.academic.domain.DomainObjectUtil;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.contacts.EmailAddress;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.groups.Group;

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
        this(group.getPresentationName(), group);
    }

    public Recipient(final Collection<Person> persons) {
        this(Person.convertToUserGroup(persons));
    }

    public Recipient(final String toName, final Group members) {
        this();
        setToName(toName);
        setMembers(members);
    }

    public Group getMembers() {
        return getMembersGroup().toGroup();
    }

    public void setMembers(Group members) {
        setMembersGroup(members.toPersistentGroup());
    }

    @Atomic(mode = TxMode.WRITE)
    public void delete() {
        setMembersGroup(null);
        
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
        getMembers().getMembers().forEach(user -> {
            final EmailAddress emailAddress =
                    user.getPerson() != null ? user.getPerson().getEmailAddressForSendingEmails() : null;
            if (emailAddress != null) {
                final String value = emailAddress.getValue();
                if (value != null && !value.isEmpty()) {
                    emailAddresses.add(value);
                }
            }
        });
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
        setToName(getMembers().getPresentationName());
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

    public static Recipient getRecipientFromGroup(Group group) {
        return group.toPersistentGroup().getRecipientAsMembersSet().stream().findAny()
                .orElseGet(() -> Recipient.newInstance(group));
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

        membersGroup.getMembers().forEach(user -> builder.append(user.getPerson().getName()).append(" (")
                .append(user.getPerson().getEmailForSendingEmails()).append(")").append("\n"));

        return builder.toString();
    }
}
