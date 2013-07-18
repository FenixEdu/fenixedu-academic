package net.sourceforge.fenixedu.applicationTier.Servico.manager;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.support.FAQSection;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteFAQSection {

    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Service
    public static void run(Integer faqSectionId) throws FenixServiceException {
        FAQSection faqSection = RootDomainObject.getInstance().readFAQSectionByOID(faqSectionId);
        if (faqSection != null) {
            faqSection.delete();
        }
    }

}