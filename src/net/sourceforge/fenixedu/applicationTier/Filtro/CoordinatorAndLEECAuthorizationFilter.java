/*
 * Created on 2/Fev/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

/**
 * @author Tânia Pousão
 * 
 */
public class CoordinatorAndLEECAuthorizationFilter extends AuthorizationByRoleFilter {

    protected RoleType getRoleType() {
        return RoleType.COORDINATOR;
    }

    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
        IUserView id = getRemoteUser(request);
        Object[] argumentos = getServiceCallArguments(request);

        if ((id == null) || (id.getRoleTypes() == null) || !id.hasRoleType(getRoleType())
                || !coordinatorLEEC(id, argumentos)) {
            throw new NotAuthorizedFilterException();
        }
    }

    private boolean coordinatorLEEC(IUserView id, Object[] argumentos) {
        if (argumentos == null || argumentos[0] == null) {
            return false;
        }

        final ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID((Integer) argumentos[0]);
        if (executionDegree != null) {
            final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
            final Degree degree = degreeCurricularPlan.getDegree();
            if (degree.getSigla().startsWith("LEEC") || degree.getSigla().equalsIgnoreCase("LEEC-pB")) {
                final Person person = id.getPerson();

                for (final Coordinator coordinator : executionDegree.getCoordinatorsListSet()) {
                    if (coordinator.getPerson() == person) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

}