package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import pt.ist.fenixWebFramework.services.Service;

public class AddCoordinator extends FenixService {

	@Service
	public static Boolean run(final Integer executionDegreeId, final String istUsername) throws FenixServiceException {

		final Person person = Person.readPersonByIstUsername(istUsername);
		if (person == null) {
			throw new FenixServiceException("error.noEmployeeForIstUsername");
		}

		final Employee employee = person.getEmployee();
		if (employee == null) {
			throw new FenixServiceException("error.noEmployeeForIstUsername");
		}

		final ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(executionDegreeId);
		if (executionDegree == null) {
			throw new FenixServiceException("error.noExecutionDegree");
		}

		final Coordinator coordinator = executionDegree.getCoordinatorByTeacher(person);
		if (coordinator == null) {
			Coordinator.createCoordinator(executionDegree, person, Boolean.FALSE);
		}

		return Boolean.TRUE;
	}

}
