package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class ScientificCommission extends ScientificCommission_Base {
    
    public ScientificCommission(ExecutionDegree executionDegree, Person person) {
        super();
        
        if (executionDegree.isPersonInScientificCommission(person)) {
            throw new DomainException("scientificCommission.person.duplicate");
        }
        
        setRootDomainObject(RootDomainObject.getInstance());

        setContact(false);
        setExecutionDegree(executionDegree);
        setPerson(person);
    }
    
    public Boolean isContact() {
        return getContact() == null ? false : getContact();
    }

    public void delete() {
        removePerson();
        removeExecutionDegree();
        
        deleteDomainObject();
    }
    
}
