package net.sourceforge.fenixedu.applicationTier.Servico.fileManager;

import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentItem;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadItemSlideName implements IService {

    public String run(final Integer itemId) throws ExcepcaoPersistencia {
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentItem persistentItem = sp.getIPersistentItem();
        final Item item = (Item) persistentItem.readByOID(Item.class, itemId);
        return (item != null) ? item.getSlideName() : null;
    }

}