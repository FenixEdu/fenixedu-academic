package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Fernanda Quitério
 */
public class EditSection extends Service {

    public Boolean run(Integer infoExecutionCourseCode, Integer sectionCode, String newSectionName,
            Integer newOrder) throws FenixServiceException, ExcepcaoPersistencia {
        Section iSection = (Section) persistentObject.readByOID(Section.class, sectionCode);

        if (iSection == null) {
            throw new NonExistingServiceException();
        }
        
        List<Section> sectionsList = iSection.getSite().getAssociatedSections(iSection.getSuperiorSection());
        if (newOrder.intValue() == -2) {
            newOrder = new Integer(sectionsList.size() - 1);
        }
        
        int diffOrder = newOrder.intValue() - iSection.getSectionOrder().intValue();
        if(diffOrder < 0)
            newOrder += 1; 
        
        iSection.edit(newSectionName, newOrder);

        return new Boolean(true);
    }
}