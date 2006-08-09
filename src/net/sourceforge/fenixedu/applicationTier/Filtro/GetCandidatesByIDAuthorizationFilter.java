package net.sourceforge.fenixedu.applicationTier.Filtro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

public class GetCandidatesByIDAuthorizationFilter extends Filtro {

    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
        IUserView id = getRemoteUser(request);
        Object[] arguments = getServiceCallArguments(request);
        if ((id != null && id.getRoles() != null && !containsRole(id.getRoles()))
                || (id != null && id.getRoles() != null && !hasPrivilege(id, arguments)) || (id == null)
                || (id.getRoles() == null)) {
            throw new NotAuthorizedFilterException();
        }
    }

    /**
     * @return The Needed Roles to Execute The Service
     */
    protected Collection getNeededRoles() {
        List<Role> roles = new ArrayList<Role>();
        roles.add(Role.getRoleByRoleType(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE));
        roles.add(Role.getRoleByRoleType(RoleType.COORDINATOR));
        return roles;
    }

    /**
     * @param id
     * @param argumentos
     * @return
     */
    private boolean hasPrivilege(IUserView id, Object[] arguments) throws ExcepcaoPersistencia {
        List<RoleType> roles = getRoleList(id.getRoles());
        CollectionUtils.intersection(roles, getNeededRoles());

        List<RoleType> roleTemp = new ArrayList<RoleType>();
        roleTemp.add(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE);
        if (CollectionUtils.containsAny(roles, roleTemp)) {
            return true;
        }

        roleTemp = new ArrayList<RoleType>();
        roleTemp.add(RoleType.COORDINATOR);
        if (CollectionUtils.containsAny(roles, roleTemp)) {

            Teacher teacher = null;
            // Read The ExecutionDegree
            try {

                Integer candidateID = (Integer) arguments[0];

                teacher = Teacher.readTeacherByUsername(id.getUtilizador());

                MasterDegreeCandidate masterDegreeCandidate = rootDomainObject.readMasterDegreeCandidateByOID(candidateID);
                if (masterDegreeCandidate == null) {
                    return false;
                }

                //modified by Tânia Pousão
                Coordinator coordinator = masterDegreeCandidate.getExecutionDegree().getCoordinatorByTeacher(teacher);
                if (coordinator == null) {
                    return false;
                }

                return true;

            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    private List<RoleType> getRoleList(Collection roles) {
        List<RoleType> result = new ArrayList<RoleType>();
        Iterator iterator = roles.iterator();
        while (iterator.hasNext()) {
            result.add(((Role) iterator.next()).getRoleType());
        }

        return result;
    }

}