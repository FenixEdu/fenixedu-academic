package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.Collections;

import net.sourceforge.fenixedu.domain.util.email.EmailBean;
import net.sourceforge.fenixedu.domain.util.email.Sender;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class EmailRecipientsProvider implements DataProvider {

    public Object provide(final Object source, final Object currentValue) {
	final EmailBean emailBean = (EmailBean) source;
	final Sender sender = emailBean.getSender();
	return sender == null ? Collections.EMPTY_LIST : sender.getRecipientsSet();
    }

    public Converter getConverter() {
	return null;
    }

}
