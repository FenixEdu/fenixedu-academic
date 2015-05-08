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

import java.util.Set;

import org.fenixedu.academic.domain.accessControl.UnitGroup;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
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
    public Set<ReplyTo> getReplyTosSet() {
        if (super.getReplyTosSet().isEmpty()) {
            addReplyTos(new CurrentUserReplyTo());
        }
        return super.getReplyTosSet();
    }

    @Atomic
    private void createCurrentUserReplyTo() {
        addReplyTos(new CurrentUserReplyTo());
    }

    @Override
    public void setFromName(final String fromName) {
        throw new Error("method.not.available.for.this.type.of.sender");
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

    protected void createRecipient(final Group group) {
        addRecipients(new Recipient(null, group));
    }

    @Atomic
    public static UnitBasedSender newInstance(Unit unit) {
        return new UnitBasedSender(unit, Sender.getNoreplyMail(), UnitGroup.recursiveWorkers(unit));
    }

}
