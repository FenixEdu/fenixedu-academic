/*
 * Created on 2004/08/30
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.support.FAQEntry;
import net.sourceforge.fenixedu.domain.support.FAQSection;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentFAQEntries;
import net.sourceforge.fenixedu.persistenceTier.IPersistentFAQSection;

/**
 * @author Luis Cruz
 */
public class DeleteFAQSection extends Service {

    public void run(Integer sectionId) throws FenixServiceException, ExcepcaoPersistencia {
        IPersistentFAQEntries persistentFAQEntries = persistentSupport.getIPersistentFAQEntries();
        IPersistentFAQSection persistentFAQSection = persistentSupport.getIPersistentFAQSection();

        List entriesToDelete = persistentFAQEntries.readEntriesInSection(sectionId);
        for (int i = 0; i < entriesToDelete.size(); i++) {
            FAQEntry faqEntry = (FAQEntry) entriesToDelete.get(i);
            persistentObject.deleteByOID(FAQEntry.class, faqEntry.getIdInternal());
        }

        List subSectionsToDelete = persistentFAQSection.readSubSectionsInSection(sectionId);
        for (int i = 0; i < subSectionsToDelete.size(); i++) {
            FAQSection subSction = (FAQSection) subSectionsToDelete.get(i);
            run(subSction.getIdInternal());
        }

        persistentObject.deleteByOID(FAQSection.class, sectionId);
    }

}