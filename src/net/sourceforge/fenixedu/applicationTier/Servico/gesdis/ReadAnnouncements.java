/*
 * Created on 14/Mar/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.gesdis;

import java.util.ArrayList;
import java.util.Iterator;
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

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        ISite site = sp.getIPersistentSite().readByExecutionCourse(
                infoSite.getInfoExecutionCourse().getIdInternal());
        List announcementsList = sp.getIPersistentAnnouncement().readAnnouncementsBySite(
                site.getIdInternal());
        List<InfoAnnouncement> infoAnnouncementsList = new ArrayList<InfoAnnouncement>();

        if (announcementsList != null && announcementsList.isEmpty() == false) {
            Iterator iterAnnouncements = announcementsList.iterator();
            while (iterAnnouncements.hasNext()) {
                IAnnouncement announcement = (IAnnouncement) iterAnnouncements.next();
                infoAnnouncementsList.add(Cloner.copyIAnnouncement2InfoAnnouncement(announcement));
            }
        }
        return infoAnnouncementsList;
    }

}
