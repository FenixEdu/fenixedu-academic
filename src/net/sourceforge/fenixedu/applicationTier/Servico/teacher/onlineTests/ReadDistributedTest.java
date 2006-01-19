/*
 * Created on 20/Ago/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoDistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * @author Susana Fernandes
 */
public class ReadDistributedTest implements IService {

    public InfoDistributedTest run(Integer executionCourseId, Integer distributedTestId) throws ExcepcaoPersistencia,
            InvalidArgumentsServiceException {
        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        DistributedTest distributedTest = (DistributedTest) persistentSuport.getIPersistentDistributedTest().readByOID(DistributedTest.class,
                distributedTestId);
        if (distributedTest == null)
            throw new InvalidArgumentsServiceException();
        return InfoDistributedTest.newInfoFromDomain(distributedTest);
    }
}