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
package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Installation;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.util.email.ConcreteReplyTo;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.domain.util.email.Sender;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.UserGroup;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class ChangeStudentsShift {

    @Atomic
    public static void run(User userView, String oldShiftId, String newShiftId, final Set<Registration> registrations)
            throws FenixServiceException {
        check(RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE);

        if (newShiftId != null && newShiftId.isEmpty()) {
            return;
        }

        final Shift oldShift = FenixFramework.getDomainObject(oldShiftId);
        final Shift newShift = FenixFramework.getDomainObject(newShiftId);

        if (newShift != null) {
            if (oldShift == null || !oldShift.getTypes().containsAll(newShift.getTypes())
                    || !newShift.getTypes().containsAll(oldShift.getTypes())
                    || !oldShift.getExecutionCourse().equals(newShift.getExecutionCourse())) {
                throw new UnableToTransferStudentsException();
            }
        }

        final Set<Person> recievers = new HashSet<Person>();

        oldShift.getStudentsSet().removeAll(registrations);
        if (newShift != null) {
            newShift.getStudentsSet().addAll(registrations);
        }
        for (final Registration registration : registrations) {
            recievers.add(registration.getPerson());
        }

        final String subject = getString("changeStudentsShift.email.subject");

        final String groupName = getString("changeStudentsShift.email.groupName", oldShift.getNome());

        final String messagePrefix = getString("changeStudentsShift.email.body", oldShift.getNome());

        final String messagePosfix =
                newShift == null ? getString("changeStudentsShift.email.body.notNewShift") : getString(
                        "changeStudentsShift.email.body.newShift", oldShift.getNome());

        final String message = messagePrefix + messagePosfix;

        Recipient recipient = new Recipient(groupName, UserGroup.of(Person.convertToUsers(recievers)));
        Sender sender = Bennu.getInstance().getSystemSender();
        String gopEmailAddress = Installation.getInstance().getInstituitionalEmailAddress("gop");
        new Message(sender, new ConcreteReplyTo(gopEmailAddress).asCollection(), recipient.asCollection(), subject, message, "");
    }

    private static String getString(final String key, final String... args) {
        return BundleUtil.getString(Bundle.RESOURCE_MANAGER, key, args);
    }

    public static class UnableToTransferStudentsException extends FenixServiceException {
    }

}