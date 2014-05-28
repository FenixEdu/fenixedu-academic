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
package net.sourceforge.fenixedu.domain.accounting.events.export;

import java.util.List;

import net.sourceforge.fenixedu.domain.QueueJob;
import net.sourceforge.fenixedu.domain.QueueJobResult;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class SIBSOutgoingPaymentQueueJob extends SIBSOutgoingPaymentQueueJob_Base {

    public SIBSOutgoingPaymentQueueJob(DateTime lastSuccessfulSentPaymentFileDate) {
        super();
        setLastSuccessfulSentPaymentFileDate(lastSuccessfulSentPaymentFileDate);
    }

    @Override
    public QueueJobResult execute() throws Exception {
        new SIBSOutgoingPaymentFile(getLastSuccessfulSentPaymentFileDate());
        return new QueueJobResult();
    }

    @Atomic
    public static SIBSOutgoingPaymentQueueJob launchJob(DateTime lastSuccessfulSentPaymentFileDate) {
        return new SIBSOutgoingPaymentQueueJob(lastSuccessfulSentPaymentFileDate);
    }

    public static List<SIBSOutgoingPaymentQueueJob> readAllSIBSOutgoingPaymentQueueJobs() {
        return Lists.newArrayList(Iterables.filter(Bennu.getInstance().getJobsSet(), SIBSOutgoingPaymentQueueJob.class));
    }

    public static SIBSOutgoingPaymentQueueJob getQueueJobNotDoneAndNotCancelled() {
        List<SIBSOutgoingPaymentQueueJob> jobList = readAllSIBSOutgoingPaymentQueueJobs();

        return (SIBSOutgoingPaymentQueueJob) CollectionUtils.find(jobList, new Predicate() {

            @Override
            public boolean evaluate(Object arg0) {
                return ((QueueJob) arg0).getIsNotDoneAndNotCancelled();
            }

        });
    }

    public static boolean hasExportationQueueJobToRun() {
        return getQueueJobNotDoneAndNotCancelled() != null;
    }

    @Deprecated
    public boolean hasLastSuccessfulSentPaymentFileDate() {
        return getLastSuccessfulSentPaymentFileDate() != null;
    }

}
