/*
 * Created on 17/Set/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.fileManager;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import net.sourceforge.fenixedu.domain.IItem;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FileAlreadyExistsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FileNameTooLongServiceException;
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
public class StoreItemFile implements IService {

    public StoreItemFile() {
    }

    public Boolean run(FileSuportObject file, Integer itemId) throws FenixServiceException {
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentItem persistentItem = sp.getIPersistentItem();
            IItem item = (IItem) persistentItem.readByOID(Item.class, itemId);
            file.setUri(item.getSlideName());
            file.setRootUri(item.getSection().getSite().getExecutionCourse().getSlideName());
            IFileSuport fileSuport = FileSuport.getInstance();
            try {
                fileSuport.beginTransaction();
            } catch (Exception e) {
                throw new FenixServiceException(e);
            }
            try {
                if (!fileSuport.isFileNameValid(file)) {
                    try {
                        fileSuport.abortTransaction();
                    } catch (Exception e1) {
                        throw new FenixServiceException(e1);
                    }
                    throw new FileNameTooLongServiceException();
                }
                if (fileSuport.isStorageAllowed(file)) {
                    boolean result = fileSuport.storeFile(file);
                    if (!result) {
                        try {
                            fileSuport.abortTransaction();
                        } catch (Exception e1) {
                            throw new FenixServiceException(e1);
                        }
                        throw new FileAlreadyExistsServiceException();
                    }
                    fileSuport.commitTransaction();
                    return new Boolean(true);
                }
                fileSuport.commitTransaction();
                return new Boolean(false);

            } catch (FileNameTooLongServiceException e1) {
                throw e1;
            } catch (FileAlreadyExistsServiceException e1) {
                throw e1;
            } catch (FenixServiceException e1) {
                throw e1;
            } catch (Exception e1) {
                throw new FenixServiceException(e1);
            }
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }
}