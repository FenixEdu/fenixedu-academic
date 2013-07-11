package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
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
        setRootDomainObject(RootDomainObject.getInstance());
        setRootDomainObjectQueueUndone(RootDomainObject.getInstance());
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
        return !getDone() && !hasRootDomainObjectQueueUndone();
    }

    public boolean getIsNotDoneAndNotCancelled() {
        return !getDone() && hasRootDomainObjectQueueUndone();
    }

    public static List<QueueJob> getAllJobsForClassOrSubClass(final Class aClass, int max) {
        List<QueueJob> tempList =
                (List<QueueJob>) CollectionUtils.select(RootDomainObject.getInstance().getQueueJob(), new Predicate() {

                    @Override
                    public boolean evaluate(Object arg0) {
                        return aClass.isInstance(arg0);
                    }

                });

        return tempList.size() > max ? tempList.subList(0, max) : tempList;
    }

    public static List<QueueJob> getUndoneJobsForClass(final Class clazz) {
        return new ArrayList<QueueJob>(CollectionUtils.select(RootDomainObject.getInstance().getQueueJobUndone(),
                new Predicate() {

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
        setRootDomainObjectQueueUndone(RootDomainObject.getInstance());
    }

}
