/*
 * Created on 2004/08/30
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;


import net.sourceforge.fenixedu.dataTransferObject.support.InfoFAQSection;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.support.FAQSection;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Luis Cruz
 */
public class CreateFAQSection {

    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Service
    public static void run(InfoFAQSection infoFAQSection) {
        FAQSection parentFAQSection = null;
        if (infoFAQSection.getParentSection() != null && infoFAQSection.getParentSection().getIdInternal() != null) {
            parentFAQSection = RootDomainObject.getInstance().readFAQSectionByOID(infoFAQSection.getParentSection().getIdInternal());
        }

        FAQSection faqSection = new FAQSection();
        faqSection.setSectionName(infoFAQSection.getSectionName());
        faqSection.setParentSection(parentFAQSection);
    }

}