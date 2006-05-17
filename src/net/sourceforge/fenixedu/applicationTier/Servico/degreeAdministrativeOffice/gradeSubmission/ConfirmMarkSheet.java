package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice.gradeSubmission;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.MarkSheet;

public class ConfirmMarkSheet extends Service {
	
	public void run(MarkSheet markSheet, Employee employee) {
		markSheet.confirm(employee);
	}

}
