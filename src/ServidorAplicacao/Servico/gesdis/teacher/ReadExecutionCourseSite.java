package ServidorAplicacao.Servico.gesdis.teacher;

/**
 * 
 * @author EP 15
 */

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoSite;
import DataBeans.util.Cloner;
import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.ISite;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ReadExecutionCourseSite implements IService {

    /**
     * Executes the service. Returns the current collection of sitios names.
     * 
     * @throws ExcepcaoInexistente
     *             is there is none sitio.
     */

    public InfoSite run(InfoExecutionCourse infoExecutionCourse) throws FenixServiceException {

        try {
            ISite site = null;

            ISuportePersistente sp;

            sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionCourse executionCourseDAO = sp.getIPersistentExecutionCourse();
            IExecutionCourse executionCourse = (IExecutionCourse) executionCourseDAO.readByOID(
                    ExecutionCourse.class, infoExecutionCourse.getIdInternal());

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