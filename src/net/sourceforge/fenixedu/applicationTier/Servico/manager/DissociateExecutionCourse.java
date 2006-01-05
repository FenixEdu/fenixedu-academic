package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class DissociateExecutionCourse implements IService {

    public void run(Integer executionCourseId, Integer curricularCourseId) throws FenixServiceException,
            ExcepcaoPersistencia {
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        final ICurricularCourse curricularCourse = (ICurricularCourse) sp.getIPersistentObject()
                .readByOID(CurricularCourse.class, curricularCourseId);
        if (curricularCourse == null) {
            throw new NonExistingServiceException("message.nonExistingCurricularCourse", null);
        }

        final IExecutionCourse executionCourse = (IExecutionCourse) sp.getIPersistentObject().readByOID(
                ExecutionCourse.class, executionCourseId);
        if (executionCourse == null) {
            throw new NonExistingServiceException("message.nonExisting.executionCourse", null);
        }

        List<IExecutionCourse> executionCourses = curricularCourse.getAssociatedExecutionCourses();
        List<ICurricularCourse> curricularCourses = executionCourse.getAssociatedCurricularCourses();

        if (!executionCourses.isEmpty() && !curricularCourses.isEmpty()) {
            executionCourses.remove(executionCourse);
            curricularCourses.remove(curricularCourse);
        }
    }

}
