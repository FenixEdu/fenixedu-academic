/*
 * Created on 17/Set/2003
 *
 */
package ServidorAplicacao.Servico.fileManager;

import org.apache.slide.common.SlideException;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.IItem;
import Dominio.Item;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentItem;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import fileSuport.FileSuport;
import fileSuport.IFileSuport;

/**
 * fenix-head ServidorAplicacao.Servico.fileManager
 * 
 * @author João Mota 17/Set/2003
 *  
 */
public class DeleteItemFile implements IService {

    public void run(Integer itemId, String fileName) throws FenixServiceException {

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentItem persistentItem = sp.getIPersistentItem();
            IItem item = (IItem) persistentItem.readByOID(Item.class, itemId);
            IFileSuport fileSuport = FileSuport.getInstance();
            try {
                fileSuport.beginTransaction();
            } catch (Exception e1) {
                throw new FenixServiceException(e1);
            }
            fileSuport.deleteFile(item.getSlideName() + "/" + fileName);
            try {
                fileSuport.commitTransaction();
            } catch (Exception e2) {
                try {
                    fileSuport.abortTransaction();
                    throw new FenixServiceException(e2);
                } catch (Exception e3) {
                    throw new FenixServiceException(e3);
                }
            }
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        } catch (SlideException e) {
            throw new FenixServiceException(e);
        }

    }
}