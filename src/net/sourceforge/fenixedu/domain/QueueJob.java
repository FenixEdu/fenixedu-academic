package net.sourceforge.fenixedu.domain;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.services.Service;

public class QueueJob extends QueueJob_Base {

    @Service
    public static void newInstance() {
	new QueueJob();
    }

    public QueueJob() {
	super();
	this.setRequestDate(new DateTime());
	setFailedCounter(new Integer(0));
	setRootDomainObject(RootDomainObject.getInstance());
	setRootDomainObjectQueueUndone(RootDomainObject.getInstance());
    }

    public void execute() throws Exception {
    }

    public String getDescription() {
	return "Tarefa";
    }

    public String getFilename() {
	return "ficheiro";
    }

}
