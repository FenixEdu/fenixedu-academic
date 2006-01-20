package net.sourceforge.fenixedu.applicationTier.Servico.gesdis;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoAnnouncement;
import net.sourceforge.fenixedu.dataTransferObject.InfoSite;
import net.sourceforge.fenixedu.domain.Announcement;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Ivo Brandão
 */
public class ReadLastAnnouncement extends Service {

    public InfoAnnouncement run(InfoSite infoSite) throws FenixServiceException, ExcepcaoPersistencia {
        Site site = persistentSupport.getIPersistentSite().readByExecutionCourse(
                infoSite.getInfoExecutionCourse().getIdInternal());
        Announcement announcement = site.getLastAnnouncement();

        InfoAnnouncement infoAnnouncement = null;
        if (announcement != null)
            infoAnnouncement = InfoAnnouncement.newInfoFromDomain(announcement);

        return infoAnnouncement;
    }
}
