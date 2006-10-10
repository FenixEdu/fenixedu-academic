package net.sourceforge.fenixedu.applicationTier.Filtro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class StudentListByCurricularCourseAuthorizationFilter extends Filtro {

    /**
     * 
     */
    public StudentListByCurricularCourseAuthorizationFilter() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see pt.utl.ist.berserk.logic.filterManager.IFilter#execute(pt.utl.ist.berserk.ServiceRequest,
     *      pt.utl.ist.berserk.ServiceResponse)
     */
    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
        IUserView id = (IUserView) request.getRequester();
        if ((id != null && id.getRoleTypes() != null && !containsRoleType(id.getRoleTypes()))
                || (id != null && id.getRoleTypes() != null && !hasPrivilege(id, request
                        .getServiceParameters().parametersArray())) || (id == null)
                || (id.getRoleTypes() == null)) {
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
    private boolean hasPrivilege(IUserView id, Object[] arguments) {
        Integer curricularCourseID = (Integer) arguments[1];

        CurricularCourse curricularCourse = null;

        // Read The DegreeCurricularPlan
        try {
            curricularCourse = (CurricularCourse) rootDomainObject
                    .readDegreeModuleByOID(curricularCourseID);
        } catch (Exception e) {
            return false;
        }

        if (curricularCourse == null) {
            return false;
        }

        if (id.hasRoleType(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE)) {
            if (curricularCourse.getDegreeCurricularPlan().getDegree().getTipoCurso().equals(
                    DegreeType.MASTER_DEGREE)) {
                return true;
            }
            return false;

        }

        if (id.hasRoleType(RoleType.COORDINATOR)) {
            List executionDegrees = curricularCourse.getDegreeCurricularPlan().getExecutionDegrees();
            if (executionDegrees == null || executionDegrees.isEmpty()) {
                return false;
            }
            // IMPORTANT: It's assumed that the coordinator for a Degree is
            // ALWAYS the same
            // modified by Tânia Pousão
            List<Coordinator> coodinatorsList = ((ExecutionDegree) executionDegrees.get(0))
                    .getCoordinatorsList();
            if (coodinatorsList == null) {
                return false;
            }
            ListIterator listIterator = coodinatorsList.listIterator();
            while (listIterator.hasNext()) {
                Coordinator coordinator = (Coordinator) listIterator.next();

                if (coordinator.getTeacher().getPerson().getUsername().equals(id.getUtilizador())) {
                    return true;
                }
            }

            return false;
        }
        return false;
    }
}