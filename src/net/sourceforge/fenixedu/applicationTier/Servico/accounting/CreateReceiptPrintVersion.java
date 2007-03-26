package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.accounting.Receipt;
import net.sourceforge.fenixedu.domain.accounting.ReceiptPrintVersion;

public class CreateReceiptPrintVersion extends Service {

    public ReceiptPrintVersion run(final Receipt receipt, final Employee employee) throws FenixServiceException {
        
        if (receipt == null) {
            throw new InvalidArgumentsServiceException("error.masterDegreeAdministrativeOffice.payments.receipt.not.found");
        }
        return receipt.createReceiptPrintVersion(employee);
    }
}