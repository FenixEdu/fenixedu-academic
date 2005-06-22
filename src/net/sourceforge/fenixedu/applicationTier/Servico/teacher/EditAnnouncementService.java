package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.sql.Timestamp;
import java.util.Date;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.Announcement;
import net.sourceforge.fenixedu.domain.IAnnouncement;
import net.sourceforge.fenixedu.domain.ISite;
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

    private ISuportePersistente persistentSupport = null;

    private IPersistentAnnouncement persistentAnnouncement = null;

    private void checkIfAnnouncementExists(String oldAnnouncementTitle, Date date,
            String announcementTitle, ISite announcementSite) throws FenixServiceException {
        IAnnouncement announcement = null;
        if (!oldAnnouncementTitle.equals(announcementTitle)) {
            try {
                announcement = persistentAnnouncement.readAnnouncementByTitleAndCreationDateAndSite(
                        announcementTitle, date, announcementSite.getIdInternal());
            } catch (ExcepcaoPersistencia excepcaoPersistencia) {
                throw new FenixServiceException(excepcaoPersistencia.getMessage());
            }

            if (announcement != null) {
                throw new ExistingServiceException();
            }
        }
    }

    public boolean run(Integer infoExecutionCourseCode, Integer announcementCode,
            String announcementNewTitle, String announcementNewInformation)
            throws FenixServiceException, ExcepcaoPersistencia {

        persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        persistentAnnouncement = persistentSupport.getIPersistentAnnouncement();

        IAnnouncement iAnnouncement = (IAnnouncement) persistentAnnouncement.readByOID(
                Announcement.class, announcementCode);
        if (iAnnouncement == null) {
            throw new InvalidArgumentsServiceException();
        }
        persistentAnnouncement.simpleLockWrite(iAnnouncement);

        String announcementOldTitle = iAnnouncement.getTitle();
        Date date = iAnnouncement.getCreationDate();
        ISite site = iAnnouncement.getSite();

        checkIfAnnouncementExists(announcementOldTitle, date, announcementNewTitle, site);

        Timestamp lastModificationDate = new Timestamp(new Date(System.currentTimeMillis()).getTime());
        iAnnouncement.setTitle(announcementNewTitle);
        iAnnouncement.setLastModifiedDate(lastModificationDate);
        iAnnouncement.setInformation(announcementNewInformation);

        return true;
    }
}