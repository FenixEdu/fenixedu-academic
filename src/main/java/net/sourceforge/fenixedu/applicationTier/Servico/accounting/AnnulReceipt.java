package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Receipt;
import pt.ist.fenixframework.Atomic;

public class AnnulReceipt {

    @Atomic
    public static void run(final Person responsible, final Receipt receipt) {
        receipt.annul(responsible);
    }

}