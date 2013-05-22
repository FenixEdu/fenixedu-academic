package net.sourceforge.fenixedu.applicationTier.Filtro;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Seminaries.SeminaryCandidacy;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public class SeminariesCoordinatorFilter {

    public static final SeminariesCoordinatorFilter instance = new SeminariesCoordinatorFilter();

    public void execute(Integer executionDegreeCode, Integer studentCurricularPlanID) throws NotAuthorizedException {
        IUserView id = AccessControl.getUserView();
        Integer SCPIDINternal = studentCurricularPlanID;

        boolean seminaryCandidate = false;
        if (SCPIDINternal != null) {
            seminaryCandidate = this.doesThisSCPBelongToASeminaryCandidate(SCPIDINternal);
        }

        if (((id != null && id.getRoleTypes() != null && !(id.hasRoleType(RoleType.SEMINARIES_COORDINATOR) && seminaryCandidate)))
                || (id == null) || (id.getRoleTypes() == null)) {
            throw new NotAuthorizedException();
        }
    }

    public boolean doesThisSCPBelongToASeminaryCandidate(Integer SCPIDInternal) {
        StudentCurricularPlan scp = RootDomainObject.getInstance().readStudentCurricularPlanByOID(SCPIDInternal);
        if (scp != null) {
            List<SeminaryCandidacy> candidacies = scp.getRegistration().getAssociatedCandidancies();
            return !candidacies.isEmpty();
        }

        return false;
    }
}
