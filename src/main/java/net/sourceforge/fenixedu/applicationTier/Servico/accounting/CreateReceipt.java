package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Entry;
import net.sourceforge.fenixedu.domain.accounting.Receipt;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.AcademicPredicates;
import pt.ist.fenixframework.Atomic;

public class CreateReceipt {

    @Atomic
    public static Receipt run(final Person responsible, final Person person, final Party contributor,
            final String contributorName, final Integer year, final List<Entry> entries) {
        check(AcademicPredicates.MANAGE_STUDENT_PAYMENTS);

        return Receipt.createWithContributorPartyOrContributorName(responsible, person, contributor, contributorName, year,
                entries);
    }

}