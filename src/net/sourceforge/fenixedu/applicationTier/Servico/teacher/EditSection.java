package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
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
public class EditSection implements IService {

    public Boolean run(Integer infoExecutionCourseCode, Integer sectionCode, String newSectionName,
            Integer newOrder) throws FenixServiceException, ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentSection persistentSection = sp.getIPersistentSection();

        ISection iSection = (ISection) persistentSection.readByOID(Section.class, sectionCode);

        if (iSection == null) {
            throw new NonExistingServiceException();
        }
                   
        iSection.edit(newSectionName, newOrder);

        return new Boolean(true);
    }
}