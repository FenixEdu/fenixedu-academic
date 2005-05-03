package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Announcement;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IAnnouncement;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.ISite;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentAnnouncement;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSite;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Fernanda Quitério
 *  
 */
public class CreateAnnouncement implements IService {

    private ISuportePersistente persistentSupport = null;

    private IPersistentAnnouncement persistentAnnouncement = null;

    public CreateAnnouncement() {
    }

    private void checkIfAnnouncementExists(String announcementTitle, ISite announcementSite,
            Date currentDate) throws FenixServiceException {
        IAnnouncement announcement = null;
        persistentAnnouncement = persistentSupport.getIPersistentAnnouncement();

        try {
            announcement = persistentAnnouncement.readAnnouncementByTitleAndCreationDateAndSite(
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
    public boolean run(Integer infoExecutionCourseCode, String newAnnouncementTitle,
            String newAnnouncementInformation) throws FenixServiceException {
        ISite site = null;

        Calendar calendar = Calendar.getInstance();

        try {
            persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentExecutionCourse persistentExecutionCourse = persistentSupport
                    .getIPersistentExecutionCourse();
            IPersistentSite persistentSite = persistentSupport.getIPersistentSite();

            IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                    ExecutionCourse.class, infoExecutionCourseCode);
            site = persistentSite.readByExecutionCourse(executionCourse);

        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia.getMessage());
        }

        checkIfAnnouncementExists(newAnnouncementTitle, site,
                new Timestamp(calendar.getTime().getTime()));

        try {
            IAnnouncement newAnnouncement = new Announcement(newAnnouncementTitle, calendar.getTime(), calendar.getTime(),
                    newAnnouncementInformation, site);
            persistentAnnouncement.simpleLockWrite(newAnnouncement);

        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia.getMessage());
        }
        return true;
    }
}