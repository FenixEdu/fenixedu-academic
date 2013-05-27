/*
 * Created on 2004/08/30
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;


import net.sourceforge.fenixedu.dataTransferObject.support.InfoFAQEntry;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.support.FAQEntry;
import net.sourceforge.fenixedu.domain.support.FAQSection;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Luis Cruz
 */
public class CreateFAQEntry {

    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Service
    public static void run(InfoFAQEntry infoFAQEntry) {
        FAQSection parentFAQSection = null;
        if (infoFAQEntry.getParentSection() != null && infoFAQEntry.getParentSection().getIdInternal() != null) {
            parentFAQSection = RootDomainObject.getInstance().readFAQSectionByOID(infoFAQEntry.getParentSection().getIdInternal());
        }

        FAQEntry faqEntry = new FAQEntry();
        faqEntry.setParentSection(parentFAQSection);
        faqEntry.setQuestion(infoFAQEntry.getQuestion());
        faqEntry.setAnswer(infoFAQEntry.getAnswer());
    }

}