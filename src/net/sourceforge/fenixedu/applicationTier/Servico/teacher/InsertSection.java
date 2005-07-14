package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ISection;
import net.sourceforge.fenixedu.domain.ISite;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSection;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSite;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Fernanda Quitério
 */
public class InsertSection implements IService {


    public Boolean run(Integer infoExecutionCourseCode, Integer sectionCode, String sectionName,
            Integer sectionOrder) throws FenixServiceException, ExcepcaoPersistencia {
     
        final ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentSite persistentSite = persistentSuport.getIPersistentSite();
        final IPersistentSection persistentSection = persistentSuport.getIPersistentSection();
        final ISite site = persistentSite.readByExecutionCourse(infoExecutionCourseCode);

        ISection parentSection = null;
        if (sectionCode != null) {
            parentSection = (ISection) persistentSection.readByOID(Section.class, sectionCode);
        }
        site.createSection(sectionName, parentSection, sectionOrder);
         
        return new Boolean(true);
    }
}