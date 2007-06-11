package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.PersistentGroupMembers;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

public class ManageUnitPersistentGroup extends Filtro {

	@Override
	public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
		IUserView userView = getRemoteUser(request);
		Person person = userView.getPerson();

		PersistentGroupMembers group = null;
		if (request.getServiceParameters().getParameter(0) instanceof PersistentGroupMembers) {
			group = (PersistentGroupMembers) request.getServiceParameters().getParameter(0);
		}

		Unit unit = (group == null) ? (Unit) request.getServiceParameters().getParameter(0) : group
				.getUnit();

		if (!(unit.getSite() != null &&  unit.getSite().hasManagers(person))) {
			throw new NotAuthorizedFilterException("error.person.not.manager.of.site");
		}

	}

}
