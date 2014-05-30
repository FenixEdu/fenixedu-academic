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
