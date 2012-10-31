package net.sourceforge.fenixedu.domain.cardGeneration;

import java.util.Comparator;

import org.joda.time.DateTime;

import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;

import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class SantanderBatch extends SantanderBatch_Base {
    
    static final public Comparator<SantanderBatch> COMPARATOR_BY_MOST_RECENTLY_CREATED = new Comparator<SantanderBatch>() {

	public int compare(SantanderBatch o1, SantanderBatch o2) {
	    return o1.getCreated().isAfter(o2.getCreated()) ? 1 : 0;
	}

    };
    
    private  SantanderBatch() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }
    
    public SantanderBatch(Person requester, ExecutionYear executionYear) {
	this();
	setSantanderBatchRequester(new SantanderBatchRequester(requester));
	setExecutionYear(executionYear);
	setCreated(new DateTime());
    }
    
    public void delete() {
	if (getSent() != null) {
	    throw new DomainException("santander.cards.cant.delete.batch.was.already.sent");
	}
	getSantanderBatchRequester().delete();
	if (getSantanderBatchSender() != null) {
	    getSantanderBatchSender().delete();
	}
	removeSantanderSequenceNumberGenerator();
	for (SantanderEntry entry : getSantanderEntries()) {
	    entry.delete();
	}
	for (SantanderProblem problem : getSantanderProblems()) {
	    problem.delete();
	}
	removeExecutionYear();
	removeRootDomainObject();

	super.deleteDomainObject();
    }
    
}
