package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.accounting.Receipt;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;

public class EditReceipt extends FenixService {

    public EditReceipt() {
	super();
    }

    public void run(final Receipt receipt, final Employee employee, final Party contributorParty, final String contributorName) {
	receipt.edit(employee, contributorParty, contributorName);
    }

}