package ServidorAplicacao.Servico.teacher;

import java.sql.Timestamp;
import java.util.Date;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.Announcement;
import Dominio.IAnnouncement;
import Dominio.ISite;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentAnnouncement;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Fernanda Quitério
 *  
 */

public class EditAnnouncementService implements IService {

    ISuportePersistente persistentSupport = null;

    private IPersistentSite persistentSite = null;

    private IPersistentAnnouncement persistentAnnouncement = null;

    private IPersistentExecutionCourse persistentExecutionCourse = null;

    private static EditAnnouncementService service = new EditAnnouncementService();

    /**
     * The constructor of this class.
     */
    public EditAnnouncementService() {
    }

    private void checkIfAnnouncementExists(String oldAnnouncementTitle,
            Timestamp date, String announcementTitle, ISite announcementSite)
            throws FenixServiceException {
        IAnnouncement announcement = null;
        if (!oldAnnouncementTitle.equals(announcementTitle)) {
            try {
                announcement = persistentAnnouncement
                        .readAnnouncementByTitleAndCreationDateAndSite(
                                announcementTitle, date, announcementSite);
            } catch (ExcepcaoPersistencia excepcaoPersistencia) {
                throw new FenixServiceException(excepcaoPersistencia
                        .getMessage());
            }

            if (announcement != null) {
                throw new ExistingServiceException();
            }
        }
    }

    /**
     * Executes the service.
     */
    public boolean run(Integer infoExecutionCourseCode,
            Integer announcementCode, String announcementNewTitle,
            String announcementNewInformation) throws FenixServiceException {

        ISite site = null;
        Timestamp date = null;
        IAnnouncement iAnnouncement = null;
        try {
            persistentSupport = SuportePersistenteOJB.getInstance();
            persistentExecutionCourse = persistentSupport
                    .getIPersistentExecutionCourse();
            persistentSite = persistentSupport.getIPersistentSite();
            persistentAnnouncement = persistentSupport
                    .getIPersistentAnnouncement();

            // read announcement

            iAnnouncement = (IAnnouncement) persistentAnnouncement.readByOID(
                    Announcement.class, announcementCode, true);
            if (iAnnouncement == null) {
                throw new InvalidArgumentsServiceException();
            }

            String announcementOldTitle = iAnnouncement.getTitle();
            date = iAnnouncement.getCreationDate();

            // read executionCourse and site
            site = iAnnouncement.getSite();

            checkIfAnnouncementExists(announcementOldTitle, date,
                    announcementNewTitle, site);
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia.getMessage());
        }

        Timestamp lastModificationDate = new Timestamp(new Date(System
                .currentTimeMillis()).getTime());
        iAnnouncement.setTitle(announcementNewTitle);
        iAnnouncement.setLastModifiedDate(lastModificationDate);
        iAnnouncement.setInformation(announcementNewInformation);

        return true;
    }
}