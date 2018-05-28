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

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.thesis.Thesis;
import org.fenixedu.academic.domain.thesis.ThesisEvaluationParticipant;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.I18N;
import org.fenixedu.messaging.core.domain.Message;

public abstract class ThesisServiceWithMailNotification {

    public void run(Thesis thesis) {
        process(thesis);
        sendEmail(thesis);
    }

    abstract void process(Thesis thesis);

    private void sendEmail(Thesis thesis) {
        Message.from(AccessControl.getPerson().getSender())
                .singleBcc(getReceiversEmails(thesis))
                .subject(getSubject(thesis))
                .textBody(getMessage(thesis))
                .send();
    }

    protected String getMessage(String key, Object... args) {
        return getMessage(I18N.getLocale(), key, args);
    }

    protected String getMessage(Locale locale, String key, Object... args) {
        String template = BundleUtil.getString(Bundle.MESSAGING, locale, key);
        return MessageFormat.format(template, args);
    }

    protected abstract String getSubject(Thesis thesis);

    protected abstract String getMessage(Thesis thesis);

    protected abstract Collection<String> getReceiversEmails(Thesis thesis);

    //
    // Utility methods
    //

    protected static Set<Person> personSet(Person... persons) {
        Set<Person> result = new HashSet<>();

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
