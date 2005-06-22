/*
 * Created on 14/Mar/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.gesdis;

import java.util.ArrayList;
import java.util.List;

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
 * @author lmac1
 */

public class ReadAnnouncements implements IService {

    public List run(InfoSite infoSite) throws FenixServiceException, ExcepcaoPersistencia {

        final ISuportePersistente suportePersistente = PersistenceSupportFactory
                .getDefaultPersistenceSupport();

        final ISite site = suportePersistente.getIPersistentSite().readByExecutionCourse(
                infoSite.getInfoExecutionCourse().getIdInternal());
        final List<IAnnouncement> announcementsList = site.getAssociatedAnnouncements();

        final List<InfoAnnouncement> infoAnnouncementsList = new ArrayList<InfoAnnouncement>();

        if (announcementsList != null) {
            for (final IAnnouncement announcement : announcementsList) {
                infoAnnouncementsList.add(Cloner.copyIAnnouncement2InfoAnnouncement(announcement));
            }
        }
        return infoAnnouncementsList;
    }

}
