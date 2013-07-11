package net.sourceforge.fenixedu.domain.phd.email;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.domain.util.email.ReplyTo;
import net.sourceforge.fenixedu.domain.util.email.Sender;

import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.i18n.Language;
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
        new Message(getSender(), getReplyTos(), getRecipients(), getSubject().getContent(Language.pt), getBody().getContent(
                Language.pt), getBccs());
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

}
