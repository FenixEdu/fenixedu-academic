package net.sourceforge.fenixedu.applicationTier.Filtro;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Seminaries.SeminaryCandidacy;
import net.sourceforge.fenixedu.domain.person.RoleType;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

public class ManagerOrSeminariesCoordinatorFilter extends Filtro {

    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
        IUserView id = getRemoteUser(request);
        Integer SCPIDINternal = (Integer) request.getServiceParameters().getParameter(1);
        
        boolean seminaryCandidate = false;
        if(SCPIDINternal != null){
            seminaryCandidate = this.doesThisSCPBelongToASeminaryCandidate(SCPIDINternal);
        }
                
        if (((id != null && id.getRoleTypes() != null
                && !id.hasRoleType(getRoleType1()) && !(id.hasRoleType(getRoleType2()) && seminaryCandidate)))
                || (id == null) || (id.getRoleTypes() == null)) {
            throw new NotAuthorizedFilterException();
        }
    }
    
    public boolean doesThisSCPBelongToASeminaryCandidate(Integer SCPIDInternal) {
        StudentCurricularPlan scp = rootDomainObject.readStudentCurricularPlanByOID(SCPIDInternal);
        if (scp != null) {
            List<SeminaryCandidacy> candidacies = scp.getStudent().getAssociatedCandidancies();
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
