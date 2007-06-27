package net.sourceforge.fenixedu.applicationTier.Filtro.spaceManager;

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.Filtro;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.person.RoleType;

public class SpaceAdministratorAuthorizationFilter extends Filtro {

    @Override
    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
	IUserView userView = getRemoteUser(request);
	if ((userView != null && userView.getRoleTypes() != null 
		&& (!userView.hasRoleType(RoleType.SPACE_MANAGER) 
			|| (!userView.hasRoleType(RoleType.SPACE_MANAGER_SUPER_USER) 
				&& !userView.hasRoleType(RoleType.MANAGER))))
		|| userView == null
		|| userView.getRoleTypes() == null) {
	    
	    throw new NotAuthorizedFilterException();
	}
    }

}
