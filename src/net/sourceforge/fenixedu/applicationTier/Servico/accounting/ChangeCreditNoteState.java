package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.accounting.CreditNote;
import net.sourceforge.fenixedu.domain.accounting.CreditNoteState;
import net.sourceforge.fenixedu.domain.accounting.PaymentMode;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class ChangeCreditNoteState extends FenixService {

    @Checked("RolePredicates.ACADEMIC_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Service
    public static void run(final Employee employee, final CreditNote creditNote, final CreditNoteState state) {
	creditNote.changeState(employee, PaymentMode.CASH, state);
    }

}