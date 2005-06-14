package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Luis Cruz
 */
public class ReadExecutionCourseOIDByCodeInLatestPeriod implements IService {

    public Integer run(String executionCourseCode) throws ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentExecutionCourse executionCourseDAO = sp.getIPersistentExecutionCourse();
        IPersistentExecutionPeriod executionPeriodDAO = sp.getIPersistentExecutionPeriod();

        IExecutionPeriod executionPeriod = executionPeriodDAO.readActualExecutionPeriod();
        IExecutionCourse executionCourse = executionCourseDAO
                .readByExecutionCourseInitialsAndExecutionPeriodId(executionCourseCode, executionPeriod.getIdInternal());

        if (executionCourse != null) {
            return executionCourse.getIdInternal();
        }

        return null;
    }

}