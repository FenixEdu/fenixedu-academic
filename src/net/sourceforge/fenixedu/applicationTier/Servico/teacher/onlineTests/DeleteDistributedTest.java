/*
 * Created on 24/Set/2003
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.IDistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.IOnlineTest;
import net.sourceforge.fenixedu.domain.onlineTests.OnlineTest;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentDistributedTest;
import net.sourceforge.fenixedu.util.tests.TestType;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class DeleteDistributedTest implements IService {
    public void run(Integer executionCourseId, Integer distributedTestId) throws ExcepcaoPersistencia {
        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentDistributedTest persistentDistributedTest = persistentSuport.getIPersistentDistributedTest();
        IDistributedTest distributedTest = (IDistributedTest) persistentDistributedTest.readByOID(DistributedTest.class, distributedTestId, true);
        if (distributedTest.getTestType().getType().intValue() == TestType.EVALUATION) {
            IOnlineTest onlineTest = (IOnlineTest) persistentSuport.getIPersistentOnlineTest().readByDistributedTest(distributedTest);
            persistentSuport.getIPersistentMark().deleteByEvaluation(onlineTest);
            persistentSuport.getIPersistentOnlineTest().deleteByOID(OnlineTest.class, onlineTest.getIdInternal());
        }
        persistentSuport.getIPersistentDistributedTestAdvisory().deleteByDistributedTest(distributedTest);
        persistentSuport.getIPersistentQuestion().cleanQuestions(distributedTest);
        persistentSuport.getIPersistentMetadata().cleanMetadatas();
        persistentDistributedTest.deleteByOID(DistributedTest.class, distributedTest.getIdInternal());
    }
}