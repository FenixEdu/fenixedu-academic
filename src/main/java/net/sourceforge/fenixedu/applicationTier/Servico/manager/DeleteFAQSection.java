package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.support.FAQSection;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.FenixFramework;

public class DeleteFAQSection {

    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Service
    public static void run(String faqSectionId) throws FenixServiceException {
        FAQSection faqSection = FenixFramework.getDomainObject(faqSectionId);
        if (faqSection != null) {
            faqSection.delete();
        }
    }

}