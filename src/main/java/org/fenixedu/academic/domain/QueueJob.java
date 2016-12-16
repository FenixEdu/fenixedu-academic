/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public abstract class QueueJob extends QueueJob_Base {
    public static enum Priority {
        HIGH, NORMAL;
    }

    static final public Comparator<QueueJob> COMPARATORY_BY_REQUEST_DATE = new Comparator<QueueJob>() {
        @Override
        public int compare(QueueJob o1, QueueJob o2) {
            return o2.getRequestDate().compareTo(o1.getRequestDate());
        }
    };

    public QueueJob() {
        super();
        this.setRequestDate(new DateTime());
        setFailedCounter(new Integer(0));
        setRootDomainObject(Bennu.getInstance());
        setRootDomainObjectQueueUndone(Bennu.getInstance());
        setDone(Boolean.FALSE);
        setPerson(AccessControl.getPerson());
    }

    public abstract QueueJobResult execute() throws Exception;

    public String getDescription() {
        return "Tarefa";
    }

    public String getFilename() {
        return "ficheiro";
    }

    public boolean getIsNotDoneAndCancelled() {
        return !getDone() && getRootDomainObjectQueueUndone() == null;
    }

    public boolean getIsNotDoneAndNotCancelled() {
        return !getDone() && getRootDomainObjectQueueUndone() != null;
    }

    public static List<QueueJob> getLastJobsForClassOrSubClass(final Class<? extends QueueJob> type, int maxSize) {
        return getAllJobsForClassOrSubClass(type, maxSize, COMPARATORY_BY_REQUEST_DATE);
    }

    public static List<QueueJob> getAllJobsForClassOrSubClass(final Class<? extends QueueJob> type, int maxSize,
            Comparator<QueueJob> comparator) {
        return Bennu.getInstance().getQueueJobSet().stream().filter((type)::isInstance).sorted(comparator).limit(maxSize)
                .collect(Collectors.toList());
    }

    public static List<QueueJob> getUndoneJobsForClass(final Class<? extends QueueJob> type) {
        return Bennu.getInstance().getQueueJobUndoneSet().stream().filter((type)::isInstance).collect(Collectors.toList());
    }

    public Priority getPriority() {
        return Priority.NORMAL;
    }

    @Atomic
    public void cancel() {
        setRootDomainObjectQueueUndone(null);
    }

    @Atomic
    public void resend() {
        setRootDomainObjectQueueUndone(Bennu.getInstance());
    }

}
