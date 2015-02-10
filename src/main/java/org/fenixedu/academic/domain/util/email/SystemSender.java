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
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.groups.NobodyGroup;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

public class SystemSender extends SystemSender_Base {

    public SystemSender() {
        super();
        setMembers(RoleType.MANAGER.actualGroup());
        setFromAddress(Sender.getNoreplyMail());
        setSystemRootDomainObject(getRootDomainObject());
        setFromName(createFromName());
    }

    public String createFromName() {
        return String.format("%s (%s)", Unit.getInstitutionAcronym(), "Sistema Fénix");
    }

    public Recipient getRoleRecipient(RoleType roleType) {
        final Group roleGroup = roleType.actualGroup();
        for (Recipient recipient : getRecipientsSet()) {
            final Group members = recipient.getMembers();
            if (roleGroup.equals(members)) {
                return recipient;
            }
        }
        return createRoleRecipient(roleGroup);
    }

    @Atomic(mode = TxMode.WRITE)
    private Recipient createRoleRecipient(Group roleGroup) {
        final Recipient recipient = new Recipient(roleGroup);
        addRecipients(recipient);
        return recipient;
    }

    public Group getOptOutGroup() {
        PersistentGroup optOutGroup = super.getOptOutPersistentGroup();
        if (optOutGroup == null) {
            return NobodyGroup.get();
        }
        return optOutGroup.toGroup();
    }

    public void setOptOutGroup(Group optOutGroup) {
        setOptOutPersistentGroup(optOutGroup.toPersistentGroup());
    }
}
