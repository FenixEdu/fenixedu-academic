/*
 * Created on 2004/08/30
 * 
 */
package ServidorAplicacao.Servico.manager;

import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.support.FAQEntry;
import Dominio.support.FAQSection;
import Dominio.support.IFAQEntry;
import Dominio.support.IFAQSection;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentFAQEntries;
import ServidorPersistente.IPersistentFAQSection;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Luis Cruz
 */
public class DeleteFAQSection implements IService {

    public void run(Integer sectionId) throws FenixServiceException, ExcepcaoPersistencia {
        ISuportePersistente sp = SuportePersistenteOJB.getInstance();
        IPersistentObject dao = sp.getIPersistentObject();
        IPersistentFAQEntries persistentFAQEntries = sp.getIPersistentFAQEntries();
        IPersistentFAQSection persistentFAQSection = sp.getIPersistentFAQSection();

        List entriesToDelete = persistentFAQEntries.readEntriesInSection(sectionId);
        for (int i = 0; i < entriesToDelete.size(); i++) {
            IFAQEntry faqEntry = (IFAQEntry) entriesToDelete.get(i);
            dao.deleteByOID(FAQEntry.class, faqEntry.getIdInternal());
        }

        List subSectionsToDelete = persistentFAQSection.readSubSectionsInSection(sectionId);
        for (int i = 0; i < subSectionsToDelete.size(); i++) {
            IFAQSection subSction = (IFAQSection) subSectionsToDelete.get(i);
            run(subSction.getIdInternal());
        }

        dao.deleteByOID(FAQSection.class, sectionId);
    }

}