/*
 * Created on 2004/08/30
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.dataTransferObject.support.InfoFAQSection;
import net.sourceforge.fenixedu.domain.support.FAQSection;
import net.sourceforge.fenixedu.domain.support.IFAQSection;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Luis Cruz
 */
public class CreateFAQSection implements IService {

    public void run(InfoFAQSection infoFAQSection) throws FenixServiceException, ExcepcaoPersistencia {
        ISuportePersistente sp = SuportePersistenteOJB.getInstance();
        IPersistentObject dao = sp.getIPersistentObject();

        IFAQSection parentFAQSection = null;
        if (infoFAQSection.getParentSection() != null
                && infoFAQSection.getParentSection().getIdInternal() != null) {
            parentFAQSection = (IFAQSection) dao.readByOID(FAQSection.class, infoFAQSection
                    .getParentSection().getIdInternal());
        }

        IFAQSection faqSection = new FAQSection();
        sp.getIPersistentObject().simpleLockWrite(faqSection);
        faqSection.setSectionName(infoFAQSection.getSectionName());
        faqSection.setParentSection(parentFAQSection);
    }

}