package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Entry;
import net.sourceforge.fenixedu.domain.accounting.Receipt;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class CreateReceipt {

    @Checked("AcademicPredicates.MANAGE_STUDENT_PAYMENTS")
    @Service
    public static Receipt run(final Person responsible, final Person person, final Party contributor,
	    final String contributorName, final Integer year, final List<Entry> entries) {

	return Receipt.createWithContributorPartyOrContributorName(responsible, person, contributor, contributorName, year,
		entries);
    }

}