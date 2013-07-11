/*
 * Created on 2004/08/30
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;


import net.sourceforge.fenixedu.dataTransferObject.support.InfoFAQSection;
import net.sourceforge.fenixedu.domain.support.FAQSection;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Luis Cruz
 */
public class CreateFAQSection {

    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Service
    public static void run(InfoFAQSection infoFAQSection) {
        FAQSection parentFAQSection = null;
        if (infoFAQSection.getParentSection() != null && infoFAQSection.getParentSection().getExternalId() != null) {
            parentFAQSection = FenixFramework.getDomainObject(infoFAQSection.getParentSection().getExternalId());
        }

        FAQSection faqSection = new FAQSection();
        faqSection.setSectionName(infoFAQSection.getSectionName());
        faqSection.setParentSection(parentFAQSection);
    }

}