package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoItem;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSection;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author Fernanda Quitério
 */
public class InsertItem extends Service {

    public Boolean run(Integer infoExecutionCourseCode, Integer sectionCode, InfoItem infoItem)
            throws FenixServiceException, ExcepcaoPersistencia, DomainException {

        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final IPersistentSection persistentSection = persistentSupport.getIPersistentSection();
                       
        final Section section = (Section) persistentSection.readByOID(Section.class, sectionCode);
              
        if (infoItem.getItemOrder() == -1) {
            infoItem.setItemOrder(section.getAssociatedItemsCount());
        }
        
        section.insertItem(infoItem.getName(), infoItem.getInformation(), infoItem.getUrgent(), infoItem.getItemOrder());
          
        return new Boolean(true);
    }

}