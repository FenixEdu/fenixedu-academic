package net.sourceforge.fenixedu.applicationTier.Filtro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixframework.FenixFramework;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class WriteCandidateEnrolmentsAuhorizationFilter extends Filtro {

    public static final WriteCandidateEnrolmentsAuhorizationFilter instance = new WriteCandidateEnrolmentsAuhorizationFilter();

    public void execute(Set<String> selectedCurricularCoursesIDs, String candidateID, Double credits, String givenCreditsRemarks)
            throws NotAuthorizedException {
        User id = Authenticate.getUser();

        if ((id != null && id.getPerson().getPersonRolesSet() != null && !containsRoleType(id.getPerson().getPersonRolesSet()))
                || (id != null && id.getPerson().getPersonRolesSet() != null && !hasPrivilege(id, selectedCurricularCoursesIDs,
                        candidateID)) || (id == null) || (id.getPerson().getPersonRolesSet() == null)) {
            throw new NotAuthorizedException();
        }
    }

    @Override
    protected Collection<RoleType> getNeededRoleTypes() {
        List<RoleType> roles = new ArrayList<RoleType>();
        roles.add(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE);
        roles.add(RoleType.COORDINATOR);
        return roles;
    }

    private boolean hasPrivilege(User id, Set<String> selectedCurricularCoursesIDs, String candidateID) {
        if (id.getPerson().hasRole(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE)) {
            return true;
        }

        if (id.getPerson().hasRole(RoleType.COORDINATOR)) {
            final Person person = id.getPerson();

            MasterDegreeCandidate masterDegreeCandidate = FenixFramework.getDomainObject(candidateID);

            if (masterDegreeCandidate == null) {
                return false;
            }

            // modified by Tânia Pousão
            Coordinator coordinator = masterDegreeCandidate.getExecutionDegree().getCoordinatorByTeacher(person);

            if (coordinator == null) {
                return false;
            }

            for (String selectedCurricularCourse : selectedCurricularCoursesIDs) {

                // Modified by Fernanda Quitério

                CurricularCourse curricularCourse = (CurricularCourse) FenixFramework.getDomainObject(selectedCurricularCourse);
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