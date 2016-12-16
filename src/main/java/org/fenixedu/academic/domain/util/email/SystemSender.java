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

import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.fenixedu.bennu.core.groups.Group;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

public class SystemSender extends SystemSender_Base {

    public SystemSender() {
        super();
        setMembers(Group.managers());
        setFromAddress(Sender.getNoreplyMail());
        setSystemRootDomainObject(getRootDomainObject());
        setFromName(createFromName());
    }

    public String createFromName() {
        return String.format("%s (%s)", Unit.getInstitutionAcronym(), "Sistema Fénix");
    }

    public Recipient getGroupRecipient(Group group) {
        for (Recipient recipient : getRecipientsSet()) {
            final Group members = recipient.getMembers();
            if (group.equals(members)) {
                return recipient;
            }
        }
        return createGroupRecipient(group);
    }

    @Atomic(mode = TxMode.WRITE)
    private Recipient createGroupRecipient(Group group) {
        final Recipient recipient = new Recipient(group);
        addRecipients(recipient);
        return recipient;
    }

    public Group getOptOutGroup() {
        PersistentGroup optOutGroup = super.getOptOutPersistentGroup();
        if (optOutGroup == null) {
            return Group.nobody();
        }
        return optOutGroup.toGroup();
    }

    public void setOptOutGroup(Group optOutGroup) {
        setOptOutPersistentGroup(optOutGroup.toPersistentGroup());
    }
}
