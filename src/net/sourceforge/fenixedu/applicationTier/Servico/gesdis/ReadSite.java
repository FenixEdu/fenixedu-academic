package net.sourceforge.fenixedu.applicationTier.Servico.gesdis;

/**
 * 
 * @author Joao Pereira
 * @version
 */

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoSite;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ISite;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadSite implements IService {

    public InfoSite run(InfoExecutionCourse infoExecutionCourse) throws FenixServiceException,
            ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final ISite site = sp.getIPersistentSite().readByExecutionCourse(
                infoExecutionCourse.getIdInternal());
        InfoSite infoSite = null;
        if (site != null) {
            infoSite = Cloner.copyISite2InfoSite(site);
        }
        return infoSite;
    }
}