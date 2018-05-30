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
package org.fenixedu.academic.service.services.resourceAllocationManager;

import static org.fenixedu.academic.predicate.AccessControl.check;

import java.util.HashSet;
import java.util.Set;

import org.fenixedu.academic.domain.Installation;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.predicate.RolePredicates;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.NamedGroup;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.messaging.core.domain.Message;
import org.fenixedu.messaging.core.template.DeclareMessageTemplate;
import org.fenixedu.messaging.core.template.TemplateParameter;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

@DeclareMessageTemplate(
        id = "org.fenixedu.academic.service.changeStudentsShift.message.template",
        description = "changeStudentsShift.message.template.description",
        subject = "changeStudentsShift.message.template.subject",
        text = "changeStudentsShift.message.template.text",
        parameters = {
                @TemplateParameter(id = "oldShift", description = "changeStudentsShift.message.template.parameter.oldShift"),
                @TemplateParameter(id = "newShift", description = "changeStudentsShift.message.template.parameter.newShift"),
        },
        bundle = Bundle.RESOURCE_MANAGER
)
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

        oldShift.getStudentsSet().removeAll(registrations);
        if (newShift != null) {
            newShift.getStudentsSet().addAll(registrations);
        }

        final Set<Person> receivers = new HashSet<>();
        for (final Registration registration : registrations) {
            receivers.add(registration.getPerson());
        }

        final LocalizedString groupName = BundleUtil.getLocalizedString(Bundle.RESOURCE_MANAGER,
                "changeStudentsShift.email.groupName", oldShift.getNome());

        Message.fromSystem()
                .replyTo(Installation.getInstance().getInstituitionalEmailAddress("gop"))
                .to(new NamedGroup(groupName, Person.convertToUserGroup(receivers)))
                .template("org.fenixedu.academic.service.changeStudentsShift.message.template")
                    .parameter("oldShift", oldShift)
                    .parameter("newShift", newShift)
                    .and()
                .send();
    }

    public static class UnableToTransferStudentsException extends FenixServiceException {
    }

}