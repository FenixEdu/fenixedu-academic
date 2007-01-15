package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice.gradeSubmission;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.MarkSheet;


public class PrintMarkSheet extends AbstractPrintMarkSheet{
	
	public void run(MarkSheet markSheet, String printerName) throws FenixServiceException {
	    print(markSheet, printerName);
	}
}
