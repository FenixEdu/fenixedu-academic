package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice.gradeSubmission;

import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.MarkSheet;


public class PrintMarkSheets extends AbstractPrintMarkSheet{
	
	public void run(Collection<MarkSheet> markSheets, String printerName) throws FenixServiceException {
		for (MarkSheet markSheet : markSheets) {
			try {
				print(markSheet, printerName);
			} catch(InvalidArgumentsServiceException e) {
				
			}
		}
	}
}
