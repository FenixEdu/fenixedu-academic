package net.sourceforge.fenixedu.domain;

import java.util.List;

import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.apache.commons.collections.Predicate;
import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.services.Service;

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

    public static class FindQueueJobsForAClass implements Predicate {

	Class aClass;

	public FindQueueJobsForAClass(Class reportClass) {
	    this.aClass = reportClass;
	}

	public boolean evaluate(Object object) {
	    QueueJob queueJob = (QueueJob) object;
	    try {
		aClass.cast(queueJob);
		return true;

	    } catch (ClassCastException E) {
		return false;
	    }

	}
    }

    public static List<QueueJob> getAllJobsForClassOrSubClass(Class aClass, int max) {
	Predicate predicate = new FindQueueJobsForAClass(aClass);
	List<QueueJob> tempList = (List<QueueJob>) org.apache.commons.collections.CollectionUtils.select(RootDomainObject
		.getInstance().getQueueJob(), predicate);

	return tempList.size() > max ? tempList.subList(0, max) : tempList;
    }

    public Priority getPriority() {
	return Priority.NORMAL;
    }

    @Service
    public void cancel() {
	removeRootDomainObjectQueueUndone();
    }

    @Service
    public void resend() {
	setRootDomainObjectQueueUndone(RootDomainObject.getInstance());
    }

}
