/*
 * Created on May 5, 2006
 */
package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.gradeSubmission;

import net.sourceforge.fenixedu.domain.MarkSheet;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.ist.fenixframework.Atomic;

public class DeleteMarkSheet {

    @Atomic
    public static void run(MarkSheet markSheet) {
        if (markSheet == null) {
            throw new DomainException("error.noMarkSheet");
        }
        markSheet.delete();
    }
}
