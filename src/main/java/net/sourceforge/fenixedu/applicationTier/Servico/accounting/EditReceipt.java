package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Receipt;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.predicates.AcademicPredicates;
import pt.ist.fenixframework.Atomic;

public class EditReceipt {

    @Atomic
    public static void run(final Receipt receipt, final Person responsible, final Party contributorParty,
            final String contributorName) {
        check(AcademicPredicates.MANAGE_STUDENT_PAYMENTS);
        receipt.edit(responsible, contributorParty, contributorName);
    }

}