/*
 * Created on 28/Jul/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import net.sourceforge.fenixedu.domain.ITest;
import net.sourceforge.fenixedu.domain.Test;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTest;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

/**
 * @author Susana Fernandes
 */
public class DeleteTest implements IService {

    public DeleteTest() {
    }

    public boolean run(Integer executionCourseId, Integer testId) throws FenixServiceException {

        try {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();

            IPersistentTest persistentTest = persistentSuport.getIPersistentTest();
            ITest test = (ITest) persistentTest.readByOID(Test.class, testId, true);
            if (test == null)
                throw new FenixServiceException();

            IPersistentTestQuestion persistentTestQuestion = persistentSuport
                    .getIPersistentTestQuestion();
            persistentTestQuestion.deleteByTest(test);
            persistentTest.delete(test);

            return true;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }

}