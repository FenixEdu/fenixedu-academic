package net.sourceforge.fenixedu.domain.util;

import java.util.HashSet;
import java.util.Set;

import pt.ist.bennu.core.domain.Bennu;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.Sender;
import pt.ist.bennu.scheduler.CronTask;
import pt.ist.bennu.scheduler.annotation.Task;
import pt.utl.ist.fenix.tools.util.i18n.Language;

@Task(englishTitle = "Message Task")
public class MessageTask extends CronTask {

    @Override
    public void runTask() {
        Language.setLocale(Language.getDefaultLocale());
        final Bennu rootDomainObject = Bennu.getInstance();
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
