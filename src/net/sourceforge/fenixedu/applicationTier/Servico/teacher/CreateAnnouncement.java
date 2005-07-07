package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.ISite;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSite;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Fernanda Quitério
 * 
 */
public class CreateAnnouncement implements IService {

    public boolean run(Integer infoExecutionCourseCode, String announcementTitle,
            String announcementInformation) throws ExcepcaoPersistencia, InvalidArgumentsServiceException {

        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final IPersistentSite persistentSite = persistentSupport.getIPersistentSite();

        final ISite site = persistentSite.readByExecutionCourse(infoExecutionCourseCode);
        if (site == null)
            throw new InvalidArgumentsServiceException();
        persistentSite.simpleLockWrite(site);
        site.createAnnouncement(announcementTitle, announcementInformation);
        
        return true;
    }
}