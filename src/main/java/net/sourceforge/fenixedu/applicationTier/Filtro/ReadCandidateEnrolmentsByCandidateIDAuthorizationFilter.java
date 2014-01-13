package net.sourceforge.fenixedu.applicationTier.Filtro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Coordinator;
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
public class ReadCandidateEnrolmentsByCandidateIDAuthorizationFilter extends Filtro {

    public static final ReadCandidateEnrolmentsByCandidateIDAuthorizationFilter instance =
            new ReadCandidateEnrolmentsByCandidateIDAuthorizationFilter();

    public ReadCandidateEnrolmentsByCandidateIDAuthorizationFilter() {
    }

    public void execute(String candidateID) throws NotAuthorizedException {
        User id = Authenticate.getUser();
        if ((id != null && id.getPerson().getPersonRolesSet() != null && !containsRoleType(id.getPerson().getPersonRolesSet()))
                || (id != null && id.getPerson().getPersonRolesSet() != null && !hasPrivilege(id, candidateID)) || (id == null)
                || (id.getPerson().getPersonRolesSet() == null)) {
            throw new NotAuthorizedException();
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
    private boolean hasPrivilege(User id, String candidateID) {
        if (id.getPerson().hasRole(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE)) {
            return true;
        }

        if (id.getPerson().hasRole(RoleType.COORDINATOR)) {
            final Person person = id.getPerson();
            // Read The ExecutionDegree

            MasterDegreeCandidate masterDegreeCandidate = FenixFramework.getDomainObject(candidateID);

            if (masterDegreeCandidate == null) {
                return false;
            }

            // modified by Tânia Pousão
            Coordinator coordinator = masterDegreeCandidate.getExecutionDegree().getCoordinatorByTeacher(person);
            if (coordinator != null) {
                return true;
            }
            return false;
        }
        return true;
    }

}