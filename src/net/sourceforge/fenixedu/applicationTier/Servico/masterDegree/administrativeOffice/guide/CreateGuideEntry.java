package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide;

import java.util.Iterator;

import net.sourceforge.fenixedu.domain.Guide;
import net.sourceforge.fenixedu.domain.GuideEntry;
import net.sourceforge.fenixedu.domain.IGuide;
import net.sourceforge.fenixedu.domain.IGuideEntry;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.domain.DocumentType;
import net.sourceforge.fenixedu.domain.GraduationType;
import net.sourceforge.fenixedu.util.NumberUtils;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author <a href="mailto:shezad@ist.utl.pt">Shezad Anavarali </a>
 * 
 */
public class CreateGuideEntry implements IService {

    public void run(Integer guideID, GraduationType graduationType, DocumentType documentType,
            String description, Double price, Integer quantity) throws ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IGuide guide = (IGuide) sp.getIPersistentGuide().readByOID(Guide.class, guideID, true);

        IGuideEntry guideEntry = new GuideEntry();
        sp.getIPersistentGuideEntry().simpleLockWrite(guideEntry);

        guideEntry.setDescription(description);
        guideEntry.setDocumentType(documentType);
        guideEntry.setGraduationType(graduationType);
        guideEntry.setGuide(guide);
        guideEntry.setPrice(price);
        guideEntry.setQuantity(quantity);

        // update guide's total value
        double guideTotal = price.doubleValue() * quantity.doubleValue();
        for (Iterator iter = guide.getGuideEntries().iterator(); iter.hasNext();) {
            IGuideEntry tmpEntry = (IGuideEntry) iter.next();
            guideTotal += tmpEntry.getPrice().doubleValue() * tmpEntry.getQuantity().doubleValue();
        }
        guide.setTotal(NumberUtils.formatNumber(new Double(guideTotal), 2));

        guide.getGuideEntries().add(guideEntry);
    }

}