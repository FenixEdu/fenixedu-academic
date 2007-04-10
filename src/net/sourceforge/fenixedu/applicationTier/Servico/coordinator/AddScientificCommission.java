package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.ScientificCommission;

public class AddScientificCommission extends Service {

    public void run(Integer executionDegreeId, Person person) {
        ExecutionDegree execution = RootDomainObject.getInstance().readExecutionDegreeByOID(executionDegreeId);
        
        new ScientificCommission(execution, person);
    }
    
}
