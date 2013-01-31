package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.util.email.EmailBean;
import net.sourceforge.fenixedu.domain.util.email.ReplyTo;
import net.sourceforge.fenixedu.domain.util.email.Sender;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class EmailReplyTosProvider implements DataProvider {

	@Override
	public Object provide(final Object source, final Object currentValue) {
		final EmailBean emailBean = (EmailBean) source;
		final Sender sender = emailBean.getSender();
		final Set<ReplyTo> replyTos = new TreeSet<ReplyTo>(ReplyTo.COMPARATOR_BY_ADDRESS);
		if (sender != null) {
			replyTos.addAll(sender.getConcreteReplyTos());
		}
		return replyTos;
	}

	@Override
	public Converter getConverter() {
		return null;
	}

}
