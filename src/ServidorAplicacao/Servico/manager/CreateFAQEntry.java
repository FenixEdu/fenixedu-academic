/*
 * Created on 2004/08/30
 * 
 */
package ServidorAplicacao.Servico.manager;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.support.InfoFAQEntry;
import Dominio.support.FAQEntry;
import Dominio.support.FAQSection;
import Dominio.support.IFAQEntry;
import Dominio.support.IFAQSection;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Luis Cruz
 */
public class CreateFAQEntry implements IService {

    public void run(InfoFAQEntry infoFAQEntry) throws FenixServiceException, ExcepcaoPersistencia {
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