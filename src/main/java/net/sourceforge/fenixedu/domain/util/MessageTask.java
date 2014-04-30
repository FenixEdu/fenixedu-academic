package net.sourceforge.fenixedu.domain.util;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.Sender;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.scheduler.CronTask;
import org.fenixedu.bennu.scheduler.annotation.Task;
import org.fenixedu.commons.i18n.I18N;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import java.util.Locale;

@Task(englishTitle = "Message Task", readOnly = true)
public class MessageTask extends CronTask {
    @Override
    public void runTask() {
        I18N.setLocale(Locale.getDefault());
        deleteOldSenders();
        dispatchMessages();
    }

    @Atomic(mode = TxMode.WRITE)
    private void deleteOldSenders() {
        int deletedCounter = 0;
        final Set<Sender> senders = new HashSet<Sender>();
        for (final Message message : Bennu.getInstance().getPendingUtilEmailMessagesSet()) {
            senders.add(message.getSender());
        }
        for (final Sender sender : senders) {
            deletedCounter += sender.deleteOldMessages();
        }
        taskLog("Deleted %s old messages using a sender threshold of %d\n", deletedCounter, Message.NUMBER_OF_SENT_EMAILS_TO_STAY);
    }

    private void dispatchMessages() {
        int dispatchCounter = 0;
        for (final Message message : Bennu.getInstance().getPendingUtilEmailMessagesSet()) {
            long start = System.currentTimeMillis();
            int emails = message.dispatch();
            getLogger().info("Dispatched message: {} in {}ms for {} emails", message.getExternalId(),
                    System.currentTimeMillis() - start, emails);
            dispatchCounter++;
        }
        taskLog("Dispatched %d messages\n", dispatchCounter);
    }
}
