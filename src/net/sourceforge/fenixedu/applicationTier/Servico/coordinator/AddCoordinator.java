package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class AddCoordinator extends Service {

    public Boolean run(Integer executionDegreeId, Integer teacherNumber) throws FenixServiceException, ExcepcaoPersistencia {
        
        final Teacher teacher = Teacher.readByNumber(teacherNumber);
        if (teacher == null) {
            throw new FenixServiceException("error.noTeacher");
        }

        final ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(executionDegreeId);
        if (executionDegree == null) {
            throw new FenixServiceException("error.noExecutionDegree");
        }
        
        Coordinator coordinator = executionDegree.getCoordinatorByTeacher(teacher);
        if (coordinator == null) {
            coordinator = new Coordinator(executionDegree, teacher, Boolean.FALSE);
            // verify if the teacher already was coordinator
            final List<ExecutionDegree> executionDegreesTeacherList = teacher.getCoordinatedExecutionDegrees();
            if (executionDegreesTeacherList == null || executionDegreesTeacherList.isEmpty()) {
                final Role role = Role.getRoleByRoleType(RoleType.COORDINATOR);
                final Person person = teacher.getPerson();
                if (!person.getPersonRoles().contains(role)) {
                    person.getPersonRoles().add(role);
                }
            }
        } else {
            throw new FenixServiceException("error.impossibleInsertCoordinator");
        }
        
        return Boolean.TRUE;
    }

}
