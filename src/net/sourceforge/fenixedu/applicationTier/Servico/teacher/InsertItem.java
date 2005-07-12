package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoItem;
import net.sourceforge.fenixedu.domain.IItem;
import net.sourceforge.fenixedu.domain.ISection;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSection;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Fernanda Quitério
 */
public class InsertItem implements IService {

    public Boolean run(Integer infoExecutionCourseCode, Integer sectionCode, InfoItem infoItem)
            throws FenixServiceException, ExcepcaoPersistencia {

        final ISuportePersistente persistentSuport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final IPersistentSection persistentSection = persistentSuport.getIPersistentSection();
                       
        final ISection section = (ISection) persistentSection.readByOID(Section.class, sectionCode);
                     
        persistentSection.simpleLockWrite(section);
                       
        //IItem item = section.insertItem(infoItem.getName(), infoItem.getInformation(), infoItem.getUrgent(), infoItem.getItemOrder());
        
        //
        //persistentSuport.getIPersistentItem().simpleLockWrite(item);
        //
        
        return new Boolean(true);
    }

}