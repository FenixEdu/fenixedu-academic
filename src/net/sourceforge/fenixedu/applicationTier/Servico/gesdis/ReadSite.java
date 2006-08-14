package net.sourceforge.fenixedu.applicationTier.Servico.gesdis;

/**
 * 
 * @author Joao Pereira
 * @version
 */

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoSite;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadSite extends Service {

    public InfoSite run(InfoExecutionCourse infoExecutionCourse) throws ExcepcaoPersistencia {
    	final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID( infoExecutionCourse.getIdInternal());
        final Site site = executionCourse.getSite();
        if (site != null) {
            final InfoSite infoSite = InfoSite.newInfoFromDomain(site);
            infoSite.setInfoExecutionCourse(InfoExecutionCourse.newInfoFromDomain(executionCourse));
            return infoSite;
        } else {
            return null;
        }
    }

}