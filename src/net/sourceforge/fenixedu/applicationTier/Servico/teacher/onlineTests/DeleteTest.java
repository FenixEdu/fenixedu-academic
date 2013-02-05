package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.onlineTests.Test;

public class DeleteTest extends FenixService {

    public void run(final Integer executionCourseId, final Integer testId) throws InvalidArgumentsServiceException {
        Test test = rootDomainObject.readTestByOID(testId);
        test.delete();
    }

}
