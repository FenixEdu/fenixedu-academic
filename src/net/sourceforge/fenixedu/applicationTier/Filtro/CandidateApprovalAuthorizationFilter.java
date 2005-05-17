package net.sourceforge.fenixedu.applicationTier.Filtro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.dataTransferObject.InfoRole;
import net.sourceforge.fenixedu.domain.ICoordinator;
import net.sourceforge.fenixedu.domain.IMasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

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

        InfoRole infoRole = new InfoRole();
        infoRole.setRoleType(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE);
        roles.add(infoRole);

        infoRole = new InfoRole();
        infoRole.setRoleType(RoleType.COORDINATOR);
        roles.add(infoRole);

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

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        List roleTemp = new ArrayList();
        roleTemp.add(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE);
        if (CollectionUtils.containsAny(roles, roleTemp)) {
            return true;
        }

        roleTemp = new ArrayList();
        roleTemp.add(RoleType.COORDINATOR);
        if (CollectionUtils.containsAny(roles, roleTemp)) {

            ITeacher teacher = null;
            // Read The ExecutionDegree
            try {

                String ids[] = (String[]) arguments[1];

                teacher = sp.getIPersistentTeacher().readTeacherByUsername(id.getUtilizador());

                for (int i = 0; i < ids.length; i++) {

                    IMasterDegreeCandidate masterDegreeCandidate = (IMasterDegreeCandidate) sp
                            .getIPersistentMasterDegreeCandidate().readByOID(
                                    MasterDegreeCandidate.class, new Integer(ids[i]));

                    //modified by Tânia Pousão
                    ICoordinator coordinator = sp.getIPersistentCoordinator()
                            .readCoordinatorByTeacherIdAndExecutionDegreeId(teacher.getIdInternal(),
                                    masterDegreeCandidate.getExecutionDegree().getIdInternal());
                    if (coordinator == null) {
                        return false;
                    }

                }
            } catch (Exception e) {
                return false;
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