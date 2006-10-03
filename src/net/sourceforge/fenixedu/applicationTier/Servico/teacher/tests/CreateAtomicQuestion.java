package net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.tests.NewQuestion;
import net.sourceforge.fenixedu.domain.tests.NewQuestionGroup;
import net.sourceforge.fenixedu.domain.tests.NewQuestionType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class CreateAtomicQuestion extends Service {
	public NewQuestion run(NewQuestionGroup parentQuestionGroup, NewQuestionType questionType)
			throws FenixServiceException, ExcepcaoPersistencia {
		return questionType.newInstance(parentQuestionGroup);
	}
}
