/*
 * Created on May 5, 2006
 */
package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.gradeSubmission;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.MarkSheet;

public class DeleteMarkSheet extends Service {

    public void run(Integer markSheetID) throws FenixServiceException {
	MarkSheet markSheet = rootDomainObject.readMarkSheetByOID(markSheetID);
	if (markSheet == null) {
	    throw new FenixServiceException("error.noMarkSheet");
	}
	markSheet.delete();
    }
}
