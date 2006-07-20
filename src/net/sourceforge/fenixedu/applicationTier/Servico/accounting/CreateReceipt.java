package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Contributor;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Entry;
import net.sourceforge.fenixedu.domain.accounting.Receipt;

public class CreateReceipt extends Service {

    public CreateReceipt() {
        super();
    }

    public Receipt run(final Employee employee, final Person person, final Contributor contributor,
            final List<Entry> entries) {
        return new Receipt(employee, person, contributor, entries);
    }

}