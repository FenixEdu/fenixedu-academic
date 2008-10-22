/*
 * Created on 2004/08/30
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.support.InfoFAQSection;
import net.sourceforge.fenixedu.domain.support.FAQSection;

/**
 * @author Luis Cruz
 */
public class CreateFAQSection extends FenixService {

    public void run(InfoFAQSection infoFAQSection) {
	FAQSection parentFAQSection = null;
	if (infoFAQSection.getParentSection() != null && infoFAQSection.getParentSection().getIdInternal() != null) {
	    parentFAQSection = rootDomainObject.readFAQSectionByOID(infoFAQSection.getParentSection().getIdInternal());
	}

	FAQSection faqSection = new FAQSection();
	faqSection.setSectionName(infoFAQSection.getSectionName());
	faqSection.setParentSection(parentFAQSection);
    }

}