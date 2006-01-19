package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCoordinator;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.IPersistentRole;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.IService;

public class AddCoordinator implements IService {

    public Boolean run(Integer executionDegreeId, Integer teacherNumber) throws FenixServiceException,
            ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
        Teacher teacher = persistentTeacher.readByNumber(teacherNumber);
        if (teacher == null) {
            throw new NonExistingServiceException();
        }
        IPersistentExecutionDegree persistentExecutionDegree = sp.getIPersistentExecutionDegree();
        ExecutionDegree executionDegree = (ExecutionDegree) persistentExecutionDegree.readByOID(
                ExecutionDegree.class, executionDegreeId);
        if (executionDegree == null) {
            throw new InvalidArgumentsServiceException();
        }
        IPersistentCoordinator persistentCoordinator = sp.getIPersistentCoordinator();
        Coordinator coordinator = persistentCoordinator.readCoordinatorByTeacherIdAndExecutionDegreeId(
                teacher.getIdInternal(), executionDegree.getIdInternal());
        if (coordinator == null) {
            coordinator = DomainFactory.makeCoordinator();
            coordinator.setExecutionDegree(executionDegree);
            coordinator.setTeacher(teacher);
            coordinator.setResponsible(new Boolean(false));
            // verify if the teacher already was coordinator
            List executionDegreesTeacherList = persistentCoordinator
                    .readExecutionDegreesByTeacher(teacher.getIdInternal());
            if (executionDegreesTeacherList == null || executionDegreesTeacherList.size() <= 0) {
                // Role Coordinator
                IPersistentRole persistentRole = sp.getIPersistentRole();
                Role role = persistentRole.readByRoleType(RoleType.COORDINATOR);

                Person person = teacher.getPerson();

                if (!person.getPersonRoles().contains(role)) {
                    person.getPersonRoles().add(role);
                }

            }
        } else {
            throw new ExistingServiceException();
        }
        return new Boolean(true);

    }

}
