package net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.tests.NewTest;
import net.sourceforge.fenixedu.domain.tests.NewTestGroup;
import pt.ist.fenixframework.Atomic;

public class GetStudentTest {
    @Atomic
    public static NewTest run(Person person, NewTestGroup testGroup) throws FenixServiceException {
        return testGroup.getOrAssignTest(person);
    }
}