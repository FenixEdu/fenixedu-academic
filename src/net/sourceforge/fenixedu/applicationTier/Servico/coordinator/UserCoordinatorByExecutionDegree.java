package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Tânia Pousão Create on 04/Fev/2003
 */
public class UserCoordinatorByExecutionDegree extends Service {

    public Boolean run(Integer executionDegreeCode, String teacherUserName, String degree2Compare)
            throws FenixServiceException, ExcepcaoPersistencia {
        final Teacher teacher = persistentSupport.getIPersistentTeacher().readTeacherByUsername(
                teacherUserName);

        final Coordinator coordinator = persistentSupport.getIPersistentCoordinator()
                .readCoordinatorByTeacherIdAndExecutionDegreeId(teacher.getIdInternal(),
                        executionDegreeCode);
        if (coordinator == null) {
            throw new FenixServiceException("error.exception.notAuthorized");
        }
        return Boolean.valueOf(coordinator.getExecutionDegree().getDegreeCurricularPlan().getDegree()
                .getSigla().equals(degree2Compare));
    }
}