package net.sourceforge.fenixedu.applicationTier.Servico.gesdis.teacher;

/**
 * 
 * @author EP 15
 */

import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoSite;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.ISite;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

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

            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
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