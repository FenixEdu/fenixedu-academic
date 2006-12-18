package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.accounting.CreateCreditNoteBean;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.accounting.CreditNote;
import net.sourceforge.fenixedu.domain.accounting.PaymentMode;

public class CreateCreditNote extends Service {

    public CreateCreditNote() {
	super();
    }

    public CreditNote run(final Employee employee, final CreateCreditNoteBean createCreditNoteBean) {
	return createCreditNoteBean.getReceipt().createCreditNote(employee, PaymentMode.CASH,
		createCreditNoteBean.getSelectedEntries());
    }
   
}