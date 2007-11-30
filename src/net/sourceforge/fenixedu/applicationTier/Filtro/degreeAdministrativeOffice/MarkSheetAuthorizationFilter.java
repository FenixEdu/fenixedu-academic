package net.sourceforge.fenixedu.applicationTier.Filtro.degreeAdministrativeOffice;

import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.Filtro;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

public abstract class MarkSheetAuthorizationFilter extends Filtro {

    @Override
    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
	IUserView userView = getRemoteUser(request);
	if (userView.getPerson().getEmployee() == null
		|| !getAuthorizedEmployees().contains(userView.getPerson().getEmployee().getEmployeeNumber().toString())) {
	    throw new NotAuthorizedFilterException("not.authorized");
	}
    }

    public abstract Set<String> getAuthorizedEmployees();
}
