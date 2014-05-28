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

import java.util.Set;

import net.sourceforge.fenixedu.domain.accessControl.MembersLinkGroup;
import net.sourceforge.fenixedu.domain.accessControl.PersistentGroupMembers;
import net.sourceforge.fenixedu.domain.accessControl.UnitGroup;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.fenixedu.bennu.core.groups.Group;

import pt.ist.fenixframework.Atomic;

public class UnitBasedSender extends UnitBasedSender_Base {

    public void init(final Unit unit, final String fromAddress, final Group members) {
        setUnit(unit);
        setFromAddress(fromAddress);
        setMembers(members);
    }

    public UnitBasedSender(final Unit unit, final String fromAddress, final Group members) {
        super();
        init(unit, fromAddress, members);
    }

    public UnitBasedSender() {
        super();
    }

    @Override
    public void delete() {
        setUnit(null);
        super.delete();
    }

    @Override
    public String getFromName() {
        return String.format("%s (%s)", Unit.getInstitutionAcronym(), getUnit().getName());
    }

    @Override
    public Set<ReplyTo> getReplyTos() {
        if (!hasAnyReplyTos()) {
            addReplyTos(new CurrentUserReplyTo());
        }
        return super.getReplyTos();
    }

    @Atomic
    private void createCurrentUserReplyTo() {
        addReplyTos(new CurrentUserReplyTo());
    }

    @Override
    public void setFromName(final String fromName) {
        throw new Error("method.not.available.for.this.type.of.sender");
    }

    protected boolean hasRecipientWithToName(final String toName) {
        for (final Recipient recipient : super.getRecipientsSet()) {
            if (recipient.getToName().equals(toName)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasPersistentGroup(final String toName) {
        final Unit unit = getUnit();
        for (final PersistentGroupMembers persistentGroupMembers : unit.getPersistentGroupsSet()) {
            if (persistentGroupMembers.getName().equals(toName)) {
                return true;
            }
        }
        return false;
    }

    @Atomic
    private void updateRecipients() {
        final Unit unit = getUnit();
        if (unit != null) {

            for (final Group group : unit.getGroups()) {
                if (!hasRecipientWithToName(group.getPresentationName())) {
                    createRecipient(group);
                }
            }

            for (final PersistentGroupMembers persistentGroupMembers : unit.getPersistentGroupsSet()) {
                if (!hasRecipientWithToName(persistentGroupMembers.getName())) {
                    createRecipient(persistentGroupMembers);
                }
            }
            for (final Recipient recipient : super.getRecipientsSet()) {
                if (recipient.getMembers() instanceof MembersLinkGroup) {
                    if (!hasPersistentGroup(recipient.getToName())) {
                        if (recipient.getMessagesSet().isEmpty()) {
                            recipient.delete();
                        } else {
                            removeRecipients(recipient);
                        }
                    }
                }
            }
        }
    }

    public Set<Recipient> getRecipientsWithoutUpdate() {
        return super.getRecipientsSet();
    }

    @Override
    public Set<Recipient> getRecipientsSet() {
        updateRecipients();
        return super.getRecipientsSet();
    }

    @Atomic
    @Override
    public void addRecipients(final Recipient recipients) {
        super.addRecipients(recipients);
    }

    @Atomic
    @Override
    public void removeRecipients(final Recipient recipients) {
        super.removeRecipients(recipients);
    }

    @Atomic
    protected void createRecipient(final PersistentGroupMembers persistentGroupMembers) {
        addRecipients(new Recipient(null, MembersLinkGroup.get(persistentGroupMembers)));
    }

    protected void createRecipient(final Group group) {
        addRecipients(new Recipient(null, group));
    }

    @Atomic
    public static UnitBasedSender newInstance(Unit unit) {
        return new UnitBasedSender(unit, Sender.getNoreplyMail(), UnitGroup.recursiveWorkers(unit));
    }

    @Deprecated
    public boolean hasUnit() {
        return getUnit() != null;
    }

}
