package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.onlineTests.Test;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeleteTest extends Service {

    public void run(final Integer executionCourseId, final Integer testId) throws InvalidArgumentsServiceException {
	Test test = rootDomainObject.readTestByOID(testId);
	test.delete();
    }

}
