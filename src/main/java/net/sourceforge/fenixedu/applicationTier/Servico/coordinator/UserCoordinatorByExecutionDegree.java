package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Filtro.CoordinatorExecutionDegreeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Tânia Pousão Create on 04/Fev/2003
 */
public class UserCoordinatorByExecutionDegree extends FenixService {

    protected Boolean run(Integer executionDegreeCode, final Person person, String degree2Compare) throws FenixServiceException {
        final ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(executionDegreeCode);
        final Coordinator coordinator = executionDegree.getCoordinatorByTeacher(person);

        if (coordinator == null) {
            throw new FenixServiceException("error.exception.notAuthorized");
        }
        return Boolean.valueOf(coordinator.getExecutionDegree().getDegreeCurricularPlan().getDegree().getSigla()
                .startsWith(degree2Compare));
    }

    // Service Invokers migrated from Berserk

    private static final UserCoordinatorByExecutionDegree serviceInstance = new UserCoordinatorByExecutionDegree();

    @Service
    public static Boolean runUserCoordinatorByExecutionDegree(Integer executionDegreeCode, final Person person,
            String degree2Compare) throws FenixServiceException {
        CoordinatorExecutionDegreeAuthorizationFilter.instance.execute(executionDegreeCode);
        return serviceInstance.run(executionDegreeCode, person, degree2Compare);
    }

}