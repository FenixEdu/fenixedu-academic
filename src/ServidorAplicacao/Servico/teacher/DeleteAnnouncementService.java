package ServidorAplicacao.Servico.teacher;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.Announcement;
import Dominio.IAnnouncement;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentAnnouncement;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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
            ISuportePersistente persistentSupport = SuportePersistenteOJB
                    .getInstance();
            IPersistentAnnouncement persistentAnnouncement = persistentSupport
                    .getIPersistentAnnouncement();

            IAnnouncement iAnnouncement = (IAnnouncement) persistentAnnouncement
                    .readByOID(Announcement.class, announcementCode);

            if (iAnnouncement != null) {
                persistentAnnouncement.delete(iAnnouncement);
            }
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
        return true;
    }
}