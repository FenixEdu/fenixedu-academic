package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ICoordinator;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Tânia Pousão Create on 04/Fev/2003
 */
public class UserCoordinatorByExecutionDegree implements IService {

    public Boolean run(Integer executionDegreeCode, String teacherUserName, String degree2Compare)
            throws FenixServiceException, ExcepcaoPersistencia {
        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final ITeacher teacher = persistentSupport.getIPersistentTeacher().readTeacherByUsername(
                teacherUserName);

        final ICoordinator coordinator = persistentSupport.getIPersistentCoordinator()
                .readCoordinatorByTeacherIdAndExecutionDegreeId(teacher.getIdInternal(),
                        executionDegreeCode);
        if (coordinator == null) {
            throw new FenixServiceException("error.exception.notAuthorized");
        }
        return Boolean.valueOf(coordinator.getExecutionDegree().getDegreeCurricularPlan().getDegree()
                .getSigla().equals(degree2Compare));
    }
}