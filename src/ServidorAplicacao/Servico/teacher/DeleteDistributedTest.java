/*
 * Created on 24/Set/2003
 */

package ServidorAplicacao.Servico.teacher;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.DistributedTest;
import Dominio.IDistributedTest;
import Dominio.IOnlineTest;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentDistributedTest;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.tests.TestType;

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