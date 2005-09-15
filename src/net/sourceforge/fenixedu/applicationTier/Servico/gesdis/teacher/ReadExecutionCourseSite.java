package net.sourceforge.fenixedu.applicationTier.Servico.gesdis.teacher;

/**
 * 
 * @author EP 15
 */

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoSite;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteWithInfoExecutionCourse;
import net.sourceforge.fenixedu.domain.ISite;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadExecutionCourseSite implements IService {

    public InfoSite run(InfoExecutionCourse infoExecutionCourse) throws FenixServiceException,
            ExcepcaoPersistencia {

        InfoSite infoSite = null;
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        final ISite site = sp.getIPersistentSite().readByExecutionCourse(infoExecutionCourse.getIdInternal());
        if (site != null)
            infoSite = InfoSiteWithInfoExecutionCourse.newInfoFromDomain(site);

        return infoSite;
    }
}