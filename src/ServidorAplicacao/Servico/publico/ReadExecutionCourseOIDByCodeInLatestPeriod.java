package ServidorAplicacao.Servico.publico;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Luis Cruz
 */
public class ReadExecutionCourseOIDByCodeInLatestPeriod implements IService {

    public Integer run(String executionCourseCode) throws ExcepcaoPersistencia {

        ISuportePersistente sp = SuportePersistenteOJB.getInstance();
        IPersistentExecutionCourse executionCourseDAO = sp.getIPersistentExecutionCourse();
        IPersistentExecutionPeriod executionPeriodDAO = sp.getIPersistentExecutionPeriod();

        IExecutionPeriod executionPeriod = executionPeriodDAO.readActualExecutionPeriod();
        IExecutionCourse executionCourse = executionCourseDAO
                .readByExecutionCourseInitialsAndExecutionPeriod(executionCourseCode, executionPeriod);

        if (executionCourse != null) {
            return executionCourse.getIdInternal();
        }

        return null;
    }

}