package net.sourceforge.fenixedu.domain.elections;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Student;

public class DelegateElectionVote extends DelegateElectionVote_Base {
    
    private  DelegateElectionVote() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }
    
    public DelegateElectionVote(DelegateElection election, Student student) {
    	this();
    	checkParameters(student, election);
    	setDelegateElection(election);
    	setStudent(student);
    }
    
    public void delete() {
    	removeStudent();
    	removeDelegateElection();
    	removeRootDomainObject();
    	super.deleteDomainObject();
    }
    private void checkParameters(final Student student, final DelegateElection election) {
    	if (student == null) {
    	    throw new DomainException("error.student.cannot.be.null");
    	}
    	if (election == null) {
    	    throw new DomainException("error.delegateElection.cannot.be.null");
    	}
        }
    
}
