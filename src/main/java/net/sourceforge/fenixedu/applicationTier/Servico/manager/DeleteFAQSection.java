package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.support.FAQSection;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class DeleteFAQSection {

    @Atomic
    public static void run(String faqSectionId) throws FenixServiceException {
        check(RolePredicates.MANAGER_PREDICATE);
        FAQSection faqSection = FenixFramework.getDomainObject(faqSectionId);
        if (faqSection != null) {
            faqSection.delete();
        }
    }

}