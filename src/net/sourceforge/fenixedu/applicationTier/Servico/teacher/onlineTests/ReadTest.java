package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.onlineTests.Test;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadTest extends Service {

    public Test run(Integer executionCourseId, Integer testId) throws FenixServiceException,
            ExcepcaoPersistencia {
        final Test test = rootDomainObject.readTestByOID(testId);
        if (test == null) {
            throw new FenixServiceException();
        }
        return test;
    }

}
