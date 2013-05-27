package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;


import net.sourceforge.fenixedu.applicationTier.Filtro.ResponsibleDegreeCoordinatorAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.ScientificCommission;
import pt.ist.fenixWebFramework.services.Service;

public class AddScientificCommission {

    protected void run(Integer executionDegreeId, Person person) {
        ExecutionDegree execution = RootDomainObject.getInstance().readExecutionDegreeByOID(executionDegreeId);

        new ScientificCommission(execution, person);
    }

    // Service Invokers migrated from Berserk

    private static final AddScientificCommission serviceInstance = new AddScientificCommission();

    @Service
    public static void runAddScientificCommission(Integer executionDegreeId, Person person) throws NotAuthorizedException {
        ResponsibleDegreeCoordinatorAuthorizationFilter.instance.execute();
        serviceInstance.run(executionDegreeId, person);
    }

}