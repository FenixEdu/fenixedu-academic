/*
 * Created on 2004/08/30
 * 
 */
package ServidorAplicacao.Servico.manager;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.support.InfoFAQSection;
import Dominio.support.FAQSection;
import Dominio.support.IFAQSection;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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