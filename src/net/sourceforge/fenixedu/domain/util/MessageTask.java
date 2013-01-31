package net.sourceforge.fenixedu.domain.util;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.Sender;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class MessageTask extends MessageTask_Base {

	@Override
	@Service
	public void runTask() {
		Language.setLocale(Language.getDefaultLocale());
		final RootDomainObject rootDomainObject = RootDomainObject.getInstance();
		final Set<Sender> senders = new HashSet<Sender>();
		for (final Message message : rootDomainObject.getPendingUtilEmailMessagesSet()) {
			senders.add(message.getSender());
		}
		for (final Sender sender : senders) {
			sender.deleteOldMessages();
		}
		for (final Message message : rootDomainObject.getPendingUtilEmailMessagesSet()) {
			message.dispatch();
		}
	}

}
