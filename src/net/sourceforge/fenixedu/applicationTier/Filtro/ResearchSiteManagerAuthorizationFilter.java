package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.UnitSite;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

public class ResearchSiteManagerAuthorizationFilter extends Filtro {

    @Override
    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
	IUserView userView = getRemoteUser(request);
	Person person = userView.getPerson();

	UnitSite site= (UnitSite) request.getServiceParameters().getParameter(0);

	if (!site.hasManagers(person)) {
	    throw new NotAuthorizedFilterException("error.person.not.manager.of.site");
	}
    }

}
