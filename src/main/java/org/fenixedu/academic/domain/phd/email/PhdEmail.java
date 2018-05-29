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
package org.fenixedu.academic.domain.phd.email;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.util.LocaleUtils;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.commons.i18n.I18N;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.messaging.core.domain.Message;
import org.fenixedu.messaging.core.domain.Sender;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public abstract class PhdEmail extends PhdEmail_Base {

    protected PhdEmail() {
        super();
    }

    protected void init(final String subject, final String body, String additionalTo, String additionalBcc) {
        super.init(new LocalizedString(I18N.getLocale(), subject), new LocalizedString(I18N.getLocale(), body));
    }

    protected void init(final String subject, final String body, String additionalTo, String additionalBcc, Person creator,
            DateTime whenCreated) {
        super.init(new LocalizedString(I18N.getLocale(), subject), new LocalizedString(I18N.getLocale(), body));
        setAdditionalTo(additionalTo);
        setAdditionalBcc(additionalBcc);
        setPerson(creator);
        setWhenCreated(whenCreated);
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    protected boolean isToDiscard() {
        return !isToFire();
    }

    @Override
    protected boolean isToFire() {
        return getActive() && getFireDate() == null;
    }

    @Override
    protected void generateMessage() {
        Message.from(getSender())
                .replyTo(getReplyTo())
                .to(getRecipients())
                .singleBcc(getBccs().split(","))
                .subject(getSubject().getContent(LocaleUtils.PT))
                .textBody(getBody().getContent(LocaleUtils.PT))
                .send();
    }

    @Override
    public boolean isToSendMail() {
        return true;
    }

    @Atomic
    public void cancel() {
        discard();
    }

    @Override
    @Atomic
    public void fire() {
        super.fire();
    }

    protected abstract String getReplyTo();

    protected abstract Sender getSender();

    protected abstract Group getRecipients();

    protected abstract String getBccs();

}
