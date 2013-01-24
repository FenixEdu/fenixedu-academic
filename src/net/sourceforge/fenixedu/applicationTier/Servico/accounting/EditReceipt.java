package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Receipt;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class EditReceipt {

    @Checked("AcademicPredicates.MANAGE_STUDENT_PAYMENTS")
    @Service
    public static void run(final Receipt receipt, final Person responsible, final Party contributorParty,
	    final String contributorName) {
	receipt.edit(responsible, contributorParty, contributorName);
    }

}