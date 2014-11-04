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

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.scheduler.CronTask;
import org.fenixedu.bennu.scheduler.annotation.Task;

@Task(englishTitle = "EmailTask", readOnly = true)
public class EmailTask extends CronTask {
    @Override
    public void runTask() {
        int sentCounter = 0;
        for (final Email email : Bennu.getInstance().getEmailQueueSet()) {
            email.deliver();
            getLogger().debug("Sent email: {} succeeded: {} failed: {}", email.getExternalId(),
                    email.getConfirmedAddresses() != null ? email.getConfirmedAddresses().size() : 0,
                    email.getFailedAddresses() != null ? email.getFailedAddresses().size() : 0);
            sentCounter++;
        }
        taskLog("Sent %d email batches\n", sentCounter);
    }
}
