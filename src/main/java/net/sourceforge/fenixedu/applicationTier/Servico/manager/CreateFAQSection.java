/*
 * Created on 2004/08/30
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.dataTransferObject.support.InfoFAQSection;
import net.sourceforge.fenixedu.domain.support.FAQSection;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Luis Cruz
 */
public class CreateFAQSection {

    @Atomic
    public static void run(InfoFAQSection infoFAQSection) {
        check(RolePredicates.MANAGER_PREDICATE);
        FAQSection parentFAQSection = null;
        if (infoFAQSection.getParentSection() != null && infoFAQSection.getParentSection().getExternalId() != null) {
            parentFAQSection = FenixFramework.getDomainObject(infoFAQSection.getParentSection().getExternalId());
        }

        FAQSection faqSection = new FAQSection();
        faqSection.setSectionName(infoFAQSection.getSectionName());
        faqSection.setParentSection(parentFAQSection);
    }

}