package net.sourceforge.fenixedu.applicationTier.Filtro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class WriteCandidateEnrolmentsAuhorizationFilter extends Filtro {

    public WriteCandidateEnrolmentsAuhorizationFilter() {
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
    @Override
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
    private boolean hasPrivilege(IUserView id, Object[] arguments) throws ExcepcaoPersistencia {
        if (id.hasRoleType(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE)) {
            return true;
        }

        if (id.hasRoleType(RoleType.COORDINATOR)) {
            Set<Integer> selection = (Set<Integer>) arguments[0];
            Integer candidateID = (Integer) arguments[1];
            Teacher teacher = id.getPerson().getTeacher();

            MasterDegreeCandidate masterDegreeCandidate = rootDomainObject
                    .readMasterDegreeCandidateByOID(candidateID);

            if (masterDegreeCandidate == null) {
                return false;
            }

            // modified by Tânia Pousão
            Coordinator coordinator = masterDegreeCandidate.getExecutionDegree()
                    .getCoordinatorByTeacher(teacher);

            if (coordinator == null) {
                return false;
            }

            for (Integer selectedCurricularCourse : selection) {

                // Modified by Fernanda Quitério

                CurricularCourse curricularCourse = (CurricularCourse) rootDomainObject
                        .readDegreeModuleByOID(selectedCurricularCourse);
                if (!curricularCourse.getDegreeCurricularPlan().equals(
                        masterDegreeCandidate.getExecutionDegree().getDegreeCurricularPlan())) {
                    return false;
                }

            }
            return true;
        }
        return true;
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