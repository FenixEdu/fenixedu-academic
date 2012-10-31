package net.sourceforge.fenixedu.domain.cardGeneration;

public class SantanderProblem extends SantanderProblem_Base {
    
    public  SantanderProblem() {
        super();
    }
    
    public void delete() {
	removeSantanderBatch();
	removeRootDomainObject();
	super.deleteDomainObject();
    }
    
}
