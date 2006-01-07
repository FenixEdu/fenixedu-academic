package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class DefineExamComment implements IService {

    public void run(String executionCourseInitials, Integer executionPeriodId, String comment)
            throws FenixServiceException, ExcepcaoPersistencia {

        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final ExecutionCourse executionCourse = persistentSupport.getIPersistentExecutionCourse()
                .readByExecutionCourseInitialsAndExecutionPeriodId(executionCourseInitials,
                        executionPeriodId);
        if (executionCourse == null) {
            throw new FenixServiceException("error.noExecutionCourse");
        }
        executionCourse.setComment(comment);
    }

}
