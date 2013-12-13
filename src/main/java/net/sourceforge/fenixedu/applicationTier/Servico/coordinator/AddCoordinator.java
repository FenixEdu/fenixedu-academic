package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import net.sourceforge.fenixedu.applicationTier.Filtro.ResponsibleDegreeCoordinatorAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class AddCoordinator {

    @Atomic
    public static Boolean run(final String executionDegreeId, final String istUsername) throws FenixServiceException {

        final Person person = Person.readPersonByUsername(istUsername);
        if (person == null) {
            throw new FenixServiceException("error.noEmployeeForIstUsername");
        }

        final Employee employee = person.getEmployee();
        if (employee == null) {
            throw new FenixServiceException("error.noEmployeeForIstUsername");
        }

        final ExecutionDegree executionDegree = FenixFramework.getDomainObject(executionDegreeId);
        if (executionDegree == null) {
            throw new FenixServiceException("error.noExecutionDegree");
        }

        final Coordinator coordinator = executionDegree.getCoordinatorByTeacher(person);
        if (coordinator == null) {
            Coordinator.createCoordinator(executionDegree, person, Boolean.FALSE);
        }

        return Boolean.TRUE;
    }

    // Service Invokers migrated from Berserk

    @Atomic
    public static Boolean runAddCoordinator(String executionDegreeId, String istUsername) throws FenixServiceException,
            NotAuthorizedException {
        ResponsibleDegreeCoordinatorAuthorizationFilter.instance.execute(executionDegreeId);
        return run(executionDegreeId, istUsername);
    }

}