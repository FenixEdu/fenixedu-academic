package net.sourceforge.fenixedu.applicationTier.Filtro.degreeAdministrativeOffice;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.Filtro;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.Employee;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

public abstract class MarkSheetAuthorizationFilter extends Filtro {

    @Override
    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
	final IUserView userView = getRemoteUser(request);
	if (!userView.getPerson().hasEmployee() || !isAuthorized(userView.getPerson().getEmployee())) {
	    throw new NotAuthorizedFilterException("not.authorized");
	}
    }

    abstract public boolean isAuthorized(final Employee employee);
}
