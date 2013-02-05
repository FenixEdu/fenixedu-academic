package net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.tests.NewTest;
import net.sourceforge.fenixedu.domain.tests.NewTestGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import pt.ist.fenixWebFramework.services.Service;

public class GetStudentTest extends FenixService {
    @Service
    public static NewTest run(Person person, NewTestGroup testGroup) throws FenixServiceException, ExcepcaoPersistencia {
        return testGroup.getOrAssignTest(person);
    }
}