package ServidorAplicacao.Servico.gesdis;

/**
 * Serviço ObterSitio. Obtem a informacao sobre um sitio.
 * 
 * @author Joao Pereira
 * @version
 */

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoSite;
import DataBeans.util.Cloner;
import Dominio.IExecutionCourse;
import Dominio.ISite;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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