package net.sourceforge.fenixedu.applicationTier.Servico.fileManager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadItemSlideName extends Service {

    public String run(final Integer itemId) throws ExcepcaoPersistencia {
        final Item item = rootDomainObject.readItemByOID(itemId);
        return (item != null) ? item.getSlideName() : null;
    }

}
