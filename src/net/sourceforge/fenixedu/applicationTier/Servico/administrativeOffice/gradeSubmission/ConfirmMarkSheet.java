package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.gradeSubmission;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.MarkSheet;
import net.sourceforge.fenixedu.domain.Person;
import pt.ist.fenixWebFramework.services.Service;

public class ConfirmMarkSheet {

	@Service
	public static void run(MarkSheet markSheet, Person person) throws InvalidArgumentsServiceException {
		if (markSheet == null) {
			throw new InvalidArgumentsServiceException();
		}
		markSheet.confirm(person);
	}

}
