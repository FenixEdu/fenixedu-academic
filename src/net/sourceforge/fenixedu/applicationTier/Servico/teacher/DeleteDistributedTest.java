/*
 * Created on 24/Set/2003
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.domain.DistributedTest;
import net.sourceforge.fenixedu.domain.IDistributedTest;
import net.sourceforge.fenixedu.domain.IOnlineTest;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDistributedTest;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.util.tests.TestType;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class DeleteDistributedTest implements IService {
    public DeleteDistributedTest() {
    }

    public boolean run(Integer executionCourseId, Integer distributedTestId)
            throws FenixServiceException {
        try {

            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
            IPersistentDistributedTest persistentDistributedTest = persistentSuport
                    .getIPersistentDistributedTest();
            IDistributedTest distributedTest = (IDistributedTest) persistentDistributedTest.readByOID(
                    DistributedTest.class, distributedTestId, true);
            if (distributedTest.getTestType().getType().intValue() == TestType.EVALUATION) {
                IOnlineTest onlineTest = (IOnlineTest) persistentSuport.getIPersistentOnlineTest()
                        .readByDistributedTest(distributedTest);
                persistentSuport.getIPersistentMark().deleteByEvaluation(onlineTest);
                persistentSuport.getIPersistentOnlineTest().delete(onlineTest);
            }
            persistentSuport.getIPersistentDistributedTestAdvisory().deleteByDistributedTest(
                    distributedTest);
            persistentSuport.getIPersistentQuestion().cleanQuestions(distributedTest);
            persistentSuport.getIPersistentMetadata().cleanMetadatas();
            persistentDistributedTest.delete(distributedTest);
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
        return true;
    }
}