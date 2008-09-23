package net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.tests.NewAtomicQuestion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class GiveUpQuestion extends FenixService {
    public void run(NewAtomicQuestion atomicQuestion) throws FenixServiceException {
	atomicQuestion.giveUpQuestion();
    }
}
