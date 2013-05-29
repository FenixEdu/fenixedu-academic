/*
 * Created on May 5, 2006
 */
package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.gradeSubmission;

import net.sourceforge.fenixedu.domain.MarkSheet;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class DeleteMarkSheet {

    @Service
    public static void run(String markSheetID) {
        MarkSheet markSheet = AbstractDomainObject.fromExternalId(markSheetID);
        if (markSheet == null) {
            throw new DomainException("error.noMarkSheet");
        }
        markSheet.delete();
    }
}
