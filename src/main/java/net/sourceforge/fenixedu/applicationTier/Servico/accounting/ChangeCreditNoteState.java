package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.CreditNote;
import net.sourceforge.fenixedu.domain.accounting.CreditNoteState;
import net.sourceforge.fenixedu.domain.accounting.PaymentMode;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;

public class ChangeCreditNoteState {

    @Checked("AcademicPredicates.MANAGE_STUDENT_PAYMENTS")
    @Atomic
    public static void run(final Person responsible, final CreditNote creditNote, final CreditNoteState state) {
        creditNote.changeState(responsible, PaymentMode.CASH, state);
    }

}