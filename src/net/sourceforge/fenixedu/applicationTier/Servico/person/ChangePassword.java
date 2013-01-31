package net.sourceforge.fenixedu.applicationTier.Servico.person;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidPasswordServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class ChangePassword extends FenixService {

	@Checked("RolePredicates.PERSON_PREDICATE")
	@Service
	public static void run(IUserView userView, String oldPassword, String newPassword) throws Exception {
		Person person = Person.readPersonByUsername(userView.getUtilizador());
		try {
			person.changePassword(oldPassword, newPassword);
		} catch (DomainException e) {
			throw new InvalidPasswordServiceException(e.getKey());
		}
	}
}