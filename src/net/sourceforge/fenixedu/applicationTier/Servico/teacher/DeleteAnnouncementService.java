package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.domain.Announcement;
import net.sourceforge.fenixedu.domain.IAnnouncement;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentAnnouncement;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Fernanda Quitério
 *  
 */
public class DeleteAnnouncementService implements IService {

    public DeleteAnnouncementService() {
    }

    public boolean run(Integer infoExecutionCourseCode, Integer announcementCode)
            throws FenixServiceException {

        try {
            ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
            IPersistentAnnouncement persistentAnnouncement = persistentSupport
                    .getIPersistentAnnouncement();

            IAnnouncement iAnnouncement = (IAnnouncement) persistentAnnouncement.readByOID(
                    Announcement.class, announcementCode);

            if (iAnnouncement != null) {
                persistentAnnouncement.delete(iAnnouncement);
            }
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
        return true;
    }
}