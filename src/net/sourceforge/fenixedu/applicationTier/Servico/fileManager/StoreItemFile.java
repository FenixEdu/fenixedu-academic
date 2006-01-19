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
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.fileSuport.FileSuportObject;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentItem;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.fileSupport.JdbcMysqlFileSupport;

import org.apache.slide.common.SlideException;

import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * fenix-head ServidorAplicacao.Servico.fileManager
 * 
 * @author João Mota 17/Set/2003
 * 
 */
public class StoreItemFile extends Service {

    public Boolean run(FileSuportObject file, Integer itemId) throws FenixServiceException,
            ExcepcaoPersistencia, NotSupportedException, SystemException, SlideException,
            SecurityException, IllegalStateException, RollbackException, HeuristicMixedException,
            HeuristicRollbackException {
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentItem persistentItem = sp.getIPersistentItem();
        Item item = (Item) persistentItem.readByOID(Item.class, itemId);
        file.setUri("/files" + item.getSlideName() + "/" + file.getFileName());
        file.setRootUri(item.getSection().getSite().getExecutionCourse().getSlideName());
        JdbcMysqlFileSupport.createFile(file);
        return Boolean.TRUE;
    }
}