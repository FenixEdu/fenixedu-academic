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
package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.thesis;

import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisEvaluationParticipant;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.PersonSender;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.domain.util.email.Sender;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.groups.UserGroup;
import org.fenixedu.bennu.core.i18n.BundleUtil;

public abstract class ThesisServiceWithMailNotification {

    public void run(Thesis thesis) {
        process(thesis);
        sendEmail(thesis);
    }

    abstract void process(Thesis thesis);

    private void sendEmail(Thesis thesis) {
        Sender sender = PersonSender.newInstance(AccessControl.getPerson());
        new Message(sender, null, getRecipients(thesis), getSubject(thesis), getMessage(thesis), "");
    }

    protected String getMessage(String key, Object... args) {
        return getMessage(key, new Locale("pt"), args);
    }

    protected String getMessage(String key, Locale locale, String... args) {
        return BundleUtil.getString(Bundle.MESSAGING, locale, key, args);
    }

    private Set<Recipient> getRecipients(Thesis thesis) {
        Set<Recipient> recipients = new HashSet<Recipient>();
        for (Person person : getReceivers(thesis)) {
            recipients.add(Recipient.newInstance(UserGroup.of(person.getUser())));
        }
        return recipients;
    }

    protected abstract String getSubject(Thesis thesis);

    protected abstract String getMessage(Thesis thesis);

    protected abstract Collection<Person> getReceivers(Thesis thesis);

    //
    // Utility methods
    //

    protected static Set<Person> personSet(Person... persons) {
        Set<Person> result = new HashSet<Person>();

        for (Person person : persons) {
            if (person != null) {
                result.add(person);
            }
        }

        return result;
    }

    protected static Person getPerson(ThesisEvaluationParticipant participant) {
        if (participant == null) {
            return null;
        } else {
            return participant.getPerson();
        }
    }

}
