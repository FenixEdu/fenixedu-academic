package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.notAuthorizedServiceDeleteException;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.fileSuport.FileSuportObject;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentItem;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.fileSupport.JdbcMysqlFileSupport;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author Fernanda Quitério
 * 
 */
public class DeleteItem extends Service {

    public Boolean run(final Integer infoExecutionCourseCode, final Integer itemCode)
            throws FenixServiceException, ExcepcaoPersistencia {
        
        final ISuportePersistente persistentSuport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();    
        final IPersistentItem persistentItem = persistentSuport.getIPersistentItem();
        final Item deletedItem = (Item) persistentItem.readByOID(Item.class, itemCode);
        
        if (deletedItem == null) {
            return new Boolean(true);
        }

        testFilesExistence(deletedItem);
       
        deletedItem.delete();

        return Boolean.TRUE;
    }

    private void testFilesExistence(final Item deletedItem) throws notAuthorizedServiceDeleteException {
        Collection<FileSuportObject> listFiles = JdbcMysqlFileSupport.listFiles(deletedItem.getSlideName());
        if (!listFiles.isEmpty()) {
            throw new notAuthorizedServiceDeleteException();
        }
    }

}