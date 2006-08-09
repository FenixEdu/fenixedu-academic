package net.sourceforge.fenixedu.applicationTier.Filtro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.dataTransferObject.InfoRole;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.apache.commons.collections.CollectionUtils;

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
        if ((id != null && id.getRoles() != null && !containsRole(id.getRoles()))
                || (id != null && id.getRoles() != null && !hasPrivilege(id, argumentos))
                || (id == null) || (id.getRoles() == null)) {
            throw new NotAuthorizedFilterException();
        }
    }

    /**
     * @return The Needed Roles to Execute The Service
     */
    protected Collection getNeededRoles() {
        List roles = new ArrayList();
        roles.add(new InfoRole(Role.getRoleByRoleType(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE)));
        roles.add(new InfoRole(Role.getRoleByRoleType(RoleType.COORDINATOR)));
        return roles;
    }

    /**
     * @param id
     * @param argumentos
     * @return
     */
    private boolean hasPrivilege(IUserView id, Object[] arguments) {

        List roles = getRoleList(id.getRoles());
        CollectionUtils.intersection(roles, getNeededRoles());

        /*
         * String executionYearString = (String) arguments[0]; String degreeCode =
         * (String) arguments[1];
         */

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

        List roleTemp = new ArrayList();
        roleTemp.add(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE);
        if (CollectionUtils.containsAny(roles, roleTemp)) {
            if (executionDegree.getDegreeCurricularPlan().getDegree().getTipoCurso().equals(
                    DegreeType.MASTER_DEGREE)) {

                return true;
            }
            return false;

        }

        roleTemp = new ArrayList();
        roleTemp.add(RoleType.COORDINATOR);
        if (CollectionUtils.containsAny(roles, roleTemp)) {

            // Read The ExecutionDegree
            try {
                // IMPORTANT: It's assumed that the coordinator for a Degree is
                // ALWAYS the same

                //modified by Tânia Pousão
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

                //            	teacher = executionDegree.getCoordinator();
                //                if (teacher == null)
                //                {
                //                    return false;
                //                }
                //
                //                if
                // (id.getUtilizador().equals(teacher.getPerson().getUsername()))
                //                {
                //                    return true;
                //                } else
                //                {
                //                    return false;
                //                }
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    private List getRoleList(Collection roles) {
        List result = new ArrayList();
        Iterator iterator = roles.iterator();
        while (iterator.hasNext()) {
            result.add(((InfoRole) iterator.next()).getRoleType());
        }

        return result;
    }

}