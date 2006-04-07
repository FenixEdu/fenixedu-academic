package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeleteDistributedTest extends Service {
    public void run(Integer executionCourseId, final Integer distributedTestId) throws ExcepcaoPersistencia {
        final DistributedTest distributedTest = rootDomainObject.readDistributedTestByOID(distributedTestId);

        persistentSupport.getIPersistentMetadata().cleanMetadatas();

        distributedTest.delete();
    }

}
