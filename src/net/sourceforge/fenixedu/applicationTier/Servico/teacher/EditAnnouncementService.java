package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.Announcement;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Fernanda Quitério
 * 
 */

public class EditAnnouncementService extends Service {

    public boolean run(Integer announcementCode, String newAnnouncementTitle,
            String newAnnouncementInformation) throws ExcepcaoPersistencia, FenixServiceException {
        final Announcement announcement = (Announcement) persistentObject.readByOID(
                Announcement.class, announcementCode);
        if (announcement == null) {
            throw new InvalidArgumentsServiceException();
        }
        announcement.edit(newAnnouncementTitle, newAnnouncementInformation);

        return true;
    }
}