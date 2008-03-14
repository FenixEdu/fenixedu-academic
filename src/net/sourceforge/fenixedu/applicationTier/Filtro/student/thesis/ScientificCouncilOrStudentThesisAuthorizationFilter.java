package net.sourceforge.fenixedu.applicationTier.Filtro.student.thesis;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.person.RoleType;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

public class ScientificCouncilOrStudentThesisAuthorizationFilter extends StudentThesisAuthorizationFilter {

    @Override
    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
	final IUserView userView = getRemoteUser(request);
	if (!userView.hasRoleType(RoleType.SCIENTIFIC_COUNCIL)) {
	    super.execute(request, response);
	}
    }

}
