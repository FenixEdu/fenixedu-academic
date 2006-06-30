package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Contributor;
import net.sourceforge.fenixedu.domain.accounting.Entry;
import net.sourceforge.fenixedu.domain.accounting.Receipt;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;

public class CreateReceipt extends Service {

    public CreateReceipt() {
        super();
    }

    public Receipt run(final Party party, final Contributor contributor, final List<Entry> entries) {
        return new Receipt(party, contributor, entries);
    }

}