/*
 * Created on 2004/08/30
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.support.FAQEntry;
import net.sourceforge.fenixedu.domain.support.FAQSection;
import net.sourceforge.fenixedu.domain.support.FAQEntry;
import net.sourceforge.fenixedu.domain.support.FAQSection;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentFAQEntries;
import net.sourceforge.fenixedu.persistenceTier.IPersistentFAQSection;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Luis Cruz
 */
public class DeleteFAQSection implements IService {

    public void run(Integer sectionId) throws FenixServiceException, ExcepcaoPersistencia {
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentObject dao = sp.getIPersistentObject();
        IPersistentFAQEntries persistentFAQEntries = sp.getIPersistentFAQEntries();
        IPersistentFAQSection persistentFAQSection = sp.getIPersistentFAQSection();

        List entriesToDelete = persistentFAQEntries.readEntriesInSection(sectionId);
        for (int i = 0; i < entriesToDelete.size(); i++) {
            FAQEntry faqEntry = (FAQEntry) entriesToDelete.get(i);
            dao.deleteByOID(FAQEntry.class, faqEntry.getIdInternal());
        }

        List subSectionsToDelete = persistentFAQSection.readSubSectionsInSection(sectionId);
        for (int i = 0; i < subSectionsToDelete.size(); i++) {
            FAQSection subSction = (FAQSection) subSectionsToDelete.get(i);
            run(subSction.getIdInternal());
        }

        dao.deleteByOID(FAQSection.class, sectionId);
    }

}