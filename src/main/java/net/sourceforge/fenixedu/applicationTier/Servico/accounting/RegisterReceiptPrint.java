package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Receipt;
import pt.ist.fenixframework.Atomic;

public class RegisterReceiptPrint {

    @Atomic
    public static void run(final Receipt receipt, final Person person) {
        receipt.registerReceiptPrint(person);
    }
}