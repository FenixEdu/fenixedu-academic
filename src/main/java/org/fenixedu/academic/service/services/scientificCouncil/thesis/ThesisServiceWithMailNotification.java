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
package org.fenixedu.academic.service.services.scientificCouncil.thesis;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.thesis.Thesis;
import org.fenixedu.academic.domain.thesis.ThesisEvaluationParticipant;
import org.fenixedu.academic.domain.util.email.Message;
import org.fenixedu.academic.domain.util.email.PersonSender;
import org.fenixedu.academic.domain.util.email.Sender;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.I18N;

public abstract class ThesisServiceWithMailNotification {

    public void run(Thesis thesis) {
        process(thesis);
        sendEmail(thesis);
    }

    abstract void process(Thesis thesis);

    private void sendEmail(Thesis thesis) {
        Sender sender = PersonSender.newInstance(AccessControl.getPerson());
        new Message(sender, null, null, getSubject(thesis), getMessage(thesis), getEmails(thesis));
    }

    protected String getMessage(String key, Object... args) {
        return getMessage(I18N.getLocale(), key, args);
    }

    protected String getMessage(Locale locale, String key, Object... args) {
        String template = BundleUtil.getString(Bundle.MESSAGING, locale, key);
        return MessageFormat.format(template, args);
    }

    private String getEmails(Thesis thesis) {
        return getReceiversEmails(thesis).stream().collect(Collectors.joining(", "));
    }

    protected abstract String getSubject(Thesis thesis);

    protected abstract String getMessage(Thesis thesis);

    protected abstract Collection<String> getReceiversEmails(Thesis thesis);

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
