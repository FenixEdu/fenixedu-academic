package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import net.sourceforge.fenixedu.dataTransferObject.accounting.CreateCreditNoteBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.CreditNote;
import net.sourceforge.fenixedu.domain.accounting.PaymentMode;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class CreateCreditNote {

	@Checked("AcademicPredicates.MANAGE_STUDENT_PAYMENTS")
	@Service
	public static CreditNote run(final Person responsible, final CreateCreditNoteBean createCreditNoteBean) {
		return createCreditNoteBean.getReceipt().createCreditNote(responsible, PaymentMode.CASH,
				createCreditNoteBean.getSelectedEntries());
	}

}