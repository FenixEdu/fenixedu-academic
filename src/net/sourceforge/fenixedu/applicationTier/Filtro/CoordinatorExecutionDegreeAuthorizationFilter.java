package net.sourceforge.fenixedu.applicationTier.Filtro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *  
 */
public class CoordinatorExecutionDegreeAuthorizationFilter extends Filtro {

    public CoordinatorExecutionDegreeAuthorizationFilter() {
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
        roles.add(Role.getRoleByRoleType(RoleType.TIME_TABLE_MANAGER));
        roles.add(Role.getRoleByRoleType(RoleType.COORDINATOR));
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
        roleTemp.add(RoleType.TIME_TABLE_MANAGER);
        if (CollectionUtils.containsAny(roles, roleTemp)) {
            return true;
        }

        roleTemp = new ArrayList();
        roleTemp.add(RoleType.COORDINATOR);
        if (CollectionUtils.containsAny(roles, roleTemp)) {
            try {
                Integer executionDegreeID = null;
                if (arguments[1] instanceof InfoExecutionDegree) {
                    executionDegreeID = ((InfoExecutionDegree) arguments[1]).getIdInternal();
                } else if (arguments[0] instanceof Integer) {
                    executionDegreeID = (Integer) arguments[0];
                }

                if (executionDegreeID == null) {
                    return false;
                }
                Teacher teacher = Teacher.readTeacherByUsername(id.getUtilizador());
                ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(executionDegreeID);
                if(executionDegree == null) {
                	return false;
                }
                Coordinator coordinator = executionDegree.getCoordinatorByTeacher(teacher);

                if (coordinator != null) {
                    return true;
                }
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
            result.add(((Role) iterator.next()).getRoleType());
        }

        return result;
    }

}