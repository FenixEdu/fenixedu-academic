package ServidorAplicacao.Servico.teacher;

import java.sql.Timestamp;
import java.util.Calendar;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.Announcement;
import Dominio.ExecutionCourse;
import Dominio.IAnnouncement;
import Dominio.IExecutionCourse;
import Dominio.ISite;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
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
public class CreateAnnouncement implements IService {

    private ISuportePersistente persistentSupport = null;

    private IPersistentAnnouncement persistentAnnouncement = null;

    public CreateAnnouncement() {
    }

    private void checkIfAnnouncementExists(String announcementTitle,
            ISite announcementSite, Timestamp currentDate)
            throws FenixServiceException {
        IAnnouncement announcement = null;
        persistentAnnouncement = persistentSupport.getIPersistentAnnouncement();

        try {
            announcement = persistentAnnouncement
                    .readAnnouncementByTitleAndCreationDateAndSite(
                            announcementTitle, currentDate, announcementSite);
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia.getMessage());
        }

        if (announcement != null)
            throw new ExistingServiceException();
    }

    /**
     * Executes the service.
     */
    public boolean run(Integer infoExecutionCourseCode,
            String newAnnouncementTitle, String newAnnouncementInformation)
            throws FenixServiceException {
        ISite site = null;

        //retrieve current date
        Calendar calendar = Calendar.getInstance();
        //		calendar.set(Calendar.HOUR_OF_DAY,0);
        //		calendar.set(Calendar.MINUTE,0);
        //		calendar.set(Calendar.SECOND,0);
        //		calendar.set(Calendar.MILLISECOND,0);

        try {
            persistentSupport = SuportePersistenteOJB.getInstance();
            IPersistentExecutionCourse persistentExecutionCourse = persistentSupport
                    .getIPersistentExecutionCourse();
            IPersistentSite persistentSite = persistentSupport
                    .getIPersistentSite();

            IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse
                    .readByOID(ExecutionCourse.class, infoExecutionCourseCode);
            site = persistentSite.readByExecutionCourse(executionCourse);

        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia.getMessage());
        }

        checkIfAnnouncementExists(newAnnouncementTitle, site, new Timestamp(
                calendar.getTime().getTime()));

        try {
            IAnnouncement newAnnouncement = new Announcement(
                    newAnnouncementTitle, new Timestamp(calendar.getTime()
                            .getTime()), new Timestamp(calendar.getTime()
                            .getTime()), newAnnouncementInformation, site);
            persistentAnnouncement.simpleLockWrite(newAnnouncement);

        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia.getMessage());
        }
        return true;
    }
}