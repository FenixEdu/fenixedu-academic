package net.sourceforge.fenixedu.applicationTier.Filtro.coordinator;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.AuthorizationByRoleFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

public class ScientificComissionMemberAuthorizationFilter extends AuthorizationByRoleFilter {

    @Override
    protected RoleType getRoleType() {
        return RoleType.COORDINATOR;
    }

    @Override
    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
        super.execute(request, response);
        
        IUserView userView = getRemoteUser(request);
        Person person = userView.getPerson();
        
        // first argument must be a degree curricularPlan
        DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) request.getServiceParameters().getParameter(0);
        
        if (!degreeCurricularPlan.getDegree().isMemberOfCurrentScientificCommission(person)) {
            throw new NotAuthorizedException("degree.scientificCommission.notMember");
        }
    }
    
}
