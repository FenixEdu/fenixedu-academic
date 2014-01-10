package net.sourceforge.fenixedu.applicationTier.Filtro;

import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Seminaries.SeminaryCandidacy;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixframework.FenixFramework;

public class SeminariesCoordinatorFilter {

    public static final SeminariesCoordinatorFilter instance = new SeminariesCoordinatorFilter();

    public void execute(String executionDegreeCode, String studentCurricularPlanID) throws NotAuthorizedException {
        User id = Authenticate.getUser();
        String SCPIDINternal = studentCurricularPlanID;

        boolean seminaryCandidate = false;
        if (SCPIDINternal != null) {
            seminaryCandidate = this.doesThisSCPBelongToASeminaryCandidate(SCPIDINternal);
        }

        if (((id != null && id.getPerson().getPersonRolesSet() != null && !(id.getPerson().hasRole(
                RoleType.SEMINARIES_COORDINATOR) && seminaryCandidate)))
                || (id == null) || (id.getPerson().getPersonRolesSet() == null)) {
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
}
