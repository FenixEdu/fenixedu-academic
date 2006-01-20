package net.sourceforge.fenixedu.applicationTier.Servico.gesdis;

/**
 * 
 * @author Joao Pereira
 * @version
 */

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoSite;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteWithInfoExecutionCourse;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadSite extends Service {

    public InfoSite run(InfoExecutionCourse infoExecutionCourse) throws ExcepcaoPersistencia {
        final Site site = persistentSupport.getIPersistentSite().readByExecutionCourse(infoExecutionCourse.getIdInternal());
        if (site != null) {
            return InfoSiteWithInfoExecutionCourse.newInfoFromDomain(site);
        } else {
            return null;
        }
    }

}