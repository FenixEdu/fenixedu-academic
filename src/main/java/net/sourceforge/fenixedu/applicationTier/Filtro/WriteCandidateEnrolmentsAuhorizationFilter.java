package net.sourceforge.fenixedu.applicationTier.Filtro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class WriteCandidateEnrolmentsAuhorizationFilter extends Filtro {

    @Override
    public void execute(ServiceRequest request) throws Exception {
        IUserView id = AccessControl.getUserView();
        Object[] argumentos = request.getServiceParameters().parametersArray();

        if ((id != null && id.getRoleTypes() != null && !containsRoleType(id.getRoleTypes()))
                || (id != null && id.getRoleTypes() != null && !hasPrivilege(id, argumentos)) || (id == null)
                || (id.getRoleTypes() == null)) {
            throw new NotAuthorizedFilterException();
        }
    }

    @Override
    protected Collection<RoleType> getNeededRoleTypes() {
        List<RoleType> roles = new ArrayList<RoleType>();
        roles.add(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE);
        roles.add(RoleType.COORDINATOR);
        return roles;
    }

    private boolean hasPrivilege(IUserView id, Object[] arguments) {
        if (id.hasRoleType(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE)) {
            return true;
        }

        if (id.hasRoleType(RoleType.COORDINATOR)) {
            Set<Integer> selection = (Set<Integer>) arguments[0];
            Integer candidateID = (Integer) arguments[1];
            final Person person = id.getPerson();

            MasterDegreeCandidate masterDegreeCandidate = RootDomainObject.getInstance().readMasterDegreeCandidateByOID(candidateID);

            if (masterDegreeCandidate == null) {
                return false;
            }

            // modified by Tânia Pousão
            Coordinator coordinator = masterDegreeCandidate.getExecutionDegree().getCoordinatorByTeacher(person);

            if (coordinator == null) {
                return false;
            }

            for (Integer selectedCurricularCourse : selection) {

                // Modified by Fernanda Quitério

                CurricularCourse curricularCourse =
                        (CurricularCourse) RootDomainObject.getInstance().readDegreeModuleByOID(selectedCurricularCourse);
                if (!curricularCourse.getDegreeCurricularPlan().equals(
                        masterDegreeCandidate.getExecutionDegree().getDegreeCurricularPlan())) {
                    return false;
                }

            }
            return true;
        }
        return true;
    }

}