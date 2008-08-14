package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.support.FAQSection;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeleteFAQSection extends Service {

    public void run(Integer faqSectionId) throws FenixServiceException {
	FAQSection faqSection = rootDomainObject.readFAQSectionByOID(faqSectionId);
	if (faqSection != null) {
	    faqSection.delete();
	}
    }

}
