/*
 * Created on 2004/08/30
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.dataTransferObject.support.InfoFAQEntry;
import net.sourceforge.fenixedu.domain.support.FAQEntry;
import net.sourceforge.fenixedu.domain.support.FAQSection;
import net.sourceforge.fenixedu.domain.support.IFAQEntry;
import net.sourceforge.fenixedu.domain.support.IFAQSection;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Luis Cruz
 */
public class CreateFAQEntry implements IService {

    public void run(InfoFAQEntry infoFAQEntry) throws ExcepcaoPersistencia {
        ISuportePersistente sp = SuportePersistenteOJB.getInstance();
        IPersistentObject dao = sp.getIPersistentObject();

        IFAQSection parentFAQSection = null;
        if (infoFAQEntry.getParentSection() != null
                && infoFAQEntry.getParentSection().getIdInternal() != null) {
            parentFAQSection = (IFAQSection) dao.readByOID(FAQSection.class, infoFAQEntry
                    .getParentSection().getIdInternal());
        }

        IFAQEntry faqEntry = new FAQEntry();
        sp.getIPersistentObject().simpleLockWrite(faqEntry);
        faqEntry.setParentSection(parentFAQSection);
        faqEntry.setQuestion(infoFAQEntry.getQuestion());
        faqEntry.setAnswer(infoFAQEntry.getAnswer());
    }

}