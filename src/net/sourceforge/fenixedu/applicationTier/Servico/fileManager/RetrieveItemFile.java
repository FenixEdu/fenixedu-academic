/*
 * Created on 17/Set/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.fileManager;

import org.apache.slide.common.SlideException;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import net.sourceforge.fenixedu.domain.IItem;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentItem;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.fileSuport.FileSuport;
import net.sourceforge.fenixedu.fileSuport.FileSuportObject;
import net.sourceforge.fenixedu.fileSuport.IFileSuport;

/**
 * fenix-head ServidorAplicacao.Servico.fileManager
 * 
 * @author João Mota 17/Set/2003
 *  
 */
public class RetrieveItemFile implements IService {

    public RetrieveItemFile() {

    }

    public FileSuportObject run(Integer itemId, String fileName) throws FenixServiceException {
        FileSuportObject file = null;
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentItem persistentItem = sp.getIPersistentItem();
            IItem item = new Item(itemId);
            item = (IItem) persistentItem.readByOID(Item.class, itemId);
            IFileSuport fileSuport = FileSuport.getInstance();

            file = fileSuport.retrieveFile(item.getSlideName() + "/" + fileName);

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        } catch (SlideException e) {
            throw new FenixServiceException(e);
        }
        return file;

    }
}