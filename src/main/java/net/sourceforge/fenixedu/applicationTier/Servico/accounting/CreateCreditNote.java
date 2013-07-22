package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import net.sourceforge.fenixedu.dataTransferObject.accounting.CreateCreditNoteBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.CreditNote;
import net.sourceforge.fenixedu.domain.accounting.PaymentMode;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.AcademicPredicates;
import pt.ist.fenixframework.Atomic;

public class CreateCreditNote {

    @Atomic
    public static CreditNote run(final Person responsible, final CreateCreditNoteBean createCreditNoteBean) {
        check(AcademicPredicates.MANAGE_STUDENT_PAYMENTS);
        return createCreditNoteBean.getReceipt().createCreditNote(responsible, PaymentMode.CASH,
                createCreditNoteBean.getSelectedEntries());
    }

}