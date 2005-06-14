/*
 * Created on 17/Set/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.fileManager;

import net.sourceforge.fenixedu.domain.IItem;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.fileSuport.FileSuport;
import net.sourceforge.fenixedu.fileSuport.FileSuportObject;
import net.sourceforge.fenixedu.fileSuport.IFileSuport;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentItem;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.slide.common.SlideException;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * fenix-head ServidorAplicacao.Servico.fileManager
 * 
 * @author João Mota 17/Set/2003
 * 
 */
public class RetrieveItemFile implements IService {

    public FileSuportObject run(Integer itemId, String fileName) throws ExcepcaoPersistencia,
            SlideException {
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentItem persistentItem = sp.getIPersistentItem();
        final IItem item = (IItem) persistentItem.readByOID(Item.class, itemId);
        final IFileSuport fileSuport = FileSuport.getInstance();

        return fileSuport.retrieveFile(item.getSlideName() + "/" + fileName);
    }
}