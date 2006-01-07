package net.sourceforge.fenixedu.applicationTier.Servico.gesdis;

/**
 * 
 * @author Joao Pereira
 * @version
 */

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoSite;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteWithInfoExecutionCourse;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadSite implements IService {

    public InfoSite run(InfoExecutionCourse infoExecutionCourse) throws ExcepcaoPersistencia {
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final Site site = sp.getIPersistentSite().readByExecutionCourse(infoExecutionCourse.getIdInternal());
        if (site != null) {
            return InfoSiteWithInfoExecutionCourse.newInfoFromDomain(site);
        } else {
            return null;
        }
    }

}