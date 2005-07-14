package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.notAuthorizedServiceDeleteException;
import net.sourceforge.fenixedu.domain.IItem;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.fileSuport.FileSuport;
import net.sourceforge.fenixedu.fileSuport.IFileSuport;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentItem;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Fernanda Quitério
 * 
 */
public class DeleteItem implements IService {

    public Boolean run(final Integer infoExecutionCourseCode, final Integer itemCode)
            throws FenixServiceException, ExcepcaoPersistencia {
        
        final ISuportePersistente persistentSuport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();    
        final IPersistentItem persistentItem = persistentSuport.getIPersistentItem();
        final IItem deletedItem = (IItem) persistentItem.readByOID(Item.class, itemCode);
        
        if (deletedItem == null) {
            return new Boolean(true);
        }
       
        testFilesExistence(deletedItem);
       
        deletedItem.delete();

        persistentItem.deleteByOID(Item.class, deletedItem.getIdInternal());

        return new Boolean(true);
    }

    private void testFilesExistence(final IItem deletedItem) throws notAuthorizedServiceDeleteException {
        IFileSuport fileSuport = FileSuport.getInstance();
        long size = fileSuport.getDirectorySize(deletedItem.getSlideName());
        
        if (size > 0) {
            throw new notAuthorizedServiceDeleteException();
        }
    }
}