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

    ISuportePersistente persistentSupport = null;

    private IPersistentAnnouncement persistentAnnouncement = null;

    /**
     * The constructor of this class.
     */
    public EditAnnouncementService() {
    }

    private void checkIfAnnouncementExists(String oldAnnouncementTitle, Timestamp date,
            String announcementTitle, ISite announcementSite) throws FenixServiceException {
        IAnnouncement announcement = null;
        if (!oldAnnouncementTitle.equals(announcementTitle)) {
            try {
                announcement = persistentAnnouncement.readAnnouncementByTitleAndCreationDateAndSite(
                        announcementTitle, date, announcementSite);
            } catch (ExcepcaoPersistencia excepcaoPersistencia) {
                throw new FenixServiceException(excepcaoPersistencia.getMessage());
            }

            if (announcement != null) {
                throw new ExistingServiceException();
            }
        }
    }

    /**
     * Executes the service.
     */
    public boolean run(Integer infoExecutionCourseCode, Integer announcementCode,
            String announcementNewTitle, String announcementNewInformation) throws FenixServiceException {

        ISite site = null;
        Timestamp date = null;
        IAnnouncement iAnnouncement = null;
        try {
            persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();

            persistentAnnouncement = persistentSupport.getIPersistentAnnouncement();

            // read announcement

            iAnnouncement = (IAnnouncement) persistentAnnouncement.readByOID(Announcement.class,
                    announcementCode, true);
            if (iAnnouncement == null) {
                throw new InvalidArgumentsServiceException();
            }

            String announcementOldTitle = iAnnouncement.getTitle();
            date = iAnnouncement.getCreationDate();

            // read executionCourse and site
            site = iAnnouncement.getSite();

            checkIfAnnouncementExists(announcementOldTitle, date, announcementNewTitle, site);
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia.getMessage());
        }

        Timestamp lastModificationDate = new Timestamp(new Date(System.currentTimeMillis()).getTime());
        iAnnouncement.setTitle(announcementNewTitle);
        iAnnouncement.setLastModifiedDate(lastModificationDate);
        iAnnouncement.setInformation(announcementNewInformation);

        return true;
    }
}