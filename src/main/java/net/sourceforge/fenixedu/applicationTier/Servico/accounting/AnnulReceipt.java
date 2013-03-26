package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Receipt;
import pt.ist.fenixWebFramework.services.Service;

public class AnnulReceipt {

    @Service
    public static void run(final Person responsible, final Receipt receipt) {
        receipt.annul(responsible);
    }

}