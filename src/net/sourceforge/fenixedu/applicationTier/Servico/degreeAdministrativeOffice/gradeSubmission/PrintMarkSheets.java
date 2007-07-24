package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice.gradeSubmission;

import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.MarkSheet;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public class PrintMarkSheets extends AbstractPrintMarkSheet {

    public void run(final Collection<MarkSheet> markSheets, final String printerName) throws FenixServiceException {
        for (final MarkSheet markSheet : markSheets) {
            final Employee employee = getEmployee();
            if (markSheet.canManage(employee)) {
                try {
                    print(markSheet, printerName);
                } catch (InvalidArgumentsServiceException e) {

                }
            }
        }
    }

    public Employee getEmployee() {
        return AccessControl.getPerson().getEmployee();
    }

}
