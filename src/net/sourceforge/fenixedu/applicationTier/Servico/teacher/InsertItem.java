package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoItem;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class InsertItem extends Service {

    public Boolean run(Integer infoExecutionCourseCode, Integer sectionCode, InfoItem infoItem)
            throws FenixServiceException, ExcepcaoPersistencia, DomainException {

        final Section section = rootDomainObject.readSectionByOID(sectionCode);
        if (infoItem.getItemOrder() == -1) {
            infoItem.setItemOrder(section.getAssociatedItemsCount());
        }
        
        section.insertItem(infoItem.getName(), infoItem.getInformation(), infoItem.getItemOrder());
          
        return Boolean.TRUE;
    }

}
