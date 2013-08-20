/*
 * Created on May 5, 2006
 */
package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.gradeSubmission;

import net.sourceforge.fenixedu.domain.MarkSheet;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteMarkSheet {

    @Service
    public static void run(MarkSheet markSheet) {
        if (markSheet == null) {
            throw new DomainException("error.noMarkSheet");
        }
        markSheet.delete();
    }
}
