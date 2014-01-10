package net.sourceforge.fenixedu.applicationTier.Filtro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.util.SituationName;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixframework.FenixFramework;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadCandidatesForSelectionAuthorizationFilter extends Filtro {

    public static final ReadCandidatesForSelectionAuthorizationFilter instance =
            new ReadCandidatesForSelectionAuthorizationFilter();

    public ReadCandidatesForSelectionAuthorizationFilter() {

    }

    public void execute(String executionDegreeID, List<SituationName> situationNames) throws NotAuthorizedException {

        User id = Authenticate.getUser();
        if ((id != null && id.getPerson().getPersonRolesSet() != null && !containsRoleType(id.getPerson().getPersonRolesSet()))
                || (id != null && id.getPerson().getPersonRolesSet() != null && !hasPrivilege(id, executionDegreeID))
                || (id == null) || (id.getPerson().getPersonRolesSet() == null)) {
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
    private boolean hasPrivilege(User id, String executionDegreeID) {

        ExecutionDegree executionDegree = null;

        // Read The DegreeCurricularPlan
        try {

            executionDegree = FenixFramework.getDomainObject(executionDegreeID);

        } catch (Exception e) {
            return false;
        }

        if (executionDegree == null) {
            return false;
        }

        if (id.getPerson().hasRole(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE)) {
            if (executionDegree.getDegreeCurricularPlan().getDegree().getDegreeType().equals(DegreeType.MASTER_DEGREE)) {

                return true;
            }
            return false;

        }

        if (id.getPerson().hasRole(RoleType.COORDINATOR)) {
            // modified by Tânia Pousão
            Collection<Coordinator> coodinatorsList = executionDegree.getCoordinatorsList();
            if (coodinatorsList == null) {
                return false;
            }
            Iterator<Coordinator> listIterator = coodinatorsList.iterator();
            while (listIterator.hasNext()) {
                Coordinator coordinator = listIterator.next();

                if (coordinator.getPerson() == id.getPerson()) {
                    return true;
                }
            }
        }
        return false;
    }

}