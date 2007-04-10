package net.sourceforge.fenixedu.domain;

import dml.runtime.RelationAdapter;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;

public class ScientificCommission extends ScientificCommission_Base {

    static {
        ScientificCommissionPerson.addListener(new ManageCoordinatorRole());
    }
    
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
    
    /**
     * Manage the role COORDINATOR associated with the person. The person
     * becomes a COORDINATOR when it's added to a scientific commission. This
     * listerner also removes the role from the person when it's removed from
     * every scientific commissions and it's not in a coordination team.
     * 
     * @author cfgi
     */
    private static class ManageCoordinatorRole extends RelationAdapter<ScientificCommission, Person> {

        @Override
        public void afterAdd(ScientificCommission commission, Person person) {
            super.afterAdd(commission, person);
            
            if (person != null && commission != null) {
                person.addPersonRoleByRoleType(RoleType.COORDINATOR);
            }
        }

        @Override
        public void afterRemove(ScientificCommission commission, Person person) {
            super.afterRemove(commission, person);
            
            if (person != null && commission != null) {
                if (person.hasAnyCoordinators()) {
                    return;
                }
                
                if (person.hasAnyScientificCommissions()) {
                    return;
                }
                
                person.removeRoleByType(RoleType.COORDINATOR);
            }
        }
        
    }
}
