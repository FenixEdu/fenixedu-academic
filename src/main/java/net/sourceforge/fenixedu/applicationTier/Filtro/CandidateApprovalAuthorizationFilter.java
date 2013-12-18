package net.sourceforge.fenixedu.applicationTier.Filtro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class CandidateApprovalAuthorizationFilter extends Filtro {

    public static final CandidateApprovalAuthorizationFilter instance = new CandidateApprovalAuthorizationFilter();

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
    private boolean hasPrivilege(User id, String[] ids) {
        if (id.getPerson().hasRole(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE)) {
            return true;
        }

        if (id.getPerson().hasRole(RoleType.COORDINATOR)) {

            final Person person = id.getPerson();

            for (String id2 : ids) {

                MasterDegreeCandidate masterDegreeCandidate = FenixFramework.getDomainObject(id2);

                // modified by Tânia Pousão
                Coordinator coordinator = masterDegreeCandidate.getExecutionDegree().getCoordinatorByTeacher(person);

                if (coordinator == null) {
                    return false;
                }

            }
        }

        return true;
    }

    public void execute(String[] situations, String[] ids, String[] remarks, String[] substitutes) throws NotAuthorizedException {
        User userView = Authenticate.getUser();
        if ((userView != null && userView.getPerson().getPersonRolesSet() != null && !containsRoleType(userView.getPerson()
                .getPersonRolesSet()))
                || (userView != null && userView.getPerson().getPersonRolesSet() != null && !hasPrivilege(userView, ids))
                || (userView == null) || (userView.getPerson().getPersonRolesSet() == null)) {
            throw new NotAuthorizedException();
        }

    }

}