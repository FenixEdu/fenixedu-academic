package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.CreditNote;
import net.sourceforge.fenixedu.domain.accounting.CreditNoteState;
import net.sourceforge.fenixedu.domain.accounting.PaymentMode;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.AcademicPredicates;
import pt.ist.fenixframework.Atomic;

public class ChangeCreditNoteState {

    @Atomic
    public static void run(final Person responsible, final CreditNote creditNote, final CreditNoteState state) {
        check(AcademicPredicates.MANAGE_STUDENT_PAYMENTS);
        creditNote.changeState(responsible, PaymentMode.CASH, state);
    }

}