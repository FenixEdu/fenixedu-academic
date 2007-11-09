package net.sourceforge.fenixedu.applicationTier.Filtro.spaceManager;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.Filtro;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

public class SpaceAdministratorAuthorizationFilter extends Filtro {

    @Override
    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
	IUserView userView = getRemoteUser(request);		
	if (userView != null) {	    
	    Person person = userView.getPerson();
	    if(!AllocatableSpace.personIsSpacesAdministrator(person)) {
		throw new NotAuthorizedFilterException();
	    }
	} else {
	    throw new NotAuthorizedFilterException();
	}
    }

}
