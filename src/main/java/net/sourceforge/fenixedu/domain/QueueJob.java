package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public abstract class QueueJob extends QueueJob_Base {
    public static enum Priority {
        HIGH, NORMAL;
    }

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
        return !getDone() && !hasBennuQueueUndone();
    }

    public boolean getIsNotDoneAndNotCancelled() {
        return !getDone() && hasBennuQueueUndone();
    }

    public static List<QueueJob> getAllJobsForClassOrSubClass(final Class aClass, int max) {
        List<QueueJob> tempList = (List<QueueJob>) CollectionUtils.select(Bennu.getInstance().getQueueJobSet(), new Predicate() {

            @Override
            public boolean evaluate(Object arg0) {
                return aClass.isInstance(arg0);
            }

        });

        return tempList.size() > max ? tempList.subList(0, max) : tempList;
    }

    public static List<QueueJob> getUndoneJobsForClass(final Class clazz) {
        return new ArrayList<QueueJob>(CollectionUtils.select(Bennu.getInstance().getQueueJobUndoneSet(), new Predicate() {

            @Override
            public boolean evaluate(Object arg0) {
                return clazz.isInstance(arg0);
            }

        }));
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

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasFailedCounter() {
        return getFailedCounter() != null;
    }

    @Deprecated
    public boolean hasRequestDate() {
        return getRequestDate() != null;
    }

    @Deprecated
    public boolean hasJobEndTime() {
        return getJobEndTime() != null;
    }

    @Deprecated
    public boolean hasDone() {
        return getDone() != null;
    }

    @Deprecated
    public boolean hasJobStartTime() {
        return getJobStartTime() != null;
    }

    @Deprecated
    public boolean hasPerson() {
        return getPerson() != null;
    }

    @Deprecated
    public boolean hasBennuQueueUndone() {
        return getRootDomainObjectQueueUndone() != null;
    }

}
