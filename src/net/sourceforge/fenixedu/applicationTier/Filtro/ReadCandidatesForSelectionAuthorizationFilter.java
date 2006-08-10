package net.sourceforge.fenixedu.applicationTier.Filtro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadCandidatesForSelectionAuthorizationFilter extends Filtro {

    public ReadCandidatesForSelectionAuthorizationFilter() {

    }

    /*
     * (non-Javadoc)
     * 
     * @see pt.utl.ist.berserk.logic.filterManager.IFilter#execute(pt.utl.ist.berserk.ServiceRequest,
     *      pt.utl.ist.berserk.ServiceResponse)
     */
    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {

        IUserView id = getRemoteUser(request);
        Object[] argumentos = getServiceCallArguments(request);
        if ((id != null && id.getRoleTypes() != null && !containsRoleType(id.getRoleTypes()))
                || (id != null && id.getRoleTypes() != null && !hasPrivilege(id, argumentos))
                || (id == null) || (id.getRoleTypes() == null)) {
            throw new NotAuthorizedFilterException();
        }
    }

    /**
     * @return The Needed Roles to Execute The Service
     */
    protected Collection<RoleType> getNeededRoleTypes() {
        List<RoleType> roles = new ArrayList<RoleType>();
        roles.add(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE);
        roles.add(RoleType.COORDINATOR);
        return roles;
    }

    /**
     * @param id
     * @param argumentos
     * @return
     */
    private boolean hasPrivilege(IUserView id, Object[] arguments) {
        Integer executionDegreeID = (Integer) arguments[0];

        ExecutionDegree executionDegree = null;

        // Read The DegreeCurricularPlan
        try {

            executionDegree = rootDomainObject.readExecutionDegreeByOID(executionDegreeID);

        } catch (Exception e) {
            return false;
        }

        if (executionDegree == null) {
            return false;
        }

        if (id.hasRoleType(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE)) {
            if (executionDegree.getDegreeCurricularPlan().getDegree().getTipoCurso().equals(
                    DegreeType.MASTER_DEGREE)) {

                return true;
            }
            return false;

        }

        if (id.hasRoleType(RoleType.COORDINATOR)) {
            // modified by Tânia Pousão
            List<Coordinator> coodinatorsList = executionDegree.getCoordinatorsList();
            if (coodinatorsList == null) {
                return false;
            }
            ListIterator listIterator = coodinatorsList.listIterator();
            while (listIterator.hasNext()) {
                Coordinator coordinator = (Coordinator) listIterator.next();

                if (id.getUtilizador().equals(coordinator.getTeacher().getPerson().getUsername())) {
                    return true;
                }
            }
        }
        return false;
    }

}