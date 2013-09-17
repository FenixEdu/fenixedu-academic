package net.sourceforge.fenixedu.applicationTier.Filtro;

import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Seminaries.SeminaryCandidacy;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixframework.FenixFramework;

public class SeminariesCoordinatorFilter {

    public static final SeminariesCoordinatorFilter instance = new SeminariesCoordinatorFilter();

    public void execute(String executionDegreeCode, String studentCurricularPlanID) throws NotAuthorizedException {
        IUserView id = AccessControl.getUserView();
        String SCPIDINternal = studentCurricularPlanID;

        boolean seminaryCandidate = false;
        if (SCPIDINternal != null) {
            seminaryCandidate = this.doesThisSCPBelongToASeminaryCandidate(SCPIDINternal);
        }

        if (((id != null && id.getRoleTypes() != null && !(id.hasRoleType(RoleType.SEMINARIES_COORDINATOR) && seminaryCandidate)))
                || (id == null) || (id.getRoleTypes() == null)) {
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
