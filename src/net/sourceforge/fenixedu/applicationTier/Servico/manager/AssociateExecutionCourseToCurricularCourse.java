package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.IService;

public class AssociateExecutionCourseToCurricularCourse implements IService {

    public void run(Integer executionCourseId, Integer curricularCourseId, Integer executionPeriodId)
            throws FenixServiceException, ExcepcaoPersistencia {

        final ISuportePersistente persistentSuport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();

        final CurricularCourse curricularCourse = (CurricularCourse) persistentSuport
                .getIPersistentObject().readByOID(CurricularCourse.class, curricularCourseId);
        if (curricularCourse == null) {
            throw new NonExistingServiceException("message.nonExistingCurricularCourse", null);
        }

        final ExecutionPeriod executionPeriod = (ExecutionPeriod) persistentSuport
                .getIPersistentObject().readByOID(ExecutionPeriod.class, executionPeriodId);
        if (executionPeriod == null) {
            throw new NonExistingServiceException("message.nonExistingExecutionPeriod", null);
        }

        List<ExecutionCourse> executionCourses = curricularCourse.getAssociatedExecutionCourses();
        for (ExecutionCourse executionCourse : executionCourses) {
            if (executionCourse.getExecutionPeriod() == executionPeriod) {
                throw new ExistingServiceException("message.unavailable.execution.period", null);
            }
        }

        final ExecutionCourse executionCourse = (ExecutionCourse) persistentSuport
                .getIPersistentObject().readByOID(ExecutionCourse.class, executionCourseId);
        if (executionCourse == null) {
            throw new NonExistingServiceException("message.nonExisting.executionCourse", null);
        }

        if (!curricularCourse.hasAssociatedExecutionCourses(executionCourse)) {
            curricularCourse.addAssociatedExecutionCourses(executionCourse);
        }
    }

}
