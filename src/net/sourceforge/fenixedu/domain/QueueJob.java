package net.sourceforge.fenixedu.domain;

import java.util.List;

import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.reports.GepReportFile;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.gep.ReportsByDegreeTypeDA.FindSelectedGepReports;

import org.apache.commons.collections.Predicate;
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
	setDone(Boolean.FALSE);
	setPerson(AccessControl.getPerson());
    }

    public void execute() throws Exception {
    }

    public String getDescription() {
	return "Tarefa";
    }

    public String getFilename() {
	return "ficheiro";
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
    
    public static List<QueueJob> getAllJobsForClassOrSubClass(Class aClass, int max){
	Predicate predicate = new FindQueueJobsForAClass(aClass);
	List<QueueJob> tempList = (List<QueueJob>) org.apache.commons.collections.CollectionUtils.select(
		RootDomainObject.getInstance().getQueueJob(), predicate);
	
	return tempList.size() > max ? tempList.subList(0, max) : tempList;
    }

}
