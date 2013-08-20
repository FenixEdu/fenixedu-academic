package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import net.sourceforge.fenixedu.applicationTier.Filtro.ResponsibleDegreeCoordinatorAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.ScientificCommission;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class AddScientificCommission {

    protected void run(String executionDegreeId, Person person) {
        ExecutionDegree execution = AbstractDomainObject.fromExternalId(executionDegreeId);

        new ScientificCommission(execution, person);
    }

    // Service Invokers migrated from Berserk

    private static final AddScientificCommission serviceInstance = new AddScientificCommission();

    @Service
    public static void runAddScientificCommission(String executionDegreeId, Person person) throws NotAuthorizedException {
        ResponsibleDegreeCoordinatorAuthorizationFilter.instance.execute();
        serviceInstance.run(executionDegreeId, person);
    }

}