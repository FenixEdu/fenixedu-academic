package net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.tests.NewTest;
import net.sourceforge.fenixedu.domain.tests.NewTestGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class GetStudentTest extends Service {
	public NewTest run(Person person, NewTestGroup testGroup) throws FenixServiceException,
			ExcepcaoPersistencia {
		return testGroup.getOrAssignTest(person);
	}
}
