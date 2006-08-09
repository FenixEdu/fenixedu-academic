package net.sourceforge.fenixedu.applicationTier.Filtro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.dataTransferObject.InfoRole;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class CandidateApprovalAuthorizationFilter extends Filtro {

    public CandidateApprovalAuthorizationFilter() {
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
    private boolean hasPrivilege(IUserView id, Object[] arguments) throws ExcepcaoPersistencia {

        List roles = getRoleList(id.getRoles());
        CollectionUtils.intersection(roles, getNeededRoles());

        List roleTemp = new ArrayList();
        roleTemp.add(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE);
        if (CollectionUtils.containsAny(roles, roleTemp)) {
            return true;
        }

        roleTemp = new ArrayList();
        roleTemp.add(RoleType.COORDINATOR);
        if (CollectionUtils.containsAny(roles, roleTemp)) {

            Teacher teacher = null;
            // Read The ExecutionDegree

                String ids[] = (String[]) arguments[1];

                teacher = Teacher.readTeacherByUsername(id.getUtilizador());

                for (int i = 0; i < ids.length; i++) {

                    MasterDegreeCandidate masterDegreeCandidate = rootDomainObject.readMasterDegreeCandidateByOID(new Integer(ids[i]));

                    //modified by Tânia Pousão
                    Coordinator coordinator = masterDegreeCandidate.getExecutionDegree().getCoordinatorByTeacher(teacher);

                    if (coordinator == null) {
                        return false;
                    }

                }
        }
        return true;
    }

    private List getRoleList(Collection roles) {
        List result = new ArrayList();
        Iterator iterator = roles.iterator();
        while (iterator.hasNext()) {
            result.add(((InfoRole) iterator.next()).getRoleType());
        }

        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see pt.utl.ist.berserk.logic.filterManager.IFilter#execute(pt.utl.ist.berserk.ServiceRequest,
     *      pt.utl.ist.berserk.ServiceResponse)
     */
    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
        IUserView userView = getRemoteUser(request);
        if ((userView != null && userView.getRoles() != null && !containsRole(userView.getRoles()))
                || (userView != null && userView.getRoles() != null && !hasPrivilege(userView,
                        getServiceCallArguments(request))) || (userView == null)
                || (userView.getRoles() == null)) {
            throw new NotAuthorizedFilterException();
        }

    }

}