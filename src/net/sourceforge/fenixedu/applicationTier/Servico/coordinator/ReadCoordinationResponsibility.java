/*
 * Created on 17/Set/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCoordinator;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * @author João Mota 17/Set/2003
 * 
 */
public class ReadCoordinationResponsibility implements IService {

    public Boolean run(Integer executionDegreeId, IUserView userView) throws FenixServiceException,
            ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentCoordinator persistentCoordinator = sp.getIPersistentCoordinator();
        IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
        Teacher teacher = persistentTeacher.readTeacherByUsername(userView.getUtilizador());
        if (teacher == null) {
            throw new FenixServiceException("error.teacher.not.found");
        }
        Coordinator coordinator = persistentCoordinator.readCoordinatorByTeacherIdAndExecutionDegreeId(
                teacher.getIdInternal(), executionDegreeId);
        if (coordinator == null || !coordinator.getResponsible().booleanValue()) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}