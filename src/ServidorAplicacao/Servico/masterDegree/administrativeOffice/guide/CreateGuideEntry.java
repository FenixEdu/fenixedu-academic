package ServidorAplicacao.Servico.masterDegree.administrativeOffice.guide;

import java.util.Iterator;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.Guide;
import Dominio.GuideEntry;
import Dominio.IGuide;
import Dominio.IGuideEntry;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.DocumentType;
import Util.GraduationType;
import Util.NumberUtils;

/**
 * 
 * @author <a href="mailto:shezad@ist.utl.pt">Shezad Anavarali </a>
 * 
 */
public class CreateGuideEntry implements IService {

    public void run(Integer guideID, GraduationType graduationType, DocumentType documentType,
            String description, Double price, Integer quantity) throws FenixServiceException,
            ExcepcaoPersistencia {

        ISuportePersistente sp = SuportePersistenteOJB.getInstance();

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