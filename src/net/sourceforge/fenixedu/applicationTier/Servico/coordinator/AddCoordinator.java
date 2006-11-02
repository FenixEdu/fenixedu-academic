package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class AddCoordinator extends Service {

    public Boolean run(Integer executionDegreeId, Integer number) throws FenixServiceException, ExcepcaoPersistencia {

        final Employee employee = Employee.readByNumber(number);

        if (employee == null) {
            throw new FenixServiceException("error.noTeacher");
        }

        final Person person = employee.getPerson();

        final ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(executionDegreeId);
        if (executionDegree == null) {
            throw new FenixServiceException("error.noExecutionDegree");
        }
        
        Coordinator coordinator = executionDegree.getCoordinatorByTeacher(person);
        if (coordinator == null) {
            new Coordinator(executionDegree, person, Boolean.FALSE);
            person.addPersonRoleByRoleType(RoleType.COORDINATOR);
        } else {
            throw new FenixServiceException("error.impossibleInsertCoordinator");
        }
        
        return Boolean.TRUE;
    }

}
