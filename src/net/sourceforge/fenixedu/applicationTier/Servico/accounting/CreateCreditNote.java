package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.accounting.CreateCreditNoteBean;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.accounting.CreditNote;
import net.sourceforge.fenixedu.domain.accounting.PaymentMode;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class CreateCreditNote extends FenixService {

    @Checked("RolePredicates.ACADEMIC_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Service
    public static CreditNote run(final Employee employee, final CreateCreditNoteBean createCreditNoteBean) {
	return createCreditNoteBean.getReceipt().createCreditNote(employee, PaymentMode.CASH,
		createCreditNoteBean.getSelectedEntries());
    }

}