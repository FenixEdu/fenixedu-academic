package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.Announcement;
import net.sourceforge.fenixedu.domain.IAnnouncement;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentAnnouncement;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Fernanda Quitério
 * 
 */

public class EditAnnouncementService implements IService {

    public boolean run(Integer infoExecutionCourseCode, Integer announcementCode,
            String newAnnouncementTitle, String newAnnouncementInformation) throws ExcepcaoPersistencia,
            FenixServiceException {

        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final IPersistentAnnouncement persistentAnnouncement = persistentSupport
                .getIPersistentAnnouncement();

        final IAnnouncement announcement = (IAnnouncement) persistentAnnouncement.readByOID(
                Announcement.class, announcementCode);
        if (announcement == null) {
            throw new InvalidArgumentsServiceException();
        }
        persistentAnnouncement.simpleLockWrite(announcement);
        announcement.edit(newAnnouncementTitle, newAnnouncementInformation);

        return true;
    }
}