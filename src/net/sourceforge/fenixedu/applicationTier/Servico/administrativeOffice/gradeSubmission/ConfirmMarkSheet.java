package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.gradeSubmission;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.MarkSheet;

public class ConfirmMarkSheet extends FenixService {

    public void run(MarkSheet markSheet, Employee employee) throws InvalidArgumentsServiceException {
	if (markSheet == null) {
	    throw new InvalidArgumentsServiceException();
	}
	markSheet.confirm(employee);
    }

}
