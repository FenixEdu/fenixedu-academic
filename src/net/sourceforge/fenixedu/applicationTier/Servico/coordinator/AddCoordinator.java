package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
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

public class AddCoordinator extends Service {

    public Boolean run(Integer executionDegreeId, Integer teacherNumber) throws FenixServiceException,
            ExcepcaoPersistencia {        
        Teacher teacher = Teacher.readByNumber(teacherNumber);
        if (teacher == null) {
            throw new NonExistingServiceException();
        }

        ExecutionDegree executionDegree = (ExecutionDegree) persistentObject.readByOID(
                ExecutionDegree.class, executionDegreeId);
        if (executionDegree == null) {
            throw new InvalidArgumentsServiceException();
        }
        Coordinator coordinator = executionDegree.getCoordinatorByTeacher(teacher);

        if (coordinator == null) {
            coordinator = DomainFactory.makeCoordinator();
            coordinator.setExecutionDegree(executionDegree);
            coordinator.setTeacher(teacher);
            coordinator.setResponsible(new Boolean(false));
            // verify if the teacher already was coordinator
            List<ExecutionDegree> executionDegreesTeacherList = teacher.getCoordinatedExecutionDegrees();
            if (executionDegreesTeacherList == null || executionDegreesTeacherList.size() <= 0) {
                // Role Coordinator
                Role role = Role.getRoleByRoleType(RoleType.COORDINATOR);

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
