/*
 * Created on 14/Mar/2003
 */
package ServidorAplicacao.Servico.gesdis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoSite;
import DataBeans.util.Cloner;
import Dominio.IAnnouncement;
import Dominio.IExecutionCourse;
import Dominio.ISite;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */

public class ReadAnnouncements implements IService {

    /**
     * Executes the service.
     *  
     */
    public List run(InfoSite infoSite) throws FenixServiceException {

        try {

            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            InfoExecutionCourse infoExecutionCourse = infoSite.getInfoExecutionCourse();
            IExecutionCourse executionCourse = Cloner
                    .copyInfoExecutionCourse2ExecutionCourse(infoExecutionCourse);

            ISite site = sp.getIPersistentSite().readByExecutionCourse(executionCourse);

            List announcementsList = sp.getIPersistentAnnouncement().readAnnouncementsBySite(site);
            List infoAnnouncementsList = new ArrayList();

            if (announcementsList != null && announcementsList.isEmpty() == false) {
                Iterator iterAnnouncements = announcementsList.iterator();
                while (iterAnnouncements.hasNext()) {
                    IAnnouncement announcement = (IAnnouncement) iterAnnouncements.next();
                    infoAnnouncementsList.add(Cloner.copyIAnnouncement2InfoAnnouncement(announcement));
                }
            }

            return infoAnnouncementsList;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }

}

