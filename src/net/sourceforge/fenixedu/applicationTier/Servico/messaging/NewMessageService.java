package net.sourceforge.fenixedu.applicationTier.Servico.messaging;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.domain.util.email.ReplyTo;
import net.sourceforge.fenixedu.domain.util.email.Sender;
import pt.ist.fenixWebFramework.services.Service;

public class NewMessageService {

	@Service
	public static Message run(final Sender sender, final Collection<ReplyTo> replyTos, final Collection<Recipient> recipients,
			final String subject, final String body, final String bccs) {
		return new Message(sender, replyTos, recipients, subject, body, bccs);
	}

}
