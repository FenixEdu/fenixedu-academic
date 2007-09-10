package net.sourceforge.fenixedu.domain.elections;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.student.Student;

public class DelegateElectionVote extends DelegateElectionVote_Base {
    
    private  DelegateElectionVote() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }
    
    public DelegateElectionVote(DelegateElection election, Student student) {
    	this();
    	setDelegateElection(election);
    	setStudent(student);
    }
    
    public void delete() {
    	removeStudent();
    	removeDelegateElection();
    	removeRootDomainObject();
    	super.deleteDomainObject();
    }
    
}
