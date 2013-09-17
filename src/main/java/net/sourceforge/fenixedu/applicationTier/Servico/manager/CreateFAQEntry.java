/*
 * Created on 2004/08/30
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;


import net.sourceforge.fenixedu.dataTransferObject.support.InfoFAQEntry;
import net.sourceforge.fenixedu.domain.support.FAQEntry;
import net.sourceforge.fenixedu.domain.support.FAQSection;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Luis Cruz
 */
public class CreateFAQEntry {

    @Atomic
    public static void run(InfoFAQEntry infoFAQEntry) {
        check(RolePredicates.MANAGER_PREDICATE);
        FAQSection parentFAQSection = null;
        if (infoFAQEntry.getParentSection() != null && infoFAQEntry.getParentSection().getExternalId() != null) {
            parentFAQSection = FenixFramework.getDomainObject(infoFAQEntry.getParentSection().getExternalId());
        }

        FAQEntry faqEntry = new FAQEntry();
        faqEntry.setParentSection(parentFAQSection);
        faqEntry.setQuestion(infoFAQEntry.getQuestion());
        faqEntry.setAnswer(infoFAQEntry.getAnswer());
    }

}