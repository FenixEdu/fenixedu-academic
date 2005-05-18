package net.sourceforge.fenixedu.applicationTier.Servico.gesdis;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoAnnouncement;
import net.sourceforge.fenixedu.dataTransferObject.InfoSite;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IAnnouncement;
import net.sourceforge.fenixedu.domain.ISite;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Ivo Brandão
 */
public class ReadLastAnnouncement implements IService {

    public InfoAnnouncement run(InfoSite infoSite) throws FenixServiceException, ExcepcaoPersistencia {

        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        ISite site = persistentSupport.getIPersistentSite().readByExecutionCourse(
                infoSite.getInfoExecutionCourse().getIdInternal());
        IAnnouncement announcement = persistentSupport.getIPersistentAnnouncement()
                .readLastAnnouncementForSite(site.getIdInternal());

        InfoAnnouncement infoAnnouncement = null;
        if (announcement != null)
            infoAnnouncement = Cloner.copyIAnnouncement2InfoAnnouncement(announcement);

        return infoAnnouncement;
    }
}