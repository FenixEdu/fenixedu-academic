package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.research.Prize;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

public class DeletePrizeFilter extends Filtro {

	@Override
	public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
		IUserView userView = getRemoteUser(request);
		Person person = userView.getPerson();

		Prize prize = null;
		if (request.getServiceParameters().getParameter(0) instanceof Prize) {
			prize = (Prize) request.getServiceParameters().getParameter(0);
		}
		if (prize != null) {
			if (!prize.isDeletableByUser(person)) {
				throw new NotAuthorizedFilterException("error.not.authorized");
			}
		} else {
			throw new NotAuthorizedFilterException("error.not.authorized");
		}
	}

}
