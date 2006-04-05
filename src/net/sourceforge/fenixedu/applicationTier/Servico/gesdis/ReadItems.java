package net.sourceforge.fenixedu.applicationTier.Servico.gesdis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoItem;
import net.sourceforge.fenixedu.dataTransferObject.InfoSection;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadItems extends Service {

    public List run(InfoSection infoSection) throws ExcepcaoPersistencia, FenixServiceException {
        
        final Section section = rootDomainObject.readSectionByOID(infoSection.getIdInternal());
        if (section == null) {
            throw new FenixServiceException("error.noSection");
        }

        final List<InfoItem> infoItemsList = new ArrayList<InfoItem>(section.getAssociatedItemsCount());
        for (final Item elem : section.getAssociatedItems()) {
            infoItemsList.add(InfoItem.newInfoFromDomain(elem));
        }
        Collections.sort(infoItemsList);
        
        return infoItemsList;
    }

}
