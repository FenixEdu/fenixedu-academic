package net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.tests.NewAllGroup;
import net.sourceforge.fenixedu.domain.tests.NewQuestionGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class CreateAllGroup extends Service {
	public NewAllGroup run(NewQuestionGroup parentQuestionGroup) throws FenixServiceException,
			ExcepcaoPersistencia {
		return new NewAllGroup(parentQuestionGroup);
	}
}
