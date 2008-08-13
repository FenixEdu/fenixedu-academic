/*
 * Created on 20/Ago/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoDistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Susana Fernandes
 */
public class ReadDistributedTest extends Service {

    public InfoDistributedTest run(Integer executionCourseId, Integer distributedTestId) throws InvalidArgumentsServiceException {
        DistributedTest distributedTest = rootDomainObject.readDistributedTestByOID(distributedTestId);
        if (distributedTest == null)
            throw new InvalidArgumentsServiceException();

        return InfoDistributedTest.newInfoFromDomain(distributedTest);
    }

}
