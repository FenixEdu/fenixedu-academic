package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.accounting.CreditNote;
import net.sourceforge.fenixedu.domain.accounting.CreditNoteState;
import net.sourceforge.fenixedu.domain.accounting.PaymentMode;

public class ChangeCreditNoteState extends Service {

    public ChangeCreditNoteState() {
	super();
    }

    public void run(final Employee employee, final CreditNote creditNote, final CreditNoteState state) {
	creditNote.changeState(employee, PaymentMode.CASH, state);
    }

}