package net.sourceforge.fenixedu.applicationTier.Filtro;

import java.util.Collection;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Seminaries.SeminaryCandidacy;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixframework.FenixFramework;

public class ManagerOrSeminariesCoordinatorFilter {

    public static final ManagerOrSeminariesCoordinatorFilter instance = new ManagerOrSeminariesCoordinatorFilter();

    public void execute(String SCPIDINternal) throws NotAuthorizedException {
        User id = Authenticate.getUser();

        boolean seminaryCandidate = false;
        if (SCPIDINternal != null) {
            seminaryCandidate = this.doesThisSCPBelongToASeminaryCandidate(SCPIDINternal);
        }

        if (((id != null && id.getPerson().getPersonRolesSet() != null && !id.getPerson().hasRole(getRoleType1()) && !(id
                .getPerson().hasRole(getRoleType2()) && seminaryCandidate)))
                || (id == null)
                || (id.getPerson().getPersonRolesSet() == null)) {
            throw new NotAuthorizedException();
        }
    }

    public boolean doesThisSCPBelongToASeminaryCandidate(String SCPIDInternal) {
        StudentCurricularPlan scp = FenixFramework.getDomainObject(SCPIDInternal);
        if (scp != null) {
            Collection<SeminaryCandidacy> candidacies = scp.getRegistration().getAssociatedCandidancies();
            return !candidacies.isEmpty();
        }

        return false;
    }

    protected RoleType getRoleType1() {
        return RoleType.MANAGER;
    }

    protected RoleType getRoleType2() {
        return RoleType.SEMINARIES_COORDINATOR;
    }

}
