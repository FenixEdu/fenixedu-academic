package net.sourceforge.fenixedu.applicationTier.Filtro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
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
public class CandidateApprovalAuthorizationFilter extends Filtro {

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
        if (id.hasRoleType(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE)) {
            return true;
        }

        if (id.hasRoleType(RoleType.COORDINATOR)) {
            String ids[] = (String[]) arguments[1];

            final Person person = id.getPerson();

            for (String id2 : ids) {

                MasterDegreeCandidate masterDegreeCandidate =
                        RootDomainObject.getInstance().readMasterDegreeCandidateByOID(new Integer(id2));

                // modified by Tânia Pousão
                Coordinator coordinator = masterDegreeCandidate.getExecutionDegree().getCoordinatorByTeacher(person);

                if (coordinator == null) {
                    return false;
                }

            }
        }

        return true;
    }

    public void execute(Object[] parameters) throws Exception {
        IUserView userView = AccessControl.getUserView();
        if ((userView != null && userView.getRoleTypes() != null && !containsRoleType(userView.getRoleTypes()))
                || (userView != null && userView.getRoleTypes() != null && !hasPrivilege(userView, parameters))
                || (userView == null) || (userView.getRoleTypes() == null)) {
            throw new NotAuthorizedFilterException();
        }

    }

}