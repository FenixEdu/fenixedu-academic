package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ICoordinator;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCoordinator;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Tânia Pousão Create on 04/Fev/2003
 */
public class UserCoordinatorByExecutionDegree implements IService {
    public UserCoordinatorByExecutionDegree() {

    }

    public Boolean run(Integer executionDegreeCode, String teacherUserName, String degree2Compare)
            throws FenixServiceException {
        boolean result = false;

        try {
            ISuportePersistente sp;
            IDegree degree = null;

            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
            ITeacher teacher = persistentTeacher.readTeacherByUsername(teacherUserName);

            IPersistentCoordinator persistentCoordinator = sp.getIPersistentCoordinator();
            ICoordinator coordinator = persistentCoordinator
                    .readCoordinatorByTeacherAndExecutionDegreeId(teacher, executionDegreeCode);
            degree = coordinator.getExecutionDegree().getCurricularPlan().getDegree();

            result = degree.getSigla().equals(degree2Compare);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FenixServiceException(e);
        }

        return new Boolean(result);
    }
}