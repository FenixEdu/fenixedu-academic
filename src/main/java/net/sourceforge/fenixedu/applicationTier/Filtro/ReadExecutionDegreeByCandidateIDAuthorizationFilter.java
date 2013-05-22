package net.sourceforge.fenixedu.applicationTier.Filtro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadExecutionDegreeByCandidateIDAuthorizationFilter extends Filtro {

    public static final ReadExecutionDegreeByCandidateIDAuthorizationFilter instance =
            new ReadExecutionDegreeByCandidateIDAuthorizationFilter();

    public ReadExecutionDegreeByCandidateIDAuthorizationFilter() {
    }

    public void execute(Integer candidateID) throws NotAuthorizedException {
        IUserView id = AccessControl.getUserView();
        if ((id != null && id.getRoleTypes() != null && !containsRoleType(id.getRoleTypes()))
                || (id != null && id.getRoleTypes() != null && !hasPrivilege(id, candidateID)) || (id == null)
                || (id.getRoleTypes() == null)) {
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
    private boolean hasPrivilege(IUserView id, Integer candidateID) {
        if (id.hasRoleType(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE)) {
            return true;
        }

        if (id.hasRoleType(RoleType.COORDINATOR)) {
            // Read The ExecutionDegree

            final Person person = id.getPerson();

            MasterDegreeCandidate masterDegreeCandidate =
                    RootDomainObject.getInstance().readMasterDegreeCandidateByOID(candidateID);

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