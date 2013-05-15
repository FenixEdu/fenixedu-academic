package net.sourceforge.fenixedu.applicationTier.Filtro.student.thesis;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.utl.ist.berserk.ServiceRequest;

public class ScientificCouncilOrStudentThesisAuthorizationFilter extends StudentThesisAuthorizationFilter {

    @Override
    public void execute(ServiceRequest request) throws Exception {
        final IUserView userView = AccessControl.getUserView();
        if (!userView.hasRoleType(RoleType.SCIENTIFIC_COUNCIL)) {
            super.execute(request);
        }
    }

}
