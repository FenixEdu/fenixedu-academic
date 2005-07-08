/*
 * Created on 17/Set/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.fileManager;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FileAlreadyExistsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FileNameTooLongServiceException;
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
public class StoreItemFile implements IService {

    public Boolean run(FileSuportObject file, Integer itemId) throws FenixServiceException,
            ExcepcaoPersistencia, NotSupportedException, SystemException, SlideException,
            SecurityException, IllegalStateException, RollbackException, HeuristicMixedException,
            HeuristicRollbackException {
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentItem persistentItem = sp.getIPersistentItem();
        IItem item = (IItem) persistentItem.readByOID(Item.class, itemId);
        file.setUri(item.getSlideName());
        file.setRootUri(item.getSection().getSite().getExecutionCourse().getSlideName());
        IFileSuport fileSuport = FileSuport.getInstance();

        fileSuport.beginTransaction();

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
                fileSuport.commitTransaction();
                throw new FileAlreadyExistsServiceException();
            }
            fileSuport.commitTransaction();
            return new Boolean(true);
        }
        fileSuport.commitTransaction();
        return new Boolean(false);

    }
}