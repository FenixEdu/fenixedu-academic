package net.sourceforge.fenixedu.applicationTier.Servico.gesdis.teacher;

/**
 * 
 * @author EP 15
 */

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoSite;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadExecutionCourseSite extends Service {

    public InfoSite run(InfoExecutionCourse infoExecutionCourse) throws FenixServiceException,
            ExcepcaoPersistencia {

        InfoSite infoSite = null;

    	final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID( infoExecutionCourse.getIdInternal());
        final Site site = executionCourse.getSite();

        if (site != null) {
            infoSite = InfoSite.newInfoFromDomain(site);
            infoSite.setInfoExecutionCourse(InfoExecutionCourse.newInfoFromDomain(executionCourse));
        }

        return infoSite;
    }
}