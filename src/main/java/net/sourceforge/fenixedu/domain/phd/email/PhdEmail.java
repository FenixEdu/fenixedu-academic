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
package net.sourceforge.fenixedu.domain.phd.email;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.domain.util.email.ReplyTo;
import net.sourceforge.fenixedu.domain.util.email.Sender;

import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public abstract class PhdEmail extends PhdEmail_Base {

    protected PhdEmail() {
        super();
    }

    protected void init(final String subject, final String body, String additionalTo, String additionalBcc) {
        super.init(new MultiLanguageString(subject), new MultiLanguageString(body));
    }

    protected void init(final String subject, final String body, String additionalTo, String additionalBcc, Person creator,
            DateTime whenCreated) {
        super.init(new MultiLanguageString(subject), new MultiLanguageString(body));
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
        new Message(getSender(), getReplyTos(), getRecipients(), getSubject().getContent(MultiLanguageString.pt), getBody().getContent(
                MultiLanguageString.pt), getBccs());
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

    protected abstract Collection<? extends ReplyTo> getReplyTos();

    protected abstract Sender getSender();

    protected abstract Collection<Recipient> getRecipients();

    protected abstract String getBccs();

    @Deprecated
    public boolean hasAdditionalBcc() {
        return getAdditionalBcc() != null;
    }

    @Deprecated
    public boolean hasAdditionalTo() {
        return getAdditionalTo() != null;
    }

    @Deprecated
    public boolean hasPerson() {
        return getPerson() != null;
    }

}
