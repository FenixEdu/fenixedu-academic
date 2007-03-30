package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.ScientificCommission;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;

public class DeleteScientificCommission extends Service {

    public void run(Integer executionDegreeId, ScientificCommission commission) {
        ExecutionDegree executionDegree = RootDomainObject.getInstance().readExecutionDegreeByOID(executionDegreeId);
        
        if (! executionDegree.getScientificCommissionMembers().contains(commission)) {
            throw new DomainException("scientificCommission.delete.incorrectExecutionDegree");
        }
        
        Person person = commission.getPerson();
        person.removeRoleByType(RoleType.COORDINATOR);
        
        commission.delete();
    }
    
}
