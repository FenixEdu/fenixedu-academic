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
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author Susana Fernandes
 */
public class ReadDistributedTest extends Service {

    public InfoDistributedTest run(Integer executionCourseId, Integer distributedTestId) throws ExcepcaoPersistencia,
            InvalidArgumentsServiceException {
        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        DistributedTest distributedTest = (DistributedTest) persistentSupport.getIPersistentDistributedTest().readByOID(DistributedTest.class,
                distributedTestId);
        if (distributedTest == null)
            throw new InvalidArgumentsServiceException();
        return InfoDistributedTest.newInfoFromDomain(distributedTest);
    }
}