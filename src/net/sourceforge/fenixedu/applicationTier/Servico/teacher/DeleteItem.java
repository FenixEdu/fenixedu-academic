package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Fernanda Quitério
 * 
 */
public class DeleteItem extends Service {

    public Boolean run(final Integer infoExecutionCourseCode, final Integer itemCode)
            throws FenixServiceException, ExcepcaoPersistencia {
        final Item deletedItem = rootDomainObject.readItemByOID(itemCode);
        
        if (deletedItem == null) {
            return new Boolean(true);
        }

        //testFilesExistence(deletedItem);
       
        deletedItem.delete();

        return Boolean.TRUE;
    }

   /* private void testFilesExistence(final Item deletedItem) throws notAuthorizedServiceDeleteException {
        
        Collection<FileSuportObject> listFiles = JdbcMysqlFileSupport.listFiles(deletedItem.getSlideName());
        if (!listFiles.isEmpty()) {
            throw new notAuthorizedServiceDeleteException();
        }
    }*/

}

