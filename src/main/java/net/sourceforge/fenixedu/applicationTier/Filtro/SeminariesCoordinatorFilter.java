package net.sourceforge.fenixedu.applicationTier.Filtro;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Seminaries.SeminaryCandidacy;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

public class SeminariesCoordinatorFilter extends Filtro {

    @Override
    public void execute(ServiceRequest request) throws Exception {
        IUserView id = AccessControl.getUserView();
        Integer SCPIDINternal = (Integer) request.getServiceParameters().getParameter(1);

        boolean seminaryCandidate = false;
        if (SCPIDINternal != null) {
            seminaryCandidate = this.doesThisSCPBelongToASeminaryCandidate(SCPIDINternal);
        }

        if (((id != null && id.getRoleTypes() != null && !(id.hasRoleType(RoleType.SEMINARIES_COORDINATOR) && seminaryCandidate)))
                || (id == null) || (id.getRoleTypes() == null)) {
            throw new NotAuthorizedFilterException();
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
