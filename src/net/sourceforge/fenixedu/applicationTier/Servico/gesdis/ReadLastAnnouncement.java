package net.sourceforge.fenixedu.applicationTier.Servico.gesdis;

import net.sourceforge.fenixedu.dataTransferObject.InfoAnnouncement;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoSite;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IAnnouncement;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.ISite;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Ivo Brandão
 */
public class ReadLastAnnouncement implements IService {

    /**
     * Executes the service.
     */
    public InfoAnnouncement run(InfoSite infoSite) throws FenixServiceException {

        try {
            ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();

            InfoExecutionCourse infoExecutionCourse = infoSite.getInfoExecutionCourse();
            IExecutionCourse executionCourse = Cloner
                    .copyInfoExecutionCourse2ExecutionCourse(infoExecutionCourse);

            ISite site = persistentSupport.getIPersistentSite().readByExecutionCourse(executionCourse);

            IAnnouncement announcement = persistentSupport.getIPersistentAnnouncement()
                    .readLastAnnouncementForSite(site);

            InfoAnnouncement infoAnnouncement = null;
            if (announcement != null)
                infoAnnouncement = Cloner.copyIAnnouncement2InfoAnnouncement(announcement);

            return infoAnnouncement;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }
}