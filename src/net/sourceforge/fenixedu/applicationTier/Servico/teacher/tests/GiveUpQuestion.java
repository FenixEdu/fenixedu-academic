package net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.tests.NewAtomicQuestion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class GiveUpQuestion extends Service {
	public void run(NewAtomicQuestion atomicQuestion) throws FenixServiceException, ExcepcaoPersistencia {
		atomicQuestion.giveUpQuestion();
	}
}
