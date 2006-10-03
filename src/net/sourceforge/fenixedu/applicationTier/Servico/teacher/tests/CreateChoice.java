package net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.tests.NewChoice;
import net.sourceforge.fenixedu.domain.tests.NewMultipleChoiceQuestion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class CreateChoice extends Service {
	public NewChoice run(NewMultipleChoiceQuestion multipleChoiceQuestion) throws FenixServiceException,
			ExcepcaoPersistencia {
		return new NewChoice(multipleChoiceQuestion);
	}
}
