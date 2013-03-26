package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.util.email.EmailBean;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.domain.util.email.Sender;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class EmailRecipientsProvider implements DataProvider {

    @Override
    public Object provide(final Object source, final Object currentValue) {
        final EmailBean emailBean = (EmailBean) source;
        final Sender sender = emailBean.getSender();
        final Set<Recipient> recipients = new TreeSet<Recipient>(Recipient.COMPARATOR_BY_NAME);
        recipients.addAll(emailBean.getRecipients());
        if (sender != null) {
            recipients.addAll(sender.getRecipientsSet());
        }
        return recipients;
    }

    @Override
    public Converter getConverter() {
        return null;
    }

}
