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
package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.util.email.ConcreteReplyTo;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.PersonSender;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.domain.vigilancy.ExamCoordinator;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantWrapper;
import net.sourceforge.fenixedu.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import org.fenixedu.bennu.core.groups.UserGroup;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class CreateConvokes {

    @Atomic
    public static void run(List<VigilantWrapper> vigilants, WrittenEvaluation writtenEvaluation, VigilantGroup group,
            ExamCoordinator coordinator, String emailMessage) {
        group.convokeVigilants(vigilants, writtenEvaluation);

        Set<Person> recievers = new HashSet<Person>();
        Set<String> bccs = new HashSet<String>();

        if (emailMessage.length() != 0) {
            Person person = coordinator.getPerson();
            for (VigilantWrapper vigilant : vigilants) {
                recievers.add(vigilant.getPerson());
            }

            String groupEmail = group.getContactEmail();
            String replyTo;

            recievers.addAll(writtenEvaluation.getTeachers());

            if (groupEmail != null) {
                bccs.add(groupEmail);
                replyTo = groupEmail;
            } else {
                replyTo = person.getEmail();
            }

            DateTime date = writtenEvaluation.getBeginningDateTime();
            String beginDateString = date.getDayOfMonth() + "/" + date.getMonthOfYear() + "/" + date.getYear();

            String subject =
                    BundleUtil.getString(Bundle.VIGILANCY, "email.convoke.subject", new String[] {
                            group.getEmailSubjectPrefix(), writtenEvaluation.getName(), group.getName(), beginDateString });

            new Message(PersonSender.newInstance(person), new ConcreteReplyTo(replyTo).asCollection(), new Recipient(
                    UserGroup.of(Person.convertToUsers(recievers))).asCollection(), Collections.EMPTY_LIST, Collections.EMPTY_LIST, subject,
                    emailMessage, bccs);
        }
    }
}