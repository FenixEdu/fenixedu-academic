package net.sourceforge.fenixedu.applicationTier.Servico.gesdis;

/**
 * Serviço ObterSitio. Obtem a informacao sobre um sitio.
 * 
 * @author Joao Pereira
 * @version
 */

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoSite;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.ISite;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadSite implements IService {

    /**
     * Executes the service. Returns the all information about the desired
     * sitio.
     * 
     * @param infoExecutionCourse
     *            is the infoExecutionCourse of the desired site.
     * 
     * @throws ExcepcaoInexistente
     *             if there is none sitio with the desired name.
     */
    public InfoSite run(InfoExecutionCourse infoExecutionCourse) throws FenixServiceException {
        try {
            ISite site = null;
            ISuportePersistente sp;
            IExecutionCourse executionCourse = Cloner
                    .copyInfoExecutionCourse2ExecutionCourse(infoExecutionCourse);
            sp = SuportePersistenteOJB.getInstance();
            site = sp.getIPersistentSite().readByExecutionCourse(executionCourse);
            InfoSite infoSite = null;

            if (site != null) {
                infoSite = Cloner.copyISite2InfoSite(site);
            }

            return infoSite;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }

}