package net.sourceforge.fenixedu.applicationTier.Servico.fileManager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentItem;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.fileSupport.JdbcMysqlFileSupport;

import org.apache.slide.common.SlideException;

import pt.utl.ist.berserk.logic.serviceManager.IService;

public class DeleteItemFile implements IService {

	public void run(Integer itemId, String fileName) throws FenixServiceException, ExcepcaoPersistencia,
			SlideException {
		final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		final IPersistentItem persistentItem = sp.getIPersistentItem();
		final Item item = (Item) persistentItem.readByOID(Item.class, itemId);
        final String slidename = item.getSlideName();
        JdbcMysqlFileSupport.deleteFile(slidename, fileName);
	}
}